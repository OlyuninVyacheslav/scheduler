import React, { useState } from 'react';
import CreateTypeModal from './CreateTypeModal';

const BoardControl = ({ boardId, refreshBoard }) => {
  const [modalIsOpen, setModalIsOpen] = useState(false);

  const openModal = () => {
    setModalIsOpen(true);
  };

  const closeModal = () => {
    setModalIsOpen(false);
  };

  return (
    <div className="ml-4 mb-8">
      <button
        className="bg-cyan-500 hover:bg-cyan-400 text-white hover:shadow-md font-bold py-2 px-4 rounded"
        onClick={openModal}
      >
        Добавить тип задач
      </button>
      <CreateTypeModal
        boardId={boardId}
        modalIsOpen={modalIsOpen}
        closeModal={closeModal}
        refreshBoard={refreshBoard} // Pass the refreshBoard function
      />
    </div>
  );
};

export default BoardControl;
