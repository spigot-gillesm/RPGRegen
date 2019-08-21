package me.gilles_m.rpgregen2.mechanics;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;

public class Common {

	public static void registerCommand(Command command) {
		try {
			final Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			commandMapField.setAccessible(true);

			final CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
			commandMap.register(command.getLabel(), command);

		} catch(final Exception e) {
			e.printStackTrace();
		}
	}

	public static void tell(CommandSender toWhom, String... messages) {
		for (final String message : messages)
			tell(toWhom, message);
	}

	public static void tell(CommandSender toWhom, String message) {
		toWhom.sendMessage(colorize(message));
	}

	public static String colorize(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

}
