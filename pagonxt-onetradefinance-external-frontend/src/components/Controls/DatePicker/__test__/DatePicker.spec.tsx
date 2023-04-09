import React from 'react';
import { act, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';

import { renderComponent } from '../../../../testUtils/renderComponent';
import FormControlWrapper from '../../../../testUtils/FormControlWrapper';
import DatePicker from '../DatePicker';

describe('Control DatePicker', () => {
  const defaultProps = {
    onChange: jest.fn(),
    placeholder: 'My date control',
  };

  const renderWithProps = (props: any = {}) => {
    renderComponent(
      <FormControlWrapper
        component={DatePicker}
        {...defaultProps}
        {...props}
      />,
    );
  };

  it('renders the component successfully', () => {
    renderWithProps();
    expect(screen.getByTestId('date-picker')).toBeInTheDocument();
    expect(screen.getByText(defaultProps.placeholder)).toBeInTheDocument();
  });

  it('executes the provided onChange method when selecting new date', async () => {
    renderWithProps();

    const currentDate = new Date();
    const targetDate = new Date(
      currentDate.getFullYear(),
      currentDate.getMonth(),
      15,
    ); // Avoid high or low day numbers, as they can appear twice in next/prev month display

    userEvent.click(screen.getByTestId('date-picker').querySelector('label')!);

    await act(() => {
      userEvent.click(screen.getByText(targetDate.getDate()));
    });

    expect(defaultProps.onChange).toBeCalledWith(targetDate);
  });

  it('executes the provided onChange with empty value when clearing the field', async () => {
    renderWithProps({ value: '2022-07-20T09:03:18.689Z' });

    await act(() => {
      userEvent.click(
        screen
          .getByTestId('date-picker')
          .querySelector('.react-datepicker__close-icon')!,
      );
    });

    expect(defaultProps.onChange).toBeCalledWith(null);
  });

  it('shows the correct initial value when provided', () => {
    renderWithProps({ value: '2022-07-20T09:03:18.689Z' });
    expect(screen.getByDisplayValue('20/07/2022')).toBeInTheDocument();
  });

  it.skip('changes the year correctly', async () => {
    renderWithProps();
    userEvent.click(screen.getByTestId('date-picker').querySelector('label')!);

    const actualDate = new Date();

    userEvent.click(screen.getByText(actualDate.getFullYear()));
    userEvent.click(screen.getByText(actualDate.getFullYear() + 2));

    await act(() => {
      userEvent.click(screen.getByText('15'));
    });

    expect(defaultProps.onChange).toBeCalledWith(
      new Date(actualDate.getFullYear() + 2, actualDate.getMonth(), 15),
    );
  });

  it('changes to the previous month correctly', async () => {
    renderWithProps();

    userEvent.click(screen.getByTestId('date-picker').querySelector('label')!);
    userEvent.click(screen.getByTestId('icon-chevron-left-bold'));

    await act(() => {
      userEvent.click(screen.getByText('15'));
    });

    const actualDate = new Date();

    expect(defaultProps.onChange).toBeCalledWith(
      new Date(actualDate.getFullYear(), actualDate.getMonth() - 1, 15),
    );
  });

  it('changes to the next month correctly', async () => {
    renderWithProps();

    userEvent.click(screen.getByTestId('date-picker').querySelector('label')!);
    userEvent.click(screen.getByTestId('icon-chevron-right-bold'));

    await act(() => {
      userEvent.click(screen.getByText('15'));
    });

    const actualDate = new Date();

    expect(defaultProps.onChange).toBeCalledWith(
      new Date(actualDate.getFullYear(), actualDate.getMonth() + 1, 15),
    );
  });

  it('changes the date when written in the input', async () => {
    renderWithProps();
    const targetText = '5';

    await act(async () => {
      await userEvent.paste(
        screen.getByTestId('form-input-datepicker'),
        targetText,
      );
    });

    const actualDate = new Date();
    expect(defaultProps.onChange).toBeCalledWith(
      new Date(actualDate.getFullYear(), actualDate.getMonth(), 5),
    );
  });

  it('does not allow interaction with it when it is disabled', async () => {
    renderWithProps({ disabled: true });
    const targetText = '5';

    await act(async () => {
      await userEvent.paste(
        screen.getByTestId('form-input-datepicker'),
        targetText,
      );
    });

    expect(defaultProps.onChange).not.toBeCalled();

    userEvent.click(screen.getByTestId('date-picker').querySelector('label')!);
    expect(
      screen
        .getByTestId('date-picker')
        .querySelector('.react-datepicker-popper'),
    ).not.toBeInTheDocument();
  });
});
