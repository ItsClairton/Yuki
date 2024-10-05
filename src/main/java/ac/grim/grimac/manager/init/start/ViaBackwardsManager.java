package ac.grim.grimac.manager.init.start;

import ac.grim.grimac.manager.init.Initable;
import org.bukkit.Bukkit;

public class ViaBackwardsManager implements Initable {

    @Override
    public void start() {
        if (!Bukkit.getPluginManager().isPluginEnabled("ViaBackwards")) {
            return;
        }

        System.setProperty("com.viaversion.handlePingsAsInvAcknowledgements", "true");
    }

}
