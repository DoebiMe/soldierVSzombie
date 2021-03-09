package spriteFoundation;


import imageFoundation.ImageObject;
import javafx.scene.image.Image;
import combinatedFields.Position;

import java.util.HashMap;

public class BackGround {
    private static Position position = new Position(-1, -1);
    private static long currentId = -1;

    private static final HashMap<Long, ImageObject> backGroundMap = new HashMap<>();

    public static Image getImage(long id) {
        return backGroundMap.get(id).getImage();
    }

    public static void addImage(long id, String pathName) {
        ImageObject imageObject = new ImageObject(id, pathName);
        backGroundMap.put(id, imageObject );
        System.out.println("Background loaded " + imageObject.isLoadSuccess());
    }

    public static long getCurrentId() {
        if (backGroundMap.containsKey(currentId)) {
            return currentId;
        }
        return -1;
    }

    public static boolean setCurrentId(long currentId) {
        if (backGroundMap.containsKey(currentId)) {
            BackGround.currentId = currentId;
            return true;
        }
        return false;
    }

    public static int getxPos() {
        return position.getxPos();
    }

    public static void setxPos(int xPos) {
        position.setxPos(xPos);
    }

    public static int getyPos() {
        return position.getyPos();
    }

    public static void setyPos(int yPos) {
        position.setyPos(yPos);
    }

    public static Position getPosition() {
        return position;
    }

    public static void setPosition(Position position) {
        BackGround.position = position;
    }
}
