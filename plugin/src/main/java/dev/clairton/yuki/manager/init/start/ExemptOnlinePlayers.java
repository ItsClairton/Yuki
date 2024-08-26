package dev.clairton.yuki.manager.init.start;

import dev.clairton.yuki.Yuki;
import dev.clairton.yuki.manager.init.Initable;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ExemptOnlinePlayers implements Initable {
    @Override
    public void start() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
            Yuki.getInstance().getPlayerDataManager().exemptUsers.add(user);
        }
    }
}
