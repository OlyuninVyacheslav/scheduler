import React, { useState } from 'react';
import { useDrag } from 'react-dnd';
import TaskData from './TaskData';
import { formatDate } from '../helpers/formatDate';

const TaskCard = ({ task }) => {
  const [isModalOpen, setIsModalOpen] = useState(false);

  const [{ isDragging }, drag] = useDrag({
    type: 'TASK',
    item: { id: task.id },
    collect: (monitor) => ({
      isDragging: !!monitor.isDragging()
    })
  });

  const handleOpenModal = () => {
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
  };

  return (
    <>
      <div
        ref={drag}
        className={`p-4 mb-2 bg-white rounded shadow-md hover:shadow-xl cursor-pointer ${isDragging ? 'opacity-50' : 'opacity-100'}`}
        onClick={handleOpenModal}
      >
        <h3 className="font-semibold">{task.name}</h3>
        <p>{task.description}</p>
        <p className="text-sm text-gray-500">Срок до: {formatDate(task.deadline)}</p>
        <p className="text-sm text-gray-500">Создано: {formatDate(task.created_at)}</p>
      </div>
      <TaskData isOpen={isModalOpen} onClose={handleCloseModal} task={task} />
    </>
  );
};

export default TaskCard;

