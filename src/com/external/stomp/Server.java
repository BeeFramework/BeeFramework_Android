package com.external.stomp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

/**
 * Implements a Stomp server.  This is a tiny embeddable server that
 * can be used as an inter-net or intra-VM server to handle Stomp
 * requests.
 *
 * For intra-VM requests, use getClient() to get an instance of a Stomp
 * client.
 *
 * Example usage:
 * <pre>
 *   Server s = new Server( 61656 );  // To start it
 *   s.listen( 12345 );               // To listen on another port
 *   // Note that 's' is now listening on TWO ports
 *   s.close( 61656 );                // To close a port
 *   s.close( 12345 );                // Now no ports are open.
 *   // This is effectively the same as: 
 *   i = new Server();                // A server with no network (intra-VM)
 *   Stomp c = s.getClient();         // Creates and returns a client
 *   // that is connected to the server directly via VM method calls --
 *   // there is no network communication between this client and the
 *   // server.
 *   s.stop();                        // To stop the server
 *   i.stop();
 * </pre>
 *
 * FIXME
 * Queues are not implemented.  Therefore, this server operates as an IRC,
 * rather than a Jabber, messaging system.  That is, all messages arriving
 * before a subscription request are lost to that client.  When Queues are
 * implemented, there will be an option to set persistence on the messages.
 *
 * Would it be good if -- given a session ID -- clients could
 * reconnect and complete transactions?
 *
 * (c)2005 Sean Russell
 */
public class Server {
  private Queue _message_queue;
  private Map _transactions;
  private Map _listeners;
  private ConnectionListener _connection_listener;
  private Authenticator _authenticator = new AllowAllAuthenticator();


  /**
   * Instantiates an intra-VM server.  This will open no ports, and
   * can not be connected to except by intra-VM threads.  A port can
   * be opened on this server by using the listen() method.
   *
   * @see listen()
   */
  public Server() {
    _message_queue = new FileQueue();
    _transactions = new HashMap();
    _listeners = new HashMap();
  }


  /**
   * Instantiates an inter-network server listening on the supplied
   * port.  Additional ports can be listened on by using the listen()
   * method.
   *
   * @see listen()
   * @param port This port will be opened and will listen for client
   *  connections.  If the port value is less than 0, the default port
   *  of 61626 will be used.
   */
  public Server( int port ) throws IOException {
    this( port, null );
  }


  /**
   * Instantiates an inter-network server listening on the supplied
   * port.  Additional ports can be listened on by using the listen()
   * method.
   *
   * @see listen()
   * @param port This port will be opened and will listen for client
   *  connections.  If the port value is less than 0, the default port
   *  of 61626 will be used.
   * @param auth A class responsible for authenticating connections.
   */
  public Server( int port, Authenticator auth ) throws IOException {
    this();
    if (port < 0) port = 61626;
    if (auth != null) _authenticator = auth;
    listen(port);
  }


  /**
   * Opens a port for internet connections.  A single server can listen
   * on multiple ports, so calling this method multiple times will open
   * multiple ports.
   * 
   * @param port This port will be opened and will listen for client
   *  connections.  If the port value is less than 0, an exception is
   *  thrown.
   */
  public void listen( int port ) throws IOException {
    _connection_listener = new ConnectionListener( port, this );
    _connection_listener.start();
  }


  /**
   * Called by a SocketHandler to notify the server that a client
   * has disconnected.  Is not, and should not, be called from anywhere
   * else.
   */
  protected void disconnect( SocketHandler s ) {
    _connection_listener.disconnect( s );
  }


  /**
   * This class is necessary because Java is RETARDED.  Specifically,
   * it lacks closures.
   *
   * Listenens on a port and accepts client connections.  For each
   * connection, spawns a SocketHandler for the connection.  When
   * shut down, stops receiving connections and shuts down all existing
   * client connections.
   */
  private class ConnectionListener extends Thread {
    private int _port;
    private Server _server;
    private ServerSocket _serve_sock;
    private List _handlers = new ArrayList();


