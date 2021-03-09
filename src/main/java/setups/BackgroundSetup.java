package setups;

import spriteFoundation.BackGround;

public class BackgroundSetup {
    public static  long xPos;
    public static long ypos;
    public static void backGroundSetup() {
        //BackGround.addImage(0, "mountain.jpg");
        BackGround.addImage(0, "mountainquatro.jpg");
        xPos = 0;
        ypos = 0;
    }

    public static long getxPos() {
        return xPos;
    }

    public static void setxPos(long xPos) {
        BackgroundSetup.xPos = xPos;
    }

    public static long getYpos() {
        return ypos;
    }

    public static void setYpos(long ypos) {
        BackgroundSetup.ypos = ypos;
    }
}
