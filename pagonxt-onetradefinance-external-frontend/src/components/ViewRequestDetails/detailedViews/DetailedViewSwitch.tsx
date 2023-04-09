import React from 'react';

import { CreateApiDataDto as ModificationCreateApiDto } from '../../../api/types/cle/modification/CreateApiDataDto';
import { CreateApiDataDto as RequestCreateApiDto } from '../../../api/types/cle/request/CreateApiDataDto';
import { CreateApiDataDto as AdvanceCreateApiDto } from '../../../api/types/cle/advance/CreateApiDataDto';
import { CreateApiDataDto as AdvanceModificationCreateApiDto } from '../../../api/types/cle/advanceModification/CreateApiDataDto';
import { CreateApiDataDto as AdvanceCancellationCreateApiDto } from '../../../api/types/cle/advanceCancellation/CreateApiDataDto';
import { CreateApiDataDto as OtherOperationsCreateApiDto } from '../../../api/types/cle/otherOperations/CreateApiDataDto';
import { CreateApiDataDto as CliModificationCreateApiDto } from '../../../api/types/cli/modification/CreateApiDataDto';
import { CreateApiDataDto as CliRequestCreateApiDto } from '../../../api/types/cli/request/CreateApiDataDto';
import { CreateApiDataDto as CliOtherOperationsCreateApiDto } from '../../../api/types/cli/otherOperations/CreateApiDataDto';
import { CreateApiDataDto as CliPaymentAccountlessCreateApiDto } from '../../../api/types/cli/paymentAccountless/CreateApiDataDto';
import { CreateApiDataDto as CliPaymentChargeCreateApiDto } from '../../../api/types/cli/paymentCharge/CreateApiDataDto';
import { RequestDetailsDto } from '../../../api/types/RequestDetailsDto';
import eventTypes, { EventTypes } from '../../../enums/eventTypes';
import productTypes, { ProductTypes } from '../../../enums/productTypes';
import CleAdvanceDetailedView from './CleAdvanceDetailedView';
import CleModificationDetailedView from './CleModificationDetailedView';
import CleRequestDetailedView from './CleRequestDetailedView';
import CleAdvanceModificationDetailedView from './CleAdvanceModificationDetailedView';
import CleAdvanceCancellationDetailedView from './CleAdvanceCancellationDetailedView';
import CleOtherOperationsDetailedView from './CleOtherOperationsDetailedView';
import CliRequestDetailedView from './cliRequestDetailedView';
import CliModificationDetailedView from './cliModificationDetailView';
import CliPaymentChargeDetailedView from './cliPaymentChargeDetailView';
import CliPaymentAccountlessDetailedView from './cliPaymentAccountlessDetailView';
import CliOtherOperationsDetailedView from './cliOtherOperationsDetailedView';

interface DetailedViewSwitchProps {
  event: EventTypes;
  product: ProductTypes;
  requestDetails: any;
}

const DetailedViewSwitch: React.FC<DetailedViewSwitchProps> = ({
  event,
  product,
  requestDetails,
}) => {
  let detailedView = null;

  if (product === productTypes.CLE) {
    if (event === eventTypes.REQUEST) {
      detailedView = (
        <CleRequestDetailedView
          requestDetails={
            requestDetails as RequestDetailsDto<RequestCreateApiDto>
          }
        />
      );
    } else if (event === eventTypes.MODIFICATION) {
      detailedView = (
        <CleModificationDetailedView
          requestDetails={
            requestDetails as RequestDetailsDto<ModificationCreateApiDto>
          }
        />
      );
    } else if (event === eventTypes.ADVANCE) {
      detailedView = (
        <CleAdvanceDetailedView
          requestDetails={
            requestDetails as RequestDetailsDto<AdvanceCreateApiDto>
          }
        />
      );
    } else if (event === eventTypes.ADVANCE_MODIFICATION) {
      detailedView = (
        <CleAdvanceModificationDetailedView
          requestDetails={
            requestDetails as RequestDetailsDto<AdvanceModificationCreateApiDto>
          }
        />
      );
    } else if (event === eventTypes.ADVANCE_CANCELLATION) {
      detailedView = (
        <CleAdvanceCancellationDetailedView
          requestDetails={
            requestDetails as RequestDetailsDto<AdvanceCancellationCreateApiDto>
          }
        />
      );
    } else if (event === eventTypes.OTHER_OPERATIONS) {
      detailedView = (
        <CleOtherOperationsDetailedView
          requestDetails={
            requestDetails as RequestDetailsDto<OtherOperationsCreateApiDto>
          }
        />
      );
    }
  } else if (product === productTypes.CLI) {
    if (event === eventTypes.REQUEST) {
      detailedView = (
        <CliRequestDetailedView
          requestDetails={
            requestDetails as RequestDetailsDto<CliRequestCreateApiDto>
          }
        />
      );
    } else if (event === eventTypes.MODIFICATION) {
      detailedView = (
        <CliModificationDetailedView
          requestDetails={
            requestDetails as RequestDetailsDto<CliModificationCreateApiDto>
          }
        />
      );
    } else if (event === eventTypes.PAYMENT_CHARGE) {
      detailedView = (
        <CliPaymentChargeDetailedView
          requestDetails={requestDetails as RequestDetailsDto<CliPaymentChargeCreateApiDto>}
        />
      );
    } else if (event === eventTypes.PAYMENT_ACCOUNTLESS) {
      detailedView = (
        <CliPaymentAccountlessDetailedView
          requestDetails={requestDetails as RequestDetailsDto<CliPaymentAccountlessCreateApiDto>}
        />
      );
    } else if (event === eventTypes.OTHER_OPERATIONS) {
      detailedView = (
        <CliOtherOperationsDetailedView
          requestDetails={
            requestDetails as RequestDetailsDto<CliOtherOperationsCreateApiDto>
          }
        />
      );
    }
  }

  return detailedView;
};

export default DetailedViewSwitch;
