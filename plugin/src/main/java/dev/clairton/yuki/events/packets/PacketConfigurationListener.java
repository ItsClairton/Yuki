package dev.clairton.yuki.events.packets;

import dev.clairton.yuki.Yuki;
import dev.clairton.yuki.checks.impl.misc.ClientBrand;
import dev.clairton.yuki.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.configuration.client.WrapperConfigClientPluginMessage;

public class PacketConfigurationListener extends PacketListenerAbstract {

    public PacketConfigurationListener() {
        super(PacketListenerPriority.LOW);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Configuration.Client.PLUGIN_MESSAGE) {
            GrimPlayer player = Yuki.getInstance().getPlayerDataManager().getPlayer(event.getUser());
            if (player == null) return;
            //
            WrapperConfigClientPluginMessage wrapper = new WrapperConfigClientPluginMessage(event);
            String channelName = wrapper.getChannelName();
            byte[] data = wrapper.getData();
            if (channelName.equalsIgnoreCase("minecraft:brand") || channelName.equals("MC|Brand")) {
                player.checkManager.getPacketCheck(ClientBrand.class).handle(channelName, data);
            }
        }
    }

}