    protected ConnectionListener( int port, Server server ) {
      _port = port;
      _server = server;
    }


    public void run() {
      Socket sock = null;
      try {
        _serve_sock = new ServerSocket( _port );
        while (!isInterrupted()) {
          sock = _serve_sock.accept();
          try {
            Thread handler = new SocketHandler( sock, _server );
            handler.start();
            _handlers.add( handler );
          } catch (IOException e) {
            e.printStackTrace( System.err );
          }
        }
      } catch (SocketException e) {
        // This gets thrown when the accept() is interrupted
      } catch (IOException e) {
        e.printStackTrace( System.err );
      } catch (Exception e) {
        e.printStackTrace( System.err );
      }
      for (Iterator i=_handlers.iterator(); i.hasNext(); ) {
        try {
          Thread t = (Thread)i.next();
          t.interrupt();
          Thread.yield();
        } catch (Exception e) { }
      }
    }


    /**
     * Shut down operations.
     */
    protected void shutdown() {
      this.interrupt();
      try { _serve_sock.close(); } catch (Exception e) {}
    }


    /**
     * Called by the server to notify this object that a SocketHandler
     * has disconnected itself.
     */
    protected void disconnect( SocketHandler h ) {
      _handlers.remove(h);
    }
  }


  /**
   * Shuts down the server, closing all connections.
   */
  public void stop() {
    // The _connection_listener will be null if this is not a network
    // socket server.
    if (_connection_listener != null) {
      _connection_listener.shutdown();
    }
    close( -1 );
    Thread.yield();
  }


  /**
   * Sets the queuing mechanism used for all further messages.  Any
   * existing undelivered messages will <em>not</em> use this queue.
   * FIXME Not implemented
   * 
   * @param queue 
   */
  public void setQueue( Queue q ) {
    _message_queue = q;
  }


  /**
   * Closes a port.  All connections on this port will be closed.
   *
   * @param port The port to close.  A value of < -1 closes all ports
   */
  public void close( int port ) {
    for (Iterator i = _listeners.keySet().iterator(); i.hasNext(); ) {
      Object k = i.next();
      Object s = _listeners.get(k);
      if (s instanceof SocketHandler) {
        SocketHandler sh = (SocketHandler)s;
        if (port == -1 || sh.isPort( port )) {
          sh.interrupt();
          sh.close();
          _transactions.remove( s );
          _listeners.remove( k );
        }
      }
    }
  }


