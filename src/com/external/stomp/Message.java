package com.external.stomp;

import java.util.Map;

/**
 * (c)2005 Sean Russell
 */
public class Message {
  private Command _command;
  private Map _headers;
  private String _body;
  protected Message( Command c, Map h, String b ) {
    _command = c;
    _headers = h;
    _body = b;
  }
  public Map headers() { return _headers; }
  public String body() { return _body; }
  public Command command() { return _command; }
}



