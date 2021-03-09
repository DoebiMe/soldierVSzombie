package combinatedFields;

public class SpriteIdImageId {
    private long spriteId;
    private long imageId;

    public SpriteIdImageId(long spriteId, long imageId) {
        this.spriteId = spriteId;
        this.imageId = imageId;
    }

    public long getSpriteId() {
        return spriteId;
    }

    public void setSpriteId(long spriteId) {
        this.spriteId = spriteId;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }
}
