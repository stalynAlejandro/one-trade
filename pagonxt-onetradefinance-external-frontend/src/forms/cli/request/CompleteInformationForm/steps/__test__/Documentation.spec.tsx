import { screen, waitFor } from '@testing-library/react';
import { act } from 'react-dom/test-utils';
import userEvent from '@testing-library/user-event';
import fetchMockJest from 'fetch-mock-jest';

import * as openNewWindowModule from '../../../../../../utils/openNewWindow';
import { renderComponentWithToast } from '../../../../../../testUtils/renderComponent';
import Documentation from '../Documentation';
import ApiUrls from '../../../../../../constants/apiUrls';

describe('Form Step CLI REQUEST CompleteInformationForm Documentation', () => {
  const blobUrl = 'http://test-blob-url';
  const entityCode = 'CLI-123';
  const defaultProps = {
    formData: {},
    onDataChange: jest.fn(),
    onSaveDraft: jest.fn(),
    onSubmitStep: jest.fn(),
    onSummarizeFormData: jest.fn(),
  };

  beforeEach(() => {
    fetchMockJest.mockClear();
    fetchMockJest.mock(ApiUrls.pricesCharts.cli(entityCode), 'my data', {
      delay: 0,
      overwriteRoutes: true,
    });
    global.URL.createObjectURL = jest.fn().mockReturnValue(blobUrl);
  });

  const renderStep = async (props: any = {}) => {
    await act(() => {
      renderComponentWithToast(<Documentation {...defaultProps} {...props} />);
    });
  };

  describe('First load display data', () => {
    it('renders the component successfully', async () => {
      await renderStep();

      expect(
        screen.getByText('T_attachDocumentationAndPriority'),
      ).toBeInTheDocument();
      expect(
        screen.getByText('T_priceLetter', { selector: 'h3' }),
      ).toBeInTheDocument();
      expect(screen.getByText('T_documentation')).toBeInTheDocument();
      expect(screen.getAllByText('T_searchFile')).toHaveLength(2);
      expect(screen.getAllByText('T_selectFileOrDrag')).toHaveLength(2);
    });
  });

  describe('Interactions, expected behaviours and method executions', () => {
    it('saves draft and opens a new window with the price letter with the right URL when the price letter button is clicked', async () => {
      jest
        .spyOn(openNewWindowModule, 'openNewWindow')
        .mockImplementation(() => jest.fn());

      defaultProps.onSaveDraft.mockReturnValue({
        entity: {
          code: entityCode,
        },
      });

      await renderStep();

      expect(defaultProps.onSaveDraft).not.toBeCalled();
      userEvent.click(
        screen.getByText('T_priceLetter', { selector: 'button span' }),
      );
      expect(defaultProps.onSaveDraft).toBeCalled();

      await waitFor(() => {
        expect(
          fetchMockJest.lastCall(ApiUrls.pricesCharts.cli(entityCode)),
        ).toBeTruthy();
      });

      await waitFor(() => {
        expect(openNewWindowModule.openNewWindow).lastCalledWith(blobUrl);
      });
    });
  });

  describe('Contextual information and messages', () => {
    it('displays summarized data for previous steps', async () => {
      await renderStep();
      expect(defaultProps.onSummarizeFormData).toBeCalledWith([
        'customer',
        'operationDetails',
        'advance',
      ]);
    });
  });
});
