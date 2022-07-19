package io.swapastack.gomoku.shared;

/**
 * This class represents the HistoryNotSaved message specified in the network standard.
 * This message is used to tell the client that there was an error while saving the game history.
 * This message can happen after the client sends a HistoryPush message.
 * There are several reasons for this message:
 * - internal server error
 * - malformed HistoryPush message
 * - IOException
 * - ...
 * <p>
 * The network standard does not specify that the error is further specified in this message.
 *
 * @author Dennis Jehle
 */
public class HistoryNotSaved extends ExtractorMessage {

    public HistoryNotSaved() {
        super(MessageType.HistoryNotSaved);
    }

}
