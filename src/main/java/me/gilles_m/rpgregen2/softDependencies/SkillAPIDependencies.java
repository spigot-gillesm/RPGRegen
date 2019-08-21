package me.gilles_m.rpgregen2.softDependencies;

import org.bukkit.entity.Player;

import com.sucy.skill.SkillAPI;

public class SkillAPIDependencies {

	public static int getSkillLevel(Player player) {

		int level = 0;

		if(SkillAPI.getPlayerData(player).hasClass())
			level = SkillAPI.getPlayerData(player).getMainClass().getLevel();

		return level;

	}

}
