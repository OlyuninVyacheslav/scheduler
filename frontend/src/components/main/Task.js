import React, { useState } from 'react';
import { Draggable } from 'react-beautiful-dnd';
import { formatDate } from '../../helpers/formatDate';
import TaskData from '../TaskDataModal';

const Task = ({ task, index }) => {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const handleOpenModal = () => {
        setIsModalOpen(true);
    };

    const handleCloseModal = () => {
        setIsModalOpen(false);
    };
    return (
        <>
        <Draggable draggableId={task.id} index={index}>
            {(provided) => (
                <div
                    className="p-4 mb-2 bg-white rounded shadow-md hover:shadow-xl"
                    {...provided.draggableProps}
                    {...provided.dragHandleProps}
                    ref={provided.innerRef}
                    onClick={handleOpenModal}
                >
                    <h3 className="font-semibold text-gray-800 border-b-2 border-cyan-100 mb-2">{task.name}</h3>
                    <p className="mb-2">{task.description}</p>
                    <p className="text-sm text-gray-500">Срок до: {formatDate(task.deadline)}</p>
                    <p className="text-sm text-gray-500">Создано: {formatDate(task.created_at)}</p>
                </div>
            )}
        </Draggable>
        <TaskData isOpen={isModalOpen} onClose={handleCloseModal} task={task} />
        </>
    );
};

export default Task;
