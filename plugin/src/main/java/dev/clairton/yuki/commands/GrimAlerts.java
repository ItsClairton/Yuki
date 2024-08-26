package dev.clairton.yuki.commands;

import dev.clairton.yuki.Yuki;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.entity.Player;

@CommandAlias("grim|grimac")
public class GrimAlerts extends BaseCommand {
    @Subcommand("alerts")
    @CommandPermission("grim.alerts")
    public void onAlerts(Player player) {
        Yuki.getInstance().getAlertManager().toggleAlerts(player);
    }
}
