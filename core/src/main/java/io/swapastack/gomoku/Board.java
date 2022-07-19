package io.swapastack.gomoku;

import com.badlogic.gdx.graphics.Color;

import java.awt.*;

public class Board {

    private final GameStone[][] board;

    int countHorizontal,
            countVertical,
            countDiagonalLURD,
            countDiagonalLDRU;


    /**
     * @param x size of board in x direction
     * @param y size of board in y direction
     *          Constructor
     * @author Marlene Mika
     */
    public Board(int x, int y) {
        board = new GameStone[x][y];
    }

    /**
     * @param x current x position
     * @param y current y position
     * @return wanted GameStone
     * @author Marlene Mika
     */
    public GameStone getStone(int x, int y) {
        return board[x][y];
    }

    /**
     * @param stone place given stone
     * @return successful placement
     * @author Marlene Mika
     */
    public boolean setStone(GameStone stone) {
        if (stone.getPosition().x >= board.length || stone.getPosition().y >= board[0].length)
            return false;
        board[stone.getPosition().x][stone.getPosition().y] = stone;
        return true;
    }

    /**
     * @param gamescreen check on the given gamescreen
     * @return if move is valid or not
     * checks if the current wanted move is actually allowed or not
     * @author Marlene Mika
     */
    public boolean validMove(GameScreen gamescreen) {
        return board[gamescreen.gridPosition().x][gamescreen.gridPosition().y] == null;
    }

    /**
     * @return if board is fully placed with stones or not
     * @author Marlene Mika
     */
    public boolean fullBoard() {
        int count = 0;
        boolean isFull = false;
        for (int i = 0; i < GameScreen.grid_size_; i++) {
            for (int j = 0; j < GameScreen.grid_size_; j++) {
                if (board[i][j] != null)
                    count++;
            }
        }
        if (count == GameScreen.grid_size_ * GameScreen.grid_size_)
            isFull = true;
        return isFull;
    }

    /**
     * @param stonecolor current {@link GameStone#colour_}
     * @param point      current GameStone position
     * @return if win condition is fulfilled or not
     * @author Marlene Mika
     */
    public boolean checkWinCondition(Color stonecolor, Point point) {
        // count the horizontal stones in a row
        countHorizontal = 0;
        // count the vertical stones in a row
        countVertical = 0;
        // count the diagonal stones in a row, left side goes up and right side goes down
        countDiagonalLURD = 0;
        // count the diagonal stones in a row, left side goes down and right side goes up
        countDiagonalLDRU = 0;

        int x = point.x;
        int y = point.y;

        /**
         * recursive call of method, either one of those have to return true so that the player wins
         * start with current given position of stone
         * see method: {@link #checkWinCondition(Color, int, int, WayEnum)}
         * LEFT and RIGHT add up for {@countHorizontal}
         * UP and DOWN add up for {@countVertical}
         * LEFTUP and RIGHTDOWN add up for {@countDiagonalLURD}
         * LEFTDOWN and RIGHTUP add up for {@countDiagonalLDRU}
         * @author Marlene Mika
         * */

        return (checkWinCondition(stonecolor, x, y, WayEnum.LEFT) ||
                checkWinCondition(stonecolor, x, y, WayEnum.RIGHT) ||
                checkWinCondition(stonecolor, x, y, WayEnum.UP) ||
                checkWinCondition(stonecolor, x, y, WayEnum.DOWN) ||
                checkWinCondition(stonecolor, x, y, WayEnum.LEFTUP) ||
                checkWinCondition(stonecolor, x, y, WayEnum.LEFTDOWN) ||
                checkWinCondition(stonecolor, x, y, WayEnum.RIGHTUP) ||
                checkWinCondition(stonecolor, x, y, WayEnum.RIGHTDOWN));
    }

    /**
     * @param stonecolour colour of given stone
     * @param x           x coordinate of {@link GameStone} on Board
     * @param y           y coordinate of GameStone on board
     * @param direction   choose direction
     * @return iterative version threw too many errors -> recursive implementation
     * @author Marlene Mika
     */
    // recursive method for checking win condition
    public boolean checkWinCondition(Color stonecolour, int x, int y, WayEnum direction) {
        try {
            if (board[x][y] != null) {
                if (stonecolour == board[x][y].getColour()) {
                    switch (direction) {
                        case LEFT:
                            countHorizontal++;
                            return checkWinCondition(stonecolour, x - 1, y, WayEnum.LEFT);

                        case RIGHT:
                            countHorizontal++;
                            return checkWinCondition(stonecolour, x + 1, y, WayEnum.RIGHT);

                        case UP:
                            countVertical++;
                            return checkWinCondition(stonecolour, x, y - 1, WayEnum.UP);

                        case DOWN:
                            countVertical++;
                            return checkWinCondition(stonecolour, x, y + 1, WayEnum.DOWN);

                        case LEFTUP:
                            countDiagonalLURD++;
                            return checkWinCondition(stonecolour, x - 1, y - 1, WayEnum.LEFTUP);

                        case RIGHTUP:
                            countDiagonalLDRU++;
                            return checkWinCondition(stonecolour, x + 1, y - 1, WayEnum.RIGHTUP);

                        case LEFTDOWN:
                            countDiagonalLDRU++;
                            return checkWinCondition(stonecolour, x - 1, y + 1, WayEnum.LEFTDOWN);

                        case RIGHTDOWN:
                            countDiagonalLURD++;
                            return checkWinCondition(stonecolour, x + 1, y + 1, WayEnum.RIGHTDOWN);

                    }

                }
                else return false;
            }
            else
                return (countHorizontal == 6 || countVertical == 6 || countDiagonalLDRU == 6 || countDiagonalLURD == 6);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        return false;
    }
}