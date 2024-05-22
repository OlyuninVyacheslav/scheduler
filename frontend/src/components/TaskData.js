import React, { useState } from 'react';
import Modal from 'react-modal';
import { formatDate } from '../helpers/formatDate';
import { request } from '../helpers/axios_helper';
import DeleteConfirmationModal from './DeleteConfirmationModal';

const TaskData = ({ isOpen, onClose, task }) => {
  const [isEditingName, setIsEditingName] = useState(false);
  const [isEditingDescription, setIsEditingDescription] = useState(false);
  const [isEditingDeadline, setIsEditingDeadline] = useState(false);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);

  const [name, setName] = useState(task.name);
  const [description, setDescription] = useState(task.description);
  const [deadline, setDeadline] = useState(task.deadline);

  const handleSave = async (field) => {
    try {
      await request('PUT', `/board/type/task/${task.id}`, { name, description, deadline });
      task.name = name;
      task.description = description;
      task.deadline = deadline;
      handleCancel(field);
    } catch (error) {
      console.error('Error updating task:', error);
    }
  };

  const handleCancel = (field) => {
    switch (field) {
      case 'name':
        setIsEditingName(false);
        setName(task.name);
        break;
      case 'description':
        setIsEditingDescription(false);
        setDescription(task.description);
        break;
      case 'deadline':
        setIsEditingDeadline(false);
        setDeadline(task.deadline);
        break;
      default:
        break;
    }
  };

  const handleClose = () => {
    setIsEditingName(false);
    setIsEditingDescription(false);
    setIsEditingDeadline(false);
    onClose();
  };

  const handleDelete = async () => {
    try {
      await request('DELETE', `/board/type/task/${task.id}`);
      setIsDeleteModalOpen(false);
      onClose();
    } catch (error) {
      console.error('Error deleting task:', error);
    }
  };

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={handleClose}
      contentLabel="Task Details"
      className="bg-white p-4 rounded-lg shadow-lg max-w-xl mx-auto"
      overlayClassName="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center"
    >
      <div className="p-4">
        {isEditingName ? (
          <>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              className="border p-2 rounded w-full text-2xl font-bold"
            />
            <EditButtons
              onSave={() => handleSave('name')}
              onCancel={() => handleCancel('name')}
            />
          </>
        ) : (
          <>
          <div className="flex items-center justify-between pb-1 w-full border-b-2 border-gray-300 mb-4">
            <div className="flex items-center flex-grow">
            <h2 className="text-2xl font-bold">
              {task.name}
              </h2>
              <img
                src={require("../pictures/edit.png")}
                alt="Edit"
                className="ml-2 cursor-pointer h-4 mt-2"
                onClick={() => setIsEditingName(true)}
              />
              </div>
              <div>
               <img
                src={require("../pictures/delete.png")}
                alt="Delete Type"
                className="cursor-pointer h-4 mt-2 ml-2"
                onClick={() => setIsDeleteModalOpen(true)}
              />
              </div>
            </div>
          </>
        )}

        <p className="mb-4">
          {isEditingDescription ? (
            <>
              <textarea
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                className="border p-2 rounded w-full mt-2 text-lg max-h-64 min-h-24"
              />
              <EditButtons
                onSave={() => handleSave('description')}
                onCancel={() => handleCancel('description')}
              />
            </>
          ) : (
            <>
              <span className="text-lg">{task.description}</span>
              <img
                src={require("../pictures/edit.png")}
                alt="Edit"
                className="inline ml-2 cursor-pointer h-4 mb-1"
                onClick={() => setIsEditingDescription(true)}
              />
            </>
          )}
        </p>

        <p className="mb-2">
          <span className="font-semibold">Срок до: </span>
          {isEditingDeadline ? (
            <>
              <input
                type="date"
                value={deadline}
                onChange={(e) => setDeadline(e.target.value)}
                className="border p-2 rounded w-full mt-2"
              />
              <EditButtons
                onSave={() => handleSave('deadline')}
                onCancel={() => handleCancel('deadline')}
              />
            </>
          ) : (
            <>
              {formatDate(task.deadline)}
              <img
                src={require("../pictures/edit.png")}
                alt="Edit"
                className="inline ml-2 cursor-pointer h-4 mb-1"
                onClick={() => setIsEditingDeadline(true)}
              />
            </>
          )}
        </p>

        <p className="mb-2">
          <span className="font-semibold">Создано: </span>{formatDate(task.created_at)}
        </p>

        <div className="flex justify-between items-center mt-4">
          <button
            className="px-4 py-2 bg-gray-300 hover:bg-gray-200 text-gray-700 rounded"
            onClick={handleClose}
          >
            Закрыть
          </button>
        </div>

        <DeleteConfirmationModal
          isOpen={isDeleteModalOpen}
          onClose={() => setIsDeleteModalOpen(false)}
          onConfirm={handleDelete}
          entityName="Вы уверены, что вы хотите удалить выбранную задачу?"
        />
      </div>
    </Modal>
  );
};

export default TaskData;

const EditButtons = ({ onSave, onCancel }) => (
  <div className="flex justify-end mt-2">
    <button
      className="mr-2 px-4 py-2 bg-cyan-500 hover:bg-cyan-400 text-white rounded"
      onClick={onSave}
    >
      Сохранить
    </button>
    <button
      className="px-4 py-2 bg-gray-300 hover:bg-gray-200 text-gray-700 rounded"
      onClick={onCancel}
    >
      Отменить
    </button>
  </div>
);
