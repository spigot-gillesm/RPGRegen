package me.gilles_m.rpgregen2.mechanics;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import lombok.Getter;
import lombok.Setter;
import me.gilles_m.rpgregen2.RPGRegen;
import me.gilles_m.rpgregen2.managers.RegenManager;
import me.gilles_m.rpgregen2.softDependencies.SkillAPIDependencies;

public class RegenMechanic {

	@Getter
	@Setter
	public static List<Player> playersOutOfCombat = new ArrayList<Player>();

	@Getter
	private static BukkitTask task;

	public RegenMechanic() {

		final int period = RPGRegen.getInstance().getConfig().getInt("period");

		final BukkitRunnable runnable = new BukkitRunnable() {

			@Override
			public void run() {

				for(final Player player : playersOutOfCombat)
					if(RegenManager.canRegen(player))
						heal(player);
			}

		};

		task = runnable.runTaskTimer(RPGRegen.getInstance(), 0, period);

	}

	//Calculation of the healing output + healing player
	private void heal(Player player) {

		//Before doing anything, we check if the player is still alive
		if(player.isDead())
			return;

		final float playerMaxHealth = (float) player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		final float playerCurrentHealth = (float) player.getHealth();
		final float playerFoodLevel = (player.getFoodLevel());

		//We set the basic value for the final regen amount to what is specified by the user
		float finalRegenAmount = (float) RPGRegen.getInstance().getConfig().getDouble("regen amount");

		//We check what system is used
		final boolean useExperienceLevel = RPGRegen.getInstance().getConfig().getBoolean("use-experience-level");
		final boolean useSkillAPILevel = RPGRegen.getInstance().getConfig().getBoolean("use-SkillAPI-experience-level");
		final boolean useFoodLevel = RPGRegen.getInstance().getConfig().getBoolean("use-food-level");
		final boolean spawnParticles = RPGRegen.getInstance().getConfig().getBoolean("particles");

		/*
		 *
		 *All the variables and system checks are done
		 *
		 */

		//If both the systems are used => We don't do anything as there is a configuration error
		if(!(useSkillAPILevel && useExperienceLevel)) {

			//Every x level, the bonus is applied once more
			final int perExperience = RPGRegen.getInstance().getConfig().getInt("per-level");
			//The amount of health received as an experience bonus
			final float regenBonus = (float) RPGRegen.getInstance().getConfig().getDouble("regen-bonus");
			//ItemManager.getItemRegenBonus(player);

			//We check if the vanilla experience level system is used
			if(useExperienceLevel) {

				final int playerExp = player.getLevel();

				finalRegenAmount += playerExp/perExperience * regenBonus;

			}

			//We check if SkillAPI is present and if it is used
			if(useSkillAPILevel && RPGRegen.getInstance().getSkillAPI() != null) {

				final int playerExp = SkillAPIDependencies.getSkillLevel(player);

				finalRegenAmount += playerExp/perExperience * regenBonus;

			}

		}


		if(useFoodLevel)
			finalRegenAmount *= playerFoodLevel / 20;

		if(finalRegenAmount + playerCurrentHealth >= playerMaxHealth)
			player.setHealth(playerMaxHealth);
		else
			player.setHealth(playerCurrentHealth + finalRegenAmount);

		if(spawnParticles)
			RegenManager.playParticles(player);


	}

}
