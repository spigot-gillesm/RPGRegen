package me.gilles_m.rpgregen2.mechanics.combatmechanics;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import lombok.Setter;
import me.gilles_m.rpgregen2.RPGRegen;

public class Cooldown {

	@Setter
	public static Cache<UUID, Long> cache = CacheBuilder.newBuilder().expireAfterWrite(RPGRegen.getInstance().getConfig().getLong("delay"), TimeUnit.SECONDS).build();

}
