package io.swapastack.gomoku.shared;

/**
 * This class represents the GoodbyeClient message specified in the network standard.
 * The server sends this message after a GoodbyeServer message was received.
 * The server closes the WebSocket connection after this message.
 * The goodbyeMessage String is not further specified in the network standard, so it could be null, empty, String.
 *
 * @author Dennis Jehle
 */
public class GoodbyeClient extends ExtractorMessage {
    public String goodbyeMessage;

    public GoodbyeClient(String goodbyeMessage) {
        super(MessageType.GoodbyeClient);
        this.goodbyeMessage = goodbyeMessage;
    }
}