  // FIXME: Need to enforce CONNECT; right now, doesn't require a connect.
  // FIXME: Add login handling feature
  /**
   * Manages client connections.  There is one SocketHandler per client.
   * This class is responsible for relaying communications between the 
   * server and the client for which it is responsible.
   */
  protected class SocketHandler 
      extends Receiver 
      implements Listener, Authenticatable {
    private InputStream _input;
    private OutputStream _output;
    private Socket _socket;
    private Server _server;
    private Object _client_token;
    private boolean _authenticated = false;


    /**
     * Sets up a client communication on a given socket.
     */
    public SocketHandler( Socket sock, Server s ) throws IOException {
      super();
      _input = sock.getInputStream();
      _output = sock.getOutputStream();
      _socket = sock;
      _server = s;
      setup( this, _input );
    }


    public Object token() {
      return _client_token;
    }


    public boolean isClosed() { return _socket.isClosed(); }


    /**
     * Tests whether the supplied port is the port this handler is
     * communicating with the client over.
     *
     * @param port the port number to test.
     * @return true iff the supplied port matches the open port of this
     *  handler.
     */
    protected boolean isPort( int port ) {
      return _socket.getPort() == port;
    }


    /**
     * Close the connection with the client.
     */
    protected void close() {
      try { 
        _socket.shutdownInput();
        _input.close();  
      } catch (IOException e) { /* Who cares? */ }
      try { 
        _socket.shutdownOutput();
        _output.close(); 
      } catch (IOException e) { /* Who cares? */ }
      try { _socket.close(); } catch (IOException e) { /* Who cares? */ }
    }


    public void disconnect() { close(); }


    /**
     * Gets called when messages come in from the client, and relays the
     * message to the server.  This method handles and consumes CONNECT, 
     * DISCONNECT, and ERROR messages.  It is also responsible for sending
     * RECEIPTs back to the client.
     */
    public void receive( Command c, Map h, String b ) {
      if (c == Command.CONNECT) {
        String login = (String)h.get( "login" );
        String passcode = (String)h.get( "passcode" );
        try {
          _client_token = Server.this._authenticator.connect( login, passcode );
          HashMap headers = new HashMap();
          headers.put( "session", String.valueOf( this.hashCode() ) );
          transmit( Command.CONNECTED, headers, null );
          _authenticated = true;
        } catch (javax.security.auth.login.LoginException e) {
          transmit( Command.ERROR, null, "Login failed: " + e.getMessage());
        }
      } else {
        if (!_authenticated) {
          transmit( Command.ERROR, null, "Not CONNECTed, or not authorized" );
          return;
        }

        if (c == Command.DISCONNECT) {
          if (h != null) {
            String receipt = (String)h.get("receipt");
            if (receipt != null) {
              HashMap headers = new HashMap();
              headers.put( "receipt-id", receipt );
              receive( Command.RECEIPT, headers, null );
            }
          }
          _server.disconnect( this );
          this.interrupt();
          Thread.yield();
          close();
        } else if (c == Command.ERROR) {
          // Then there was an error in the client message.  Pass it back.
          error( h, b );
        } else {
          _server.receive( c, h, b, this );
        }
      }
    }


    /**
     * Called by the server; sends a message to this client.
     */
    public void message( Map headers, String body ) {
      transmit( Command.MESSAGE, headers, body );
    }


    /**
     * Called by the server; sends a receipt to this client.
     */
    public void receipt( Map headers ) {
      transmit( Command.RECEIPT, headers, null );
    }


    /**
     * Called by the server.  Sends an error to the client.
     */
    public void error( Map headers, String message ) {
      transmit( Command.ERROR, headers, message );
    }


    /**
     * Used by message(), receipt(), and error() to deliver the message to the
     * client.
     */
    private void transmit( Command c, Map h, String b ) {
      try {
        Transmitter.transmit( c, h, b, _output );
      } catch (Exception e) {
        this.interrupt();
        Thread.yield();
        close();
      }
    }
  }


  private String mapToStr( Map m ) {
    StringBuffer b = new StringBuffer("[ ");
    for (Iterator keys = m.keySet().iterator(); keys.hasNext(); ) {
      String k = keys.next().toString();
      b.append( k+" => "+m.get(k)+", " );
    }
    b.append( "]" );
    return b.toString();
  }

