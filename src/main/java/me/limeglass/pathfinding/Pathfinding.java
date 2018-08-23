package me.limeglass.pathfinding;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import me.limeglass.pathfinding.api.protocol.IPathfinding;
import me.limeglass.pathfinding.elements.Register;
import me.limeglass.pathfinding.utils.Utils;
import net.md_5.bungee.api.ChatColor;

public class Pathfinding extends JavaPlugin {
	
	private static Map<String, FileConfiguration> files = new HashMap<String, FileConfiguration>();
	private String packageName = "me.limeglass.pathfinding";
	private static String prefix = "&7[&ePathfinding&7] &e";
	private static String nameplate = "[Pathfinding] ";
	private static IPathfinding pathfinding;
	private static Pathfinding instance;
	private SkriptAddon addon;
	private Metrics metrics;
	
	public void onEnable(){
		addon = Skript.registerAddon(this);
		instance = this;
		saveDefaultConfig();
		File config = new File(getDataFolder(), "config.yml");
		if (!Objects.equals(getDescription().getVersion(), getConfig().getString("version"))) {
			consoleMessage("&dNew update found! Updating files now...");
			if (config.exists()) new SpigotConfigSaver(this).execute();
		}
		for (String name : Arrays.asList("config", "syntax")) { //replace config with future files here
			File file = new File(getDataFolder(), name + ".yml");
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				saveResource(file.getName(), false);
			}
			FileConfiguration configuration = new YamlConfiguration();
			try {
				configuration.load(file);
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
			files.put(name, configuration);
		}
		metrics = new Metrics(this);
		Register.metrics(metrics);
		/*String version = ReflectionUtil.getVersion();
		if (version.contains("1_8")) {
			factory = new PacketFactory_1_8_R1();
		} else if (version.contains("1_7")) {
			consoleMessage("Guardian's don't exist in 1.7 silly, thus this addon is useless. Please update your Spigot version in order to use this addon.");
			this.setEnabled(false);
			return;
		} else if (version.contains("1_9")) {
			consoleMessage("Beamer does not support 1.9 at the moment. Please update your Spigot version to latest 1.12 in order to use this addon.");
			this.setEnabled(false);
			return;
		} else {
			factory = new PacketFactory_1_12_R1();
		}*/
		if (!getConfig().getBoolean("DisableRegisteredInfo", false)) Bukkit.getLogger().info(nameplate + "has been enabled!");
	}
	
	public SkriptAddon getAddonInstance() {
		return addon;
	}
	
	public static IPathfinding getPathfindingAPI() {
		return pathfinding;
	}
	
	public String getNameplate() {
		return nameplate;
	}
	
	public static Pathfinding getInstance() {
		return instance;
	}
	
	public static String getPrefix() {
		return prefix;
	}
	
	public String getPackageName() {
		return packageName;
	}
	
	public Metrics getMetrics() {
		return metrics;
	}
	
	//Grabs a FileConfiguration of a defined name. The name can't contain .yml in it.
	public FileConfiguration getConfiguration(String file) {
		return (files.containsKey(file)) ? files.get(file) : null;
	}
	
	public static void save(String configuration) {
		try {
			File configurationFile = new File(instance.getDataFolder(), configuration + ".yml");
			instance.getConfiguration(configuration).save(configurationFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void debugMessage(@Nullable String... messages) {
		if (instance.getConfig().getBoolean("debug")) {
			for (String text : messages) consoleMessage("&b" + text);
		}
	}
	
	public static void infoMessage(@Nullable String... messages) {
		if (messages != null && messages.length > 0) {
			for (String text : messages) Bukkit.getLogger().info(nameplate + text);
		} else {
			Bukkit.getLogger().info("");
		}
	}

	public static void consoleMessage(@Nullable String... messages) {
		if (instance.getConfig().getBoolean("DisableConsoleMessages", false)) return;
		if (messages != null && messages.length > 0) {
			for (String text : messages) {
				if (instance.getConfig().getBoolean("DisableConsoleColour", false)) infoMessage(ChatColor.stripColor(Utils.cc(text)));
				else Bukkit.getConsoleSender().sendMessage(Utils.cc(prefix + text));
			}
		} else {
			Bukkit.getLogger().info("");
		}
	}

}