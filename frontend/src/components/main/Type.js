import React, { useState } from 'react';
import { Draggable, Droppable } from 'react-beautiful-dnd';
import Task from './Task';
import { AddIcon, CancelIcon, DeleteIcon, EditIcon } from '../../icons/iconsList';
import { request } from '../../helpers/axios_helper';
import DeleteConfirmationModal from '../DeleteConfirmationModal';
import CreateTaskModal from '../CreateTaskModal';

const Type = ({ type, tasks, index, refreshBoard }) => {
  const [isEditing, setIsEditing] = useState(false);
  const [newTypeName, setNewTypeName] = useState(type.name);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
  const [isTaskAddModalOpen, setIsTaskAddModalOpen] = useState(false);

  const handleEditToggle = () => setIsEditing(!isEditing);

  const handleTypeNameChange = (e) => setNewTypeName(e.target.value);

  const handleSave = async () => {
    try {
      await request('PUT', `/board/type/${type.id}`, { id: type.id, name: newTypeName });
      handleEditToggle();
      refreshBoard();
    } catch (error) {
      console.error('Failed to update type name', error);
    }
  };

  const handleDelete = async () => {
    try {
      await request('DELETE', `/board/type/${type.id}`);
      refreshBoard();
    } catch (error) {
      console.error('Failed to delete type', error);
    } finally {
      setIsDeleteModalOpen(false);
    }
  };

  return (
    <Draggable draggableId={type.id.toString()} index={index}>
      {(provided) => (
        <div
          className="bg-gray-200 rounded p-4 w-72 hover:shadow-xl"
          {...provided.draggableProps}
          ref={provided.innerRef}
        >
          <div className="flex items-center justify-between border-b-2 border-gray-300 mb-2">
            <div className="flex flex-grow items-center">
              {isEditing ? (
                <div className="flex items-center w-full">
                  <input
                    type="text"
                    value={newTypeName}
                    onChange={handleTypeNameChange}
                    className="flex-grow px-2 py-1 border rounded w-32 text-lg font-bold text-gray-700 bg-gray-200 outline-none"
                  />
                  <button
                    onClick={handleSave}
                    className="ml-2 px-2 py-1 bg-cyan-500 hover:bg-cyan-400 text-white rounded"
                  >
                    Сохранить
                  </button>
                  <div onClick={handleEditToggle}>
                    <CancelIcon />
                  </div>
                </div>
              ) : (
                <h3 className="text-lg font-bold text-gray-700" {...provided.dragHandleProps}>
                  {type.name}
                </h3>
              )}
              {!isEditing &&
              <div onClick={handleEditToggle}>
                <EditIcon />
              </div>}
            </div>
            {!isEditing &&
            <div onClick={() => setIsDeleteModalOpen(true)}>
              <DeleteIcon />
            </div>}
          </div>
          <Droppable droppableId={type.id.toString()} type="task">
            {(provided) => (
              <div
                className="rounded p-2 h-full"
                {...provided.droppableProps}
                ref={provided.innerRef}
              >
                {tasks
                  .sort((a, b) => a.order - b.order)
                  .map((task, index) => (
                    <Task key={task.id} task={task} index={index} />
                  ))}
                {provided.placeholder}
                <button 
                  className="flex items-center mt-1"
                  onClick={() => setIsTaskAddModalOpen(true)}
                >
                  <AddIcon />
                  <span className="font-medium text-gray-700">Добавить задачу</span>
                </button>
              </div>
            )}
          </Droppable>
          <DeleteConfirmationModal
            isOpen={isDeleteModalOpen}
            onClose={() => setIsDeleteModalOpen(false)}
            onConfirm={handleDelete}
            entityName="Удалить тип задачи?"
          />
          <CreateTaskModal
            isOpen={isTaskAddModalOpen}
            onClose={() => setIsTaskAddModalOpen(false)}
            typeId={type.id}
            refreshBoard={refreshBoard}
          />
        </div>
      )}
    </Draggable>
  );
};

export default Type;
