package de.speidy674.MineCord;

import java.sql.Connection;
import java.util.List;

import javax.security.auth.login.LoginException;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;

import de.speidy674.MineCord.command.cmdVerify;
import de.speidy674.MineCord.command.discord.cmdDisVerify;
import de.speidy674.MineCord.config.Messages;
import de.speidy674.MineCord.config.PlayerCFG;
import de.speidy674.MineCord.events.BotEvents;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

/**
 * Connect Discord whit Minecraft
 *
 * @author Speidy674
 */
public class Main extends JavaPlugin {

	private JDA jda = null;
	public Connection connection;
	public static Main plugin;
	public static Messages messages;

	@Override
	public void onEnable() {
		plugin = this;
		this.saveDefaultConfig();
		messages = new Messages();

		// Start DiscordBot

		CommandClientBuilder cmdBuilder = new CommandClientBuilder();

		cmdBuilder.setEmojis("✅", "⚠️", "❌");
		cmdBuilder.setOwnerId("202505193714221068");
		cmdBuilder.setPrefix("mc!");
		cmdBuilder.setLinkedCacheSize(0);
		cmdBuilder.setActivity(Activity.playing("MineCord"));

		cmdBuilder.addCommands(new cmdDisVerify(this));
		CommandClient cmdClient = cmdBuilder.build();

		String token = this.getConfig().getString("discord.token");
		if (token.equals("replace whit token")) {
			tellConsole("pls configure the Discord Bot Token.");
			this.getPluginLoader().disablePlugin(this);
		}

		JDABuilder jdaBuilder = JDABuilder.createDefault(token);
		jdaBuilder.setStatus(OnlineStatus.ONLINE);
		jdaBuilder.setEnabledIntents(GatewayIntent.GUILD_MEMBERS,GatewayIntent.DIRECT_MESSAGES,GatewayIntent.GUILD_MESSAGES);
		jdaBuilder.disableCache(CacheFlag.EMOTE,CacheFlag.VOICE_STATE);
		
		jdaBuilder.addEventListeners(cmdClient);
		jdaBuilder.addEventListeners(new BotEvents(this));

		try {
			jda = jdaBuilder.build();
		} catch (LoginException e) {
			e.printStackTrace();

			tellConsole("error starting DiscordBot");
			this.getPluginLoader().disablePlugin(this);
		}

		getCommand("verify").setExecutor(new cmdVerify(this, jda));
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				tellConsole("[MineCord] Sync Player Roles");
				for(@NotNull OfflinePlayer player : getServer().getOfflinePlayers()){
					PlayerCFG pcfg = new PlayerCFG(plugin, player, player.getUniqueId());
					if(pcfg.isVerifyed()) {
						SyncPlayerRoles(player, pcfg.getDiscordID());
					}
				}
				
			}
		}.runTaskLater(this, 6000);
		


	}

	@Override
	public void onDisable() {
		jda.shutdown();
	}

	public void tellConsole(String message) {
		Bukkit.getConsoleSender().sendMessage(message);
	}
	
	public void SyncPlayerRoles(Player player,String DiscordID) {
		User user = jda.retrieveUserById(DiscordID).complete();
		Member member = jda.getGuilds().get(0).retrieveMember(user).complete();
		List<Role> roles = member.getRoles();
		for (Role role : roles) {
			if(getConfig().isSet("discord.roles."+role.getId())){
				addRole(player,role);
			}
		}
	}
	
	public void SyncPlayerRoles(@NotNull OfflinePlayer player,String DiscordID) {
		User user = jda.retrieveUserById(DiscordID).complete();
		Member member = jda.getGuilds().get(0).retrieveMember(user).complete();
		List<Role> roles = member.getRoles();
		for (Role role : roles) {
			if(getConfig().isSet("discord.roles."+role.getId())){
				addRole(player,role);
			}
		}
	}
	
	public void addRole(Player player, Role role) {
		String cmd = getConfig().getString("discord.roles.addCMD");
		cmd = cmd.replaceAll("%player%", player.getName()).replaceAll("%group%", getConfig().getString("discord.roles."+role.getId()));
		getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
	}
	
	public void removeRole(Player player, Role role) {
		String cmd = getConfig().getString("discord.roles.removeCMD");
		cmd = cmd.replaceAll("%player%", player.getName()).replaceAll("%group%", getConfig().getString("discord.roles."+role.getId()));
		getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
	}

	public void addRole(@NotNull OfflinePlayer player, Role role) {
		String cmd = getConfig().getString("discord.roles.addCMD");
		cmd = cmd.replaceAll("%player%", player.getName()).replaceAll("%group%", getConfig().getString("discord.roles."+role.getId()));
		getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
	}
	
	public void removeRole(@NotNull OfflinePlayer player, Role role) {
		String cmd = getConfig().getString("discord.roles.removeCMD");
		cmd = cmd.replaceAll("%player%", player.getName()).replaceAll("%group%", getConfig().getString("discord.roles."+role.getId()));
		getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
	}
	
	public String getPrefix() {
		return getConfig().getString("prefix");
	}
}
