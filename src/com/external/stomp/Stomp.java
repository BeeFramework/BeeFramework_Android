package com.external.stomp;

import java.util.*;

/**
 * A Stomp messaging implementation.
 *
 * Messages are handled in one of two ways.  If subscribe was called with
 * a listener, then incoming messages are delivered to all listeners
 * of that channel, and the message is deleted from the queue.  If no
 * listener was provided for that channel, then messages are placed in
 * a queue and can be retrieved with getNext().  In all cases, when
 * messages are retrieved, they are deleted from the queue.
 *
 * Notes:
 * * FIXME: ERROR messages don't do anything.
 *
 * (c)2005 Sean Russell
 */
public abstract class Stomp {
  /**
   * A map of channel => listener pairs.  String => Listener.
   */
  private Map _listeners = new HashMap();
  /**
   * Things that are listening for communication errors.  Contains
   * Listeners.
   */
  private List _error_listeners = new ArrayList();
  /**
   * A message queue; where messages that have no listeners are
   * stored.  Contains Messages.
   */
  private Stack _queue = new Stack();
  /**
   * Incoming receipts (as String IDs)
   */
  private List _receipts = new ArrayList();
  /**
   * True if connected to a server; false otherwise
   */
  protected boolean _connected = false;
  /**
   * Incoming errors (as String messages)
   */
  private List _errors = new ArrayList();
  

  /**
   * Disconnect from a server, including headers.  
   * Must be implemented by the child class.  Should set the
   * _connected flag to false.
   *
   * @param header A map of key/value headers
   */
  public abstract void disconnect( Map header );


  /**
   * Transmit a message to a server.  Must be implemented by the child class.
   * The implementation must handle cases where the header and/or the body 
   * are null.
   *
   * @param command The Stomp command.  If null, causes an error.
   * @param header  A map of headers.  If null, an empty map is used.
   * @param body    The body of the message.  May be empty.
   */
  protected abstract void transmit( Command command, Map header, String body );


  /**
   * Disconnect from a server.  Must be implemented by the child class.
   */
  public void disconnect() { disconnect( null ); }


  /**
   * Transmit a message to a server.
   *
   * @param command The Stomp command.  If null, causes an error.
   * @param header  A map of headers.  If null, an empty map is used.
   */
  protected void transmit( Command command, Map header ) {
    transmit( command, header, null );
  }


  /**
   * Transmit a message to a server.
   *
   * @param command The Stomp command.  If null, causes an error.
   */
  protected void transmit( Command command ) {
    transmit( command, null, null );
  }


  /**
   * Begins a transaction.  Messages will not be delivered to 
   * subscribers until commit() has been called.
   */
  public void begin() { transmit( Command.BEGIN ); }


  /**
   * Begins a transaction.  Messages will not be delivered to 
   * subscribers until commit() has been called.
   *
   * @param header Additional headers to send to the server.
   */
  public void begin( Map header ) { transmit( Command.BEGIN, header ); }


  /**
   * Commits a transaction, causing any messages sent since begin()
   * was called to be delivered.
   */
  public void commit() { transmit( Command.COMMIT ); }


  /**
   * Commits a transaction, causing any messages sent since begin()
   * was called to be delivered.
   *
   * @param header Additional headers to send to the server.
   */
  public void commit( Map header ) { transmit( Command.BEGIN, header ); }


  /**
   * Commits a transaction, causing any messages sent since begin()
   * was called to be delivered.  This method does not return until
   * the server has confirmed that the commit was successfull.
   */
  public void commitW() throws InterruptedException { commitW( null ); }


  /**
   * Commits a transaction, causing any messages sent since begin()
   * was called to be delivered.  This method does not return until
   * the server has confirmed that the commit was successfull.
   */
  public void commitW( Map header ) throws InterruptedException { 
    String receipt = addReceipt( header );
    transmit( Command.COMMIT, header ); 
    waitOnReceipt( receipt );
  }


  /**
   * Aborts a transaction.  Messages sent since begin() was called
   * are destroyed and are not sent to subscribers.
   */
  public void abort() { transmit( Command.ABORT ); }


  /**
   * Aborts a transaction.  Messages sent since begin() was called
   * are destroyed and are not sent to subscribers.
   *
   * @param header Additional headers to send to the server.
   */
  public void abort( Map header ) { transmit( Command.ABORT, header ); }


  /**
   * Subscribe to a channel.
   *
   * @param name The name of the channel to listen on
   */
  public void subscribe( String name ) {
    subscribe( name, null, null );
  }


  /**
   * Subscribe to a channel.
   *
   * @param name The name of the channel to listen on
   * @param header Additional headers to send to the server.
   */
  public void subscribe( String name, Map header ) {
    subscribe( name, null, header );
  }


  /**
   * Subscribe to a channel.
   *
   * @param name The name of the channel to listen on
   * @param listener A listener to receive messages sent to the channel
   */
  public void subscribe( String name, Listener listener ) {
    subscribe( name, listener, null );
  }


