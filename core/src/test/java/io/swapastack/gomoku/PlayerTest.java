package io.swapastack.gomoku;

import com.badlogic.gdx.graphics.Color;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlayerTest {

    @Test
    void setColour() {
        Player testPlayerOne = new Player();
        testPlayerOne.colour_ = Color.BLACK;
        assertTrue(testPlayerOne.setColour(Color.BLACK));
    }

    @Test
    void getColour() {
        Player testPlayerOne = new Player();
        testPlayerOne.colour_ = Color.BLACK;
        assertEquals(testPlayerOne.colour_, testPlayerOne.getColour());
    }

    @Test
    void placeStone() {
        Board testBoard = createBoard();
        Player testPlayerOne = new Player();
        testPlayerOne.setColour(Color.BLACK);
        testBoard.setStone(testPlayerOne.placeStone(new Point(5, 5)));
        assertEquals(testBoard.getStone(5, 5).getColour(), Color.BLACK);
    }

    private Board createBoard() {
        return new Board(15, 15);
    }
}