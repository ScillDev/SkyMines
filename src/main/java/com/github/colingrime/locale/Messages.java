package com.github.colingrime.locale;

import com.github.colingrime.utils.Logger;
import com.github.colingrime.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Messages {

	// general messages
	LIST_SKYMINES_TOP_MESSAGE("general.list-skymines.top-message", "&aList of &eSkyMines&a:"),
	LIST_SKYMINES_REPEATING_MESSAGE("general.list-skymines.repeating-message", "&7→ [#%id%] &eClick to go home. &7(&a%x%x&7, &a%y%y&7, &a%z%z&7)"),
	RESET_COOLDOWN_FINISH("general.reset-cooldown-finish", "&aThe &eSkyMine &awith the ID &e%id% &ahas just finished cooling down!"),
	PLACEMENT_COOLDOWN_FINISH("general.placement-cooldown-finish", "&aYou are free to attempt &eSkyMine &aplacement again."),

	// success messages
	SUCCESS_GIVE("success.give", "&aYou have given &e%amount%x %token% &ato &e%player%&a!"),
	SUCCESS_RECEIVE("success.receive", "&aYou have received &e%amount%x %token%&a!"),
	SUCCESS_UPGRADE("success.upgrade", "&aYou have upgraded &e%upgrade% &ato level &e%level%&a!"),
	SUCCESS_HOME("success.home", "&aYou have been teleported to your &eSkyMine&a!"),
	SUCCESS_SETHOME("success.sethome", "&aYou have successfully changed your &eSkyMine&a's home location!"),
	SUCCESS_RESET("success.reset", "&aThe &eSkyMine &ahas been reset!"),
	SUCCESS_PICKUP("success.pickup", "&aYou have successfully picked up your &eSkyMine&a!"),
	SUCCESS_PLACE("success.place", "&aYou have successfully placed your &eSkyMine&a!"),

	// failure messages
	FAILURE_MAX_AMOUNT("failure.max-amount", "&cYou have reached the maximum amount of &eSkyMines &cyou can place."),
	FAILURE_TOO_SMALL("failure.too-small", "&cThe &eSkyMine &cyou are trying to create is too small."),
	FAILURE_TOO_BIG("failure.too-big", "&cFor performance reasons, a side is not permitted to exceed 100 blocks."),
	FAILURE_TOO_FAR_AWAY("failure.too-far-away", "&cYou are too far away from your &eSkyMine &cto set its home."),
	FAILURE_NO_SPACE("failure.no-space", "&cThere is no space for the &eSkyMine &cto be placed here."),
	FAILURE_NO_INVENTORY_SPACE("failure.no-inventory-space", "&cThere is no space in your inventory to get the &eSkyMine Token&c."),
	FAILURE_NO_FUNDS("failure.no-funds", "&cYou don't have enough money to buy this upgrade."),
	FAILURE_NO_SKYMINE("failure.no-skymine", "&cYou don't have a &eSkyMine &cwith the ID: &e%id%&c."),
	FAILURE_NO_SKYMINES("failure.no-skymines", "&cYou don't have any &eSkyMines&c."),
	FAILURE_NO_PERMISSION("failure.no-permission", "&cYou do not have permission to perform this command."),
	FAILURE_NO_DROP("failure.no-drop", "&cYou are not allowed to drop your &eSkyMine Token&c."),
	FAILURE_ON_PLACEMENT_COOLDOWN("failure.on-placement-cooldown", "&cThe cooldown to attempt &eSkyMine &cplacement is &e%time%&c."),
	FAILURE_ON_RESET_COOLDOWN("failure.on-reset-cooldown", "&cThe cooldown on the &eSkyMine &cis &e%time%&c."),
	FAILURE_ALREADY_MAXED("failure.already-maxed", "&cThis &eSkyMine&c's upgrade is already maxed out!"),
	FAILURE_INVALID_SENDER("failure.invalid-sender", "&cYou must be a player to perform this command."),
	FAILURE_INVALID_PLACEMENT("failure.invalid-placement", "&cYou can't place down a &eSkyMine Token&c."),

	// usage messages
	USAGE_SKYMINES_COMMAND("usage.skymines-command", "&eSkyMine &aCommands:",
			"&7→ &a/sm list &7- lists your &eSkyMines&7.",
			"&7→ &a/sm panel [id] &7- open the &eSkyMine&7's main panel.",
			"&7→ &a/sm home [id] &7- go to the &eSkyMine&7.",
			"&7→ &a/sm sethome [id] &7- set the &eSkyMine&7's home.",
			"&7→ &a/sm reset [id] &7- reset the &eSkyMine&7.",
			"&7→ &a/sm upgrades [id] &7- open the &eSkyMine&7's upgrade panel.",
			"&7→ &a/sm pickup [id] &7- pickup the &eSkyMine&7."),
	USAGE_SKYMINES_LIST("usage.skymines-list", "&7Usage: &a/sm list &7→ lists your &eSkyMines&7."),
	USAGE_SKYMINES_PANEL("usage.skymines-panel", "&7Usage: &a/sm panel [id] &7→ open the &eSkyMine&7's main panel."),
	USAGE_SKYMINES_HOME("usage.skymines-home", "&7Usage: &a/sm home [id] &7→ go to the &eSkyMine&7."),
	USAGE_SKYMINES_SETHOME("usage.skymines-sethome", "&7Usage: &a/sm sethome [id] &7→ set the &eSkyMine&7's home."),
	USAGE_SKYMINES_RESET("usage.skymines-reset", "&7Usage: &a/sm reset [id] &7→ reset the &eSkyMine&7."),
	USAGE_SKYMINES_UPGRADES("usage.skymines-upgrades", "&7Usage: &a/sm upgrades [id] &7→ open the &eSkyMine&7's upgrade panel."),
	USAGE_SKYMINES_PICKUP("usage.skymines-pickup", "&7Usage: &a/sm pickup [id] &7→ pickup the &eSkyMine&7."),
	USAGE_SKYMINES_GIVE("usage.skymines-give", "&7Usage: &a/sm give [name] {LxHxW} {amount} &7→ &egives a &eSkyMine Token &eto the player."),

	// admin messages
	RELOADED("admin.reloaded", "&aAutoSell has been reloaded!"),
	;

	private static File file;
	private static FileConfiguration config;

	private final String path;
	private List<String> messages;

	Messages(String path, String...messages) {
		this.path = path;
		this.messages = Arrays.asList(messages);
	}

	public static void init(JavaPlugin plugin) {
		file = new File(plugin.getDataFolder(), "messages.yml");
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			plugin.saveResource("messages.yml", false);
		}
	}

	public static void reload() {
		config = YamlConfiguration.loadConfiguration(file);
		Arrays.stream(Messages.values()).forEach(Messages::update);
	}

	public void update() {
		if (!config.getStringList(path).isEmpty()) {
			messages = Utils.color(config.getStringList(path));
		} else if (config.getString(path) != null) {
			messages = Collections.singletonList(Utils.color(config.getString(path)));
		} else {
			Logger.log("Messages path \"" + path + "\" has failed to load (using default value).");
			messages = Utils.color(messages);
		}
	}

	public void sendTo(CommandSender sender) {
		if (messages.isEmpty()) {
			return;
		}

		messages.forEach(sender::sendMessage);
	}

	public void sendTo(CommandSender sender, Replacer replacer) {
		if (messages.isEmpty()) {
			return;
		}

		replacer.replace(messages).forEach(sender::sendMessage);
	}

	@Override
	public String toString() {
		return String.join("\n", messages);
	}
}
