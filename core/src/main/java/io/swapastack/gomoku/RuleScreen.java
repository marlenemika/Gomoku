package io.swapastack.gomoku;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class RuleScreen implements Screen {
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
    // Skin and Texture
    // see: https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/graphics/Texture.html
    private final Texture background_texture_;
    private final Skin skin_;

    public RuleScreen(final Gomoku parent) {
        // store reference to parent class
        parent_ = parent;
        // initialize OrthographicCamera with current screen size
        // e.g. OrthographicCamera(1280.f, 720.f)
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
        // intialize the Background
        background_texture_ = new Texture("space/space_scaled_hdready.png");

        String rule_string = "Rules";

        // initialize FreeTypeFontGenerator for BitmapFont generation
        FreeTypeFontGenerator bitmap_font_generator_ = new FreeTypeFontGenerator(Gdx.files.internal
                ("fonts/venus_rising/venus_rising_rg.ttf"));
        // specify parameters for BitmapFont generation
        FreeTypeFontGenerator.FreeTypeFontParameter bitmap_font_parameter
                = new FreeTypeFontGenerator.FreeTypeFontParameter();
        // set font size
        bitmap_font_parameter.size = 60;
        // specify available letters
        bitmap_font_parameter.characters = rule_string;
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

        // create a Label with the main menu title string
        Label menu_label = new Label(rule_string, latin_label_style);
        menu_label.setFontScale(1, 1);
        menu_label.setPosition(
                (float) client_area_dimensions.first / 2.f - menu_label.getWidth() / 2.f
                , (float) client_area_dimensions.second / 1.25f - menu_label.getHeight() / 5.f
        );

        // add main menu title string Label to Stage
        stage_.addActor(menu_label);

        // create switch to GameMenu button
        Button game_menu_button = new TextButton("BACK TO\nGAME MENU", skin_);
        game_menu_button.sizeBy(50, 10);
        game_menu_button.setPosition(25.f, 25.f);
        // add InputListener to Button, and go back to GameMenu if Button is clicked
        game_menu_button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                parent_.change_screen(ScreenEnum.GAMEMENU);
            }
        });

        // add button to stage
        stage_.addActor(game_menu_button);

        // get rules text
        StringBuilder rules = new StringBuilder();
        BufferedReader buf;
        try {

            String filePath = new File("").getAbsolutePath();
            String pathFinal = filePath.concat("/core/assets/rules.txt");

            buf = new BufferedReader(new FileReader(pathFinal));
            String str;
            while ((str = buf.readLine()) != null) {
                rules.append(str).append("\n");
            }
            buf.close();
        } catch (IOException e) {
            System.err.println();
        }

        // append text to a label
        Label rulesExplained = new Label(rules.toString(), skin_);
        rulesExplained.setFontScale(1, 1);
        rulesExplained.setPosition((float) client_area_dimensions.first / 2
                , (float) client_area_dimensions.second / 2 - 75.f, Align.center);
        stage_.addActor(rulesExplained);

    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     * @author Dennis Jehle
     */
    @Override
    public void render(float delta) {
        // clear the client area (Screen) with the clear color (black)
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // update camera
        camera_.update();

        // update the current SpriteBatch
        sprite_batch_.begin();
        sprite_batch_.draw(background_texture_, 0, 0, viewport_.getScreenWidth(), viewport_.getScreenHeight());
        sprite_batch_.setProjectionMatrix(camera_.combined);
        sprite_batch_.end();

        shape_renderer_.end();

        // update the Stage
        stage_.act(delta);
        // draw the Stage
        stage_.draw();
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
