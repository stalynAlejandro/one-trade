import React from 'react';
import fetchMock from 'fetch-mock-jest';
import { screen, waitFor, within } from '@testing-library/react';
import userEvent from '@testing-library/user-event';

import { renderComponent } from '../../../testUtils/renderComponent';
import ExchangeInsuranceSelector from '../ExchangeInsuranceSelector';
import { formatDate } from '../../../utils/dates';
import { formatNumber } from '../../../utils/formatNumber';
import { changeInputValue } from '../../../testUtils/changeInputValue';
import { ExchangeInsuranceDto } from '../../../api/types/ExchangeInsuranceDto';
import { get as mockedInsurances } from '../../../testUtils/mocks/data/exchangeInsurances';

const buyCurrency = 'EUR';
const sellCurrency = 'GBP';

describe('Container ExchangeInsuranceSelector', () => {
  const defaultProps = {
    amount: '123.00',
    currencyBuy: buyCurrency,
    currencySell: sellCurrency,
    customerId: 'BUC-1234',
    expirationDate: new Date().toJSON(),
    isBuyOperation: true,
    onApply: jest.fn(),
    onClose: jest.fn(),
  };

  const waitForInsurancesLoaded = async () =>
    waitFor(async () => {
      expect(
        await screen.findByText(mockedInsurances[0].exchangeInsuranceId),
      ).toBeInTheDocument();
    });

  fetchMock.mock(/fx-deals/, JSON.stringify(mockedInsurances));

  const renderWithProps = (props: any = {}) =>
    renderComponent(<ExchangeInsuranceSelector {...defaultProps} {...props} />);

  it('renders the component successfully', async () => {
    renderWithProps();
    await waitForInsurancesLoaded();

    expect(
      await screen.findByTestId('exchange-insurance-selector'),
    ).toBeInTheDocument();
    expect(screen.getByText('T_exchangeInsurance')).toBeInTheDocument();
  });

  it(`renders a datepicker with the date provided and as disabled`, async () => {
    renderWithProps();
    await waitForInsurancesLoaded();

    const datepickerEl = await screen.findByDisplayValue(
      formatDate(new Date(defaultProps.expirationDate)),
    );
    expect(datepickerEl).toBeInTheDocument();
    expect(datepickerEl).toBeDisabled();
  });

  it(`renders the number input with the value provided formatted and disabled`, async () => {
    renderWithProps();
    await waitForInsurancesLoaded();

    const inputEl = await screen.findByDisplayValue(
      `${formatNumber(defaultProps.amount)} ${defaultProps.currencyBuy}`,
    );
    expect(inputEl).toBeInTheDocument();
    expect(inputEl).toBeDisabled();
  });

  it('shows correctly the currencies provided', async () => {
    renderWithProps();
    await waitForInsurancesLoaded();

    const { findAllByTestId } = within(
      screen.getByTestId('exchange-insurance-currencies-container'),
    );
    const currencies = await findAllByTestId('currency');

    expect(currencies[0].textContent).toEqual(defaultProps.currencySell);
    expect(currencies[1].textContent).toEqual(defaultProps.currencyBuy);
  });

  it('executes the provided onApply and onClose methods when clicking the Continue button', async () => {
    renderWithProps();
    await waitForInsurancesLoaded();

    userEvent.click(await screen.findByText('T_continue'));

    expect(defaultProps.onApply).toBeCalled();
    expect(defaultProps.onClose).toBeCalled();
  });

  it('does not display any input if the insurance is not selected', async () => {
    renderWithProps();
    await waitForInsurancesLoaded();

    const radioButton = screen.getAllByTestId('radio-button')[0];
    const { getByText, queryByTestId } = within(radioButton.closest('tr')!);

    expect(
      getByText(`${formatNumber('0')} ${defaultProps.currencyBuy}`),
    ).toBeInTheDocument();
    expect(queryByTestId('form-input-numeric')).not.toBeInTheDocument();
  });

  it('displays a numeric input when the insurance is selected', async () => {
    renderWithProps();
    await waitForInsurancesLoaded();

    const radioButton = screen.getAllByTestId('radio-button')[0];
    userEvent.click(radioButton);
    const { findByTestId } = within(radioButton.closest('tr')!);

    const input = await findByTestId('form-input-numeric');
    expect(input).toBeInTheDocument();
  });

  it('does not let to set up an amount greater than the provided one', async () => {
    renderWithProps();
    await waitForInsurancesLoaded();

    const radioButton = screen.getAllByTestId('radio-button')[0];
    userEvent.click(radioButton);
    const { findByTestId } = within(radioButton.closest('tr')!);

    const input = await findByTestId('form-input-numeric');
    changeInputValue(input, '12');

    // 12 is lower than defaultProps.amount, so we can write it without problems
    expect(input).toHaveDisplayValue(
      `${formatNumber('12')} ${defaultProps.currencyBuy}`,
    );

    changeInputValue(input, '129');

    // 129 is greater defaultProps.amount, so we should not be able to write it
    expect(input).toHaveDisplayValue(
      `${formatNumber('12')} ${defaultProps.currencyBuy}`,
    );
  });

  it('executes the provided onApply method with the correct changed data', async () => {
    renderWithProps();
    await waitForInsurancesLoaded();

    const radioButton = screen.getAllByTestId('radio-button')[0];
    userEvent.click(radioButton);
    const { findByTestId } = within(radioButton.closest('tr')!);

    const input = await findByTestId('form-input-numeric');
    changeInputValue(input, '123');

    userEvent.click(await screen.findByText('T_continue'));

    expect(defaultProps.onApply).toBeCalledWith([
      {
        ...mockedInsurances[0],
        useAmount: '123.00',
      },
    ]);
  });

  it('executes the provided onApply method with the correct data when isMulti is set to true', async () => {
    renderWithProps({ isMulti: true });
    await waitForInsurancesLoaded();

    const checkboxes = screen.getAllByTestId('checkbox');
    userEvent.click(checkboxes[0]);
    userEvent.click(checkboxes[1]);

    const { findAllByTestId } = within(checkboxes[0].closest('tbody')!);

    const inputs = await findAllByTestId('form-input-numeric');
    changeInputValue(inputs[0], '12');
    changeInputValue(inputs[1], '25');

    userEvent.click(await screen.findByText('T_continue'));

    expect(defaultProps.onApply).toBeCalledWith([
      {
        ...mockedInsurances[0],
        useAmount: '12.00',
      },
      {
        ...mockedInsurances[1],
        useAmount: '25.00',
      },
    ]);
  });

  it('deletes correctly a previously selected insurance', async () => {
    renderWithProps({ isMulti: true });
    await waitForInsurancesLoaded();

    const checkboxes = screen.getAllByTestId('checkbox');
    userEvent.click(checkboxes[0]);
    userEvent.click(checkboxes[1]);

    const { findAllByTestId } = within(checkboxes[0].closest('tbody')!);

    const inputs = await findAllByTestId('form-input-numeric');
    changeInputValue(inputs[0], '12');
    changeInputValue(inputs[1], '25');

    userEvent.click(checkboxes[0]);

    userEvent.click(await screen.findByText('T_continue'));

    expect(defaultProps.onApply).toBeCalledWith([
      {
        ...mockedInsurances[1],
        useAmount: '25.00',
      },
    ]);
  });

  it('updates correctly the selected insurances provided in selectedInsurances prop', async () => {
    const { rerender } = renderWithProps({ isMulti: true });
    await waitForInsurancesLoaded();

    expect(
      screen.queryByDisplayValue(
        `${formatNumber('12.00')} ${defaultProps.currencyBuy}`,
      ),
    ).not.toBeInTheDocument();

    rerender(
      <ExchangeInsuranceSelector
        {...defaultProps}
        selectedInsurances={
          [
            { ...mockedInsurances[1], useAmount: '12.00' },
          ] as ExchangeInsuranceDto[]
        }
      />,
    );
    await waitForInsurancesLoaded();

    userEvent.click(await screen.findByText('T_continue'));
    expect(
      screen.getByDisplayValue(
        `${formatNumber('12.00')} ${defaultProps.currencyBuy}`,
      ),
    ).toBeInTheDocument();
    expect(defaultProps.onApply).toBeCalledWith([
      {
        ...mockedInsurances[1],
        useAmount: '12.00',
      },
    ]);
  });
});
