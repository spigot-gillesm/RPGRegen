package me.gilles_m.rpgregen2.commands;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;

import com.google.common.cache.CacheBuilder;

import me.gilles_m.rpgregen2.RPGRegen;
import me.gilles_m.rpgregen2.mechanics.RegenMechanic;
import me.gilles_m.rpgregen2.mechanics.combatmechanics.CombatChecker;
import me.gilles_m.rpgregen2.mechanics.combatmechanics.Cooldown;

public class RPGRegenCommand extends PlayerCommand {

	public RPGRegenCommand() {
		super("rpgregen");

		setAliases(Arrays.asList("rpgr"));
		setDescription("Plugin general command");
		setPrefix("&f[&a&lRPG &aRegen&f]");

	}

	@Override
	protected void run(Player player, String[] args) {

		if(args.length == 0)
			tell("&aValid command: &9/rpgregen reload");
		else
			if(args[0].equals("reload") || args[0].equals("r"))
				if(player.hasPermission("rpgregen.reload") || player.isOp()) {

					RPGRegen.getInstance().reloadConfig();

					Cooldown.cache.invalidateAll();
					Cooldown.setCache(CacheBuilder.newBuilder().expireAfterWrite(RPGRegen.getInstance().getConfig().getLong("delay"), TimeUnit.SECONDS).build());

					RegenMechanic.playersOutOfCombat.clear();

					//We have to cancel the tasks before creating new instances of CombatChecker / RegenMechanic
					CombatChecker.getTask().cancel();
					RegenMechanic.getTask().cancel();
					//We "destroy" the previous CombatChecker and RegenMechanic from before to set a new one
					RPGRegen.setCombatChecker(null);
					RPGRegen.setCombatChecker(new CombatChecker());
					RPGRegen.setRegenMechanic(null);
					RPGRegen.setRegenMechanic(new RegenMechanic());

					tell("&aPlugin reloaded");

				} else
					tell("&cYou don't have the permission to run this command.");
			else
				tell("&cInvalid command. Use /rpgregen to get help");

	}

}
