import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { request } from '../helpers/axios_helper';

const EntityCard = ({ entity }) => {
  return (
    <Link to={`/entities/${entity.id}`} className="border rounded p-4 m-2 hover:shadow-md transition duration-300 ease-in-out h-36 w-80 flex flex-col justify-start items-center">
      <h2 className="text-lg font-semibold">{entity.name}</h2>
      <p className="text-gray-600">{entity.description}</p>
    </Link>
  );
};

const MemoizedEntityCard = React.memo(EntityCard); // Оптимизация рендеринга

const BoardsList = () => {
  const [entities, setEntities] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await request('GET', '/entities');
        setEntities(response.data);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    fetchData();
  }, []);

  return (
    <div className="max-w-screen-xl flex flex-wrap justify-center mx-auto">
      {entities.map(entity => (
        <MemoizedEntityCard key={entity.id} entity={entity} />
      ))}
    </div>
  );
};

export default BoardsList;
