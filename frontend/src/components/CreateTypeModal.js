import React, { useState } from 'react';
import Modal from 'react-modal';
import { request } from '../helpers/axios_helper';

Modal.setAppElement('#root'); // Устанавливаем элемент для модального окна

const CreateTypeModal = ({ boardId, modalIsOpen, closeModal, refreshBoard }) => {
  const [taskTypeName, setTaskTypeName] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  const handleInputChange = (event) => {
    setTaskTypeName(event.target.value);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const url = `/board/${boardId}/type`;
      await request('POST', url, { name: taskTypeName });
      closeModal();
      refreshBoard(); // Refresh the board data
    } catch (error) {
      setErrorMessage('Ошибка при создании нового типа задачи. Попробуйте снова.');
    }
  };

  return (
    <Modal
      isOpen={modalIsOpen}
      onRequestClose={closeModal}
      className="modal bg-white p-6 rounded shadow-lg mx-auto my-8 w-96"
      overlayClassName="modal-overlay fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center"
    >
      <h2 className="text-xl font-bold mb-4 text-gray-700">Новый вид задач</h2>
      {errorMessage && (
        <div className="text-red-500 mb-4">
          {errorMessage}
        </div>
      )}
      <form onSubmit={handleSubmit}>
        <div className="mb-4">
          <label htmlFor="taskTypeName" className="block text-gray-700">
            Название
          </label>
          <input
            id="taskTypeName"
            type="text"
            value={taskTypeName}
            onChange={handleInputChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded outline-none"
            required
          />
        </div>
        <div className="flex justify-end">
          <button
            type="button"
            className="bg-gray-300 hover:bg-gray-200 text-gray-700 py-2 px-4 rounded mr-2 hover:shadow-md"
            onClick={closeModal}
          >
            Отмена
          </button>
          <button
            type="submit"
            className="bg-cyan-500 hover:bg-cyan-400 text-white py-2 px-4 rounded hover:shadow-md"
          >
            Создать
          </button>
        </div>
      </form>
    </Modal>
  );
};

export default CreateTypeModal;
