import { screen, within } from '@testing-library/react';
import fetchMockJest from 'fetch-mock-jest';
import { act } from 'react-dom/test-utils';
import produce from 'immer';
import userEvent from '@testing-library/user-event';

import { renderComponent } from '../../../../../../testUtils/renderComponent';
import { input, select, textarea } from '../../../../../../testUtils/controls';
import Request from '../Request';
import { get as financingRequestsResponse } from '../../../../../../testUtils/mocks/data/customerFinancingRequests';
import theme from '../../../../../../resources/theme';
import { changeInputValue } from '../../../../../../testUtils/changeInputValue';
import * as userSelector from '../../../../../../redux/selectors/user';
import { formatNumber } from '../../../../../../utils/formatNumber';
import { formatDate } from '../../../../../../utils/dates';

describe('Form Step CLI FINANCING MODIFICATION CreateForm Request', () => {
  const defaultProps = {
    formData: {
      customer: { personNumber: 'BUC-123456' },
    },
    onDataChange: jest.fn(),
    onSubmitStep: jest.fn(),
    onSummarizeFormData: jest.fn(),
  };

  const renderStep = async (props: any = {}) => {
    jest.useFakeTimers();

    await act(() => {
      renderComponent(<Request {...defaultProps} {...props} />);
    });

    act(() => {
      jest.advanceTimersByTime(300);
    });

    jest.useRealTimers();
  };

  beforeAll(() => {
    fetchMockJest.mock(
      /financing-collections/,
      { body: financingRequestsResponse, status: 200 },
      { overwriteRoutes: true },
    );
  });

  beforeEach(() => {
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
          select.getByPlaceholderText('T_collectionReference'),
        ),
      ).toEqual('');
    });

    it('displays all fields without errors on load', async () => {
      await renderStep();

      expect(
        select.getByPlaceholderText('T_collectionReference'),
      ).not.toHaveStyle({
        borderBottomColor: theme.colors.boston,
      });
      expect(textarea.getByPlaceholderText('T_comments')).not.toHaveStyle({
        borderBottomColor: theme.colors.boston,
      });
    });

    it('renders the values provided in formData', async () => {
      const newProps = produce<any>(defaultProps, (immerDraft) => {
        immerDraft.formData.request = {
          comments: 'My comments',
          financingRequest: financingRequestsResponse[1],
          office: '1234',
        };
      });

      await renderStep(newProps);

      const data = newProps.formData.request;

      expect(
        select.getDisplayValue(
          select.getByPlaceholderText('T_collectionReference'),
        ),
      ).toEqual(data.financingRequest.code);
      expect(
        input.getDisplayValue(input.getByPlaceholderText('T_applicantOffice')),
      ).toEqual(data.office);
      expect(
        textarea.getDisplayValue(textarea.getByPlaceholderText('T_comments')),
      ).toEqual(data.comments);
    });
  });

  describe('First load fetch data', () => {
    it('fetches the financing requests on load and displays them', async () => {
      await renderStep();

      expect(
        fetchMockJest
          .lastCall(/financing-collections/)?.[0]
          .includes(`customer=${defaultProps.formData.customer.personNumber}`),
      ).toBeTruthy();

      userEvent.click(
        select.getInteractiveElement(
          select.getByPlaceholderText('T_collectionReference'),
        )!,
      );
      financingRequestsResponse.forEach((col) => {
        expect(screen.getByText(col.code)).toBeInTheDocument();
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
        input.getInteractiveElement(
          input.getByPlaceholderText('T_applicantOffice'),
        ),
      ).toHaveAttribute('readonly');
    });

    it('executes provided onDataChange method when changing a field value', async () => {
      await renderStep();

      userEvent.click(
        select.getInteractiveElement(
          select.getByPlaceholderText('T_collectionReference'),
        ),
      );
      userEvent.click(screen.getByText(financingRequestsResponse[1].code));
      expect(defaultProps.onDataChange).toBeCalledWith(
        expect.objectContaining({
          financingRequest: financingRequestsResponse[1],
        }),
      );

      userEvent.paste(
        textarea.getInteractiveElement(
          textarea.getByPlaceholderText('T_comments'),
        ),
        'my comments',
      );
      expect(defaultProps.onDataChange).toBeCalledWith(
        expect.objectContaining({ comments: 'my comments' }),
      );
    });

    it('displays selected collection data upon selection', async () => {
      await renderStep();
      const selectedRequest = financingRequestsResponse[1];

      userEvent.click(
        select.getInteractiveElement(
          select.getByPlaceholderText('T_collectionReference'),
        ),
      );
      userEvent.click(screen.getByText(selectedRequest.code));

      const { getByText } = within(screen.getByTestId('table-container'));
      expect(getByText(selectedRequest.code)).toBeInTheDocument();
      expect(
        getByText(formatDate(new Date(selectedRequest.approvalDate))),
      ).toBeInTheDocument();
      expect(
        getByText(formatNumber(selectedRequest.amount)),
      ).toBeInTheDocument();
      expect(getByText(selectedRequest.currency)).toBeInTheDocument();
    });

    it('executes the provided onSubmitStep method when submitting the form with valid information', async () => {
      await renderStep();

      userEvent.click(screen.getByText('T_continue'));
      expect(defaultProps.onSubmitStep).not.toHaveBeenCalled();

      userEvent.click(
        select.getInteractiveElement(
          select.getByPlaceholderText('T_collectionReference'),
        ),
      );
      userEvent.click(screen.getByText(financingRequestsResponse[1].code));

      userEvent.paste(
        input.getInteractiveElement(
          input.getByPlaceholderText('T_applicantOffice'),
        ),
        '1234',
      );

      userEvent.paste(
        textarea.getInteractiveElement(
          textarea.getByPlaceholderText('T_comments'),
        ),
        'my comments',
      );

      userEvent.click(screen.getByText('T_continue'));
      expect(defaultProps.onDataChange).lastCalledWith({
        comments: 'my comments',
        financingRequest: financingRequestsResponse[1],
        office: '1234',
      });
      expect(defaultProps.onSubmitStep).lastCalledWith();
    });
  });

  describe('Field validation', () => {
    it('displays errors in the required fields when they are empty and the form is submitted', async () => {
      await renderStep();

      userEvent.click(screen.getByText('T_continue'));
      expect(select.getByPlaceholderText('T_collectionReference')).toHaveStyle({
        borderBottomColor: theme.colors.boston,
      });
      expect(input.getByPlaceholderText('T_applicantOffice')).toHaveStyle({
        borderBottomColor: theme.colors.boston,
      });
      expect(textarea.getByPlaceholderText('T_comments')).toHaveStyle({
        borderBottomColor: theme.colors.boston,
      });
    });

    it('forces office to be 4 digits', async () => {
      await renderStep();

      changeInputValue(
        input.getInteractiveElement(
          input.getByPlaceholderText('T_applicantOffice'),
        ),
        '123',
      );

      userEvent.click(screen.getByText('T_continue'));
      expect(input.getByPlaceholderText('T_applicantOffice')).toHaveStyle({
        borderBottomColor: theme.colors.boston,
      });

      changeInputValue(
        input.getInteractiveElement(
          input.getByPlaceholderText('T_applicantOffice'),
        ),
        '1234',
      );

      userEvent.click(screen.getByText('T_continue'));
      expect(input.getByPlaceholderText('T_applicantOffice')).not.toHaveStyle({
        borderBottomColor: theme.colors.boston,
      });
    });
  });

  describe('Contextual information and messages', () => {
    it('displays summarized data for previous steps', async () => {
      await renderStep();
      expect(defaultProps.onSummarizeFormData).toBeCalledWith(['customer']);
    });
  });
});
