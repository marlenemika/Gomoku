package io.swapastack.gomoku;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.particles.values.MeshSpawnShapeValue;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.*;
import java.net.URI;

/**
 * In this class the whole logic of the game is implemented.
 *
 * @author Marlene Mika
 */
public class GameScreen implements Screen {

    // reference to the parent object
    private final Gomoku parent_;
    // OrthographicCamera
    private final OrthographicCamera camera_;
    // Viewport
    private final Viewport viewport_;
    // Stage
    private final Stage stage_;
    // SpriteBatch
    private final SpriteBatch sprite_batch_;
    // ShapeRenderer
    // see: https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/graphics/glutils/ShapeRenderer.html
    private final ShapeRenderer shape_renderer_;
    // Skin
    private final Skin skin_;
    // Background
    private final Texture background_texture_;
    // Game board
    private final Texture board_texture_;
    // grid dimensions
    public static final int grid_size_ = 15;
    private static final float padding = 100.f;
    private static final float line_width = 5.f;
    // gather necessary information for grid drawing
    private final float screen_width = Gdx.graphics.getWidth();
    private final float screen_height = Gdx.graphics.getHeight();
    private final float column_height = screen_height - 2.f * padding;
    private final float row_width = column_height;
    private final float offset = row_width / ((float) grid_size_ - 1.f);
    private final float top_left_x = screen_width / 2.f - row_width / 2.f;

    // hostname / ip of server
    // e.g. localhost
    // e.g. 127.0.0.1
    public static final String host = "localhost";
    // port to connect to
    // see: https://en.wikipedia.org/wiki/List_of_TCP_and_UDP_port_numbers
    public static final int port = 42000;

    // create a GameScreen so that the game can be saved when changing screens
    public static GameScreen gamescreen;

    // create components for the game procedure
    private boolean checkedWinCondition = false;
    private boolean valid = true;

    // create two players that play against each other
    Player player1;
    Player player2;

    // create a board
    Board board;

    // the user(s) can switch to the game menu
    Button game_menu_button;

    // buttons for swap2
    Button black;
    Button white;
    Button playerOne;

    // counters for swap2
    int count = 0;
    int oneDecides = 0;

    // component for swap2
    boolean newGameStarted = true;

    // if playersTurn is true -> player 1 is turn
    // else it is the turn of player 2
    boolean playersTurn = true;

    // texts for the labels when the game is over
    String player_one, player_two;

    // shows the two players on the game information screen
    Label playerOneLabel;
    Label playerTwoLabel;

    // make sure that only one connection happens and that there is no loop
    int serverConnect = 0;

    // works as an arrow. shows whose turn it is
    MeshSpawnShapeValue.Triangle whosTurn;

    /**
     * This method is used to save the game when switching to {@link GameMenuScreen}
     *
     * @author Marlene Mika
     */
    private void saveGame() {
        gamescreen = this;
    }

