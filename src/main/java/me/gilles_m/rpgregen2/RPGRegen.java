package me.gilles_m.rpgregen2;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sucy.skill.SkillAPI;

import lombok.Getter;
import lombok.Setter;
import me.gilles_m.rpgregen2.commands.RPGRegenCommand;
import me.gilles_m.rpgregen2.mechanics.Common;
import me.gilles_m.rpgregen2.mechanics.EventListener;
import me.gilles_m.rpgregen2.mechanics.RegenMechanic;
import me.gilles_m.rpgregen2.mechanics.combatmechanics.CombatChecker;
import me.gilles_m.rpgregen2.mechanics.combatmechanics.CombatListener;
import me.gilles_m.rpgregen2.mechanics.combatmechanics.Cooldown;

public class RPGRegen extends JavaPlugin {

	@Getter
	private static RPGRegen instance;

	@Getter
	@Setter
	private static CombatChecker combatChecker;
	@Getter
	@Setter
	private static RegenMechanic regenMechanic;

	@Override
	public void onEnable() {

		instance = this;

		getServer().getPluginManager().registerEvents(new CombatListener(), this);
		getServer().getPluginManager().registerEvents(new EventListener(), this);

		this.getConfig().options().copyDefaults();
		saveDefaultConfig();

		Common.registerCommand(new RPGRegenCommand());

		combatChecker = new CombatChecker();
		regenMechanic = new RegenMechanic();

		Bukkit.getConsoleSender().sendMessage(Common.colorize("&f[&aRPGRegen&f] &aEnabled"));

		if(getSkillAPI() != null)
			Bukkit.getConsoleSender().sendMessage(Common.colorize("&f[&aRPGRegen&f] |v|-===&aSkillAPI detected&f===-|v|"));
		else
			Bukkit.getConsoleSender().sendMessage(Common.colorize("&f[&aRPGRegen&f] |x|-===&aSkillAPI not detected&f===-|x|"));

		if(RPGRegen.getInstance().getConfig().getBoolean("use-SkillAPI-experience-level") && RPGRegen.getInstance().getConfig().getBoolean("use-experience-level"))
			Bukkit.getConsoleSender().sendMessage(Common.colorize("&f[&aRPGRegen&f] &cError in config.yml: use-SkillAPI-experience-level and use-experience-level cannot be true at the same time."));

	}

	@Override
	public void onDisable() {
		instance = null;
	}


	public SkillAPI getSkillAPI() {

		final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("SkillAPI");

		if(plugin instanceof SkillAPI)
			return (SkillAPI) plugin;
		else
			return null;

	}

	public static boolean isInCombat(Player player) {

		return Cooldown.cache.getIfPresent(player.getUniqueId()) != null;

	}

	public static List<Player> getPlayers() {

		final List<Player> players = new ArrayList<Player>();

		for(final Player player : Bukkit.getOnlinePlayers())
			if(Cooldown.cache.getIfPresent(player.getUniqueId()) !=  null)
				players.add(player);

		return players;

	}

	public static void removePlayer(Player player) {

		Cooldown.cache.invalidate(player.getUniqueId());

	}

}
