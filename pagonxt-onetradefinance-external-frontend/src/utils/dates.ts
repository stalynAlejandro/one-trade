import format from 'date-fns/format';

export const isValidDate = (date?: any) => {
  if (!date) {
    return false;
  }

  const parsedDate = new Date(date);
  return (
    parsedDate &&
    parsedDate instanceof Date &&
    !Number.isNaN(parsedDate.getTime())
  );
};

export const formatDate = (
  date: Date,
  withTime: boolean = false,
  formatString: string = '',
) => {
  if (!date || !(date instanceof Date) || Number.isNaN(date.getTime())) {
    return '';
  }

  if (formatString) {
    return format(date, formatString);
  }

  if (withTime) {
    return format(date, 'dd/MM/yyyy HH:mm');
  }

  return format(date, 'dd/MM/yyyy');
};