    /**
     * @param parent stores reference to parent class
     *               Constructor
     * @author Marlene Mika
     */
    public GameScreen(final Gomoku parent) {
        // store reference to parent class
        parent_ = parent;
        // initialize OrthographicCamera with current screen size
        Tuple<Integer> client_area_dimensions = parent_.get_window_dimensions();
        camera_ = new OrthographicCamera((float) client_area_dimensions.first, (float) client_area_dimensions.second);
        // initialize ScreenViewport with the OrthographicCamera created above
        viewport_ = new ScreenViewport(camera_);
        // initialize SpriteBatch
        sprite_batch_ = new SpriteBatch();
        // initialize the Stage with the ScreenViewport created above
        stage_ = new Stage(viewport_, sprite_batch_);
        // initialize ShapeRenderer
        shape_renderer_ = new ShapeRenderer();
        // initialize the Skin
        skin_ = new Skin(Gdx.files.internal("neon/skin/neon-ui.json"));
        // add background
        background_texture_ = new Texture("space/space_scaled_hdready.png");
        // add board texture
        board_texture_ = new Texture("Board/pexels-life-of-pix-8892.jpg");

        // create switch to MainMenu button
        Button menu_screen_button = new TextButton("LEAVE GAME", skin_);
        menu_screen_button.sizeBy(50, 10);
        menu_screen_button.setPosition(25.f, 25.f);
        // add InputListener to Button, and close app if Button is clicked
        menu_screen_button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                parent_.change_screen(ScreenEnum.MENU);
            }
        });

        // create open GameMenu button
        game_menu_button = new TextButton("GAME MENU", skin_);
        game_menu_button.sizeBy(50, 10);
        game_menu_button.setPosition(25.f, 100.f);
        // add InputListener to Button, and open menu if Button is clicked
        game_menu_button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                saveGame();
                GameMenuScreen gamemenu = new GameMenuScreen(parent_);
                parent_.setScreen(gamemenu);
            }
        });

        // add buttons to stage
        stage_.addActor(menu_screen_button);
        stage_.addActor(game_menu_button);

        // create board
        board = new Board(15, 15);

        // create two players and set colours
        player1 = new Player();
        player2 = new Player();
        player1.setColour(Color.BLACK);
        player2.setColour(Color.WHITE);

        // create buttons for swap 2
        black = new TextButton("BLACK", skin_);
        white = new TextButton("WHITE", skin_);
        playerOne = new TextButton("PLAYER\nONE", skin_);
        black.sizeBy(50, 10);
        white.sizeBy(50, 10);
        playerOne.sizeBy(40, 10);
        black.setPosition(1100.f, 375.f);
        white.setPosition(1100.f, 300.f);
        playerOne.setPosition(1100.f, 225.f - playerOne.getHeight() / 4);
        // make buttons for swap 2 invisible
        black.setVisible(false);
        white.setVisible(false);
        playerOne.setVisible(false);

        // labels for game status: show the two players
        playerOneLabel = new Label("PLAYER ONE", skin_);
        playerOneLabel.setPosition(offset, 375.f);
        playerTwoLabel = new Label("PLAYER TWO", skin_);
        playerTwoLabel.setPosition(offset, 275.f);

        // add all the components
        stage_.addActor(black);
        stage_.addActor(white);
        stage_.addActor(playerOne);
        stage_.addActor(playerOneLabel);
        stage_.addActor(playerTwoLabel);
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     * @author Dennis Jehle
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // update camera
        camera_.update();

        // update the current SpriteBatch
        sprite_batch_.setProjectionMatrix(camera_.combined);

        sprite_batch_.begin();
        sprite_batch_.draw(background_texture_, 0, 0, viewport_.getScreenWidth(), viewport_.getScreenHeight());
        sprite_batch_.end();

        sprite_batch_.begin();
        sprite_batch_.draw(board_texture_, viewport_.getScreenWidth() / 2.f - board_texture_.getWidth() / 2.f
                , viewport_.getScreenHeight() / 2.f - board_texture_.getHeight() / 2.f);
        sprite_batch_.end();

        // draw grid
        shape_renderer_.begin(ShapeType.Filled);
        for (int i = 0; i < grid_size_; i++) {
            float fraction = (float) (i + 1) / (float) grid_size_;
            shape_renderer_.rectLine(
                    top_left_x + i * offset, padding + column_height
                    , top_left_x + i * offset, padding
                    , line_width
                    , Color.BLACK
                    , Color.BLACK
            );
            shape_renderer_.rectLine(
                    top_left_x, padding + column_height - i * offset
                    , top_left_x + row_width, padding + column_height - i * offset
                    , line_width
                    , Color.BLACK
                    , Color.BLACK
            );
        }
        shape_renderer_.end();

        // draw stones
        drawStones();

        // create two circles next to the players. These are replaced later after the colours are set in swap 2
        shape_renderer_.begin(ShapeType.Line);
        Gdx.gl.glLineWidth(3);
        shape_renderer_.setColor(Color.ORANGE);
        shape_renderer_.circle(offset + playerOneLabel.getWidth() + 25, 375.f + 9.f, 14);
        shape_renderer_.end();

        shape_renderer_.begin(ShapeType.Line);
        Gdx.gl.glLineWidth(3);
        shape_renderer_.setColor(Color.ORANGE);
        shape_renderer_.circle(offset + playerOneLabel.getWidth() + 25, 275.f + 9.f, 14);
        shape_renderer_.end();

        if (valid) {
            validPreview();
        }

        if (newGameStarted) {
            swap2();
        }

        else {
            // show the coloured stones next to the players in the game status
            shape_renderer_.begin(ShapeType.Filled);
            shape_renderer_.setColor(player1.getColour());
            shape_renderer_.circle(offset + playerOneLabel.getWidth() + 25, 375.f + 9.f, 13);
            shape_renderer_.end();

            shape_renderer_.begin(ShapeType.Filled);
            shape_renderer_.setColor(player2.getColour());
            shape_renderer_.circle(offset + playerOneLabel.getWidth() + 25, 275.f + 9.f, 13);
            shape_renderer_.end();

            // switching the arrow to show whose turn it is
            if (playersTurn && ! (board.fullBoard() || checkedWinCondition)) {
                shape_renderer_.begin(ShapeType.Filled);
                shape_renderer_.setColor(Color.CYAN);
                shape_renderer_.triangle(offset + playerOneLabel.getWidth() + 75.f, 375.f + 20.f,
                        offset + playerOneLabel.getWidth() + 50.f, 375.f + 10.f,
                        offset + playerOneLabel.getWidth() + 75.f, 375.f + 0.f);
                shape_renderer_.end();
            }

            else if (! playersTurn && ! (board.fullBoard() || checkedWinCondition)) {
                shape_renderer_.begin(ShapeType.Filled);
                shape_renderer_.setColor(Color.CYAN);
                shape_renderer_.triangle(offset + playerOneLabel.getWidth() + 75.f, 275.f + 20.f,
                        offset + playerOneLabel.getWidth() + 50.f, 275.f + 10.f,
                        offset + playerOneLabel.getWidth() + 75.f, 275.f + 0.f);
                shape_renderer_.end();
            }

            // insert stones and check win condition
            if ((Gdx.input.getX() >= 200.f && Gdx.input.getY() >= 100.f) && (Gdx.input.getX() <= 1000.f
                    && Gdx.input.getY() >= 100.f)) {
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    if (board.validMove(this) && valid) {
                        if (playersTurn) {
                            board.setStone(player1.placeStone(gridPosition()));
                            if (board.checkWinCondition(player1.getColour(), gridPosition()))
                                checkedWinCondition = true;
                            playersTurn = false;
                        }
                        else {
                            board.setStone(player2.placeStone(gridPosition()));
                            if (board.checkWinCondition(player2.getColour(), gridPosition()))
                                checkedWinCondition = true;
                            playersTurn = true;
                        }
                    }
                }
            }
            isGameOver();
        }

        // update the Stage
        stage_.act(delta);
        // draw the Stage
        stage_.draw();

    }

    /**
     * Checks if the whole {@link Board}is already placed with {@link GameStone} but without the Win Condition
     * being ever true.
     * Also checks if the Win Condition is fulfilled.
     *
     * @author Marlene Mika
     */
    public void isGameOver() {
        if (board.fullBoard() || checkedWinCondition) {
            // remove game menu button and replace with start new game button
            game_menu_button.remove();

            Tuple<Integer> client_area_dimensions = parent_.get_window_dimensions();

            String game_over = "GAME OVER BLACK WHITE WINS!";
            // initialize FreeTypeFontGenerator for BitmapFont generation
            FreeTypeFontGenerator bitmap_font_generator_ = new FreeTypeFontGenerator
                    (Gdx.files.internal("fonts/venus_rising/venus_rising_rg.ttf"));
            // specify parameters for BitmapFont generation
            FreeTypeFontGenerator.FreeTypeFontParameter bitmap_font_parameter
                    = new FreeTypeFontGenerator.FreeTypeFontParameter();
            // set font size
            bitmap_font_parameter.size = 100;
            // specify available letters
            bitmap_font_parameter.characters = game_over;
            // set font color in RGBA format (red, green, blue, alpha)
            bitmap_font_parameter.color = new Color(0.f, 1.f, 1.f, 1.f);
            // other specifications
            bitmap_font_parameter.borderWidth = 1;
            bitmap_font_parameter.borderColor = Color.BLACK; // alternative enum color specification
            bitmap_font_parameter.shadowOffsetX = 3;
            bitmap_font_parameter.shadowOffsetY = 3;
            bitmap_font_parameter.shadowColor = new Color(0.f, 0.439f, 0.439f, 1.f);

            // generate BitmapFont with FreeTypeFontGenerator and FreeTypeFontParameter specification
            BitmapFont latin_font = bitmap_font_generator_.generateFont(bitmap_font_parameter);

            // create a LabelStyle object to specify Label font
            Label.LabelStyle latin_label_style = new Label.LabelStyle();
            latin_label_style.font = latin_font;

            Label game_over_text;
            String tie = "GAME\nOVER!";

            // specified procedure if tie
            if (board.fullBoard()) {
                game_over_text = new Label(tie, latin_label_style);
                game_over_text.setFontScale(1, 1);
                game_over_text.setText(tie);
                game_over_text.setPosition((float) client_area_dimensions.first / 2.f
                        , (float) client_area_dimensions.second / 2.f, Align.center);
                stage_.addActor(game_over_text);

                if (serverConnect == 0) {
                    try {
                        SimpleClient simpleClient = new SimpleClient
                                (new URI(String.format("ws://%s:%d", host, port)));
                        // blocked until server has connected
                        simpleClient.connectBlocking();
                        simpleClient.historySaved("Player 1", "Player 2",
                                false, false);
                        simpleClient.getHistoryAll();
                        simpleClient.onGoodbyeClient();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    serverConnect++;
                }
            }

            // specified procedure if player 1 wins
            else if (board.checkWinCondition(player1.getColour(), gridPosition())) {
                game_over_text = new Label(player_one, latin_label_style);
                game_over_text.setFontScale(1, 1);
                game_over_text.setText(player_one);
                game_over_text.setPosition((float) client_area_dimensions.first / 2.f
                        , (float) client_area_dimensions.second / 2.f, Align.center);
                stage_.addActor(game_over_text);

                if (serverConnect == 0) {
                    try {
                        SimpleClient simpleClient = new SimpleClient
                                (new URI(String.format("ws://%s:%d", host, port)));
                        // blocked until server has connected
                        simpleClient.connectBlocking();
                        simpleClient.historySaved("Player 1", "Player 2",
                                true, false);
                        simpleClient.getHistoryAll();
                        simpleClient.onGoodbyeClient();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    serverConnect++;
                }
            }

            // specified procedure if player 2 wins
            else if (board.checkWinCondition(player2.getColour(), gridPosition())) {
                game_over_text = new Label(player_two, latin_label_style);
                game_over_text.setFontScale(1, 1);
                game_over_text.setText(player_two);
                game_over_text.setPosition((float) client_area_dimensions.first / 2.f
                        , (float) client_area_dimensions.second / 2.f, Align.center);
                stage_.addActor(game_over_text);

                if (serverConnect == 0) {
                    try {
                        SimpleClient simpleClient = new SimpleClient
                                (new URI(String.format("ws://%s:%d", host, port)));
                        // blocked until server has connected
                        simpleClient.connectBlocking();
                        simpleClient.historySaved("Player 1", "Player 2",
                                false, true);
                        simpleClient.getHistoryAll();
                        simpleClient.onGoodbyeClient();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    serverConnect++;
                }
            }

            // if players want to play another game they can easily press the restart button
            Button new_game_button = new TextButton("NEW GAME", skin_);
            new_game_button.sizeBy(50, 10);
            new_game_button.setPosition(25.f, 100.f);

            // add InputListener to Button, and start new game if Button is clicked
            new_game_button.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    parent_.change_screen(ScreenEnum.GAME);
                }
            });

            stage_.addActor(new_game_button);
            valid = false;

        }
    }

    /**
     * Gives the user(s) a preview if the current position would be a valid move to place a {@link GameStone} on the
     * {@link Board}. If so, a green circle is shown. Otherwise there will be a red circle
     *
     * @author Marlene Mika
     */
    public void validPreview() {
        if ((Gdx.input.getX() >= 200.f && Gdx.input.getY() >= 100.f) && (Gdx.input.getX() <= 1000.f
                && Gdx.input.getY() >= 100.f)) {
            if (board.validMove(this)) {
                shape_renderer_.begin(ShapeType.Filled);
                shape_renderer_.setColor(Color.GREEN);
                shape_renderer_.circle(top_left_x + gridPosition().x * offset,
                        Gdx.graphics.getHeight() - padding - (gridPosition().y * offset), 13);
            }
            else {
                shape_renderer_.begin(ShapeType.Filled);
                shape_renderer_.setColor(Color.RED);
                shape_renderer_.circle(top_left_x + gridPosition().x * offset,
                        Gdx.graphics.getHeight() - padding - (gridPosition().y * offset), 13);
            }
            shape_renderer_.end();
        }
    }


    /**
     * Draws the {@link GameStone} in the according colour on the {@link Board}.
     *
     * @author Marlene Mika
     */
    public void drawStones() {
        shape_renderer_.begin(ShapeType.Filled);
        for (int x = 0; x < grid_size_; x++) {
            for (int y = 0; y < grid_size_; y++) {
                GameStone currentGameStone = board.getStone(x, grid_size_ - y - 1);
                if (currentGameStone != null) {
                    shape_renderer_.setColor(currentGameStone.getColour());
                    shape_renderer_.circle(top_left_x + x * offset, padding + y * offset, 13);
                }
            }
        }
        shape_renderer_.end();
    }


    /**
     * Implementation of swap 2.
     * First, three {@link GameStone} are placed by Player 1 {@link Player} (black white black) on the {@link Board}.
     * Then Player 2 decides whether they want to play with the black or white stones.
     * If the black stones are chosen, then it's Player 1's turn again with white.
     * If the white stones are chosen, Player 2 places a white stone and then it's the turn of Player 1.
     * If Player 2 doesn't want to choose colour, he can alternatively let Player 1 choose. Therefore he places
     * another white and black stone.
     * Then Player 1 can choose whether they want to play with black stones (then Player 2 being turn again with white)
     * or choose white and place another white stone with then Player 2 being turn again with black.
     * During the time when the Players have to push a button, the {@link GameScreen#validPreview()} is removed.
     *
     * @author Marlene Mika
     */
    public void swap2() {
        if (count < 3) {
            shape_renderer_.begin(ShapeType.Filled);
            shape_renderer_.setColor(Color.CYAN);
            shape_renderer_.triangle(offset + playerOneLabel.getWidth() + 75.f, 375.f + 20.f,
                    offset + playerOneLabel.getWidth() + 50.f, 375.f + 10.f,
                    offset + playerOneLabel.getWidth() + 75.f, 375.f + 0.f);
            shape_renderer_.end();

            if ((Gdx.input.getX() >= 200.f && Gdx.input.getY() >= 100.f) && (Gdx.input.getX() <= 1000.f
                    && Gdx.input.getY() >= 100.f)) {
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    if (board.validMove(this)) {
                        if (playersTurn) {
                            board.setStone(player1.placeStone(gridPosition()));
                            playersTurn = false;
                        }
                        else {
                            board.setStone(player2.placeStone(gridPosition()));
                            playersTurn = true;
                        }
                        count++;
                    }
                }
            }
        }

        if (count == 3) {
            valid = false;
            shape_renderer_.begin(ShapeType.Filled);
            shape_renderer_.setColor(Color.CYAN);
            shape_renderer_.triangle(offset + playerOneLabel.getWidth() + 75.f, 275.f + 20.f,
                    offset + playerOneLabel.getWidth() + 50.f, 275.f + 10.f,
                    offset + playerOneLabel.getWidth() + 75.f, 275.f + 0.f);
            shape_renderer_.end();
            black.setVisible(true);
            white.setVisible(true);
            playerOne.setVisible(true);
        }

        if (black.isPressed() && playerOne.isVisible() && (count == 3)) {
            valid = true;
            black.setVisible(false);
            white.setVisible(false);
            playerOne.setVisible(false);
            player1.setColour(Color.WHITE);
            player2.setColour(Color.BLACK);
            player_one = "WHITE\nWINS!";
            player_two = "BLACK\nWINS!";
            playersTurn = true;
            newGameStarted = false;
        }

        else if (white.isPressed() && playerOne.isVisible() && (count == 3)) {
            valid = true;
            black.setVisible(false);
            white.setVisible(false);
            playerOne.setVisible(false);
            player1.setColour(Color.BLACK);
            player2.setColour(Color.WHITE);
            player_one = "BLACK\nWINS!";
            player_two = "WHITE\nWINS!";
            playersTurn = false;
            newGameStarted = false;
        }

        else if (playerOne.isPressed() && (count == 3)) {
            black.setVisible(false);
            white.setVisible(false);
            playerOne.setVisible(false);
            count = 4;
        }

        if (count == 4) {
            valid = true;
            if (oneDecides < 2) {
                shape_renderer_.begin(ShapeType.Filled);
                shape_renderer_.setColor(Color.CYAN);
                shape_renderer_.triangle(offset + playerOneLabel.getWidth() + 75.f, 275.f + 20.f,
                        offset + playerOneLabel.getWidth() + 50.f, 275.f + 10.f,
                        offset + playerOneLabel.getWidth() + 75.f, 275.f + 0.f);
                shape_renderer_.end();

                if ((Gdx.input.getX() >= 200.f && Gdx.input.getY() >= 100.f) && (Gdx.input.getX() <= 1000.f
                        && Gdx.input.getY() >= 100.f)) {
                    if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                        if (board.validMove(this)) {
                            if (playersTurn) {
                                board.setStone(player1.placeStone(gridPosition()));
                                playersTurn = false;
                            }
                            else {
                                board.setStone(player2.placeStone(gridPosition()));
                                playersTurn = true;
                            }
                            oneDecides++;
                        }
                    }
                }
            }

            if (oneDecides == 2) {
                shape_renderer_.begin(ShapeType.Filled);
                shape_renderer_.setColor(Color.CYAN);
                shape_renderer_.triangle(offset + playerOneLabel.getWidth() + 75.f, 375.f + 20.f,
                        offset + playerOneLabel.getWidth() + 50.f, 375.f + 10.f,
                        offset + playerOneLabel.getWidth() + 75.f, 375.f + 0.f);
                shape_renderer_.end();

                valid = false;
                black.setVisible(true);
                white.setVisible(true);
            }

            if (black.isPressed() && ! playerOne.isVisible() && (oneDecides == 2)) {
                valid = true;
                black.setVisible(false);
                white.setVisible(false);
                player1.setColour(Color.BLACK);
                player2.setColour(Color.WHITE);
                player_one = "BLACK\nWINS!";
                player_two = "WHITE\nWINS!";
                playersTurn = false;
                newGameStarted = false;
            }

            else if (white.isPressed() && ! playerOne.isVisible() && (oneDecides == 2)) {
                valid = true;
                black.setVisible(false);
                white.setVisible(false);
                player1.setColour(Color.WHITE);
                player2.setColour(Color.BLACK);
                player_one = "WHITE\nWINS!";
                player_two = "BLACK\nWINS!";
                playersTurn = true;
                newGameStarted = false;
            }
        }
    }


    /**
     * Recalculates the actual mouse position. This ensures that the {@link GameStone} as well as the
     * {@link GameScreen#validPreview()} are placed at the correct place and not next to the grid or next to the
     * crossings of the grid.
     * inspired by Merten Dieckmann
     *
     * @return scaled Point for placing stones as well as the preview
     * @author Marlene Mika
     */
    public Point gridPosition() {
        int minX = 0;
        int minY = 0;
        int mousePosX = Gdx.input.getX();
        int mousePosY = Gdx.input.getY();
        double minDist = Math.sqrt((mousePosX - top_left_x) * (mousePosX - top_left_x)
                + (mousePosY - padding) * (mousePosY - padding));

        for (int i = 0; i < grid_size_; i++) {
            for (int j = 0; j < grid_size_; j++) {
                double xDist = mousePosX - (top_left_x + i * offset);
                double yDist = mousePosY - (padding + j * offset);
                double dist = Math.sqrt(xDist * xDist + yDist * yDist);
                if (dist < minDist) {
                    minDist = dist;
                    minX = i;
                    minY = j;
                }
            }
        }
        return new Point(minX, minY);
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     *
     * @author Dennis Jehle
     */
    @Override
    public void show() {
        // InputProcessor for Stage
        Gdx.input.setInputProcessor(stage_);
    }

    /**
     * This method is called if the window gets resized.
     *
     * @param width  new window width
     * @param height new window height
     * @author Dennis Jehle
     * @see ApplicationListener#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {
        // could be ignored because you cannot resize the window at the moment
    }

    /**
     * This method is called if the application lost focus.
     *
     * @author Dennis Jehle
     * @see ApplicationListener#pause()
     */
    @Override
    public void pause() {

    }

    /**
     * This method is called if the application regained focus.
     *
     * @author Dennis Jehle
     * @see ApplicationListener#resume()
     */
    @Override
    public void resume() {

    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     *
     * @author Dennis Jehle
     */
    @Override
    public void hide() {

    }

    /**
     * Called when this screen should release all resources.
     *
     * @author Dennis Jehle
     */
    @Override
    public void dispose() {
        skin_.dispose();
        stage_.dispose();
        sprite_batch_.dispose();
    }
}
