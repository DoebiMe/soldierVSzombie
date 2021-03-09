package logic;

import setups.KeyboardSetup;

public class ProcessKeys {
    static boolean rememberPause = false;

    public static void processPressedKeysGeneral() {
        if (KeyboardSetup.keyBuffer.contains("ESCAPE")) {
            System.exit(0);
        }
        if (KeyboardSetup.keyBuffer.contains("PAUSE")) {
            if (!rememberPause) {
                rememberPause = true;
                if (GeneralGameLogic.getStateOfGame().equals(StateOfGame.pause)) {
                    GeneralGameLogic.setStateOfGame(StateOfGame.play);
                } else {
                    GeneralGameLogic.setStateOfGame(StateOfGame.pause);
                }
            }
        } else {
            rememberPause = false;
        }
    }

}
