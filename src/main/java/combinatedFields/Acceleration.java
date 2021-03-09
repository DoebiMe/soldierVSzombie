package combinatedFields;

public class Acceleration {
    private long accelerationX;
    private long accelerationY;

    public Acceleration(long accelerationX, long accelerationY) {
        this.accelerationX = accelerationX;
        this.accelerationY = accelerationY;
    }

    public long getAccelerationX() {
        return accelerationX;
    }

    public void setAccelerationX(long accelerationX) {
        this.accelerationX = accelerationX;
    }

    public long getAccelerationY() {
        return accelerationY;
    }

    public void setAccelerationY(long accelerationY) {
        this.accelerationY = accelerationY;
    }
}
