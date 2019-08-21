package me.gilles_m.rpgregen2.mechanics.combatmechanics;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import lombok.Setter;
import me.gilles_m.rpgregen2.RPGRegen;

public class Cooldown {

	//The cache where the players in combat are stored
	@Setter
	public static Cache<UUID, Long> cache = CacheBuilder.newBuilder().expireAfterWrite(RPGRegen.getInstance().getConfig().getLong("delay"), TimeUnit.SECONDS).build();

	public static List<Player> getPlayers() {

		final List<Player> players = new ArrayList<Player>();

		for(final Player player : Bukkit.getOnlinePlayers())
			if(Cooldown.cache.getIfPresent(player.getUniqueId()) !=  null)
				players.add(player);

		return players;

	}

}
