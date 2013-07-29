package com.external.stomp;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * (c)2005 Sean Russell
 */
class Transmitter {
  public static void transmit( Command c, Map h, String b, 
      java.io.OutputStream out ) throws IOException {
    StringBuffer message = new StringBuffer( c.toString() );
    message.append( "\n" );

    if (h != null) {
      for (Iterator keys = h.keySet().iterator(); keys.hasNext(); ) {
        String key = (String)keys.next();
        String value = (String)h.get(key);
        message.append( key );
        message.append( ":" );
        message.append( value );
        message.append( "\n" );
      }
    }
    message.append( "\n" );

    if (b != null) message.append( b );

    message.append( "\000" );

    out.write( message.toString().getBytes( Command.ENCODING ) );
  }
}
