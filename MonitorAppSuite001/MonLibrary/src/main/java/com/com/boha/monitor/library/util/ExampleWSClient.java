package com.com.boha.monitor.library.util;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by aubreyM on 14/12/06.
 */
public class ExampleWSClient extends WebSocketClient {

    public ExampleWSClient( URI serverURI ) {
        super( serverURI );
    }
    public ExampleWSClient( URI serverURI, Draft draft) {
        super( serverURI, draft );
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {

    }

    @Override
    public void onMessage(String s) {

    }

    @Override
    public void onClose(int i, String s, boolean b) {

    }

    @Override
    public void onError(Exception e) {

    }
}
