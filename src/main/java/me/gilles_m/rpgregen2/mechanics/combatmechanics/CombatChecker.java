package me.gilles_m.rpgregen2.mechanics.combatmechanics;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import lombok.Getter;
import me.gilles_m.rpgregen2.RPGRegen;

public class CombatChecker {

	//This getter will allow us to cancel the task from outside

	@Getter
	private static BukkitTask task;

	public CombatChecker() {

		//final Long frequence = RPGRegen2.getInstance().getConfig().getLong("check-frequency");

		final BukkitRunnable runnable = new BukkitRunnable() {

			List<Player> previousPlayers = Cooldown.getPlayers();

			@Override
			public void run() {

				//Players in combat
				final List<Player> currentPlayers = Cooldown.getPlayers();

				//We compare the previous players with the current ones
				for(final Player player : previousPlayers)
					//If a player from previous players is not in the current players, it means he just left the cache. We can then call the PlayerLeaveCombatEvent
					if(!currentPlayers.contains(player)) {

						/*
						 * We instantiate the event and then calls it for the player that was missing from the current players
						 */

						final PlayerLeaveCombatEvent event = new PlayerLeaveCombatEvent(player);
						Bukkit.getServer().getPluginManager().callEvent(event);

					}

				previousPlayers = currentPlayers;

			}

		};


		//TODO: Making the check frequency configurable through the file
		task = runnable.runTaskTimer(RPGRegen.getInstance(), 0L, 2L);

		/*
		 *
		 * Premium feature: Check for the player max health changes
		 *
		 * new BukkitRunnable() {

			@Override
			public void run() {

				//TODO: Check if a player changed his max health -> has to be placed in combat

			}

		}.runTaskTimer(RPGRegen2.getInstance(), 0L, 2L); */

	}


}
