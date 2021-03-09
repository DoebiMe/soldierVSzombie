package mainStartup;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.stage.Stage;
import logic.GeneralGameLogic;
import logic.StartOfGame;
import setups.BackgroundSetup;
import setups.GraphicSetup;
import setups.ImagesSetup;
import setups.KeyboardSetup;

public class Main extends Application {

    @Override
    public void start(Stage theStage) {
        GraphicSetup.graphicSetup(theStage, 1500, 1000);

        GeneralGameLogic.generalGameLogicSetup();

        KeyboardSetup.keyBoardSetup(GraphicSetup.theScene);
        BackgroundSetup.backGroundSetup();
        ImagesSetup.imagesSetup();

        theStage.show();

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
