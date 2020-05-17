package de.speidy674.MineCord.config;

import java.io.File;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import de.speidy674.MineCord.Main;
import net.dv8tion.jda.api.entities.Member;

public class PlayerCFG {

	Main plugin;
	UUID MCID;
	String DISID;
	File userMCFile;
	File userDISFile;
	FileConfiguration userConfig;

	public PlayerCFG(Main plugin, final Player player) {
		MCID = player.getUniqueId();
		DISID = "";
		this.plugin = plugin;

		userMCFile = new File(plugin.getDataFolder() + "/Users", MCID + ".yml");

		userConfig = YamlConfiguration.loadConfiguration(userMCFile);
		createFile(player);

	}
	
	public PlayerCFG(Main plugin, final Member member) {
		MCID = null;
		DISID = member.getId();
		userDISFile = new File(plugin.getDataFolder() + "/Users", DISID + ".yml");
		
		this.plugin = plugin;


		userConfig = YamlConfiguration.loadConfiguration(userDISFile);
		createFile(member);

	}
	
	public PlayerCFG(Main plugin, @NotNull OfflinePlayer player, @NotNull UUID uniqueId) {
		MCID = player.getUniqueId();
		DISID = "";
		this.plugin = plugin;

		userMCFile = new File(plugin.getDataFolder() + "/Users", MCID + ".yml");

		userConfig = YamlConfiguration.loadConfiguration(userMCFile);
	}

	public void createFile(final Player player) {

		if (!(userMCFile.exists())) {
			try {

				userConfig = YamlConfiguration.loadConfiguration(userMCFile);

				userConfig.set("User.Info.UniqueID", player.getUniqueId().toString());
				userConfig.set("User.Discord.ID", "");
				userConfig.set("User.Discord.token", "");
				userConfig.set("User.verifed", false);

				userConfig.save(userMCFile);

			} catch (Exception e) {

				e.printStackTrace();

			}
		}
	}
	
	public void createFile(final Member member) {

		if (!(userDISFile.exists())) {
			try {

				userConfig = YamlConfiguration.loadConfiguration(userDISFile);

				userConfig.set("User.Info.UniqueID", MCID);
				userConfig.set("User.Discord.ID", member.getId());
				userConfig.set("User.Discord.token", "");
				userConfig.set("User.verifed", false);

				userConfig.save(userMCFile);

			} catch (Exception e) {

				e.printStackTrace();

			}
		}
	}

	public void save() {
		try {
			if(MCID == null)
				MCID = UUID.fromString(userConfig.getString("User.Info.UniqueID"));
			
			userMCFile = new File(plugin.getDataFolder() + "/Users", MCID + ".yml");
			userConfig.save(userMCFile);
			
			if(!getDiscordID().equals("")) {
				DISID = getDiscordID();
				userDISFile = new File(plugin.getDataFolder() + "/Users", DISID + ".yml");
				userConfig.save(userDISFile);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setDiscordID(String id) {
		userConfig.set("User.Discord.ID", id);
		DISID = id;
		save();
	}

	public String getDiscordID() {
		return userConfig.getString("User.Discord.ID");
	}

	public boolean verify(String token) {
		if (token.equals(userConfig.getString("User.Discord.token"))) {
			userConfig.set("User.verifed", true);
			save();
		}
		return isVerifyed();
	}

	public boolean isVerifyed() {
		return userConfig.getBoolean("User.verifed");
	}
	
	public String getToken() {
		return userConfig.getString("User.Discord.token", "");
	}
	
	public void setToken(String token) {
		userConfig.set("User.Discord.token", token);
		save();
	}
	
	public String getUUID() {
		return userConfig.getString("User.Info.UniqueID", "");
	}

}
