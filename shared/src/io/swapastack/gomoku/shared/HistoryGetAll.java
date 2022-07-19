package io.swapastack.gomoku.shared;

import java.util.UUID;

/**
 * This class represents the HistoryGetAll message specified in the network standard.
 * This message is used to request the **complete** game server history.
 * This message must contain the connection specific userId as UUID.
 *
 * @author Dennis Jehle
 */
public class HistoryGetAll extends ExtractorMessage {
    public UUID userId;

    public HistoryGetAll(UUID userId) {
        super(MessageType.HistoryGetAll);
        this.userId = userId;
    }
}
