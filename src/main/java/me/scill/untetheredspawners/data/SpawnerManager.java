package me.scill.untetheredspawners.data;

import lombok.Getter;
import me.scill.untetheredspawners.UntetheredSpawners;
import me.scill.untetheredspawners.utilities.Utility;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SpawnerManager {

	private final File spawnersFile;
	private final FileConfiguration spawnersConfig;

	@Getter
	protected Set<Location> spawnerLocations = new HashSet<>();

	public SpawnerManager(final UntetheredSpawners plugin) {
		spawnersFile = new File(plugin.getDataFolder(), "spawners.yml");
		spawnersConfig = YamlConfiguration.loadConfiguration(spawnersFile);
		loadSpawners();
	}
	
	private void saveConfig() {
		try {
			spawnersConfig.save(spawnersFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads all the spawners into a set.
	 */
	private void loadSpawners() {
		if (spawnersConfig.getStringList("spawners") == null)
			return;
		final Set<String> spawnerStringLocations = new HashSet<>(spawnersConfig.getStringList("spawners"));
		spawnerLocations = spawnerStringLocations.stream().map(Utility::stringToLocation).collect(Collectors.toSet());
	}

	/**
	 * Saves all the spawners into a file.
	 */
	public void saveSpawners() {
		final Set<String> spawnerStringLocations = spawnerLocations.stream().map(Utility::locationToString).collect(Collectors.toSet());
		spawnersConfig.set("spawners", spawnerStringLocations.toArray(new String[spawnerLocations.size()]));
		saveConfig();
	}

	/**
	 * Checks to see if the specified location is a spawner.
	 *
	 * @param potentialSpawner any location
	 * @return true if the location is a spawner
	 */
	protected boolean isSpawner(final Location potentialSpawner) {
		return potentialSpawner.getBlock().getType() == Material.MOB_SPAWNER;
	}
}