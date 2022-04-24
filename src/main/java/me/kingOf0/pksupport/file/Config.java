package me.kingOf0.pksupport.file;

import me.kingOf0.pksupport.PKSupport;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Config extends YamlConfiguration {

	private final File file;
	private PKSupport main;

	public Config() {
		main = PKSupport.getInstance();
		file = new File(main.getDataFolder(), "config.yml");
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			saveDefaults();
		}
		reload();
	}

	private void saveDefaults() {
		main.saveResource("config.yml", false);
	}

	private void reload() {
		try {
			super.load(this.file);
		} catch (Exception ignored) {
		}
	}
}