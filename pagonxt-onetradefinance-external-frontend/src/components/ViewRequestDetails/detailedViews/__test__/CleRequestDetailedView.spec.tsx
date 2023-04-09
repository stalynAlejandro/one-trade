import { screen, waitFor, within } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import produce from 'immer';
import fetchMockJest from 'fetch-mock-jest';

import { get as mockedRequestResponse } from '../../../../testUtils/mocks/data/cle/request/completeInformation';
import { renderComponentWithToast } from '../../../../testUtils/renderComponent';
import * as openNewWindowModule from '../../../../utils/openNewWindow';
import { formatDate } from '../../../../utils/dates';
import { formatNumber } from '../../../../utils/formatNumber';
import CleRequestDetailedView from '../CleRequestDetailedView';
import ApiUrls from '../../../../constants/apiUrls';

describe('Component ViewRequestDetails CleRequestDetailedView', () => {
  const blobUrl = 'http://test-blob-url';
  const defaultProps = {
    requestDetails: mockedRequestResponse.entity,
  };

  beforeEach(() => {
    fetchMockJest.mockClear();
    fetchMockJest.mock(new RegExp(ApiUrls.documents), 'my data', {
      delay: 0,
      overwriteRoutes: true,
    });
    global.URL.createObjectURL = jest.fn().mockReturnValue(blobUrl);
  });

  const renderWithProps = (props: any = {}) => {
    renderComponentWithToast(
      <CleRequestDetailedView {...defaultProps} {...props} />,
    );
  };

  it('shows the expected detailed information', async () => {
    renderWithProps();
    const mockedRequest = mockedRequestResponse.entity.request;

    expect(
      await screen.findByText(
        mockedRequest.operationDetails.clientAccount.iban,
      ),
    ).toBeInTheDocument();
    expect(
      screen.getByText(mockedRequest.operationDetails.commissionAccount.iban),
    ).toBeInTheDocument();
    expect(
      screen.getByText(
        `${formatNumber(mockedRequest.operationDetails.collectionAmount)} ${
          mockedRequest.operationDetails.collectionCurrency.currency
        }`,
      ),
    ).toBeInTheDocument();
    expect(
      screen.getByText(mockedRequest.documentation.files[0].name),
    ).toBeInTheDocument();

    // Financing section
    expect(
      screen.getByText(
        `${formatNumber(mockedRequest.advance.advanceAmount)} ${
          mockedRequest.advance.advanceCurrency.currency
        }`,
      ),
    ).toBeInTheDocument();
    expect(
      screen.getByText(
        formatDate(new Date(mockedRequest.advance.advanceExpiration)),
      ),
    ).toBeInTheDocument();
    expect(
      screen.getByText(mockedRequest.advance.riskLine.iban),
    ).toBeInTheDocument();
  });

  it('displays no files when data contains no uploaded documents', async () => {
    const newMockedRequestResponse = produce(mockedRequestResponse, (draft) => {
      // eslint-disable-next-line no-param-reassign
      draft.entity.request.documentation.files = [];
    });

    renderWithProps({ requestDetails: newMockedRequestResponse.entity });
    const mockedRequest = newMockedRequestResponse.entity.request;

    expect(
      await screen.findByText(
        mockedRequest.operationDetails.clientAccount.iban,
      ),
    ).toBeInTheDocument();
    const { getByText } = within(
      screen.getByText('T_attachedDocumentation').closest('div')!,
    );
    expect(getByText('T_no')).toBeInTheDocument();
  });

  it('displays files in documentation and opens the file in a new window when clicked', async () => {
    jest
      .spyOn(openNewWindowModule, 'openNewWindow')
      .mockImplementation(() => jest.fn());
    renderWithProps();

    const mockedRequestFiles =
      mockedRequestResponse.entity.request.documentation.files;

    await Promise.all(
      mockedRequestFiles.map(async (file) => {
        expect(await screen.findByText(file.name)).toBeInTheDocument();
      }),
    );

    userEvent.click(screen.getByText(mockedRequestFiles[0].name));

    await waitFor(() => {
      expect(
        fetchMockJest
          .lastCall(new RegExp(ApiUrls.documents))?.[0]
          .includes(mockedRequestFiles[0].id),
      ).toBeTruthy();
    });

    await waitFor(() => {
      expect(openNewWindowModule.openNewWindow).lastCalledWith(blobUrl);
    });
  });
});
