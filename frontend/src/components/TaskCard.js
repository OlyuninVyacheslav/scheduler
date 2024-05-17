import React from 'react';
import { useDrag } from 'react-dnd';

const TaskCard = ({ id, text }) => {
  const [{ isDragging }, drag] = useDrag({
    type: 'TASK',
    item: { id },
    collect: (monitor) => ({
      isDragging: !!monitor.isDragging()
    })
  });

  return (
    <div
      ref={drag}
      className={`p-4 mb-2 bg-white rounded shadow-md ${isDragging ? 'opacity-50' : 'opacity-100'}`}
    >
      {text}
    </div>
  );
};

export default TaskCard;
