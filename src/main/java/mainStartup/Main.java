package mainStartup;

import Service.JsonTileReader;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import logic.GeneralGameLogic;
import logic.StartOfGame;
import org.json.JSONException;
import scoreEngine.ScoreEngine;
import setups.BackgroundSetup;
import setups.GraphicSetup;
import setups.ImagesSetup;
import setups.KeyboardSetup;
import soundEngine.SoundEngine;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class Main extends Application {

    @Override
    public void start(Stage theStage) throws IOException, JSONException {

        //https://ytmp3.cc/youtube-to-mp3/
        //https://www.youtube.com/watch?v=z7UjROeg_FY&ab_channel=BreadBoxCommodoreComputerMuseum

        SoundEngine.setUp();
        SoundEngine.startThemeSong();


        GraphicSetup.graphicSetup(theStage, 1500, 1000);
        ScoreEngine.setUp();

        GeneralGameLogic.generalGameLogicSetup();

        KeyboardSetup.keyBoardSetup(GraphicSetup.theScene);
        BackgroundSetup.backGroundSetup();
        ImagesSetup.imagesSetup();

        theStage.show();

        JsonTileReader jsonTileReader= new  JsonTileReader();


        final LongProperty lastUpdateTime = new SimpleLongProperty(0);
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long timestamp) {
                if (lastUpdateTime.get() > 0) {
                    GeneralGameLogic.executeLogicTimeLine();
                }
                lastUpdateTime.set(timestamp);
            }
        };
        StartOfGame.startOfGame();
        animationTimer.start();

        // look to https://gamedevelopment.tutsplus.com/tutorials/introduction-to-javafx-for-game-development--cms-23835
    }

    public static void main(String[] args) {
        launch(args);
    }
}
