package com.external.stomp;

import javax.security.auth.login.LoginException;

public class AllowAllAuthenticator implements Authenticator {
  /**
   * Validates a user.
   *
   * @param user the user's login
   * @param pass the user's passcode
   * @return a token which will be used for future authorization requests
   */
  public Object connect( String user, String pass ) throws LoginException {
    return "";
  }
  
  /**
   * Authorizes a send or subscribe request.
   *
   * @param channel the channel the user is attempting to subscribe or
   *  send to.
   * @param token the token returned by a previous call to connect.
   */
  public boolean authorizeSend( Object token, String channel ) {
    return true;
  }

  public boolean authorizeSubscribe( Object token, String channel ) {
    return true;
  }
}
