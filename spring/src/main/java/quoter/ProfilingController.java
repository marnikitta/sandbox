package quoter;

public final class ProfilingController implements ProfilingControllerMBean {
  private boolean enabled;

  public ProfilingController(boolean enabled) {
    this.enabled = enabled;
  }

  public ProfilingController() {
    this(true);
  }

  @Override
  public void setEnabled(boolean isEnabled) {
    this.enabled = isEnabled;
  }

  public boolean isEnabled() {
    return this.enabled;
  }
}


