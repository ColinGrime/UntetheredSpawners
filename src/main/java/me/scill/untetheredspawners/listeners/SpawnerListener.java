package me.scill.untetheredspawners.listeners;

import me.scill.untetheredspawners.UntetheredSpawners;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;

public class SpawnerListener implements Listener {

	private final UntetheredSpawners plugin;

	public SpawnerListener(final UntetheredSpawners plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onSpawnerPlace(final BlockPlaceEvent event) {
		if (!event.isCancelled() && isSpawner(event.getBlock()))
			plugin.getSpawnerHandler().getSpawnerLocations().add(event.getBlock().getLocation());
	}
	
	@EventHandler
	public void onSpawnerBreak(final BlockBreakEvent event) {
		if (!event.isCancelled() && isSpawner(event.getBlock()))
			plugin.getSpawnerHandler().getSpawnerLocations().remove(event.getBlock().getLocation());
	}

	@EventHandler
	public void onSpawnerSpawn(final SpawnerSpawnEvent event) {
		plugin.getSpawnerHandler().getSpawnerLocations().add(event.getSpawner().getLocation());
		event.setCancelled(true);
	}

	/**
	 * Checks to see if the specified block if a spawner.
	 *
	 * @param potentialSpawner any block
	 * @return true if the location is a spawner
	 */
	protected boolean isSpawner(final Block potentialSpawner) {
		return potentialSpawner.getType() == Material.MOB_SPAWNER;
	}
}
