package de.speidy674.MineCord.config;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.speidy674.MineCord.Main;

public class Messages {
	
	File file;
	FileConfiguration config;
	
	public Messages() {
		file = new File(Main.plugin.getDataFolder() + "/messages.yml");

		config = YamlConfiguration.loadConfiguration(file);
		createFile();
	}
	
	public void createFile() {

		if (!(file.exists())) {
			try {

				config = YamlConfiguration.loadConfiguration(file);

				config.addDefault("MC.PlayerOnly","cmd only for a player");
				config.addDefault("MC.VerifedAlready", "you are already verifed");
				config.addDefault("MC.VerifedSuccses", "succses verifed Discord");
				config.addDefault("MC.VerifedWrongToken", "wrong token");
				config.addDefault("MC.VerifedUsage", "usage /verify <token>");

				config.addDefault("DIS.MCName", "you need to provide a MineCraft Username");
				config.addDefault("DIS.NeedOnline", "you need to be online on the MC server");
				config.addDefault("DIS.VerifycationTitle", "Verifaction");
				config.addDefault("DIS.VerifycationText", "pls use `/verify %token%` in Minecraft");

				config.options().copyDefaults(true);
				
				config.save(file);

			} catch (Exception e) {

				e.printStackTrace();

			}
		}
	}
	
	public String getString(String key) {
		return config.getString(key);
	}

}
