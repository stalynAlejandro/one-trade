import React, { useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { object, bool, string, InferType } from 'yup';
import { useSelector } from 'react-redux';

import { FormStepContainer } from '../../../../../components/FormStepContainer';
import { StepProps } from '../../../../common/types/StepProps';
import { StLabelHasAccount } from '../CreateFormStyled';
import { InputSelect } from '../../../../../components/Controls/InputSelect';
import { StButtonContainerRight, StTextarea } from '../../../../FormStyled';
import { Button } from '../../../../../components/Button';
import { RadioButton } from '../../../../../components/Controls/RadioButton';
import { Select } from '../../../../../components/Controls/Select';
import { useFieldsState } from '../../../../../hooks/useFieldsState';
import { AccountDto } from '../../../../../api/types/AccountDto';
import { defaultIfEmpty } from '../../../../../utils/defaultIfEmpty';
import { SummaryCard } from '../../../../../components/SummaryCard';
import { Input } from '../../../../../components/Controls/Input';
import {
  useFetchCollectionTypes,
  useFetchCurrencies,
  useFetchCustomerAccounts,
} from '../../../../../hooks/fetchDataHooks';
import { getUser } from '../../../../../redux/selectors/user';
import productTypes from '../../../../../enums/productTypes';
import eventTypes from '../../../../../enums/eventTypes';

export const fieldLimits = {
  clientReference: 30,
  debtorBank: 50,
  debtorName: 50,
};

const fieldsSchema = object({
  clientAccount: object().when('hasAccount', {
    is: true,
    then: object().required(),
  }),
  clientReference: string().ensure().max(fieldLimits.clientReference),
  collectionAmount: string().required(),
  collectionCurrency: object().required(),
  collectionType: string().required(),
  comments: string().ensure(),
  commissionAccount: object().when('hasAccount', {
    is: true,
    then: object().required(),
  }),
  debtorBank: string().ensure().max(fieldLimits.debtorBank),
  debtorName: string().ensure().max(fieldLimits.debtorName),
  hasAccount: bool(),
  office: string()
    .ensure()
    .required()
    .test({
      name: 'office-format',
      test: (d) => /^\d{4}$/.test(d),
    }),
});

const OperationDetails: React.FC<StepProps> = ({
  formData,
  onDataChange,
  onSubmitStep,
  onSummarizeFormData,
  stepNumber,
}) => {
  const { t } = useTranslation();
  const { office } = useSelector(getUser);

  const initialData = {
    collectionAmount: '',
    hasAccount: true,
    office,
  };

  const { feedbackErrors, fieldsState, setFieldValue, validateFields } =
    useFieldsState<InferType<typeof fieldsSchema>>(
      fieldsSchema,
      defaultIfEmpty(formData.operationDetails, initialData),
    );
  const { defaultCurrency, mappedCurrencies } = useFetchCurrencies(
    productTypes.CLE,
    eventTypes.REQUEST,
  );
  const { mappedCollectionTypes: collectionTypes } = useFetchCollectionTypes(
    productTypes.CLE,
    fieldsState.collectionCurrency?.currency,
  );
  const { customerAccounts } = useFetchCustomerAccounts(
    formData.customer.personNumber,
  );

  const handleSubmit = () => {
    if (validateFields()) {
      onDataChange(fieldsState);
      onSubmitStep();
    }
  };

  useEffect(() => {
    onDataChange(fieldsState);
  }, [fieldsState]);

  return (
    <FormStepContainer
      stepNumber={stepNumber || 2}
      title={t('completeClientAndOperationData')}
      withLateralContent
    >
      <form>
        <StLabelHasAccount>
          <span>{t('clientHasAccount')}</span>
          <RadioButton
            checked={fieldsState.hasAccount}
            label={t('yes')}
            name="hasAccount"
            onClick={() => setFieldValue('hasAccount', true)}
          />
          <RadioButton
            checked={!fieldsState.hasAccount}
            label={t('no')}
            name="hasAccount"
            onClick={() => {
              setFieldValue('hasAccount', false);
              setFieldValue('clientAccount', undefined);
              setFieldValue('commissionAccount', undefined);
            }}
          />
        </StLabelHasAccount>
        {fieldsState.hasAccount && (
          <>
            <Select
              className="form-field--wide"
              error={feedbackErrors.clientAccount}
              name="clientAccount"
              options={[
                {
                  label: t('accountsOfClient', {
                    client: formData.customer.name,
                  }),
                  options: customerAccounts?.map(
                    ({ currency, iban, id }: AccountDto) => ({
                      label: iban,
                      value: {
                        currency,
                        iban,
                        id,
                      },
                    }),
                  ),
                },
              ]}
              placeholder={t('nominalAccount')}
              value={fieldsState.clientAccount}
              variant="pijama"
              onChange={(val) => {
                setFieldValue('clientAccount', val);
                setFieldValue('commissionAccount', val);
              }}
            />
            <Select
              className="form-field--wide"
              error={feedbackErrors.commissionAccount}
              name="commissionAccount"
              options={[
                {
                  label: t('accountsOfClient', {
                    client: formData.customer.name,
                  }),
                  options: customerAccounts?.map(
                    ({ currency, iban, id }: AccountDto) => ({
                      label: iban,
                      value: {
                        currency,
                        iban,
                        id,
                      },
                    }),
                  ),
                },
              ]}
              placeholder={t('commissionAccount')}
              value={fieldsState.commissionAccount}
              variant="pijama"
              onChange={(val) => setFieldValue('commissionAccount', val)}
            />
          </>
        )}
        <InputSelect
          error={
            feedbackErrors.collectionAmount || feedbackErrors.collectionCurrency
          }
          name="collectionAmount"
          options={mappedCurrencies}
          placeholder={t('collectionAmount')}
          type="number"
          value={{
            input: fieldsState.collectionAmount,
            select: fieldsState.collectionCurrency || defaultCurrency,
          }}
          wide
          onChange={(val) => {
            setFieldValue('collectionAmount', val.input);
            setFieldValue('collectionCurrency', val.select);
          }}
        />
        <Select
          error={feedbackErrors.collectionType}
          name="collectionType"
          options={collectionTypes}
          placeholder={t('collectionType')}
          value={fieldsState.collectionType}
          onChange={(val) => setFieldValue('collectionType', val)}
        />
        <Input
          error={feedbackErrors.clientReference}
          maxLength={fieldLimits.clientReference}
          optional
          placeholder={t('clientReference')}
          value={fieldsState.clientReference || ''}
          onChange={(val) => setFieldValue('clientReference', val)}
        />
        <Input
          error={feedbackErrors.debtorName}
          maxLength={fieldLimits.debtorName}
          optional
          placeholder={t('debtorName')}
          value={fieldsState.debtorName || ''}
          onChange={(val) => setFieldValue('debtorName', val)}
        />
        <Input
          error={feedbackErrors.debtorBank}
          maxLength={fieldLimits.debtorBank}
          optional
          placeholder={t('debtorBank')}
          value={fieldsState.debtorBank || ''}
          onChange={(val) => setFieldValue('debtorBank', val)}
        />
        <Input
          error={feedbackErrors.office}
          placeholder={t('applicantOffice')}
          readOnly={!!office}
          value={fieldsState.office || ''}
          onChange={(val) => setFieldValue('office', val)}
        />
        <StTextarea
          error={feedbackErrors.comments}
          maxLength={500}
          optional
          placeholder={t('comments')}
          value={fieldsState.comments || ''}
          onChange={(val) => setFieldValue('comments', val)}
        />
        <StButtonContainerRight>
          <Button label={t('continue')} wide onClick={handleSubmit} />
        </StButtonContainerRight>
      </form>
      <SummaryCard
        {...onSummarizeFormData!(['customer'])}
        direction="vertical"
      />
    </FormStepContainer>
  );
};

export default OperationDetails;
