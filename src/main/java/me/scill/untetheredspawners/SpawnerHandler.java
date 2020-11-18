package me.scill.untetheredspawners;

import me.scill.untetheredspawners.data.SpawnerManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SpawnerHandler extends SpawnerManager {

	private final UntetheredSpawners plugin;
	private final Random random = new Random();
	private final Map<Location, Long> timeStamps = new HashMap<>();

	public SpawnerHandler(final UntetheredSpawners plugin) {
		super(plugin);
		this.plugin = plugin;
		Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::spawnerTick, 0L, 5L);
	}

	/**
	 * This is the main controller of spawners.
	 *
	 * It will run every 0.25 seconds to check if
	 * any spawners are ready to spawn mobs in.
	 *
	 * If a spawner is ready, an attempt
	 * will be made to spawn mobs from it.
	 */
	private void spawnerTick() {
		for (Location spawnerLocation : spawnerLocations) {
			if (timeStamps.containsKey(spawnerLocation)) {
				if (System.currentTimeMillis() > timeStamps.get(spawnerLocation))
					Bukkit.getScheduler().runTask(plugin, () -> spawnAttempt(spawnerLocation));
			} else
				timeStamps.put(spawnerLocation, System.currentTimeMillis());
		}
	}

	/**
	 * Attempts to spawn mobs from the specified spawner.
	 *
	 * @param spawnerLocation location of a spawner
	 */
	private void spawnAttempt(final Location spawnerLocation) {
		// Is the location a valid spawner?
		if (!isSpawner(spawnerLocation))
			spawnerLocations.remove(spawnerLocation);

		// Is a player nearby?
		else if (playerNear(spawnerLocation)) {
			int randomSpawnAmount = getRandomBetween(plugin.getSpawnerData().getMaxSpawnAmount(), plugin.getSpawnerData().getMinSpawnAmount());

			// This loop stops if all the mobs have been spawned OR
			// if too many attempts have been made to spawn mobs in.
			for (int attempts = 0; randomSpawnAmount > 0 && attempts < (randomSpawnAmount + 5); attempts++) {
				if (spawnMob((CreatureSpawner) spawnerLocation.getBlock().getState()))
					randomSpawnAmount--;
			}

			// Sets a new time stamp to the spawner!
			final int randomWaitTime =  getRandomBetween(plugin.getSpawnerData().getMaxWaitTime(), plugin.getSpawnerData().getMinWaitTime()) * 50;
			timeStamps.put(spawnerLocation, System.currentTimeMillis() + randomWaitTime);
		}
	}

	/**
	 * Checks to see if a player is near a spawner.
	 *
	 * @param spawnerLocation location of a spawner
	 * @return true if a player is near a spawner
	 */
	private boolean playerNear(final Location spawnerLocation) {
		final int playerRadius = Math.max(plugin.getSpawnerData().getRadiusFromPlayer(), 0);
		for (Entity entity : spawnerLocation.getWorld().getNearbyEntities(spawnerLocation, playerRadius, playerRadius, playerRadius)) {
			if (entity instanceof Player)
				return true;
		}
		return false;
	}

	/**
	 * Spawns a mob at a random location if it is safe.
	 *
	 * @param spawner any spawner
	 * @return true if a mob was successfully spawned in
	 */
	private boolean spawnMob(final CreatureSpawner spawner) {
		final Location randomSpawnLocation = getRandomSpawnLocation(spawner.getLocation());
		if (isSafeLocation(randomSpawnLocation)) {
			randomSpawnLocation.getWorld().spawnEntity(randomSpawnLocation, spawner.getSpawnedType());
			return true;
		}
		return false;
	}

	/**
	 * Gets a random location around a spawner.
	 *
	 * @param spawnerLocation location of a spawner
	 * @return a random location around the spawner
	 */
	private Location getRandomSpawnLocation(final Location spawnerLocation) {
		final int radiusFromSpawner = plugin.getSpawnerData().getRadiusFromSpawner();
		return spawnerLocation.clone().add(-radiusFromSpawner + random.nextInt(radiusFromSpawner * 2),
										   -2 + random.nextInt(4) + 1,
										   -radiusFromSpawner + random.nextInt(radiusFromSpawner * 2));
	}

	/**
	 * Checks to see if the location is safe to spawn in.
	 *
	 * @param spawnerLocation any location
	 * @return true if the location is safe to spawn in
	 */
	private boolean isSafeLocation(final Location spawnerLocation) {
		return spawnerLocation.getBlock().getType() == Material.AIR;
	}

	/**
	 * Gets a random number between two ints.
	 *
	 * @param max any int
	 * @param min any int
	 * @return a random number between two ints
	 */
	private int getRandomBetween(final int max, final int min) {
		return random.nextInt(max - min) + min;
	}
}
