const customer = {
  customerId: '001',
  email: 'mock email 1',
  name: 'Coca Cola S.L.',
  office: '1234',
  personNumber: 'BUC-1234567',
  segment: 'SME',
  taxId: 'B1234567',
};

export const get = {
  data: [
    {
      amount: null,
      client: customer.name,
      contractReference: null,
      currency: null,
      event: 'REQUEST',
      issuanceDate: '2022-10-25T10:20:35.188+02:00',
      mercuryRef: null,
      office: '1234',
      operationId: 'CLE-9',
      priority: 'urgent',
      product: 'CLE',
      requestedDate: '2022-10-25T10:20:35.188+02:00',
      requesterName: 'Office',
      resolution: null,
      rowId: 'row-1',
      status: 'DRAFT',
      task: null,
    },
    {
      amount: 2222,
      client: customer.name,
      contractReference: 'REF-1234567890',
      currency: 'EUR',
      event: 'REQUEST',
      issuanceDate: '2022-10-25T08:28:21.262+02:00',
      mercuryRef: 'REF-1234567890',
      office: '1234',
      operationId: 'CLE-79',
      priority: 'urgent',
      product: 'CLE',
      requestedDate: '2022-10-25T08:28:21.262+02:00',
      requesterName: 'Office',
      resolution: 'APPROVED',
      rowId: 'row-2',
      status: 'ISSUED',
      task: null,
    },
    {
      amount: null,
      client: customer.name,
      contractReference: null,
      currency: null,
      event: 'MODIFICATION',
      issuanceDate: '2022-09-21T09:09:02.910+02:00',
      mercuryRef: null,
      office: '1234',
      operationId: 'CLE-MOD-9',
      priority: 'urgent',
      product: 'CLE',
      requestedDate: '2022-09-21T09:09:02.910+02:00',
      requesterName: 'Office',
      resolution: null,
      rowId: 'row-3',
      status: 'IN_PROGRESS',
      task: null,
    },
    {
      amount: 2222,
      client: customer.name,
      contractReference: null,
      currency: 'EUR',
      event: 'ADVANCE',
      issuanceDate: '2022-09-20T12:29:53.354+02:00',
      mercuryRef: null,
      office: '1234',
      operationId: 'CLE-ADV-9',
      priority: 'urgent',
      product: 'CLE',
      requestedDate: '2022-09-20T12:29:53.354+02:00',
      requesterName: 'Office',
      resolution: null,
      rowId: 'row-4',
      status: 'AWAITING_INSTRUCTIONS',
      task: null,
    },
    {
      amount: null,
      client: customer.name,
      contractReference: null,
      currency: null,
      event: 'ADVANCE_MODIFICATION',
      issuanceDate: '2022-10-14T09:54:45.582+02:00',
      mercuryRef: null,
      office: '1234',
      operationId: 'CLE-ADV-MOD-14',
      priority: 'urgent',
      product: 'CLE',
      requestedDate: '2022-10-14T09:54:45.582+02:00',
      requesterName: 'Office',
      resolution: null,
      rowId: 'row-5',
      status: 'IN_PROGRESS',
      task: null,
    },
    {
      amount: 2020,
      client: customer.name,
      contractReference: 'REF-0234567890',
      currency: 'EUR',
      event: 'REQUEST',
      issuanceDate: '2022-11-25T08:28:21.262+02:00',
      mercuryRef: 'REF-0234567890',
      office: '1234',
      operationId: 'CLI-90',
      priority: 'urgent',
      product: 'CLI',
      requestedDate: '2022-11-25T08:28:21.262+02:00',
      requesterName: 'Office',
      resolution: 'APPROVED',
      rowId: 'row-cli-90',
      status: 'ISSUED',
      task: null,
    },
    {
      amount: null,
      client: customer.name,
      contractReference: null,
      currency: null,
      event: 'REQUEST',
      issuanceDate: '2022-11-25T10:20:35.188+02:00',
      mercuryRef: null,
      office: '1204',
      operationId: 'CLI-79',
      priority: 'urgent',
      product: 'CLI',
      requestedDate: '2022-10-25T10:20:35.188+02:00',
      requesterName: 'Office',
      resolution: null,
      rowId: 'row-cli-79',
      status: 'DRAFT',
      task: null,
    },
    {
      amount: null,
      client: customer.name,
      contractReference: null,
      currency: null,
      event: 'MODIFICATION',
      issuanceDate: '2021-09-21T09:09:02.910+02:00',
      mercuryRef: null,
      office: '1204',
      operationId: 'CLI-MOD-1',
      priority: 'urgent',
      product: 'CLI',
      requestedDate: '2021-09-21T09:09:02.910+02:00',
      requesterName: 'Office',
      resolution: null,
      rowId: 'row-cli-mod-1',
      status: 'IN_PROGRESS',
      task: null,
    },
  ],
  total: 69,
};