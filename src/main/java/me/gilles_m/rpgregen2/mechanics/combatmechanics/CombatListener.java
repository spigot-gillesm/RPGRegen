package me.gilles_m.rpgregen2.mechanics.combatmechanics;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.LingeringPotionSplashEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import org.bukkit.projectiles.ProjectileSource;

import me.gilles_m.rpgregen2.RPGRegen;

public class CombatListener implements Listener {

	//Function to detect if a player is in a combat state
	@EventHandler
	public void onHit(EntityDamageByEntityEvent event) {

		//Detect if a player hits an entity
		if(event.getDamager() instanceof Player) {

			final Player player = (Player) event.getDamager();

			/*
			 * We instantiate the event and then calls it
			 * If the event isn't cancelled, the player is registered in the cache
			 */

			final DamageCause damageCause = event.getCause();

			final PlayerEnterCombatEvent playerEnterCombatEvent = new PlayerEnterCombatEvent(player, damageCause);
			Bukkit.getServer().getPluginManager().callEvent(playerEnterCombatEvent);

			if(!playerEnterCombatEvent.isCancelled())
				Cooldown.cache.put(player.getUniqueId(), System.currentTimeMillis());

			return;

		}

		//Let's later create in the config a section made to configure the cause that should put players in combat
		else if(event.getCause().equals(DamageCause.PROJECTILE) || event.getCause().equals(DamageCause.MAGIC)) {

			final ProjectileSource source = ((Projectile) event.getDamager()).getShooter();

			//Check if a player shot an entity
			if(source instanceof Player) {

				final Player player = (Player) source;
				final DamageCause damageCause = event.getCause();

				final PlayerEnterCombatEvent playerEnterCombatEvent = new PlayerEnterCombatEvent(player, damageCause);
				Bukkit.getServer().getPluginManager().callEvent(playerEnterCombatEvent);

				if(!playerEnterCombatEvent.isCancelled())
					Cooldown.cache.put(player.getUniqueId(), System.currentTimeMillis());

				return;

			}

			//Check if the player was shot by another entity
			else if(event.getEntity() instanceof Player) {

				final Player player = (Player) event.getEntity();
				final DamageCause damageCause = event.getCause();

				final PlayerEnterCombatEvent playerEnterCombatEvent = new PlayerEnterCombatEvent(player, damageCause);
				Bukkit.getServer().getPluginManager().callEvent(playerEnterCombatEvent);

				if(!playerEnterCombatEvent.isCancelled())
					Cooldown.cache.put(player.getUniqueId(), System.currentTimeMillis());

				return;

			}

		}

		//We check if a player has been damaged by anything
		else if(!(event.getDamager() instanceof Player))
			if(event.getEntity() instanceof Player) {

				final Player player = (Player) event.getEntity();
				final DamageCause damageCause = event.getCause();

				final PlayerEnterCombatEvent playerEnterCombatEvent = new PlayerEnterCombatEvent(player, damageCause);
				Bukkit.getServer().getPluginManager().callEvent(playerEnterCombatEvent);

				if(!playerEnterCombatEvent.isCancelled())
					Cooldown.cache.put(player.getUniqueId(), System.currentTimeMillis());

				return;

			}

	}

	@EventHandler
	public void onDamaged(EntityDamageEvent event) {

		if(event.getEntity() instanceof Player) {

			final Player player = (Player) event.getEntity();
			final DamageCause damageCause = event.getCause();

			final PlayerEnterCombatEvent playerEnterCombatEvent = new PlayerEnterCombatEvent(player, damageCause);
			Bukkit.getServer().getPluginManager().callEvent(playerEnterCombatEvent);

			if(!playerEnterCombatEvent.isCancelled())
				Cooldown.cache.put(player.getUniqueId(), System.currentTimeMillis());

			return;

		}

	}

