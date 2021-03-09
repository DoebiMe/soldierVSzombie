package setups;

import imageFoundation.ImageFoundation;

public class ImagesSetup {
    public static void imagesSetup() {
        ImageFoundation.createImageObjectFromPath(0, "ship500.png");

        ImageFoundation.createImageObjectFromPath(100,"tank.png");
    }
}
