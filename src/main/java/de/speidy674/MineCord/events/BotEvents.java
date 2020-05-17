package de.speidy674.MineCord.events;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.speidy674.MineCord.Main;
import de.speidy674.MineCord.config.PlayerCFG;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BotEvents extends ListenerAdapter {

	Main plugin;

	public BotEvents(Main plugin) {
		this.plugin = plugin;

	}

	@Override
	public void onReady(ReadyEvent event) {

		if (event.getGuildTotalCount() == 0) {
			plugin.tellConsole("the Bot need to be on a Discord Server");
			plugin.getPluginLoader().disablePlugin(plugin);
		} else if (event.getGuildTotalCount() == 1) {
			// working :D
		} else {
			plugin.tellConsole("the Bot need to be on only 1 Discord Server");
			plugin.getPluginLoader().disablePlugin(plugin);
		}
	}

	@Override
	public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
		Member mebmer = event.getMember();
		PlayerCFG pcfg = new PlayerCFG(plugin, mebmer);
		Player player = plugin.getServer().getPlayer(UUID.fromString(pcfg.getUUID()));
		if (pcfg.isVerifyed()) {
			for (Role role : event.getRoles()) {
				if (plugin.getConfig().contains("discord.roles." + role.getId())) {
					new BukkitRunnable() {
						
						@Override
						public void run() {
							plugin.addRole(player, role);
							
						}
					}.runTask(plugin);
				}
			}
		}
	}

	@Override
	public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
		Member mebmer = event.getMember();
		PlayerCFG pcfg = new PlayerCFG(plugin, mebmer);
		Player player = plugin.getServer().getPlayer(UUID.fromString(pcfg.getUUID()));
		if (pcfg.isVerifyed()) {
			for (Role role : event.getRoles()) {
				if (plugin.getConfig().contains("discord.roles." + role.getId())) {
					new BukkitRunnable() {
						
						@Override
						public void run() {
							plugin.removeRole(player, role);
							
						}
					}.runTask(plugin);
				}
			}
		}
	}

}
