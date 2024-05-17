import React, { useState } from 'react';
import { DndProvider, useDrag, useDrop } from 'react-dnd';
import { HTML5Backend } from 'react-dnd-html5-backend';
import TaskType from './TaskType.js';

const TaskBoard = () => {
  const [tasks, setTasks] = useState({
    'in-progress': [
      { id: 1, text: 'Task 1' },
      { id: 2, text: 'Task 2' }
    ],
    'completed': [
      { id: 3, text: 'Task 3' }
    ]
  });

  const moveTask = (taskId, toType) => {
    const task = Object.keys(tasks).flatMap(type => tasks[type]).find(task => task.id === taskId);

    setTasks(prevTasks => {
      const updatedTasks = { ...prevTasks };
      for (const type in updatedTasks) {
        updatedTasks[type] = updatedTasks[type].filter(task => task.id !== taskId);
      }
      updatedTasks[toType].push(task);
      return updatedTasks;
    });
  };

  return (
    <DndProvider backend={HTML5Backend}>
      <div className="flex space-x-4 max-w-screen-2xl mx-auto">
        <TaskType type="in-progress" tasks={tasks['in-progress']} moveTask={moveTask} />
        <TaskType type="completed" tasks={tasks['completed']} moveTask={moveTask} />
      </div>
    </DndProvider>
  );
};

export default TaskBoard;
