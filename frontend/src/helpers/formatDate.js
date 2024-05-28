// export const formatDate = (dateString) => {
//     const options = {
//       day: '2-digit',
//       month: '2-digit',
//       year: 'numeric',
//       hour: '2-digit',
//       minute: '2-digit',
//       hour12: false, // 24-часовой формат
//     };
//     const date = new Date(dateString);
//     return new Intl.DateTimeFormat('ru-RU', options).format(date);
//   };

export const formatDate = (dateArray) => {
  const [year, month, day, hours, minutes, seconds, milliseconds] = dateArray;
  const date = new Date(year, month - 1, day, hours, minutes, seconds, milliseconds);
  const options = {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
    seconds: '2-digit',
    milliseconds: 'numeric',
    hour12: false, // 24-часовой формат
  };
  return new Intl.DateTimeFormat('ru-RU', options).format(date);
};

  export const formatDateDead = (dateString) => {
    const options = {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
    };
    const date = new Date(dateString);
    return new Intl.DateTimeFormat('ru-RU', options).format(date);
  };