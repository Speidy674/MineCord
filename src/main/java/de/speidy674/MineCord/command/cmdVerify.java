package de.speidy674.MineCord.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.speidy674.MineCord.Main;
import de.speidy674.MineCord.config.PlayerCFG;
import net.dv8tion.jda.api.JDA;

public class cmdVerify implements CommandExecutor {

	Main plugin;
	JDA jda;

	public cmdVerify(Main plugin, JDA jda) {
		this.plugin = plugin;
		this.jda = jda;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String arg1, String[] args) {

		Player player;
		if (sender instanceof Player) {
			player = (Player) sender;
		} else {
			sender.sendMessage(plugin.getPrefix()+Main.messages.getString("MC.PlayerOnly"));
			return true;
		}
		
		PlayerCFG pcfg = new PlayerCFG(plugin, player);
		
		if (pcfg.isVerifyed()) {
			sender.sendMessage(plugin.getPrefix()+Main.messages.getString("MC.VerifedAlready"));
		}else {
			if(args.length == 1) {
				if(pcfg.verify(args[0])) {
					sender.sendMessage(plugin.getPrefix()+Main.messages.getString("MC.VerifedSuccses"));
					plugin.SyncPlayerRoles(player, pcfg.getDiscordID());
				}else {
					sender.sendMessage(plugin.getPrefix()+Main.messages.getString("MC.VerifedWrongToken"));
				}
			}else {
				sender.sendMessage(plugin.getPrefix()+Main.messages.getString("MC.VerifedUsage"));
			}
		}
		
		return true;
	}


}
