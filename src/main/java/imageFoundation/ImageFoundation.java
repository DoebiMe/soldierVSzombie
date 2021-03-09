package imageFoundation;

import javafx.scene.image.Image;
import java.util.HashMap;

public class ImageFoundation {
    //Key = Image Number
    //Value = object that holds the image
    private static HashMap<Long, ImageObject> imageMap = new HashMap();

    public static boolean createImageObjectFromPath(long id, String path) {
        ImageObject imageObject = new ImageObject(id, path);
        if (imageObject.isLoadSuccess()) {
            imageMap.put(id, imageObject);
            return true;
        }
        return false;
    }

    public static Image getImage(long id) {
        return imageExist(id) ? imageMap.get(id).getImage() : null;
    }

    public static ImageObject getImageObject(long id) {
        return imageMap.get(id);
    }

    public static boolean imageExist(long id) {
        return imageMap.containsKey(id);
    }

    public static void removeImageObject(long id) {
        imageMap.remove(id);
    }

}
