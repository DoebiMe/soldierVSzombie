package combinatedFields;

public class EnabledVisible {
    private boolean enabled;
    private boolean visible;

    public EnabledVisible(boolean enabled, boolean visible) {
        this.enabled = enabled;
        this.visible = visible;
    }
    

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
