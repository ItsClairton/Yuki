package dev.clairton.yuki.commands;

import dev.clairton.yuki.Yuki;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.entity.Player;

@CommandAlias("grim|grimac")
public class GrimVerbose extends BaseCommand {
    @Subcommand("verbose")
    @CommandPermission("grim.verbose")
    public void onVerbose(Player player) {
        Yuki.getInstance().getAlertManager().toggleVerbose(player);
    }
}
