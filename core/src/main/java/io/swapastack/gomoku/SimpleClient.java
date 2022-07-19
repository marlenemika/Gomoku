package io.swapastack.gomoku;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swapastack.gomoku.shared.*;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.UUID;

/**
 * The SimpleClient extends the WebSocketClient class.
 * The SimpleClient class could be used to establish a WebSocket (ws, wss) connection
 * a WebSocketServer.
 * <p>
 * The send(String message) method could be used to send a String message to the
 * WebSocketServer.
 * <p>
 * note: this client could be used to implement the network standard document.
 *
 * @author Dennis Jehle
 */
public class SimpleClient extends WebSocketClient {

    // 'Google Gson is a java library that can be used to convert Java Object
    // into their JSON representation.'
    // see: https://github.com/google/gson
    // see: https://github.com/google/gson/blob/master/UserGuide.md#TOC-Serializing-and-Deserializing-Generic-Types
    private final Gson gson_;
    public UUID userId = null;
    private ExtractorMessage response;
    private HistoryAll getHistoryAll;
    private String msg;


    /**
     * This is the constructor of the SimpleClient class.
     *
     * @param server_uri {@link URI}
     * @author Dennis Jehle
     */
    public SimpleClient(URI server_uri) {
        super(server_uri);
        // create Gson Builder
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ExtractorMessage.class, new MessageDeserialize());
        gson_ = gsonBuilder.create();
        getHistoryAll = new HistoryAll();
    }

    /**
     * This method is called if the connection to the WebSocketServer is open.
     *
     * @param handshake_data {@link ServerHandshake}
     * @author Dennis Jehle
     */
    @Override
    public void onOpen(ServerHandshake handshake_data) {
        // create new HelloServer Java object
        HelloServer message = new HelloServer();
        // create JSON String from TestMessage Java object
        String test_message = gson_.toJson(message);
        // send JSON encoded test message as String to the connected WebSocket server
        send(test_message);
        // 'debug' output
        System.out.println("new connection opened");
    }

    /**
     * This method is called if the connection to the WebSocketServer was closed.
     *
     * @param code   status code
     * @param reason the reason for closing the connection
     * @param remote was the close initiated by the remote host
     * @author Dennis Jehle
     */
    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("closed with exit code " + code + " additional info: " + reason);
    }

    /**
     * This message is called if the WebSocketServer sends a String message to the client.
     *
     * @param message a String message from the WebSocketServer e.g. JSON message
     * @author Dennis Jehle
     * In this method, all the Messages regarding the client do appear.
     * @author Marlene Mika
     */
    @Override
    public void onMessage(String message) {
        msg = message;
        ExtractorMessage newMessage = gson_.fromJson(message, ExtractorMessage.class);
        if (newMessage != null) {
            switch (newMessage.messageType) {
                case WelcomeClient:
                    this.userId = ((WelcomeClient) newMessage).userId;
                    break;

                case HistorySaved:
                case HistoryNotSaved:
                case HistoryAll:
                    this.response = newMessage;
                    break;

                case GoodbyeClient:
                    this.close();
            }
        }
        System.out.println("received message: " + message);

    }

    /**
     * sends goodbye message to server
     *
     * @author Marlene Mika
     */
    public void onGoodbyeClient() {
        String goodBye = gson_.toJson(new GoodbyeServer(this.userId));
        send(goodBye);
    }

    /**
     * This method is called if the WebSocketServer send a binary message to the client.
     * note: This method is not necessary for this project, because the network standard
     * document specifies a JSON String message protocol.
     *
     * @param message a binary message
     * @author Dennis Jehle
     */
    @Override
    public void onMessage(ByteBuffer message) {
        // do nothing, because binary messages are not supported
    }

    /**
     * This method is called if an exception was thrown.
     *
     * @param exception {@link Exception}
     * @author Dennis Jehle
     */
    @Override
    public void onError(Exception exception) {
        System.err.println("an error occurred:" + exception);
    }

    /**
     * @param playerOneName   name of player one
     * @param playerTwoName   name of player two
     * @param playerOneWinner is player one winner
     * @param playerTwoWinner is player two winner
     * @return boolean contains if history was sent successfully
     * first wait for 500ms
     * check if user id is given
     * then wait for 500ms
     * check if history has been saved.
     * @author Marlene Mika
     */
    public boolean historySaved(String playerOneName, String playerTwoName,
                                boolean playerOneWinner, boolean playerTwoWinner) {
        try {
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (userId != null)
            send(gson_.toJson(new HistoryPush(userId, playerOneName, playerTwoName,
                    playerOneWinner, playerTwoWinner)));
        else
            return false;

        try {
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response != null) {
            return response instanceof HistorySaved;
        }
        return false;
    }

    /**
     * @return ArrayList containing the History of the current game
     * wait for 500ms
     * Check if userId is already given.
     * then wait 500ms
     * Check if historyAll message contains game history.
     * @author Marlene Mika
     */
    public ArrayList<History> getHistoryAll() {
        try {
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (userId != null)
            send(gson_.toJson(new HistoryGetAll(userId)));
        else
            return null;

        try {
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response != null) {
            ArrayList<History> historyPushed = getHistoryAll.getHistoryPushes();
            getHistoryAll = null;
            return historyPushed;
        }
        else
            return null;
    }

    public String getMsg() {
        return msg;
    }

}