  /**
   * Incoming mesages from clients come here, and are delivered to listeners,
   * both intra-VM and network.
   *
   * @param c the command
   * @param h the headers
   * @param b the message
   * @param y the thing that received the message and passed it to us
   */
  protected void receive( Command c, Map h, String b, Authenticatable y ) {
    long id = (int)(Math.random() * 10000);
    try {
      // Convert to MESSAGE and distribute
      if (c == Command.COMMIT) {
        synchronized (_transactions) {
          List trans = (List)_transactions.remove(y);
          trans = new ArrayList( trans );
          for (Iterator i=trans.iterator(); i.hasNext(); ) {
            Message m = (Message)i.next();
            try {
              receive( m.command(), m.headers(), m.body(), y );
            } catch (Exception e) {
              // Don't allow listener code to break us
            }
          }
        }

      } else if (c == Command.ABORT) {
        synchronized (_transactions) {
          _transactions.remove(y);
        }

      } else if (_transactions.get( y ) != null) {
        synchronized (_transactions) {
          ((List)_transactions.get( y )).add( new Message( c, h, b ) );
        }

      } else {
        if (h == null) h = new HashMap();
        String destination = (String)h.get("destination");
        if (c == Command.SEND) {
          if (y instanceof IntraVMClient ||
              _authenticator.authorizeSend( y.token(), destination )) {
            synchronized( _listeners ) {
              List l = (List)_listeners.get( destination );
              if (l != null) {
                l = new ArrayList(l);
                for (Iterator i = l.iterator(); i.hasNext(); ) {
                  Listener sh = (Listener)i.next();
                  try {
                    sh.message( h, b );
                  } catch (Exception e) {
                    // Don't allow listener code to break us
                  }
                }
              } 
            }
          } else {
            Map error_headers = new HashMap();
            error_headers.put( "message:", "authorization refused");
            error_headers.put( "type:", "send");
            error_headers.put( "channel:", destination);
            y.error( error_headers, "The message:\n-----\n"+b+
                "\n-----\nAuthentication token refused for this channel");
          }

        } else if (c == Command.SUBSCRIBE) {
          if (y instanceof IntraVMClient ||
              _authenticator.authorizeSubscribe( y.token(), destination )) {
            synchronized (_listeners ) {
              List l = (List)_listeners.get( destination );
              if (l == null) {
                l = new ArrayList();
                _listeners.put( destination, l );
              }
              if (!l.contains(y)) l.add( y );
            }
          } else {
            Map error_headers = new HashMap();
            error_headers.put( "message:", "authorization refused");
            error_headers.put( "type:", "subscription");
            error_headers.put( "channel:", destination);
            y.error( error_headers, "The message:\n-----\n"+b+
                "\n-----\nAuthentication token refused for this channel");
          }

        } else if (c == Command.UNSUBSCRIBE) {
          synchronized (_listeners) {
            List l = (List)_listeners.get( destination );
            if (l != null) l.remove( y );
          }

        } else if (c == Command.BEGIN) {
          synchronized (_transactions) {
            List trans = new ArrayList();
            _transactions.put( y, trans );
          }

        } else if (c == Command.DISCONNECT) {
          synchronized (_listeners) {
            for (Iterator i=_listeners.values().iterator(); i.hasNext(); ) {
              List l = (List)i.next();
              l.remove( y );
            }
          }
        }
      }
      if (h != null) {
        String receipt = (String)h.get("receipt");
        if (receipt != null) {
          HashMap headers = new HashMap();
          headers.put( "receipt-id", receipt );
          y.receive( Command.RECEIPT, headers, null );
        }
      }
    } catch (Exception e) {
      // Don't allow listener code to break us
    }
  }


  /**
   * Returns a Stomp client for intra-VM communications with the server.
   * This client communicates directly with the server via method() calls,
   * bypassing the network.
   */
  public Stomp getClient() {
    return new IntraVMClient( this );
  }


  /**
   * Gozirra is probably not the best choice for a stand-alone server.  If
   * you are tempted to use it as such, you might want to look at ActiveMQ,
   * which is a feature-rich MOM solution.  Gozirra is intended primarily
   * to be an ultra-light embedded messaging library.
   *
   * If you still want to run Gozirra as a stand-alone server, then you'll
   * need to know about how to call it.  The main() method takes a single
   * argument: a port to run on.  It will run until you ^C it.
   */
  public static final void main( String[] args ) {
    if (args.length != 1) {
      System.err.println("A single argument -- a port -- is required");
      System.exit(1);
    }
    int port = Integer.valueOf( args[0] ).intValue();
    System.out.println(Version.VERSION);
    try {
      new Server( port );
    } catch (Exception e) {
      System.err.println("Failed to start server");
      e.printStackTrace( System.err );
    }
  }
}

