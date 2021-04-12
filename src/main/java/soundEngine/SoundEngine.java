package soundEngine;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import mainStartup.Main;

import java.net.URL;

public class SoundEngine {

    //https://ytmp3.cc/youtube-to-mp3/
    //https://www.youtube.com/watch?v=z7UjROeg_FY&ab_channel=BreadBoxCommodoreComputerMuseum
    //https://www.fesliyanstudios.com/royalty-free-sound-effects-download/gun-shooting-300

    public static void startThemeSong() {
        final URL resource = SoundEngine.class.getResource("/music.mp3");
        final Media media = new Media(resource.toString());
        final MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
            }
        });
        mediaPlayer.play();
    }

    private static URL urlMachineGun;
    private static Media mediaMachineGun;
    private static MediaPlayer mediaPlayerMachineGun;
    private static boolean runningMachineGun;

    public static void setUp() {
        /*
        runningMachineGun = false;
        urlMachineGun = SoundEngine.class.getResource("/test.wav");///machinegun.mp3
        mediaMachineGun = new Media(urlMachineGun.toString());
        mediaPlayerMachineGun = new MediaPlayer(mediaMachineGun);
        mediaPlayerMachineGun.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                runningMachineGun = false;
            }
        });

         */
    }


    public static void startShoot() {
        /*
        if (!runningMachineGun) {
            mediaPlayerMachineGun.seek(Duration.ZERO);
            mediaPlayerMachineGun.play();
            System.out.println("play");
        }

         */
    }
        public static void stopShoot() {
        /*
        if (runningMachineGun) {
            mediaPlayerMachineGun.stop();
            runningMachineGun = false;
            System.out.println("stop");
        }

         */
        }
    }
