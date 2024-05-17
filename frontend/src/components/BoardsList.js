import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { request } from '../helpers/axios_helper';
import CreateBoardModal from './CreateBoardModal';

const EntityCard = ({ entity }) => {
  return (
    <Link to={`/boards/${entity.id}`} className="border rounded p-4 m-2 hover:shadow-md transition duration-300 ease-in-out h-36 w-80 flex flex-col justify-start items-center">
      <h2 className="text-lg font-semibold">{entity.name}</h2>
      {/* <p className="text-gray-600">{entity.description}</p> */}
    </Link>
  );
};

const MemoizedEntityCard = React.memo(EntityCard); // Оптимизация рендеринга

const BoardsList = () => {
  const [entities, setEntities] = useState([]);
  const [isModalOpen, setIsModalOpen] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await request('GET', '/boards');
        setEntities(response.data);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    fetchData();
  }, []);

  const handleCreateBoard = () => {
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
  };

  return (
    <div className="max-w-screen-xl mx-auto">
      <p className="text-gray-700 font-semibold text-xl mb-4">Доступные доски</p>
      <button className="bg-cyan-500 hover:bg-cyan-400 text-white font-semibold px-4 py-2 rounded-md mb-4" onClick={handleCreateBoard}>
        Создать доску
      </button>
      <div className="justify-center flex flex-wrap">
      {entities.map(entity => (
        <MemoizedEntityCard key={entity.id} entity={entity} />
      ))}
      <CreateBoardModal isOpen={isModalOpen} onClose={handleCloseModal} />
      </div>
    </div>
  );
};

export default BoardsList;