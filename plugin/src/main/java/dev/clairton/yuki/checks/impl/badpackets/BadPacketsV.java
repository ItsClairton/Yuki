package dev.clairton.yuki.checks.impl.badpackets;

import dev.clairton.yuki.checks.Check;
import dev.clairton.yuki.checks.CheckData;
import dev.clairton.yuki.checks.type.PacketCheck;
import dev.clairton.yuki.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;

@CheckData(name = "BadPacketsV", experimental = true)
public class BadPacketsV extends Check implements PacketCheck {
    public BadPacketsV(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity interactEntity = new WrapperPlayClientInteractEntity(event);
            if (interactEntity.getAction() != WrapperPlayClientInteractEntity.InteractAction.ATTACK) return;
            if (!player.packetStateData.isSlowedByUsingItem()) return;
            ItemStack itemInUse = player.getInventory().getItemInHand(player.packetStateData.eatingHand);
            if (flagAndAlert("UseItem=" + itemInUse.getType().getName().getKey()) && shouldModifyPackets()) {
                event.setCancelled(true);
                player.onPacketCancel();
            }
        }
    }
}