	@EventHandler
	public void onLingeringThrown(LingeringPotionSplashEvent event) {

		final ProjectileSource source = event.getEntity().getShooter();

		if(source instanceof Player) {

			final Player player = (Player) source;

			if(RPGRegen.getInstance().getConfig().getBoolean("is-potion-fighting"))
				if(RPGRegen.getInstance().getConfig().getBoolean("is-lingering-fighting")) {

					final DamageCause damageCause = DamageCause.MAGIC;

					final PlayerEnterCombatEvent playerEnterCombatEvent = new PlayerEnterCombatEvent(player, damageCause);
					Bukkit.getServer().getPluginManager().callEvent(playerEnterCombatEvent);

					if(!playerEnterCombatEvent.isCancelled())
						Cooldown.cache.put(player.getUniqueId(), System.currentTimeMillis());

					return;

				}

		}

	}

	@EventHandler
	public void onSplashThrown(PotionSplashEvent event) {

		final ProjectileSource source = event.getEntity().getShooter();

		if(source instanceof Player) {

			final Player player = (Player) source;

			if(RPGRegen.getInstance().getConfig().getBoolean("is-potion-fighting"))
				if(RPGRegen.getInstance().getConfig().getBoolean("is-splash-fighting")) {

					final DamageCause damageCause = DamageCause.MAGIC;

					final PlayerEnterCombatEvent playerEnterCombatEvent = new PlayerEnterCombatEvent(player, damageCause);
					Bukkit.getServer().getPluginManager().callEvent(playerEnterCombatEvent);

					if(!playerEnterCombatEvent.isCancelled())
						Cooldown.cache.put(player.getUniqueId(), System.currentTimeMillis());

					return;

				}

		}

	}

	@EventHandler
	public void onNaturalRegeneration(EntityRegainHealthEvent event) {

		if(RPGRegen.getInstance().getConfig().getBoolean("replace-minecraft-system"))
			if(event.getEntity() instanceof Player)
				if(RPGRegen.getInstance().getConfig().getBoolean("per-world-system")) {
					if(RPGRegen.getInstance().getConfig().getStringList("worlds").contains(event.getEntity().getWorld().getName()))
						if(event.getRegainReason().equals(RegainReason.SATIATED))
							event.setCancelled(true);
				} else
					if(event.getRegainReason().equals(RegainReason.SATIATED))
						event.setCancelled(true);

	}

	@EventHandler
	private void onConsumeItem(PlayerItemConsumeEvent event) {

		/*
		 * 1. We check if is-potion-fighting is set to true in the config
		 * 2. We check for the null pointer exceptions
		 * 3. We check if the item is a potion (event.getItem().getItemMeta() instanceof PotionMeta)
		 * 4. We check if any-potion is set to true in the config. If yes, we call the event and stop here
		 * 5. If not, we check if the drunk potion is in the list
		 */

		if(RPGRegen.getInstance().getConfig().getBoolean("is-potion-fighting"))
			if(event.getItem() != null && event.getItem().hasItemMeta())
				if(event.getItem().getItemMeta() instanceof PotionMeta)
					if(RPGRegen.getInstance().getConfig().getBoolean("any-potion")) {

						final Player player = event.getPlayer();
						final DamageCause damageCause = DamageCause.MAGIC;

						final PlayerEnterCombatEvent playerEnterCombatEvent = new PlayerEnterCombatEvent(player, damageCause);
						Bukkit.getServer().getPluginManager().callEvent(playerEnterCombatEvent);

						if(!playerEnterCombatEvent.isCancelled())
							Cooldown.cache.put(player.getUniqueId(), System.currentTimeMillis());

						return;

					} else {

						//We get the potion meta
						final PotionMeta potionmeta = (PotionMeta) event.getItem().getItemMeta();
						//We get the potion type through the potion meta
						final PotionType type = potionmeta.getBasePotionData().getType();

						for(final String typeName : RPGRegen.getInstance().getConfig().getStringList("potion-list")) {

							//We get the value of the potion type via the string set in the config
							final PotionType checkedType = PotionType.valueOf(typeName);

							if(type == checkedType) {

								final Player player = event.getPlayer();
								final DamageCause damageCause = DamageCause.MAGIC;

								final PlayerEnterCombatEvent playerEnterCombatEvent = new PlayerEnterCombatEvent(player, damageCause);
								Bukkit.getServer().getPluginManager().callEvent(playerEnterCombatEvent);

								if(!playerEnterCombatEvent.isCancelled())
									Cooldown.cache.put(player.getUniqueId(), System.currentTimeMillis());

								return;

							}

						}

					}

	}

}
