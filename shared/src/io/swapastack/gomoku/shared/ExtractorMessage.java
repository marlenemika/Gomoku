package io.swapastack.gomoku.shared;

/**
 * This class is used as a pseudo message.
 * If the server receives a message it is encoded as JSON.
 * Therefore the server has to figure out which message type was received.
 * The server parses the JSON string to this io.swapastack.gomoku.shared.ExtractorMessage object.
 * If the message received is valid, then there is a message type.
 * After the message type is extracted the JSON string gets parsed to the correct
 * message class object, e.g. HistoryPush.
 *
 * @author Dennis Jehle
 */
public class ExtractorMessage {
    public MessageType messageType;

    public ExtractorMessage(MessageType messageType) {
        this.messageType = messageType;
    }

    public MessageType MessageType() {
        return messageType;
    }

}
