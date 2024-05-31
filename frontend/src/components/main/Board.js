import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { DragDropContext, Droppable } from 'react-beautiful-dnd';
import Type from './Type';
import { request } from '../../helpers/axios_helper';
import { initialData } from '../testData';
import BoardControl from '../BoardControl';
import { formatDate, formatDateDead } from '../../helpers/formatDate';

const Board = () => {
  const params = useParams();
  const boardId = params.boardId;
  const [data, setData] = useState({ types: {}, tasks: {} });
  //const [data, setData] = useState(initialData);
  const [loading, setLoading] = useState(true);

  const fetchData = async () => {
    try {
      const typesResponse = await request('GET', `/board/types/${boardId}`);
      const typesData = typesResponse.data;

      const tasksData = {};

      for (const type of typesData) {
        const tasksResponse = await request('GET', `/board/type/tasks/${type.id}`);
        tasksResponse.data.forEach(task => {
          tasksData[task.id] = task;
        });
      }

      const formattedTypes = typesData.reduce((acc, type) => {
        acc[type.id] = type;
        return acc;
      }, {});

      setData({ types: formattedTypes, tasks: tasksData });
    } catch (error) {
      console.error("Failed to fetch data", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, [boardId]);

  const refreshBoard = () => {
    fetchData();
  };

  const onDragEnd = async (result) => {
    const { destination, source, draggableId, type } = result;

  if (!destination) return;

  if (type === 'type') {
    const newTypeOrder = Array.from(Object.values(data.types)).sort((a, b) => a.order - b.order);
    const [moved] = newTypeOrder.splice(source.index, 1);
    newTypeOrder.splice(destination.index, 0, moved);

    const updatedTypes = newTypeOrder.map((type, index) => ({
      ...type,
      order: index
    }));

    const newState = {
      ...data,
      types: updatedTypes.reduce((acc, type) => {
        acc[type.id] = type;
        return acc;
      }, {}),
    };

    setData(newState);

    try {
      console.log("Sending types:", JSON.stringify(updatedTypes));
      await request('PUT', `/board/types/move`, updatedTypes);
      refreshBoard();
    } catch (error) {
      console.error("Failed to update type order", error);
    }

    return;
  }

  
    const startType = data.types[source.droppableId];
    const finishType = data.types[destination.droppableId];
  
    if (startType === finishType) {
      const newTaskOrder = Array.from(Object.values(data.tasks)
        .filter(task => task.typeId === startType.id)
        .sort((a, b) => a.order - b.order));
  
      const [movedTask] = newTaskOrder.splice(source.index, 1);
      newTaskOrder.splice(destination.index, 0, movedTask);
  
      const updatedTasks = newTaskOrder.map((task, index) => ({
        ...task,
        order: index
      }));
  
      const newState = {
        ...data,
        tasks: updatedTasks.reduce((acc, task) => {
          acc[task.id] = task;
          return acc;
        }, {}),
      };
  
      setData(newState);
  
      try {
        await request('PUT', `/board/tasks/move`, {
          moveTasks: updatedTasks.map(task => ({
            taskId: task.id,
            sourceTypeId: startType.id,
            destinationTypeId: finishType.id,
            newOrder: task.order,
          })),
        });
        refreshBoard();
      } catch (error) {
        console.error("Failed to update task order within type", error);
      }
  
    } else {
      const startTaskOrder = Array.from(Object.values(data.tasks)
        .filter(task => task.typeId === startType.id)
        .sort((a, b) => a.order - b.order));
  
      const finishTaskOrder = Array.from(Object.values(data.tasks)
        .filter(task => task.typeId === finishType.id)
        .sort((a, b) => a.order - b.order));
  
      const [movedTask] = startTaskOrder.splice(source.index, 1);
      movedTask.typeId = finishType.id;
  
      finishTaskOrder.splice(destination.index, 0, movedTask);
  
      const updatedStartTasks = startTaskOrder.map((task, index) => ({
        ...task,
        order: index
      }));
  
      const updatedFinishTasks = finishTaskOrder.map((task, index) => ({
        ...task,
        order: index
      }));
  
      const newState = {
        ...data,
        tasks: {
          ...data.tasks,
          ...updatedStartTasks.reduce((acc, task) => {
            acc[task.id] = task;
            return acc;
          }, {}),
          ...updatedFinishTasks.reduce((acc, task) => {
            acc[task.id] = task;
            return acc;
          }, {}),
          [movedTask.id]: movedTask,
        },
      };
  
      setData(newState);
  
      const moveTasks = [
        ...updatedStartTasks.map(task => ({
          taskId: task.id,
          sourceTypeId: startType.id,
          destinationTypeId: startType.id,
          newOrder: task.order,
        })),
        ...updatedFinishTasks.map(task => ({
          taskId: task.id,
          sourceTypeId: startType.id,
          destinationTypeId: finishType.id,
          newOrder: task.order,
        })),
      ];
  
      try {
        console.log("Sending moveTasks:", JSON.stringify(moveTasks, null, 2));
        await request('PUT', `/board/tasks/move`, { moveTasks: moveTasks });
        refreshBoard();
      } catch (error) {
        console.error("Failed to update task order between types", error);
      }
    }
  };
  
  
  

  if (loading) {
    return <div>Loading...</div>;
  }

  return (
    <div className="mx-auto w-11/12">
      <BoardControl boardId={boardId} refreshBoard={refreshBoard} />
      <DragDropContext onDragEnd={onDragEnd}>
        <Droppable droppableId="all-types" direction="horizontal" type="type">
          {(provided) => (
            <div
              className="flex space-x-4 p-4"
              {...provided.droppableProps}
              ref={provided.innerRef}
            >
              {Object.values(data.types)
                .sort((a, b) => a.order - b.order)
                .map((type, index) => {
                  const tasks = Object.values(data.tasks)
                    .filter(task => task.typeId === type.id) // Ensure correct mapping
                    .sort((a, b) => a.order - b.order);

                  return <Type key={type.id} type={type} tasks={tasks} index={index} refreshBoard={refreshBoard} />;
                })}
              {provided.placeholder}
            </div>
          )}
        </Droppable>
      </DragDropContext>
    </div>
  );
};

export default Board;