import React from 'react';
import Modal from 'react-modal';

const DeleteConfirmationModal = ({ isOpen, onClose, onConfirm, entityName }) => {
  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onClose}
      contentLabel="Delete Confirmation"
      className="bg-white p-6 rounded-lg shadow-lg max-w-lg mx-auto"
      overlayClassName="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center"
    >
      <div className="p-4">
        <h2 className="text-xl font-semibold mb-4">{entityName}</h2>
        <div className="flex justify-end mt-4">
          <button
            className="mr-2 px-4 py-2 bg-gray-300 rounded"
            onClick={onClose}
          >
            Отменить
          </button>
          <button
            className="px-4 py-2 bg-red-500 text-white rounded"
            onClick={onConfirm}
          >
            Удалить
          </button>
        </div>
      </div>
    </Modal>
  );
};

export default DeleteConfirmationModal;
