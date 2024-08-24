package dev.clairton.yuki.checks.impl.misc;

import dev.clairton.yuki.checks.Check;
import dev.clairton.yuki.checks.CheckData;
import dev.clairton.yuki.checks.type.PacketCheck;
import dev.clairton.yuki.player.GrimPlayer;

@CheckData(name = "TransactionOrder", experimental = false)
public class TransactionOrder extends Check implements PacketCheck {

    public TransactionOrder(GrimPlayer player) {
        super(player);
    }

}