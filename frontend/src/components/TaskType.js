import React, { useState } from 'react';
import { useDrop } from 'react-dnd';
import TaskCard from './TaskCard';
import TaskAdd from './TaskAdd';
import DeleteConfirmationModal from './DeleteConfirmationModal';
import { request } from '../helpers/axios_helper';

const TaskType = ({ type, moveTask }) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [typeName, setTypeName] = useState(type.name);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);

  const [{ isOver }, drop] = useDrop({
    accept: 'TASK',
    drop: (item) => moveTask(item.id, type.id),
    collect: (monitor) => ({
      isOver: !!monitor.isOver()
    })
  });

  const handleOpenModal = () => {
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
  };

  const handleSave = async () => {
    try {
      await request('PUT', `/board/type/${type.id}`, { id: type.id, name: typeName });
      type.name = typeName;
      setIsEditing(false);
    } catch (error) {
      console.error('Error updating type:', error);
    }
  };

  const handleCancel = () => {
    setTypeName(type.name);
    setIsEditing(false);
  };

  const handleDelete = async () => {
    try {
      await request('DELETE', `/board/type/${type.id}`);
      setIsDeleteModalOpen(false);
    } catch (error) {
      console.error('Error deleting type:', error);
    }
  };

  return (
    <div ref={drop} className={`w-1/2 p-4 rounded-lg hover:shadow-md ${isOver ? 'bg-cyan-100' : 'bg-gray-100'}`}>
      <div className="flex justify-between items-center mb-4">
        {isEditing ? (
          <>
            <input
              type="text"
              value={typeName}
              onChange={(e) => setTypeName(e.target.value)}
              className="border p-2 rounded w-full text-xl font-bold"
            />
            <div className="flex ml-2">
              <button
                className="mr-2 px-4 py-2 bg-blue-500 text-white rounded"
                onClick={handleSave}
              >
                Сохранить
              </button>
              <button
                className="px-4 py-2 bg-gray-300 rounded"
                onClick={handleCancel}
              >
                Отменить
              </button>
            </div>
          </>
        ) : (
          <>
            <div className="flex item-center">
              <h2 className="text-xl font-bold">{type.name}</h2>
              <img
                src={require("../pictures/edit.png")}
                alt="Edit Type"
                className="cursor-pointer h-5 mt-1 ml-2"
                onClick={() => setIsEditing(true)}
              />
              <img
                src={require("../pictures/delete.png")}
                alt="Delete Type"
                className="cursor-pointer h-5 mt-1 ml-2"
                onClick={() => setIsDeleteModalOpen(true)}
              />
            </div>
            <div className="flex items-center">
              <img
                src={require("../pictures/add.png")}
                alt="Add Task"
                className="cursor-pointer h-6 ml-2"
                onClick={handleOpenModal}
              />
            </div>
          </>
        )}
      </div>
      {type.tasks.map(task => (
        <TaskCard key={task.id} task={task} />
      ))}
      <TaskAdd isOpen={isModalOpen} onClose={handleCloseModal} typeId={type.id} />
      <DeleteConfirmationModal
        isOpen={isDeleteModalOpen}
        onClose={() => setIsDeleteModalOpen(false)}
        onConfirm={handleDelete}
        entityName="Вы уверены что вы хотите удалить выбранный тип задач вместе с задачами?"
      />
    </div>
  );
};

export default TaskType;
