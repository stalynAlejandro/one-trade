import { formatDate, isValidDate } from '../dates';

describe('Util dates', () => {
  describe('method formatDate', () => {
    const date = new Date('2022-09-30T06:14:02.118Z');

    it('formats the date with the expected format if none is provided', () => {
      expect(formatDate(date)).toEqual('30/09/2022');
    });

    it('formats the date with the provided format', () => {
      expect(formatDate(date, false, 'yyyy-MM-dd')).toEqual('2022-09-30');
    });

    it('formats the date with the time included if specified', () => {
      expect(formatDate(date, true)).toEqual(
        `30/09/2022 ${date.getHours().toString().padStart(2, '0')}:14`,
      );
    });
  });

  describe('method isValidDate', () => {
    it('returns true if parameter is a date', () => {
      expect(isValidDate(new Date())).toBeTruthy();
    });

    it('returns true if parameter is a valid date string', () => {
      expect(isValidDate('2022-09-30T06:14:02.118Z')).toBeTruthy();
    });

    it('returns false if parameter is not provided', () => {
      expect(isValidDate()).toBeFalsy();
    });

    it('returns false if parameter is a non valid string', () => {
      expect(isValidDate('my test')).toBeFalsy();
    });
  });
});
