package dev.clairton.yuki.manager.init.start;

import dev.clairton.yuki.manager.init.Initable;

public class ViaBackwardsManager implements Initable {
    @Override
    public void start() {
        System.setProperty("com.viaversion.handlePingsAsInvAcknowledgements", "true");
    }
}
