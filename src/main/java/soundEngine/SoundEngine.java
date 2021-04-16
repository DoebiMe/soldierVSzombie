package soundEngine;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;

public class SoundEngine {

    //https://ytmp3.cc/youtube-to-mp3/
    //https://www.youtube.com/watch?v=z7UjROeg_FY&ab_channel=BreadBoxCommodoreComputerMuseum
    //https://www.fesliyanstudios.com/royalty-free-sound-effects-download/gun-shooting-300

    private static URL resourceGameTheme;
    private static Media mediaGameTheme;
    private static MediaPlayer mediaPlayerGameTheme;
    private static URL resourceSingleShot;
    private static Media mediaSingleShot;
    private static MediaPlayer mediaPlayerSingleShot;

    public static void startThemeSong() {

        mediaPlayerGameTheme.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaPlayerGameTheme.seek(Duration.ZERO);
                System.out.println("First *************************");
            }
        });
        mediaPlayerGameTheme.play();
    }



    public static void setUp() {
        resourceGameTheme = SoundEngine.class.getResource("/music.mp3");
        mediaGameTheme = new Media(resourceGameTheme.toString());
        mediaPlayerGameTheme = new MediaPlayer(mediaGameTheme);
        resourceSingleShot = SoundEngine.class.getResource("/gun-single.mp3");
        mediaSingleShot = new Media(resourceSingleShot.toString());
        mediaPlayerSingleShot = new MediaPlayer(mediaSingleShot);
    }


    public static void startShoot() {
        mediaPlayerSingleShot.seek(Duration.ZERO);
        mediaPlayerSingleShot.play();
    }

    public static void stopShoot() {
        mediaPlayerSingleShot.stop();
    }
}
