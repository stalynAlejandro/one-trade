import NumberFormat from 'react-number-format';

import { CompleteInformationFormDataDto } from '../../../api/types/cli/financingModification/CompleteInformationFormDataDto';
import { CreateFormDataDto } from '../../../api/types/cli/financingModification/CreateFormDataDto';
import { SummaryCardProps } from '../../../components/SummaryCard/SummaryCard';
import i18n from '../../../i18n';
import { formatDate } from '../../../utils/dates';
import { getNumberFormatProps } from '../../../utils/getNumberFormatProps';
import { keyIsIncluded, SummarizeProps } from './common';

export const summarizeCliFinancingModificationCreation: SummarizeProps<
  CreateFormDataDto | CompleteInformationFormDataDto
> = (formData, includedSteps, onEditStep = () => null, t = i18n.t) => {
  const scp: SummaryCardProps = {
    sections: [],
    title: t('summaryTitle'),
  };

  if (!formData) {
    return scp;
  }

  if (keyIsIncluded('customer', includedSteps)) {
    scp.sections.push({
      fields: [
        {
          label: 'client',
          value: formData.customer?.name,
        },
      ],
      key: 'customer',
    });
  }

  if (keyIsIncluded('request', includedSteps)) {
    const { financingRequest } = formData.request || {};

    scp.sections.push({
      fields: [
        {
          label: 'collectionRef',
          value: financingRequest?.code,
        },
        {
          label: 'contractRef',
          value: financingRequest?.contractReference,
        },
        {
          label: 'issuanceDate',
          value: financingRequest?.creationDate
            ? formatDate(new Date(financingRequest?.creationDate))
            : '-',
        },
        {
          label: 'collectionAmount',
          value: (
            <NumberFormat
              {...getNumberFormatProps()}
              displayType="text"
              suffix={` ${financingRequest?.currency}`}
              value={financingRequest?.amount}
            />
          ),
        },
        {
          label: 'applicantOffice',
          value: formData.request?.office,
        },
        {
          collapsible: true,
          label: 'comments',
          value: formData.request?.comments,
        },
      ],
      key: 'request',
      onEdit: () => onEditStep(1),
      title: 'requestDetails',
    });
  }

  if (keyIsIncluded('documentation', includedSteps)) {
    scp.sections.push({
      fields: [
        {
          label: 'documentation',
          value: t('nFilesAttached', {
            files: formData.documentation?.files?.length,
          }),
        },
        {
          label: 'priority',
          value: t(formData.documentation?.priority!),
        },
      ],
      key: 'documentation',
      onEdit: () => onEditStep(2),
      title: 'documentationAndPriority',
    });
  }

  return scp;
};
