export const formatDate = (dateString) => {
    const options = {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
      hour12: false, // 24-часовой формат
    };
    const date = new Date(dateString);
    return new Intl.DateTimeFormat('ru-RU', options).format(date);
  };