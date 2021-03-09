package spriteFoundation;

import combinatedFields.*;

public class Sprite {
    private long rotation;
    private Position position;
    private Scaling scaling;
    private Acceleration acceleration;
    private EnabledVisible enabledVisible;
    private SpriteIdImageId spriteIdImageId;

    public Sprite(Position position, EnabledVisible enabledVisible, Acceleration acceleration, Scaling scaling, long rotation,  SpriteIdImageId spriteIdImageId) {
        this.position = position;
        this.enabledVisible = enabledVisible;
        this.acceleration = acceleration;
        this.scaling = scaling;
        this.rotation = rotation;
        this.spriteIdImageId = spriteIdImageId;
    }

    public long getRotation() {
        return rotation;
    }

    public void setRotation(long rotation) {
        this.rotation = rotation;
    }

    public long getSpriteId() {
        return spriteIdImageId.getSpriteId();
    }

    public void setSpriteId(long spriteId)  {
        spriteIdImageId.setSpriteId(spriteId);
    }

    public long getImageId() {
        return spriteIdImageId.getImageId();
    }

    public void setImageId(long id) {
        spriteIdImageId.setImageId(id);
    }

    public SpriteIdImageId getSpriteIdAndImageId() {
        return spriteIdImageId;
    }

    public void setSpriteIdAndImageId(SpriteIdImageId spriteIdImageId) {
        this.spriteIdImageId = spriteIdImageId;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Scaling getScaling() {
        return scaling;
    }

    public void setScaling(Scaling scaling) {
        this.scaling = scaling;
    }

    public boolean isVisible() {
        return enabledVisible.isVisible();
    }

    public void setVisible(boolean visible) {
        enabledVisible.setVisible(visible);
    }

    public boolean isEnabled() {
        return enabledVisible.isEnabled();
    }

    public void setEnabled(boolean enabled) {
        enabledVisible.setEnabled(enabled);
    }

    public EnabledVisible getEnabledVisible() {
        return enabledVisible;
    }

    public void setEnabledVisible(EnabledVisible enabledVisible) {
        this.enabledVisible = enabledVisible;
    }

    public Acceleration getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Acceleration acceleration) {
        this.acceleration = acceleration;
    }
}