  /**
   * Subscribe to a channel.
   *
   * @param name The name of the channel to listen on
   * @param headers Additional headers to send to the server.
   * @param listener A listener to receive messages sent to the channel
   */
  public void subscribe( String name,  Listener listener, Map headers ) {
    synchronized (_listeners) {
      if (listener != null) {
        List list = (List)_listeners.get( name );
        if (list == null) {
          list = new ArrayList();
          _listeners.put( name, list );
        }
        if (!list.contains( listener )) list.add( listener );
      }
    }
    if (headers == null) headers = new HashMap();
    headers.put( "destination", name );
    transmit( Command.SUBSCRIBE, headers );
  }


  private String addReceipt( Map header ) {
    if (header == null) header = new HashMap();
    String receipt = String.valueOf(hashCode())+"&"+System.currentTimeMillis();
    header.put( "receipt",receipt );
    return receipt;
  }


  /**
   * Subscribe to a channel.  This method blocks until it receives a
   * receipt from the server.
   *
   * @param name The name of the channel to listen on
   * @param headers Additional headers to send to the server.
   * @param listener A listener to receive messages sent to the channel
   */
  public void subscribeW( String name, Listener listener, Map header ) 
  throws InterruptedException {
    String receipt = addReceipt( header );
    subscribe( name, listener, header );
    waitOnReceipt( receipt );
  }


  /**
   * Subscribe to a channel.  This method blocks until it receives a
   * receipt from the server.
   *
   * @param name The name of the channel to listen on
   * @param listener A listener to receive messages sent to the channel
   */
  public void subscribeW( String name, Listener listener ) 
  throws InterruptedException {
    subscribeW( name, listener, null );
  }


  /**
   * Unsubscribe from a channel.  Automatically unregisters all
   * listeners of the channel.  To re-subscribe with listeners,
   * subscribe must be passed the listeners again.
   *
   * @param name The name of the channel to unsubscribe from.
   */
  public void unsubscribe( String name ) {
    unsubscribe( name, (HashMap)null );
  }


  /**
   * Unsubscribe a single listener from a channel.  This does not
   * send a message to the server unless the listener is the only
   * listener of this channel.
   *
   * @param name The name of the channel to unsubscribe from.
   * @param listener The listener to unsubscribe
   */
  public void unsubscribe( String name, Listener l ) {
    synchronized (_listeners) {
      List list = (List)_listeners.get( name );
      if (list != null) {
        list.remove( l );
        if (list.size() == 0) {
          unsubscribe( name );
        }
      }
    }
  }
  /**
   * Unsubscribe from a channel.  Automatically unregisters all
   * listeners of the channel.  To re-subscribe with listeners,
   * subscribe must be passed the listeners again.
   *
   * @param name The name of the channel to unsubscribe from.
   * @param header Additional headers to send to the server.
   */
  public void unsubscribe( String name, Map header ) {
    if (header == null) header = new HashMap();
    synchronized( _listeners ) { _listeners.remove( name ); }
    header.put( "destination", name );
    transmit( Command.UNSUBSCRIBE, header );
  }


  /**
   * Unsubscribe from a channel.  Automatically unregisters all
   * listeners of the channel.  To re-subscribe with listeners,
   * subscribe must be passed the listeners again.  This method
   * blocks until a receipt is received from the server.
   *
   * @param name The name of the channel to unsubscribe from.
   */
  public void unsubscribeW( String name ) throws InterruptedException {
    unsubscribe( name, (HashMap)null );
  }


  /**
   * Unsubscribe from a channel.  Automatically unregisters all
   * listeners of the channel.  To re-subscribe with listeners,
   * subscribe must be passed the listeners again.  This method
   * blocks until a receipt is received from the server.
   *
   * @param name The name of the channel to unsubscribe from.
   */
  public void unsubscribeW( String name, Map header ) throws InterruptedException {
    String receipt = addReceipt( header );
    unsubscribe( name, (HashMap)null );
    waitOnReceipt( receipt );
  }




  /**
   * Send a message to a channel synchronously.  This method does
   * not return until the server acknowledges with a receipt.
   *
   * @param dest The name of the channel to send the message to
   * @param mesg The message to send.
   */
  public void sendW( String dest, String mesg ) 
    throws InterruptedException {
    sendW( dest, mesg, null );
  }


  /**
   * Send a message to a channel synchronously.  This method does
   * not return until the server acknowledges with a receipt.
   *
   * @param dest The name of the channel to send the message to
   * @param mesg The message to send.
   */
  public void sendW( String dest, String mesg, Map header ) 
    throws InterruptedException {
    String receipt = addReceipt( header );
    send( dest, mesg, header );
    waitOnReceipt( receipt );
  }


  /**
   * Send a message to a channel.
   *
   * @param dest The name of the channel to send the message to
   * @param mesg The message to send.
   */
  public void send( String dest, String mesg ) {
    send( dest, mesg, null );
  }


