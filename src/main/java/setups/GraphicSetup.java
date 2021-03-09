package setups;

import drawEngine.DrawEngine;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class GraphicSetup {
    private static Canvas canvas;
    private static Group root;
    private static GraphicsContext gc;
    public static GraphicsContext buffer;
    public static Scene theScene;

    private static int theWidth;
    private static int theHeight;
    private static Stage myStage;

    public static void graphicSetup(Stage theStage, int width, int height) {

        myStage = theStage;

        myStage.setMaximized(false);

        myStage.setWidth(width);
        myStage.setHeight(height);


        myStage.setTitle("Timeline Example");

        theWidth = width;
        theHeight = height;

        root = new Group();
        theScene = new Scene(root);
        theStage.setScene(theScene);//2560 x 1080   1024 512               3840 2160
        canvas = new Canvas(theWidth,theHeight     /*(TilesSetup.TILE_MAP_COLS) * DrawEngine.SCALE_FACTOR_SPRITE, (TilesSetup.TILE_MAP_ROWS) * DrawEngine.SCALE_FACTOR_SPRITE/* 3840, 2160*/);
        gc = canvas.getGraphicsContext2D();
        buffer = canvas.getGraphicsContext2D();

        root.getChildren().add(canvas);
    }

    public static GraphicsContext getGraphicsContext() {
        return gc;
    }

    public static int getWidth() {
        return /*theWidth;*/ (int) myStage.getWidth();
    }

    public static int getHeight() {
        return /*theHeight;*/ (int) myStage.getHeight();
    }

    public static int getHalfWidth() {
        return getWidth() / 2;
    }

    public static int getHalfHeight() {
        return getHeight() / 2 + DrawEngine.SCALE_FACTOR_SPRITE/2; //(rows is odd)
    }

}
