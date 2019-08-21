package me.gilles_m.rpgregen2.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lombok.AccessLevel;
import lombok.Setter;
import me.gilles_m.rpgregen2.mechanics.Common;

public abstract class PlayerCommand extends Command {

	private Player player;
	@Setter(value=AccessLevel.PROTECTED)
	private String prefix;

	protected PlayerCommand(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {

		if(!(sender instanceof Player)) {
			Common.tell(sender, "&cYou must be a player to run this command.");
			return false;
		}

		final Player player = (Player) sender;
		this.player = player;

		run(player, args);

		return false;
	}

	protected abstract void run(Player player, String[] args);

	protected void tell(String message) {
		Common.tell(player, (prefix != null ? prefix + " " : "") + message);
	}

}
