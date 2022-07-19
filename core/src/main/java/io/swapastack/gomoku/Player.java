package io.swapastack.gomoku;

import com.badlogic.gdx.graphics.Color;

import java.awt.*;

public class Player {

    Color colour_;

    /**
     * @param colour wanted colour
     *               set certain colour to a player
     * @author Marlene Mika
     */
    public boolean setColour(Color colour) {
        this.colour_ = colour;
        return true;
    }

    /**
     * @return colour of current player
     * @author Marlene Mika
     */
    public Color getColour() {
        return this.colour_;
    }

    /**
     * @param position wanted position to place a {@link GameStone}
     * @return a new stone
     * @author Marlene Mika
     */
    public GameStone placeStone(Point position) {
        return new GameStone(this.colour_, position);
    }

}
