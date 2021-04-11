package setups;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.List;

public class KeyboardSetup {
    public static List<String> keyBuffer;

    public static void keyBoardSetup(Scene theScene) {
        if (keyBuffer == null) {
            keyBuffer = new ArrayList<String>();
        }
        theScene.setOnKeyPressed(
                new EventHandler<KeyEvent>() {
                    public void handle(KeyEvent e) {
                        String code = e.getCode().toString();
                        // only add once... prevent duplicates
                        if (!keyBuffer.contains(code))
                            keyBuffer.add(code);
                        System.out.println(code);
                    }
                });

        theScene.setOnKeyReleased(
                new EventHandler<KeyEvent>() {
                    public void handle(KeyEvent e) {
                        String code = e.getCode().toString();
                        keyBuffer.remove(code);
                    }
                });
    }
}
