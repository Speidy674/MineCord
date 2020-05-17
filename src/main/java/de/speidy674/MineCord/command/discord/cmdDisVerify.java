package de.speidy674.MineCord.command.discord;

import java.awt.Color;
import java.util.Random;

import org.bukkit.entity.Player;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import de.speidy674.MineCord.Main;
import de.speidy674.MineCord.config.PlayerCFG;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

public class cmdDisVerify extends Command {

	Main plugin;

	public cmdDisVerify(Main plugin) {
		this.plugin = plugin;
		this.name = "verify";
		this.aliases = new String[] {};
		this.help = "verify minecraft and discord";
		this.botPermissions = new Permission[] { Permission.MESSAGE_EMBED_LINKS };
		this.guildOnly = false;
		this.category = new Command.Category("General");
	}

	@Override
	protected void execute(CommandEvent event) {
		String token = "";

		if (event.getArgs().equals("")) {
			event.reply(new EmbedBuilder().setColor(Color.RED).setDescription(Main.messages.getString("DIS.MCName")).build());
			event.reactError();
			return;
		}

		Player player = plugin.getServer().getPlayer(event.getArgs());
		if (!plugin.getServer().getOnlinePlayers().contains(player)) {
			event.reply(new EmbedBuilder().setColor(Color.RED).setDescription(Main.messages.getString("DIS.NeedOnline")).build());
			event.reactError();
			return;
		} else {

			PlayerCFG pcfg = new PlayerCFG(plugin, player);

			token = pcfg.getToken();

			if (token.equals("")) {
				token = genToken();
				pcfg.setToken(token);
				pcfg.setDiscordID(event.getAuthor().getId());
			}

			EmbedBuilder builder = new EmbedBuilder();
			builder.setColor(Color.ORANGE);
			builder.setTitle(Main.messages.getString("DIS.VerifycationTitle"));
			builder.setDescription(Main.messages.getString("DIS.VerifycationText").replaceAll("%token%", token));

			event.replyInDm(builder.build());
			event.reactSuccess();

		}

	}

	public String genToken() {
		int leftLimit = 48; // numeral '0'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 25;
		Random random = new Random();

		String generatedString = random.ints(leftLimit, rightLimit + 1)
				.filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)).limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

		return generatedString;
	}

}
