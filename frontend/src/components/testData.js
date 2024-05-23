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
  