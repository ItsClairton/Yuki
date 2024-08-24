package dev.clairton.yuki.checks.impl.crash;

import dev.clairton.yuki.checks.Check;
import dev.clairton.yuki.checks.CheckData;
import dev.clairton.yuki.checks.impl.exploit.ExploitA;
import dev.clairton.yuki.checks.type.PacketCheck;
import dev.clairton.yuki.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSettings;

@CheckData(name = "CrashE", experimental = false)
public class CrashE extends Check implements PacketCheck {

    public CrashE(GrimPlayer playerData) {
        super(playerData);
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.CLIENT_SETTINGS) {
            WrapperPlayClientSettings wrapper = new WrapperPlayClientSettings(event);
            int viewDistance = wrapper.getViewDistance();
            boolean invalidLocale = player.checkManager.getPrePredictionCheck(ExploitA.class).checkString(wrapper.getLocale());
            if (viewDistance < 2) {
                flagAndAlert("distance=" + viewDistance);
                wrapper.setViewDistance(2);
            }
            if (invalidLocale) wrapper.setLocale("en_us");
        }
    }

}
