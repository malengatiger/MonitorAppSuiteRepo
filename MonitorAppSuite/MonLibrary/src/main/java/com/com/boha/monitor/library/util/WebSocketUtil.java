package com.com.boha.monitor.library.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.com.boha.monitor.library.dto.RequestDTO;
import com.com.boha.monitor.library.dto.ResponseDTO;
import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ServerHandshake;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

/**
 * Utility class to manage web socket communications for the application
 * Created by aubreyM on 2014/08/10.
 */
public class WebSocketUtil {
    public interface WebSocketListener {
        public void onMessage(ResponseDTO response);

        public void onClose();

        public void onError(String message);

    }

    static WebSocketListener webSocketListener;
    static RequestDTO request;
    static Context ctx;
    static long start, end;

    public static void disconnectSession() {
        if (mWebSocketClient != null) {
            mWebSocketClient.close();
            Log.e(LOG, "@@@@@@@@ webSocket session disconnected");
        }
    }
    public static void sendRequest(Context c, final String suffix, RequestDTO req, WebSocketListener listener)  {
        start = System.currentTimeMillis();
        webSocketListener = listener;
        request = req;
        ctx = c;
        TimerUtil.startTimer(new TimerUtil.TimerListener() {
            @Override
            public void onSessionDisconnected() {
                try {
                    connectWebSocket(suffix, request);
                    return;
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            if (mWebSocketClient == null) {
                connectWebSocket(suffix);
            } else {
                String json = gson.toJson(req);
                mWebSocketClient.send(json);
                Log.d(LOG, "########### web socket message sent\n" + json);
            }
        } catch (WebsocketNotConnectedException e) {
            try {
                Log.e(LOG,"WebsocketNotConnectedException. Problems with web socket", e);
                connectWebSocket(suffix, req);
            } catch (URISyntaxException e1) {
                Log.e(LOG,"Problems with web socket", e);
                webSocketListener.onError("Problem starting server socket communications\n" + e1.getMessage());
            }
        } catch (URISyntaxException e) {
            Log.e(LOG,"Problems with web socket", e);
            webSocketListener.onError("Problem starting server socket communications");
        }
    }

    private static void connectWebSocket(String socketSuffix, final RequestDTO request) throws URISyntaxException {
        URI uri = new URI(Statics.WEBSOCKET_URL + socketSuffix);

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.w(LOG, "########## WEBSOCKET Opened: " + serverHandshake.getHttpStatusMessage() + " elapsed ms: " + (end-start));
                String json = gson.toJson(request);
                mWebSocketClient.send(json);
                Log.d(LOG, "########### web socket request sent after onOpen\n" + json);
            }

            @Override
            public void onMessage(String response) {
                end = System.currentTimeMillis();
                TimerUtil.killTimer();
                Log.i(LOG, "########## onMessage, length: " + response.length()  + " elapsed: " + getElapsed()
                        + "\nString: " + response);
                try {
                    ResponseDTO r = gson.fromJson(response, ResponseDTO.class);
                    if (r.getStatusCode() == 0) {
                        if (r.getSessionID() != null) {
                            //SharedUtil.setSessionID(ctx, r.getSessionID());
                            String json = gson.toJson(request);
                            mWebSocketClient.send(json);
                            Log.d(LOG, "########### websocket message sent\n" + json);
                        } else {
                            webSocketListener.onMessage(r);
                        }
                    } else {
                        webSocketListener.onError(r.getMessage());
                    }
                } catch (Exception e) {
                    Log.e(LOG, "Failed to parse response from server", e);
                    webSocketListener.onError("Failed to parse response from server");
                }

            }

            @Override
            public void onMessage(ByteBuffer bb) {
                end = System.currentTimeMillis();
                TimerUtil.killTimer();
                Log.i(LOG, "########## onMessage, ByteBuffer capacity: " + bb.capacity());
                File dir = Environment.getExternalStorageDirectory();
                File zip = new File(dir, "data.zip");
                File unZip = new File(dir, "data.json");
                BufferedOutputStream stream = null;
                String content = null;
                try {
                    FileOutputStream fos = new FileOutputStream(zip);
                    stream = new BufferedOutputStream(fos);
                } catch (FileNotFoundException e) {
                    Log.e(LOG, "Failed to get output file", e);
                    webSocketListener.onError("Failed to get output file for saving server response");
                    return;
                }
                try {
                    stream.write(bb.array());
                    stream.flush();
                    stream.close();
                    //Log.d(LOG, "###### zip file: " + zip.getAbsolutePath() + " length: " + zip.length());
                    content = ZipUtil.unpack(zip, unZip);
                    //Log.d(LOG, "################ unpacked length: " + unZip.length());
                    if (content != null) {
                        Log.e(LOG, "############# onMessage, unpacked length: " + content.length() + " elapsed: " + getElapsed()
                                + "\n" + content);
                        ResponseDTO response = gson.fromJson(content, ResponseDTO.class);
                        if (response.getStatusCode() == 0) {
                            webSocketListener.onMessage(response);
                        } else {
                            webSocketListener.onError(response.getMessage());
                        }
                    } else {
                        webSocketListener.onError("Content from server failed. Response is null");
                    }
                } catch (IOException e) {
                    Log.e(LOG, "onMessage Failed", e);
                    webSocketListener.onError("Failed to unpack server response");
                }
            }


            @Override
            public void onClose(final int i, String s, boolean b) {
                Log.e(LOG, "########## WEBSOCKET onClose, status code:  " + i + " boolean: " + b);
                webSocketListener.onClose();
            }

            @Override
            public void onError(final Exception e) {
                Log.e(LOG, "----------> onError ", e);
                webSocketListener.onError("Server communications failed. Please try again");


            }
        };

        Log.d(LOG, "#### #### -------------> starting mWebSocketClient.connect ...");
        mWebSocketClient.connect();
    }

    private static void connectWebSocket(String socketSuffix) throws URISyntaxException {
        URI uri = new URI(Statics.WEBSOCKET_URL + socketSuffix);

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.w(LOG, "########## WEBSOCKET Opened: " + serverHandshake.getHttpStatusMessage());
                String json = gson.toJson(request);
                mWebSocketClient.send(json);
                Log.d(LOG, "########### web socket request sent after onOpen\n" + json);
            }

            @Override
            public void onMessage(String response) {
                TimerUtil.killTimer();
                end = System.currentTimeMillis();
                Log.i(LOG, "########## onMessage, length: " + response.length()  + " elapsed: " + getElapsed()
                        + "\n" + response);
                try {
                    ResponseDTO r = gson.fromJson(response, ResponseDTO.class);
                    if (r.getStatusCode() == 0) {
                        if (r.getSessionID() != null) {
                            //SharedUtil.setSessionID(ctx, r.getSessionID());
                        } else {
                            webSocketListener.onMessage(r);
                        }
                    } else {
                        webSocketListener.onError(r.getMessage());
                    }
                } catch (Exception e) {
                    Log.e(LOG, "Failed to parse response from server", e);
                    webSocketListener.onError("Failed to parse response from server");
                }

            }

            @Override
            public void onMessage(ByteBuffer bb) {
                TimerUtil.killTimer();
                end = System.currentTimeMillis();
                Log.i(LOG, "########## onMessage ByteBuffer capacity: " + bb.capacity());
                File dir = Environment.getExternalStorageDirectory();
                File zip = new File(dir, "data.zip");
                File unZip = new File(dir, "data.json");
                BufferedOutputStream stream = null;
                String content = null;
                try {
                    FileOutputStream fos = new FileOutputStream(zip);
                    stream = new BufferedOutputStream(fos);
                } catch (FileNotFoundException e) {
                    Log.e(LOG, "Failed to get output file", e);
                    webSocketListener.onError("Failed to get output file for saving server response");
                    return;
                }
                try {
                    stream.write(bb.array());
                    stream.flush();
                    stream.close();
                    //Log.d(LOG, "###### zip file: " + zip.getAbsolutePath() + " length: " + zip.length());
                    content = ZipUtil.unpack(zip, unZip);
                    //Log.d(LOG, "################ unpacked length: " + unZip.length());
                    if (content != null) {
                        Log.e(LOG, "############# onMessage, unpacked length: " + content.length() + " elapsed: " + getElapsed()
                                + "\n" + content);
                        ResponseDTO response = gson.fromJson(content, ResponseDTO.class);
                        if (response.getStatusCode() == 0) {
                            webSocketListener.onMessage(response);
                        } else {
                            webSocketListener.onError(response.getMessage());
                        }
                    } else {
                        webSocketListener.onError("Content from server failed. Response is null");
                    }
                } catch (IOException e) {
                    Log.e(LOG, "onMessage Failed", e);
                    webSocketListener.onError("Failed to unpack server response");
                }
            }


            @Override
            public void onClose(final int i, String s, boolean b) {
                Log.e(LOG, "########## WEBSOCKET onClose, status code:  " + i);
                webSocketListener.onClose();
            }

            @Override
            public void onError(final Exception e) {
                Log.e(LOG, "onError ", e);
                webSocketListener.onError("Server communications failed. Please try again");


            }
        };

        Log.d(LOG, "### #### -------------> starting mWebSocketClient.connect ...");
        mWebSocketClient.connect();
    }

    static WebSocketClient mWebSocketClient;
    static final String LOG = WebSocketUtil.class.getName();
    static final Gson gson = new Gson();
    public static String getElapsed() {
        BigDecimal m = new BigDecimal(end - start).divide(new BigDecimal(1000));

        return "" + m.doubleValue() + " seconds";
    }
}
