package io.swapastack.gomoku;

import com.google.gson.Gson;
import io.swapastack.gomoku.shared.GoodbyeClient;
import io.swapastack.gomoku.shared.WelcomeClient;
import org.java_websocket.server.WebSocketServer;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleClientTest {

    @Test
    void onOpen() throws URISyntaxException {
        SimpleClient testSimpleClient = new SimpleClient(new URI(String.format("ws://%s:%d", "localhost", 42000 + 1)));
        WebSocketServer testServer = new GomokuServer(new InetSocketAddress("localhost", 42000 + 1));
        testServer.setReuseAddr(true);
        testServer.setTcpNoDelay(true);
        testServer.start();
        Gson gson = new Gson();
        try {
            Thread.sleep(1500);
        } catch (Exception e) {
            e.printStackTrace();
        }
        testSimpleClient.connect();
        try {
            Thread.sleep(1500);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WelcomeClient welcome_client = new WelcomeClient(testSimpleClient.userId, "Moin!");
        String temp = testSimpleClient.getMsg();
        assertEquals(temp, gson.toJson(welcome_client));
    }

    @Test
    void onClose() throws URISyntaxException {
        SimpleClient testSimpleClient = new SimpleClient(new URI(String.format("ws://%s:%d", "localhost", 42000 + 2)));
        WebSocketServer testServer = new GomokuServer(new InetSocketAddress("localhost", 42000 + 2));
        testServer.setReuseAddr(true);
        testServer.setTcpNoDelay(true);
        testServer.start();
        Gson gson = new Gson();
        try {
            Thread.sleep(1500);
        } catch (Exception e) {
            e.printStackTrace();
        }
        testSimpleClient.connect();
        try {
            Thread.sleep(1500);
        } catch (Exception e) {
            e.printStackTrace();
        }
        GoodbyeClient goodbyeClient = new GoodbyeClient("Servus!");
        testSimpleClient.onGoodbyeClient();
        try {
            Thread.sleep(1500);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String temp = testSimpleClient.getMsg();
        assertEquals(temp, gson.toJson(goodbyeClient));
    }

    @Test
    void onMessage() throws URISyntaxException {
        SimpleClient testSimpleClient = new SimpleClient(new URI(String.format("ws://%s:%d", "localhost", 42000 + 3)));
        WebSocketServer testServer = new GomokuServer(new InetSocketAddress("localhost", 42000 + 3));
        testServer.setReuseAddr(true);
        testServer.setTcpNoDelay(true);
        testServer.start();
        Gson gson = new Gson();
        try {
            Thread.sleep(1500);
        } catch (Exception e) {
            e.printStackTrace();
        }
        testSimpleClient.connect();
        try {
            Thread.sleep(1500);
        } catch (Exception e) {
            e.printStackTrace();
        }
        GoodbyeClient goodbyeClient = new GoodbyeClient("Servus!");
        testSimpleClient.onGoodbyeClient();
        try {
            Thread.sleep(1500);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String temp = testSimpleClient.getMsg();
        assertEquals(temp, gson.toJson(goodbyeClient));
    }
}