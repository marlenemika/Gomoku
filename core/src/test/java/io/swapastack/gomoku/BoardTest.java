package io.swapastack.gomoku;

import com.badlogic.gdx.graphics.Color;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void getStone() {
        Board testBoard = createBoard();
        GameStone testStone = new GameStone(Color.BLACK, new Point(5, 5));
        testBoard.setStone(testStone);
        assertEquals(testStone, testBoard.getStone(5, 5));
        assertNull(testBoard.getStone(5, 6));
    }

    @Test
    void setStone() {
        Board testBoard = createBoard();
        GameStone testStone1 = new GameStone(Color.BLACK, new Point(10, 5));
        GameStone testStone2 = new GameStone(Color.BLACK, new Point(16, 20));
        assertTrue(testBoard.setStone(testStone1));
        assertFalse(testBoard.setStone(testStone2));
    }

    @Test
    void fullBoard() {
        Board testBoard = createBoard();
        GameStone testStone = new GameStone(Color.BLACK, new Point(0, 0));
        for (int row = 0; row < GameScreen.grid_size_; row++) {
            for (int column = 0; column < GameScreen.grid_size_ - 1; column++) {
                testStone.setPosition(new Point(row, column));
                testBoard.setStone(testStone);
            }
        }

        GameStone testStoneLastRow = new GameStone(Color.WHITE, new Point(GameScreen.grid_size_ - 1, 0));
        for (int i = 0; i < GameScreen.grid_size_ - 1; i++) {
            testStoneLastRow.setPosition(new Point(i, GameScreen.grid_size_ - 1));
            testBoard.setStone(testStoneLastRow);
        }

        if (testBoard.setStone(new GameStone(Color.WHITE, new Point(GameScreen.grid_size_ - 1, GameScreen.grid_size_ - 1))))
            assertTrue(testBoard.fullBoard());

        else
            assertFalse(testBoard.fullBoard());
    }

    @Test
    void checkWinCondition() {
        Board testBoard = createBoard();
        GameStone testStone1 = new GameStone(Color.BLACK, new Point(0, 0));
        GameStone testStone2 = new GameStone(Color.BLACK, new Point(0, 1));
        GameStone testStone3 = new GameStone(Color.BLACK, new Point(0, 2));
        GameStone testStone4 = new GameStone(Color.BLACK, new Point(0, 3));
        GameStone testStone5 = new GameStone(Color.BLACK, new Point(0, 4));
        GameStone testStone6 = new GameStone(Color.WHITE, new Point(0, 5));

        testBoard.setStone(testStone1);
        testBoard.setStone(testStone2);
        testBoard.setStone(testStone3);
        testBoard.setStone(testStone4);

        if (testBoard.setStone(testStone5))
            assertTrue(testBoard.checkWinCondition(testStone1.getColour(), testStone1.getPosition()));
        else if (testBoard.setStone(testStone6) && testBoard.setStone(testStone5))
            assertFalse(testBoard.checkWinCondition(testStone6.getColour(), testStone6.getPosition()));
        else if (testBoard.setStone(testStone6))
            assertFalse(testBoard.checkWinCondition(testStone6.getColour(), testStone6.getPosition()));
    }

    private Board createBoard() {
        return new Board(15, 15);
    }

}