package me.gilles_m.rpgregen2.managers;

import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import me.gilles_m.rpgregen2.RPGRegen;

public class RegenManager {

	//Check if the player is at his maximum health
	private static boolean hasMaxHealth(Player player) {

		return player.getHealth() >= player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();

	}

	@SuppressWarnings("deprecation")
	private static boolean isInRightWorld(Player player) {

		final boolean perWorldSystem = RPGRegen.getInstance().getConfig().getBoolean("per-world-system");
		final boolean replaceMinecraftSystem = RPGRegen.getInstance().getConfig().getBoolean("replace-minecraft-system");

		//If we don't replace the Minecraft system, we check if the world has its natural regen gamerule set to false
		if(!replaceMinecraftSystem)
			if(!player.getWorld().getGameRuleValue("naturalRegeneration").equals("false"))
				return false;

		//If the per world system is set, we check if the player is in one of the listed world
		if(perWorldSystem)
			if(!RPGRegen.getInstance().getConfig().getStringList("worlds").contains(player.getWorld().getName()))
				return false;

		return true;

	}

	public static boolean canRegen(Player player) {

		return !hasMaxHealth(player) && isInRightWorld(player);

	}

	public static void playParticles(Player player) {

		player.spawnParticle(Particle.VILLAGER_HAPPY, player.getLocation().getX(), player.getLocation().getY() + 1, player.getLocation().getZ(),
				RPGRegen.getInstance().getConfig().getInt("amount"),
				RPGRegen.getInstance().getConfig().getDouble("x-offset"),
				RPGRegen.getInstance().getConfig().getDouble("y-offset"),
				RPGRegen.getInstance().getConfig().getDouble("z-offset"));

	}

}