  /**
   * Send a message to a channel.
   *
   * @param dest The name of the channel to send the message to
   * @param mesg The message to send.
   * @param header Additional headers to send to the server.
   */
  public void send( String dest, String mesg, Map header ) {
    if (header == null) header = new HashMap();
    header.put( "destination", dest );
    transmit( Command.SEND, header, mesg );
  }


  /**
   * Get the next unconsumed message in the queue.  This is non-blocking.
   *
   * @return the next message in the queue, or null if the queue
   *  contains no messages.  This is non-blocking.
   */
  public Message getNext() {
    synchronized(_queue) { return (Message)_queue.pop(); }
  }


  /**
   * Get the next unconsumed message for a particular channel.   This is 
   * non-blocking.
   *
   * @param name the name of the channel to search for
   *
   * @return the next message for the channel, or null if the queue
   *  contains no messages for the channel.
   */
  public Message getNext( String name ) {
    synchronized( _queue ) {
      for (int idx = 0; idx < _queue.size(); idx++) {
        Message m = (Message)_queue.get(idx);
        if (m.headers().get( "destination" ).equals(name)) {
          _queue.remove(idx);
          return m;
        }
      }
    }
    return null;
  }


  public void addErrorListener( Listener l ) {
    synchronized (_error_listeners) { _error_listeners.add( l ); }
  }

  public void delErrorListener( Listener l ) {
    synchronized (_error_listeners) { _error_listeners.remove( l ); }
  }


  /**
   * Checks to see if a receipt has come in.
   *
   * @param receipt_id the id of the receipts to find
   */
  public boolean hasReceipt( String receipt_id ) {
    synchronized( _receipts ) {
      for (Iterator i=_receipts.iterator(); i.hasNext();) {
        String o = (String)i.next();
        if (o.equals(receipt_id)) return true;
      }
    }
    return false;
  }


  /**
   * Deletes all receipts with a given ID
   *
   * @param receipt_id the id of the receipts to delete
   */
  public void clearReceipt( String receipt_id ) {
    synchronized( _receipts ) {
      for (Iterator i=_receipts.iterator(); i.hasNext();) {
        String o = (String)i.next();
        if (o.equals(receipt_id)) i.remove();
      }
    }
  }


  /**
   * Remove all of the receipts
   */
  public void clearReceipts() {
    synchronized( _receipts ) {
      _receipts.clear();
    }
  }

  public void waitOnReceipt( String receipt_id ) 
    throws java.lang.InterruptedException {
    synchronized( _receipts ) {
      while (!hasReceipt(receipt_id))
        _receipts.wait();
    }
  }
  public boolean waitOnReceipt( String receipt_id, long timeout ) 
    throws java.lang.InterruptedException {
    synchronized( _receipts ) {
      while (!hasReceipt(receipt_id ))
        _receipts.wait( timeout );
      if (_receipts.contains( receipt_id )) {
        return true;
      } else {
        return false;
      }
    }
  }


  public boolean isConnected() { return _connected; }


  public String nextError() {
    /*
    StackTraceElement[] trace = new Throwable().getStackTrace();
    StringBuffer buff = new StringBuffer();
    for (int i=0; i<trace.length; buff.append( trace[i].getClassName()+":"+trace[i++].getLineNumber()+" << " ));
    System.err.println("In nextError with "+_errors.size()+" errors from "+buff.toString());
    */
    synchronized( _errors ) {
      if (_errors.size() == 0) return null;
      return (String)_errors.remove(0);
    }
  }


  public void receive( Command c, Map h, String b ) {
    if (c == Command.MESSAGE ) {
      String destination = (String)h.get( "destination" );
      synchronized( _listeners ) {
        List listeners = (List)_listeners.get( destination );
        if (listeners != null) {
          listeners = new ArrayList( listeners );
          for (Iterator i = listeners.iterator(); i.hasNext(); ) {
            Listener l = (Listener)i.next();
            try {
              l.message( h, b );
            } catch (Exception e) {
              // Don't let listeners screw us over by throwing exceptions
            }
          }
        } else {
          _queue.push( new Message( c, h, b ) );
        }
      }

    } else if (c == Command.CONNECTED ) {
      _connected = true;

    } else if (c == Command.RECEIPT ) {
      _receipts.add( h.get("receipt-id") );
      synchronized(_receipts) { _receipts.notify(); }

    } else if (c == Command.ERROR ) {
      if (_error_listeners.size() > 0) {
        synchronized (_error_listeners) {
          for (Iterator i = _error_listeners.iterator(); i.hasNext(); ) {
            try {
              ((Listener)i.next()).message( h, b );
            } catch (Exception e) {
              // Don't let listeners screw us over by throwing exceptions
            }
          }
        }
      } else {
        synchronized( _errors ) {
          _errors.add( b );
        }
      }
    } else {
      // FIXME
    }
  }
}
