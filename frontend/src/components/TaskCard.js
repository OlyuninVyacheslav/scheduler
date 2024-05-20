import React from 'react';
import { useDrag } from 'react-dnd';
import { formatDate } from '../helpers/formatDate';

const TaskCard = ({ task }) => {
  const [{ isDragging }, drag] = useDrag({
    type: 'TASK',
    item: { id: task.id },
    collect: (monitor) => ({
      isDragging: !!monitor.isDragging()
    })
  });

  return (
    <div
      ref={drag}
      className={`p-4 mb-2 bg-white rounded shadow-md ${isDragging ? 'opacity-50' : 'opacity-100'}`}
    >
      <h3 className="font-semibold">{task.name}</h3>
      <p>{task.description}</p>
      <p className="text-sm text-gray-500">Срок до: {formatDate(task.deadline)}</p>
      <p className="text-sm text-gray-500">Создано: {formatDate(task.created_at)}</p>
    </div>
  );
};

export default TaskCard;
