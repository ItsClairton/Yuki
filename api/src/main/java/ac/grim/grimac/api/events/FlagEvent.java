package ac.grim.grimac.api.events;

import ac.grim.grimac.api.AbstractCheck;
import ac.grim.grimac.api.GrimUser;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"checkstyle:MissingJavadocType", "checkstyle:MissingJavadocMethod"})
public class FlagEvent extends Event implements Cancellable {
  private static final HandlerList handlers = new HandlerList();
  private final GrimUser grimUser;
  @Getter
  private final AbstractCheck check;
  private boolean cancelled;

  public FlagEvent(GrimUser grimUser, AbstractCheck check) {
    super(true); // Async!
    this.grimUser = grimUser;
    this.check = check;
  }

  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  @Override
  public void setCancelled(boolean cancel) {
    cancelled = cancel;
  }

  public GrimUser getPlayer() {
    return grimUser;
  }

  public double getViolations() {
    return check.getViolations();
  }

  @NotNull
  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }

  public boolean isSetback() {
    return check.getViolations() > check.getSetbackVL();
  }


}
