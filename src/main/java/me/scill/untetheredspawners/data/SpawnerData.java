package me.scill.untetheredspawners.data;

import lombok.Getter;
import me.scill.untetheredspawners.UntetheredSpawners;
import me.scill.untetheredspawners.utilities.Utility;

@Getter
public class SpawnerData  {

	private final int maxWaitTime, minWaitTime, maxSpawnAmount, minSpawnAmount, radiusFromPlayer, radiusFromSpawner;
	private final String reloadMessage, noPermsError;

	public SpawnerData(final UntetheredSpawners plugin) {
		// Spawner specific.
		maxWaitTime = plugin.getConfig().getInt("max-wait-time");
		minWaitTime = plugin.getConfig().getInt("min-wait-time");
		maxSpawnAmount = plugin.getConfig().getInt("max-spawn-amount");
		minSpawnAmount = plugin.getConfig().getInt("min-spawn-amount");
		radiusFromPlayer = plugin.getConfig().getInt("radius-from-player");
		radiusFromSpawner = Math.max(plugin.getConfig().getInt("radius-from-spawner"), 1);

		reloadMessage = Utility.color(plugin.getConfig().getString("admin.reload"));
		noPermsError = Utility.color(plugin.getConfig().getString("errors.insufficient-permissions"));
	}
}