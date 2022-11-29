package org.cubeville.cvscoreboards.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cubeville.commons.commands.*;
import org.cubeville.cvscoreboards.CVScoreboards;
import org.cubeville.cvscoreboards.scoreboard.ScoreboardManager;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScoreboardHide extends BaseCommand {

    private final CVScoreboards plugin;
    private final ScoreboardManager scoreboardManager;

    public ScoreboardHide(CVScoreboards cvScoreboards, ScoreboardManager scoreboardManager) {
        super("hide");
        addParameter("player", true, new CommandParameterString());

        this.plugin = cvScoreboards;
        this.scoreboardManager = scoreboardManager;
    }

    public CommandResponse execute(CommandSender sender, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters) throws CommandExecutionException {
        if(parameters.containsKey("player")) {
            String player = (String) parameters.get("player");
            Player p = Bukkit.getPlayer(player);
            if(p == null) {
                throw new CommandExecutionException(ChatColor.GOLD + player + ChatColor.RED + " is not online!");
            }
            this.scoreboardManager.hideScoreboard(p);
            return new CommandResponse(ChatColor.GREEN + "Scoreboard hidden for " + ChatColor.GOLD + player);
        }
        if(!(sender instanceof Player)) {
            return new CommandResponse(ChatColor.RED + "You cannot hide a scoreboard from console!");
        }
        this.scoreboardManager.hideScoreboard((Player) sender);
        return new CommandResponse(ChatColor.GREEN + "Scoreboard hidden");
    }
}
