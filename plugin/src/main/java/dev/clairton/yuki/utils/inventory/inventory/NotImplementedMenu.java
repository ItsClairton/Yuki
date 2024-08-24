package dev.clairton.yuki.utils.inventory.inventory;

import dev.clairton.yuki.player.GrimPlayer;
import dev.clairton.yuki.utils.inventory.Inventory;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;

public class NotImplementedMenu extends AbstractContainerMenu {
    public NotImplementedMenu(GrimPlayer player, Inventory playerInventory) {
        super(player, playerInventory);
        player.getInventory().isPacketInventoryActive = false;
        player.getInventory().needResend = true;
    }

    @Override
    public void doClick(int button, int slotID, WrapperPlayClientClickWindow.WindowClickType clickType) {

    }
}
