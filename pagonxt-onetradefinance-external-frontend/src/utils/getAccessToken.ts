import { InteractionRequiredAuthError } from '@azure/msal-browser';

import { loginRequest, msalInstance } from '../authConfig';

export const getAccessToken = async () => {
  const activeAccount = msalInstance?.getActiveAccount();

  if (activeAccount) {
    try {
      const msalTokenInfo = await msalInstance?.acquireTokenSilent({
        ...loginRequest,
        account: activeAccount,
      });
      return msalTokenInfo.idToken;
    } catch (error) {
      if (error instanceof InteractionRequiredAuthError) {
        msalInstance.loginRedirect(loginRequest);
      }
    }
  }

  // Do not force login here to avoid endless loops
  return null;
};
