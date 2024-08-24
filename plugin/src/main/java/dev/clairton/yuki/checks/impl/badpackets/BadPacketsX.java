package dev.clairton.yuki.checks.impl.badpackets;

import dev.clairton.yuki.checks.Check;
import dev.clairton.yuki.checks.CheckData;
import dev.clairton.yuki.checks.type.PacketCheck;
import dev.clairton.yuki.player.GrimPlayer;
import dev.clairton.yuki.utils.nmsutil.BlockBreakSpeed;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;

@CheckData(name = "BadPacketsX", experimental = true)
public class BadPacketsX extends Check implements PacketCheck {
    public BadPacketsX(GrimPlayer player) {
        super(player);
    }

    public final boolean noFireHitbox = player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_15_2);

    public final void handle(PacketReceiveEvent event, WrapperPlayClientPlayerDigging dig, StateType block) {
        if (dig.getAction() != DiggingAction.START_DIGGING && dig.getAction() != DiggingAction.FINISHED_DIGGING)
            return;

        final boolean invalid;

        if (dig.getAction() == DiggingAction.FINISHED_DIGGING) {
            invalid = BlockBreakSpeed.getBlockDamage(player, dig.getBlockPosition()) >= 1 || block.getHardness() == -1.0f;
        } else invalid = (block == StateTypes.LIGHT && !(player.getInventory().getHeldItem().is(ItemTypes.LIGHT) || player.getInventory().getOffHand().is(ItemTypes.LIGHT)))
                || block.isAir()
                || block == StateTypes.WATER
                || block == StateTypes.LAVA
                || block == StateTypes.BUBBLE_COLUMN
                || block == StateTypes.MOVING_PISTON
                || (block == StateTypes.FIRE && noFireHitbox);

        if (invalid && flagAndAlert("block=" + block.getName() + ", type=" + dig.getAction()) && shouldModifyPackets()) {
            event.setCancelled(true);
            player.onPacketCancel();
        }
    }
}
