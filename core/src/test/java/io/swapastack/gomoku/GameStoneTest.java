package io.swapastack.gomoku;

import com.badlogic.gdx.graphics.Color;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class GameStoneTest {

    @Test
    void getColour() {
        GameStone testStoneOne = new GameStone(Color.BLACK, new Point(5, 5));
        GameStone testStoneTwo = new GameStone();
        assertEquals(testStoneOne.colour_, testStoneOne.getColour());
        assertNull(testStoneTwo.getColour());
    }

    @Test
    void getPosition() {
        GameStone testStoneOne = new GameStone(Color.BLACK, new Point(5, 5));
        GameStone testStoneTwo = new GameStone();
        assertEquals(testStoneOne.getPosition(), new Point(5, 5));
        assertNull(testStoneTwo.getPosition());
    }

    @Test
    void setPosition() {
        GameStone testStoneOne = new GameStone();
        assertTrue(testStoneOne.setPosition(new Point(13, 13)));
    }
}