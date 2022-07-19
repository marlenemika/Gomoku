package io.swapastack.gomoku.shared;

import java.util.UUID;

/**
 * This class represents the HistoryPush message specified in the network standard.
 * This message is used to add a new game history record to the game history server.
 * This message must contain the connection specific userId as UUID.
 * This message must contain two non-empty && non-null Strings for playerOneName, playerTwoName.
 * This message must contain two boolean values for playerOneWinner, playerTwoWinner, they must not be both true.
 *
 * @author Dennis Jehle
 */
public class HistoryPush extends ExtractorMessage {
    public UUID userId;
    public String playerOneName;
    public String playerTwoName;
    public boolean playerOneWinner;
    public boolean playerTwoWinner;

    public HistoryPush(
            UUID userId
            , String playerOneName
            , String playerTwoName
            , boolean playerOneWinner
            , boolean playerTwoWinner) {
        super(MessageType.HistoryPush);
        this.userId = userId;
        this.playerOneName = playerOneName;
        this.playerTwoName = playerTwoName;
        this.playerOneWinner = playerOneWinner;
        this.playerTwoWinner = playerTwoWinner;
    }
}
