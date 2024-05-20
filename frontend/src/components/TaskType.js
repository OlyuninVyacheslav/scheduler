import React from 'react';
import { useDrop } from 'react-dnd';
import TaskCard from './TaskCard';

const TaskType = ({ type, moveTask }) => {
  const [{ isOver }, drop] = useDrop({
    accept: 'TASK',
    drop: (item) => moveTask(item.id, type.id),
    collect: (monitor) => ({
      isOver: !!monitor.isOver()
    })
  });

  return (
    <div ref={drop} className={`w-1/2 p-4 rounded-lg ${isOver ? 'bg-blue-100' : 'bg-gray-100'}`}>
      <h2 className="text-xl font-bold mb-4">{type.name}</h2>
      {type.tasks.map(task => (
        <TaskCard key={task.id} task={task} />
      ))}
    </div>
  );
};

export default TaskType;