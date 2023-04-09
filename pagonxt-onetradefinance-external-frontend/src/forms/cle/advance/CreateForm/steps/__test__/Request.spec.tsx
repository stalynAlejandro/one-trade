import { screen, waitFor, within } from '@testing-library/react';
import fetchMockJest from 'fetch-mock-jest';
import { act } from 'react-dom/test-utils';
import produce from 'immer';
import userEvent from '@testing-library/user-event';

import { renderComponent } from '../../../../../../testUtils/renderComponent';
import {
  input,
  inputSelect,
  select,
  textarea,
} from '../../../../../../testUtils/controls';
import Request from '../Request';
import { get as collectionsResponse } from '../../../../../../testUtils/mocks/data/customerCollections';
import { get as riskLinesResponse } from '../../../../../../testUtils/mocks/data/riskLines';
import { get as currenciesResponse } from '../../../../../../testUtils/mocks/data/currencies';
import { get as exchangeInsurances } from '../../../../../../testUtils/mocks/data/exchangeInsurances';
import theme from '../../../../../../resources/theme';
import { changeInputValue } from '../../../../../../testUtils/changeInputValue';
import * as userSelector from '../../../../../../redux/selectors/user';
import { formatDate } from '../../../../../../utils/dates';
import { formatNumber } from '../../../../../../utils/formatNumber';

