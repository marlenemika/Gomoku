package io.swapastack.gomoku;

import com.badlogic.gdx.graphics.Color;

import java.awt.*;

public class GameStone {

    public Color colour_;
    public Point position_;

    /**
     * @param colour   attribute, can either be black or white
     * @param position attribute
     *                 Constructor with attributes
     * @author Marlene Mika
     */
    public GameStone(Color colour, Point position) {
        colour_ = colour;
        position_ = position;
    }

    public GameStone() {

    }

    /**
     * @return colour of current stone
     * @author Marlene Mika
     */
    public Color getColour() {
        return colour_;
    }

    /**
     * @return position of current stone
     * @author Marlene Mika
     */
    public Point getPosition() {
        return position_;
    }

    /**
     * @param point wanted point
     *              specify position of a stone
     * @author Marlene Mika
     */
    public boolean setPosition(Point point) {
        position_ = point;
        return true;
    }
}
