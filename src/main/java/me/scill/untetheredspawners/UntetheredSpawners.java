package me.scill.untetheredspawners;

import lombok.Getter;
import me.scill.untetheredspawners.commands.ReloadCommand;
import me.scill.untetheredspawners.data.SpawnerData;
import me.scill.untetheredspawners.listeners.SpawnerListener;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class UntetheredSpawners extends JavaPlugin {

	private SpawnerData spawnerData;
	private SpawnerHandler spawnerHandler;

	@Override
	public void onEnable() {
		// Data
		saveDefaultConfig();
		spawnerData = new SpawnerData(this);

		// Spawner handler!
		spawnerHandler = new SpawnerHandler(this);

		// Listeners
		getServer().getPluginManager().registerEvents(new SpawnerListener(this), this);

		// Commands
		getCommand("spawners").setExecutor(new ReloadCommand(this));
	}

	@Override
	public void onDisable() {
		spawnerHandler.saveSpawners();
	}

	/**
	 * Reloads the plugin with new spawner values.
	 */
	public void reloadSpawnerData() {
		reloadConfig();
		spawnerData = new SpawnerData(this);
	}
}