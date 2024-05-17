import React, { useState } from 'react';
import Modal from 'react-modal';
import { request } from '../helpers/axios_helper';

Modal.setAppElement('#root'); // Установите элемент приложения для повышения доступности

const CreateBoardModal = ({ isOpen, onClose, currentUser }) => {
  const [name, setName] = useState('');
  //const [description, setDescription] = useState('');
  const [creatorId, setCreatorId] = useState('');
  const [createdAt, setCreatorAt] = useState('');
  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      // await request('POST', '/board', { name, description });
      await request('POST', '/boards/create', { name, creatorId, createdAt});
      console.log("Name: " + name + " creatorId: " + creatorId + " createdAt: " + createdAt);
      onClose();
    } catch (error) {
      console.error('Error creating board:', error);
    }
  };
  

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onClose}
      className="bg-white p-6 rounded-lg shadow-lg max-w-lg mx-auto"
      overlayClassName="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center"
    >
      <h2 className="text-2xl font-bold mb-4">Создать новую доску</h2>
      <form onSubmit={handleSubmit}>
        <div className="mb-4">
          <label htmlFor="name" className="block text-gray-700">Название</label>
          <input
            type="text"
            id="name"
            className="w-full border border-gray-300 p-2 rounded-md"
            value={name}
            onChange={(e) => setName(e.target.value)}
            required
          />
        </div>
        {/* <div className="mb-4">
          <label htmlFor="description" className="block text-gray-700">Описание</label>
          <textarea
            id="description"
            className="w-full border border-gray-300 p-2 rounded-md"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            required
          ></textarea>
        </div> */}
        <div className="flex justify-end">
          <button type="button" className="bg-gray-500 hover:bg-gray-400 text-white px-4 py-2 rounded-md mr-2" onClick={onClose}>
            Отмена
          </button>
          <button type="submit" className="bg-cyan-500 hover:bg-cyan-400 text-white px-4 py-2 rounded-md">
            Создать
          </button>
        </div>
      </form>
    </Modal>
  );
};

export default CreateBoardModal;
