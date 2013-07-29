package com.external.stomp;

import javax.security.auth.login.LoginException;

public interface Authenticator {
  /**
   * Validates a user.
   *
   * @param user the user's login
   * @param pass the user's passcode
   * @return a token which will be used for future authorization requests
   */
  public Object connect( String user, String pass ) throws LoginException;
  
  /**
   * Authorizes a send request.
   *
   * @param channel the channel the user is attempting to send to
   * @param token the token returned by a previous call to connect.
   */
  public boolean authorizeSend( Object token, String channel );

  /**
   * Authorizes a Subscribe request.
   *
   * @param channel the channel the user is attempting to subscribe to
   * @param token the token returned by a previous call to connect.
   */
  public boolean authorizeSubscribe( Object token, String channel );
}
