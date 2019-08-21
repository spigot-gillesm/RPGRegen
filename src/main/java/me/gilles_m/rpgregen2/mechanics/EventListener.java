package me.gilles_m.rpgregen2.mechanics;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.gilles_m.rpgregen2.RPGRegen;
import me.gilles_m.rpgregen2.mechanics.combatmechanics.PlayerEnterCombatEvent;
import me.gilles_m.rpgregen2.mechanics.combatmechanics.PlayerLeaveCombatEvent;

public class EventListener implements Listener {

	@EventHandler
	private void onPlayerJoin(PlayerJoinEvent event) {

		RegenMechanic.playersOutOfCombat.add(event.getPlayer());

	}

	@EventHandler
	private void onPlayerEnterCombat(PlayerEnterCombatEvent event) {

		if(RPGRegen.getInstance().getConfig().getStringList("excluded-cause").contains(event.getDamageCause().toString())) {
			event.setCancelled(true);
			return;
		}

		RegenMechanic.playersOutOfCombat.remove(event.getPlayer());

	}

	@EventHandler
	private void onPlayerLeaveCombat(PlayerLeaveCombatEvent event) {

		RegenMechanic.playersOutOfCombat.add(event.getPlayer());

	}

}
