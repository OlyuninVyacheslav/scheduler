import React, { useState } from 'react';
import Modal from 'react-modal';
import { formatDate, formatDateDead } from '../helpers/formatDate';
import { request } from '../helpers/axios_helper';
import DeleteConfirmationModal from './DeleteConfirmationModal';
import { CancelIcon, DeleteIcon, EditIcon } from '../icons/iconsList';

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
      className="bg-white p-4 rounded-lg shadow-lg w-72 mx-auto"
      overlayClassName="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center"
    >
      <div className="p-4">
        {isEditingName ? (
          <div className="mb-4">
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              className="border p-2 rounded w-full text-2xl font-bold outline-none"
            />
            <EditButtons
              onSave={() => handleSave('name')}
              onCancel={() => handleCancel('name')}
            />
          </div>
        ) : (
          <>
          <div className="flex items-center justify-between pb-1 w-full border-b-2 border-gray-300 mb-4">
            <div className="flex items-center flex-grow">
            <h2 className="text-2xl font-bold text-gray-700">
              {task.name}
              </h2>
              <div className="mt-1" onClick={() => setIsEditingName(true)}>
                <EditIcon/>
              </div>
              </div>
              <div>
              <div className="mt-1" onClick={() => setIsDeleteModalOpen(true)}>
                <DeleteIcon/>
              </div>
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
            <div className="flex items-center">
              <span className="text-lg">{task.description}</span>
              <div onClick={() => setIsEditingDescription(true)}>
                <EditIcon/>
              </div>
            </div>
          )}
        </p>

        <p className="mb-2">
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
            <div className="flex items-center">
            <span className="font-semibold text-gray-700">Срок до:&nbsp;&nbsp;</span>
            <span>{formatDateDead(task.deadline)}</span>
              <div onClick={() => setIsEditingDeadline(true)}>
                <EditIcon/>
              </div>
            </div>
          )}
        </p>

        <p className="mb-2">
          <span className="font-semibold text-gray-700">Создано: </span>{formatDate(task.createdAt)}
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
          entityName="Удалить задачу?"
        />
      </div>
    </Modal>
  );
};

export default TaskData;

const EditButtons = ({ onSave, onCancel }) => (
  <div className="flex items-center justify-end mt-2">
    <button
      className="mr-2 px-4 py-2 bg-cyan-500 hover:bg-cyan-400 text-white rounded"
      onClick={onSave}
    >
      Сохранить
    </button>
    <div onClick={onCancel}>
      <CancelIcon/>
    </div>
  </div>
);