describe('Form Step CLE ADVANCE CreateForm Request', () => {
  const defaultProps = {
    formData: {
      customer: { personNumber: 'BUC-123456' },
    },
    onDataChange: jest.fn(),
    onSubmitStep: jest.fn(),
    onSummarizeFormData: jest.fn(),
  };

  const renderStep = async (props: any = {}) => {
    await act(() => {
      renderComponent(<Request {...defaultProps} {...props} />);
    });

    await waitFor(() => {
      expect(fetchMockJest.lastCall(/risk-lines/)).toBeTruthy();
    });
  };

  beforeEach(() => {
    fetchMockJest.reset();
    fetchMockJest.mock(
      /export-collections/,
      { body: collectionsResponse, status: 200 },
      { overwriteRoutes: true },
    );
    fetchMockJest.mock(
      /fx-deals/,
      { body: exchangeInsurances, status: 200 },
      { overwriteRoutes: true },
    );
    fetchMockJest.mock(
      /currencies/,
      { body: currenciesResponse, status: 200 },
      { overwriteRoutes: true },
    );
    fetchMockJest.mock(
      /risk-lines/,
      { body: riskLinesResponse, status: 200 },
      { overwriteRoutes: true },
    );
    jest.spyOn(userSelector, 'getUser').mockImplementation(() => ({} as any));
  });

  describe('First load display data', () => {
    it('renders the component successfully', async () => {
      await renderStep();

      expect(
        screen.getByText('T_completeClientAndOperationData'),
      ).toBeInTheDocument();
      expect(
        select.getDisplayValue(
          select.getByPlaceholderText('T_requestReference'),
        ),
      ).toEqual('');
      expect(
        inputSelect.input.getDisplayValue(
          inputSelect.input.getByPlaceholderText('T_advanceAmount'),
        ),
      ).toEqual('');
      expect(
        input.getDisplayValue(input.getByPlaceholderText('T_expiration')),
      ).toEqual('');
      expect(
        select.getDisplayValue(select.getByPlaceholderText('T_riskLine')),
      ).toEqual('');
    });

    it('displays all fields without errors on load', async () => {
      await renderStep();

      expect(select.getByPlaceholderText('T_requestReference')).not.toHaveStyle(
        {
          borderBottomColor: theme.colors.boston,
        },
      );
      expect(input.getByPlaceholderText('T_expiration')).not.toHaveStyle({
        borderBottomColor: theme.colors.boston,
      });
      expect(select.getByPlaceholderText('T_riskLine')).not.toHaveStyle({
        borderBottomColor: theme.colors.boston,
      });
      expect(
        inputSelect.input.getByPlaceholderText('T_advanceAmount'),
      ).not.toHaveStyle({
        borderBottomColor: theme.colors.boston,
      });
    });

    it('renders the values provided in formData', async () => {
      const newProps = produce<any>(defaultProps, (immerDraft) => {
        immerDraft.formData.request = {
          advanceAmount: '1234.00',
          advanceCurrency: {
            currency: 'USD',
            id: 'USD',
          },
          comments: 'My comments',
          exchangeInsurances: [exchangeInsurances[0]],
          exportCollection: collectionsResponse[1],
          office: '1234',
          requestExpiration: '2023-12-02T00:00:00.000Z',
          riskLine: riskLinesResponse[0],
        };
      });

      await renderStep(newProps);

      const data = newProps.formData.request;

      expect(
        select.getDisplayValue(
          select.getByPlaceholderText('T_requestReference'),
        ),
      ).toEqual(data.exportCollection.code);

      expect(
        inputSelect.input.getDisplayValue(
          inputSelect.input.getByPlaceholderText('T_advanceAmount'),
        ),
      ).toEqual(formatNumber(data.advanceAmount));
      expect(
        inputSelect.select.getDisplayValue(
          inputSelect.select.getByPlaceholderText('T_advanceAmount'),
        ),
      ).toEqual(data.advanceCurrency.currency);

      expect(
        input.getDisplayValue(input.getByPlaceholderText('T_expiration')),
      ).toEqual(formatDate(new Date(data.requestExpiration)));

      expect(
        input.getDisplayValue(input.getByPlaceholderText('T_office')),
      ).toEqual(data.office);
      expect(
        textarea.getDisplayValue(
          textarea.getByPlaceholderText('T_comments', true),
        ),
      ).toEqual(data.comments);

      expect(
        screen.getByText('T_exchangeInsurance', {
          selector: '[data-testid="step-subtitle"]',
        }),
      ).toBeInTheDocument();

      await waitFor(() => {
        expect(
          select.getDisplayValue(select.getByPlaceholderText('T_riskLine')),
        ).toEqual(data.riskLine.iban);
      });
    });
  });

  describe('First load fetch data', () => {
    it('fetches the export collections on load and displays them', async () => {
      await renderStep();

      expect(
        fetchMockJest
          .lastCall(/export-collections/)?.[0]
          .includes(`customer=${defaultProps.formData.customer.personNumber}`),
      ).toBeTruthy();

      userEvent.click(
        select.getInteractiveElement(
          select.getByPlaceholderText('T_requestReference'),
        )!,
      );
      collectionsResponse.forEach((col) => {
        expect(screen.getByText(col.code)).toBeInTheDocument();
      });
    });

    it('fetches currencies on load and displays them', async () => {
      await renderStep();

      expect(
        fetchMockJest
          .lastCall(/currencies/)?.[0]
          .includes(`product=CLE&event=ADVANCE`),
      ).toBeTruthy();

      userEvent.click(
        inputSelect.select.getInteractiveElement(
          inputSelect.select.getByPlaceholderText('T_advanceAmount'),
        ),
      );

      currenciesResponse.forEach((cur) => {
        expect(screen.getAllByText(cur.currency).length).toBeTruthy();
      });
    });

    it('fetches risk lines on load and displays them', async () => {
      const newProps = produce<any>(defaultProps, (immerDraft) => {
        immerDraft.formData.request = {
          advanceAmount: '1234.00',
          advanceCurrency: {
            currency: 'USD',
            id: 'USD',
          },
          comments: 'My comments',
          exportCollection: collectionsResponse[1],
          office: '1234',
          requestExpiration: '2023-12-02T00:00:00.000Z',
        };
      });

      await renderStep(newProps);

      const data = newProps.formData.request;

      expect(
        fetchMockJest
          .lastCall(/risk-lines/)?.[0]
          .includes(`client=${defaultProps.formData.customer.personNumber}`),
      ).toBeTruthy();
      expect(
        fetchMockJest
          .lastCall(/risk-lines/)?.[0]
          .includes(`operation_amount=${data.advanceAmount}`),
      ).toBeTruthy();
      expect(
        fetchMockJest
          .lastCall(/risk-lines/)?.[0]
          .includes(`operation_currency=${data.advanceCurrency.currency}`),
      ).toBeTruthy();
      expect(
        fetchMockJest.lastCall(/risk-lines/)?.[0].includes(`product_id=CLE`),
      ).toBeTruthy();

      userEvent.click(
        select.getInteractiveElement(
          select.getByPlaceholderText('T_riskLine'),
        )!,
      );

      await waitFor(() => {
        riskLinesResponse.forEach((rl) => {
          expect(screen.getByText(rl.iban)).toBeInTheDocument();
        });
      });
    });
  });

  describe('Interactions, expected behaviours and method executions', () => {
    it('sets the office field to readonly if office is set in session', async () => {
      jest
        .spyOn(userSelector, 'getUser')
        .mockImplementation(() => ({ office: '1234' } as any));
      await renderStep();

      expect(
        input.getInteractiveElement(input.getByPlaceholderText('T_office')),
      ).toHaveAttribute('readonly');
    });

    it('executes provided onDataChange method when changing a field value', async () => {
      await renderStep();

      userEvent.click(
        select.getInteractiveElement(
          select.getByPlaceholderText('T_requestReference'),
        ),
      );
      userEvent.click(screen.getByText(collectionsResponse[1].code));
      expect(defaultProps.onDataChange).lastCalledWith(
        expect.objectContaining({
          exportCollection: collectionsResponse[1],
        }),
      );

      userEvent.paste(
        inputSelect.input.getInteractiveElement(
          inputSelect.input.getByPlaceholderText('T_advanceAmount'),
        ),
        '123',
      );
      expect(defaultProps.onDataChange).lastCalledWith(
        expect.objectContaining({
          advanceAmount: '123.00',
        }),
      );

      userEvent.click(
        inputSelect.select.getInteractiveElement(
          inputSelect.select.getByPlaceholderText('T_advanceAmount'),
        ),
      );
      userEvent.click(screen.getByText(currenciesResponse[0].currency));
      expect(defaultProps.onDataChange).lastCalledWith(
        expect.objectContaining({
          advanceCurrency: currenciesResponse[0],
        }),
      );

      userEvent.click(
        input.getInteractiveElement(input.getByPlaceholderText('T_expiration')),
      );
      userEvent.click(screen.getByText('15'));
      const date = new Date();
      date.setDate(15);
      date.setHours(0, 0, 0, 0);

      expect(defaultProps.onDataChange).lastCalledWith(
        expect.objectContaining({
          requestExpiration: date.toJSON(),
        }),
      );

      userEvent.paste(
        textarea.getInteractiveElement(
          textarea.getByPlaceholderText('T_comments', true),
        ),
        'my comments',
      );
      expect(defaultProps.onDataChange).lastCalledWith(
        expect.objectContaining({ comments: 'my comments' }),
      );

      await waitFor(() => {
        expect(
          fetchMockJest
            .lastCall(/risk-lines/)?.[0]
            .includes('operation_amount'),
        ).toBeTruthy();
      });

      userEvent.click(
        select.getInteractiveElement(select.getByPlaceholderText('T_riskLine')),
      );
      userEvent.click(await screen.findByText(riskLinesResponse[1].iban));
      expect(defaultProps.onDataChange).lastCalledWith(
        expect.objectContaining({
          riskLine: riskLinesResponse[1],
        }),
      );
    });

    it('executes the provided onSubmitStep method when submitting the form with valid information', async () => {
      await renderStep();

      userEvent.click(screen.getByText('T_continue'));
      expect(defaultProps.onSubmitStep).not.toHaveBeenCalled();

      userEvent.click(
        select.getInteractiveElement(
          select.getByPlaceholderText('T_requestReference'),
        ),
      );
      userEvent.click(screen.getByText(collectionsResponse[1].code));

      userEvent.paste(
        inputSelect.input.getInteractiveElement(
          inputSelect.input.getByPlaceholderText('T_advanceAmount'),
        ),
        '123',
      );

      userEvent.click(
        inputSelect.select.getInteractiveElement(
          inputSelect.select.getByPlaceholderText('T_advanceAmount'),
        ),
      );
      userEvent.click(screen.getByText(currenciesResponse[0].currency));

      userEvent.click(
        input.getInteractiveElement(input.getByPlaceholderText('T_expiration')),
      );
      userEvent.click(screen.getByText('15'));

      await waitFor(() => {
        expect(
          fetchMockJest
            .lastCall(/risk-lines/)?.[0]
            .includes('operation_amount'),
        ).toBeTruthy();
      });

      userEvent.click(
        select.getInteractiveElement(select.getByPlaceholderText('T_riskLine')),
      );
      userEvent.click(await screen.findByText(riskLinesResponse[1].iban));

      userEvent.paste(
        input.getInteractiveElement(input.getByPlaceholderText('T_office')),
        '1234',
      );

      userEvent.paste(
        textarea.getInteractiveElement(
          textarea.getByPlaceholderText('T_comments', true),
        ),
        'my comments',
      );

      const date = new Date();
      date.setDate(15);
      date.setHours(0, 0, 0, 0);

      userEvent.click(screen.getByText('T_continue'));
      expect(defaultProps.onDataChange).lastCalledWith({
        advanceAmount: '123.00',
        advanceCurrency: currenciesResponse[0],
        comments: 'my comments',
        exportCollection: collectionsResponse[1],
        office: '1234',
        requestExpiration: date.toJSON(),
        riskLine: riskLinesResponse[1],
      });
      expect(defaultProps.onSubmitStep).lastCalledWith();
    });

    it('allows searching within received collections', async () => {
      await renderStep();
      userEvent.click(
        select.getInteractiveElement(
          select.getByPlaceholderText('T_requestReference'),
        ),
      );

      act(() => {
        userEvent.paste(
          select
            .getByPlaceholderText('T_requestReference')!
            .querySelector('input')!,
          collectionsResponse[1].code,
        );
      });

      expect(
        screen.queryByText(collectionsResponse[0].code),
      ).not.toBeInTheDocument();
      expect(
        screen.queryByText(collectionsResponse[1].code),
      ).toBeInTheDocument();
    });

    it('renders the Exchange Insurance button when the advance currency is different from the collection currency', async () => {
      await renderStep();

      expect(screen.queryByText('T_exchangeInsurance')).not.toBeInTheDocument();
      userEvent.click(
        select.getInteractiveElement(
          select.getByPlaceholderText('T_requestReference'),
        ),
      );
      userEvent.click(screen.getByText(collectionsResponse[0].code));
      userEvent.paste(
        inputSelect.input.getInteractiveElement(
          inputSelect.input.getByPlaceholderText('T_advanceAmount'),
        ),
        '1000',
      );

      userEvent.click(
        inputSelect.select.getInteractiveElement(
          inputSelect.select.getByPlaceholderText('T_advanceAmount'),
        ),
      );
      act(() => {
        userEvent.click(screen.getByText(currenciesResponse[0].currency));
      });

      expect(screen.queryByText('T_exchangeInsurance')).toBeInTheDocument();
    });

    it('displays the exchange insurance screen when pressing the button', async () => {
      await renderStep();

      expect(screen.queryByText('T_exchangeInsurance')).not.toBeInTheDocument();
      userEvent.click(
        select.getInteractiveElement(
          select.getByPlaceholderText('T_requestReference'),
        ),
      );
      userEvent.click(screen.getByText(collectionsResponse[0].code));
      userEvent.paste(
        inputSelect.input.getInteractiveElement(
          inputSelect.input.getByPlaceholderText('T_advanceAmount'),
        ),
        '1000',
      );

      userEvent.click(
        inputSelect.select.getInteractiveElement(
          inputSelect.select.getByPlaceholderText('T_advanceAmount'),
        ),
      );
      act(() => {
        userEvent.click(screen.getByText(currenciesResponse[0].currency));
      });

      act(() => {
        userEvent.click(screen.getByText('T_exchangeInsurance'));
      });

      await waitFor(() => {
        expect(fetchMockJest.lastCall(/fx-deals/)).toBeTruthy();
      });

      expect(
        screen.getByTestId('exchange-insurance-selector'),
      ).toBeInTheDocument();
    });

    it('allows deleting an exchange insurance from the summary control', async () => {
      const newProps = produce<any>(defaultProps, (immerDraft) => {
        immerDraft.formData.request = {
          advanceAmount: 2222,
          advanceCurrency: currenciesResponse[0],
          exchangeInsurances: [exchangeInsurances[0]],
          exportCollection: collectionsResponse[0],
        };
      });

      await renderStep(newProps);

      const { getByTestId, getByText } = within(
        screen.getByTestId('exchange-insurance-summary'),
      );
      act(() => {
        userEvent.click(getByTestId('icon-chevron-down-bold'));
      });

      act(() => {
        userEvent.click(getByText('T_delete'));
      });

      expect(
        screen.queryByTestId('exchange-insurance-summary'),
      ).not.toBeInTheDocument();
      expect(
        await screen.findByText('T_exchangeInsurance', { selector: 'button' }),
      ).toBeInTheDocument();
    });

    it('allows editing the selected exchange insurances by clicking the edit button', async () => {
      const newProps = produce<any>(defaultProps, (immerDraft) => {
        immerDraft.formData.request = {
          advanceAmount: 2222,
          advanceCurrency: currenciesResponse[0],
          exchangeInsurances: [exchangeInsurances[0]],
          exportCollection: collectionsResponse[0],
        };
      });

      await renderStep(newProps);

      const { getByTestId, getByText } = within(
        screen.getByTestId('exchange-insurance-summary'),
      );
      act(() => {
        userEvent.click(getByTestId('icon-chevron-down-bold'));
      });

      act(() => {
        userEvent.click(getByText('T_edit'));
      });

      await waitFor(() => {
        expect(fetchMockJest.lastCall(/fx-deals/)).toBeTruthy();
      });

      expect(
        screen.queryByTestId('exchange-insurance-summary'),
      ).not.toBeInTheDocument();
      expect(
        await screen.findByTestId('exchange-insurance-selector'),
      ).toBeInTheDocument();
    });

    it('renders a table showing the reference collection data when one is selected', async () => {
      await renderStep();

      expect(screen.queryByTestId('table')).not.toBeInTheDocument();
      userEvent.click(
        select.getInteractiveElement(
          select.getByPlaceholderText('T_requestReference'),
        ),
      );
      userEvent.click(screen.getByText(collectionsResponse[0].code));
      expect(screen.queryByTestId('table')).toBeInTheDocument();
    });

    it('renders a table showing the risk line data when one is selected', async () => {
      const newProps = produce<any>(defaultProps, (immerDraft) => {
        immerDraft.formData.request = {
          advanceAmount: '1234.00',
          advanceCurrency: {
            currency: 'USD',
            id: 'USD',
          },
          comments: 'My comments',
          office: '1234',
          requestExpiration: '2023-12-02T00:00:00.000Z',
        };
      });

      await renderStep(newProps);

      expect(screen.queryByTestId('table')).not.toBeInTheDocument();
      userEvent.click(
        select.getInteractiveElement(select.getByPlaceholderText('T_riskLine')),
      );
      userEvent.click(await screen.findByText(riskLinesResponse[0].iban));
      expect(screen.queryByTestId('table')).toBeInTheDocument();
    });
  });

  describe('Field validation', () => {
    it('displays errors in the required fields when they are empty and the form is submitted', async () => {
      await renderStep();

      userEvent.click(screen.getByText('T_continue'));
      expect(select.getByPlaceholderText('T_riskLine')).toHaveStyle({
        borderBottomColor: theme.colors.boston,
      });
      expect(select.getByPlaceholderText('T_requestReference')).toHaveStyle({
        borderBottomColor: theme.colors.boston,
      });
      expect(input.getByPlaceholderText('T_office')).toHaveStyle({
        borderBottomColor: theme.colors.boston,
      });
    });

    it('forces office to be 4 digits', async () => {
      await renderStep();

      changeInputValue(
        input.getInteractiveElement(input.getByPlaceholderText('T_office')),
        '123',
      );

      userEvent.click(screen.getByText('T_continue'));
      expect(input.getByPlaceholderText('T_office')).toHaveStyle({
        borderBottomColor: theme.colors.boston,
      });

      changeInputValue(
        input.getInteractiveElement(input.getByPlaceholderText('T_office')),
        '1234',
      );

      userEvent.click(screen.getByText('T_continue'));
      expect(input.getByPlaceholderText('T_office')).not.toHaveStyle({
        borderBottomColor: theme.colors.boston,
      });
    });
  });

  describe('Contextual information and messages', () => {
    it('displays summarized data for previous steps', async () => {
      await renderStep();
      expect(defaultProps.onSummarizeFormData).lastCalledWith(['customer']);
    });
  });
});
