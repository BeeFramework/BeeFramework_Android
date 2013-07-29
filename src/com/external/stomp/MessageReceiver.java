package com.external.stomp;

import java.util.Map;

/**
 * (c)2005 Sean Russell
 */
public interface MessageReceiver {
  public void receive( Command c, Map h, String b );
  public void disconnect();
  public boolean isClosed();
}
