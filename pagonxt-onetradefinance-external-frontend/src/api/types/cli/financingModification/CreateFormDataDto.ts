import { CustomerCliFinancingDto } from '../../CustomerCliFinancingDto';
import { CustomerDto } from '../../CustomerDto';
import { FormDocumentationDto } from '../../forms/FormDocumentationDto';

export interface CreateFormDataDto {
  code?: string | null;
  customer: CustomerDto;
  documentation: FormDocumentationDto;
  request: {
    comments: string;
    financingRequest: CustomerCliFinancingDto;
    office: string;
  };
  savedStep: number;
}
