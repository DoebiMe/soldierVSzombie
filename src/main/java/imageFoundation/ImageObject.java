package imageFoundation;

import javafx.scene.image.Image;

import java.io.File;

public class ImageObject {
    private String pathName;
    private boolean loadSuccess;
    private long imageId = -1;
    private Image image = null;

    public ImageObject(long imageId,String pathName) {
        this.pathName = pathName;
        this.imageId = imageId;
        File file = new File("C:\\Users\\User\\IdeaProjects\\gamelogic3\\src\\main\\resources\\" + pathName);
        if (file.exists()) {
            image = new Image(file.toURI().toString());
            loadSuccess = !image.isError();
        } else {
            loadSuccess = false;
        }
    }

    public String getPathName() {
        return pathName;
    }

    public boolean isLoadSuccess() {
        return loadSuccess;
    }

    public long getImageId() {
        return imageId;
    }

    public Image getImage() {
        return image;
    }
}
