import React, { useState, useEffect } from 'react';
import { DndProvider, useDrag, useDrop } from 'react-dnd';
import { HTML5Backend } from 'react-dnd-html5-backend';
import { useParams } from 'react-router-dom';
import { request } from '../helpers/axios_helper';
import TaskType from './TaskType.js';
import { testData } from './testData';
import BoardControl from './BoardControl.js'

const TaskBoard = () => {
  const { boardId } = useParams();
  const [taskTypes, setTaskTypes] = useState([]);

  useEffect(() => {
    const fetchTaskTypesAndTasks = async () => {
      try {
        const types = testData.taskTypes.filter(type => type.board_id === 1);
        console.log(testData);
        setTaskTypes(types);
      } catch (error) {
        console.error('Error fetching task types and tasks:', error);
      }
    };

    fetchTaskTypesAndTasks();
  }, [boardId]);

  useEffect(() => {
    const fetchTaskTypesAndTasks = async () => {
      try {
        // Получаем типы задач
        const response = await request('GET', `/board/types/${boardId}`);
        const types = response.data;

        // Для каждого типа задач получаем задачи
        const typesWithTasks = await Promise.all(types.map(async (type) => {
          const tasksResponse = await request('GET', `/board/type/tasks/${type.id}`);
          return {
            ...type,
            tasks: tasksResponse.data,
          };
        }));

        setTaskTypes(typesWithTasks);
      } catch (error) {
        console.error('Error fetching task types and tasks:', error);
      }
    };

    fetchTaskTypesAndTasks();
  }, [boardId]);

  const moveTask = async (taskId, toTypeId) => {
    try {
      // Выполняем запрос PUT для обновления типа задачи
      // await request('PUT', `/board/type/task/${taskId}`, { type_id: toTypeId });

      // Обновляем состояние на клиенте
      setTaskTypes(prevTaskTypes => {
        const updatedTaskTypes = prevTaskTypes.map(type => {
          const updatedTasks = type.tasks.filter(task => task.id !== taskId);
          return { ...type, tasks: updatedTasks };
        });

        const task = prevTaskTypes.flatMap(type => type.tasks).find(task => task.id === taskId);

        return updatedTaskTypes.map(type => {
          if (type.id === toTypeId) {
            return { ...type, tasks: [...type.tasks, task] };
          }
          return type;
        });
      });
    } catch (error) {
      console.error('Error updating task type:', error);
    }
  };
  
  return (
    <div>
    <BoardControl boardId={boardId}/>
    <DndProvider backend={HTML5Backend}>
      <div className="flex space-x-4 max-w-screen-2xl mx-auto">
        {taskTypes.map(type => (
          <TaskType key={type.id} type={type} moveTask={moveTask} />
        ))}
      </div>
    </DndProvider>
    </div>
  );
};

export default TaskBoard;