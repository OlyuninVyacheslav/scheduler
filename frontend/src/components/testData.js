export const testData = {
  taskTypes: [
    {
      id: 1,
      name: 'In Progress',
      board_id: 1,
      tasks: [
        {
          id: 1,
          name: 'Task 1',
          type_id: 2,
          description: 'This is task 1',
          deadline: '2024-05-30',
          created_at: '2024-05-01T10:00:00Z',
          order: 1,
        },
        {
          id: 2,
          name: 'Task 2',
          type_id: 1,
          description: 'This is task 2',
          deadline: '2024-06-15',
          created_at: '2024-05-02T10:00:00Z',
          order: 2,
        },
      ],
    },
    {
      id: 2,
      name: 'Completed',
      board_id: 1,
      tasks: [
        {
          id: 3,
          name: 'Task 3',
          type_id: 2,
          description: 'This is task 3',
          deadline: '2024-05-15',
          created_at: '2024-05-01T10:00:00Z',
          order: 1,
        },
      ],
    },
  ],
};


  export const testEntities = [
    { id: 1, name: 'Test Board 1', description: 'Description for Test Board 1' },
    { id: 2, name: 'Test Board 2', description: 'Description for Test Board 2' },
    { id: 3, name: 'Test Board 3', description: 'Description for Test Board 3' },
  ];

  export const initialData = {
    types: {
      'type-1': { id: 'type-1', name: 'To Do', order: 0 },
      'type-2': { id: 'type-2', name: 'In Progress', order: 1 },
      'type-3': { id: 'type-3', name: 'Done', order: 2 },
    },
    tasks: {
      'task-1': { id: 'task-1', name: 'Task 1', description: 'This is task 1', deadline: '2024-05-30T10:00:00Z', created_at: '2024-05-15T10:00:00Z', typeId: 'type-1', order: 0 },
      'task-2': { id: 'task-2', name: 'Task 2', description: 'This is task 2', deadline: '2024-05-30T10:00:00Z', created_at: '2024-05-15T10:00:00Z', typeId: 'type-1', order: 1 },
      'task-3': { id: 'task-3', name: 'Task 3', description: 'This is task 3', deadline: '2024-05-30T10:00:00Z', created_at: '2024-05-15T10:00:00Z', typeId: 'type-3', order: 0 },
    },
  };
  
  
  
  
  