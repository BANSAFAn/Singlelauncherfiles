package org.tlauncher.tlauncher.ui.listener.auth;

import org.tlauncher.tlauncher.minecraft.auth.Authenticator;

public interface AuthenticatorListener {
  void onAuthPassing(Authenticator paramAuthenticator);
  
  void onAuthPassingError(Authenticator paramAuthenticator, Exception paramException);
  
  void onAuthPassed(Authenticator paramAuthenticator);
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\listener\auth\AuthenticatorListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */