package me.scill.untetheredspawners.commands;

import me.scill.untetheredspawners.UntetheredSpawners;
import me.scill.untetheredspawners.utilities.Utility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {
	
	private final UntetheredSpawners plugin;
	
	public ReloadCommand(final UntetheredSpawners plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("spawners")) {
			if (sender.hasPermission("spawners.reload")) {
				plugin.reloadSpawnerData();
				sender.sendMessage(plugin.getSpawnerData().getReloadMessage());
			} else
				sender.sendMessage(plugin.getSpawnerData().getNoPermsError());
			return true;
		}
		return false;
	}
}
