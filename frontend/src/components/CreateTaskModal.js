import React, { useState } from 'react';
import Modal from 'react-modal';
import { request } from '../helpers/axios_helper';

const CreateTaskModal = ({ isOpen, onClose, typeId, refreshBoard }) => {
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [deadline, setDeadline] = useState('');

  const handleSave = async () => {
    try {
      const created_at = new Date().toISOString();
      await request('POST', '/board/type/task/create', {
        name,
        description,
        deadline,
        typeId,
        created_at,
      });
      onClose();
      refreshBoard(); // Refresh the board data
    } catch (error) {
      console.error('Error creating task:', error);
    }
  };

  const handleCancel = () => {
    setName('');
    setDescription('');
    setDeadline('');
    onClose();
  };

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={handleCancel}
      contentLabel="Add Task"
      className="bg-white p-6 rounded-lg shadow-lg max-w-lg mx-auto"
      overlayClassName="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center"
    >
      <div className="p-4">
        <h2 className="text-2xl font-bold mb-4">Добавить задачу</h2>
        <div className="mb-2">
          <label className="block text-sm font-medium">Название</label>
          <input
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
            className="border p-2 rounded w-full"
          />
        </div>
        <div className="mb-2">
          <label className="block text-sm font-medium">Описание</label>
          <textarea
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            className="border p-2 rounded w-full"
          />
        </div>
        <div className="mb-2">
          <label className="block text-sm font-medium">Срок до</label>
          <input
            type="date"
            value={deadline}
            onChange={(e) => setDeadline(e.target.value)}
            className="border p-2 rounded w-full"
          />
        </div>
        <div className="flex justify-end mt-4">
          <button
            className="mr-2 px-4 py-2 bg-cyan-500 hover:bg-cyan-400 text-white rounded"
            onClick={handleSave}
          >
            Сохранить
          </button>
          <button
            className="px-4 py-2 bg-gray-300 hover:bg-gray-200 text-gray-700 rounded"
            onClick={handleCancel}
          >
            Отменить
          </button>
        </div>
      </div>
    </Modal>
  );
};

export default CreateTaskModal;
