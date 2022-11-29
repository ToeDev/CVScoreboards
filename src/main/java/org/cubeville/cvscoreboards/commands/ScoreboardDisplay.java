package org.cubeville.cvscoreboards.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cubeville.commons.commands.*;
import org.cubeville.cvscoreboards.CVScoreboards;
import org.cubeville.cvscoreboards.scoreboard.ScoreboardContainer;
import org.cubeville.cvscoreboards.scoreboard.ScoreboardManager;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScoreboardDisplay extends BaseCommand {

    private final CVScoreboards plugin;
    private final ScoreboardManager scoreboardManager;

    public ScoreboardDisplay(CVScoreboards cvScoreboards, ScoreboardManager scoreboardManager) {
        super("display");
        addBaseParameter(new CommandParameterString());
        addParameter("player", true, new CommandParameterString());
        addParameter("duration", true, new CommandParameterInteger());

        this.plugin = cvScoreboards;
        this.scoreboardManager = scoreboardManager;
    }

    public CommandResponse execute(CommandSender sender, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters) throws CommandExecutionException {
        String name = ChatColor.translateAlternateColorCodes('&', (String) baseParameters.get(0));
        if(!this.scoreboardManager.scoreboardExists(name)) {
            throw new CommandExecutionException(ChatColor.RED + "Scoreboard " + ChatColor.GOLD + name + ChatColor.RED + " doesn't exist!");
        }
        ScoreboardContainer scoreboard = this.scoreboardManager.getScoreboard(name);
        if(scoreboard.getScoreboardRows().isEmpty()) {
            throw new CommandExecutionException(ChatColor.RED + "Scoreboard " + ChatColor.GOLD + scoreboard.getScoreboardTitleWithColors() + ChatColor.RED + " doesn't contain any rows!");
        }
        if(parameters.containsKey("player")) {
            String player = (String) parameters.get("player");
            Player p = Bukkit.getPlayer(player);
            if(p == null) {
                throw new CommandExecutionException(ChatColor.GOLD + player + ChatColor.RED + " is not online!");
            }
            if(parameters.containsKey("duration")) {
                long duration = (long) parameters.get("duration");
                this.scoreboardManager.showScoreboard(name, p);
                Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.scoreboardManager.hideScoreboard(p), duration * 20);
                return new CommandResponse(ChatColor.GREEN + "Scoreboard " + ChatColor.GOLD + scoreboard.getScoreboardTitleWithColors() + ChatColor.GREEN +
                        " opened for " + ChatColor.GOLD + player + ChatColor.GREEN + " for " +  ChatColor.GOLD + duration + ChatColor.GREEN + " seconds");
            }
            this.scoreboardManager.showScoreboard(name, p);
            return new CommandResponse(ChatColor.GREEN + "Scoreboard " + ChatColor.GOLD + scoreboard.getScoreboardTitleWithColors() + ChatColor.GREEN +
                    " opened for " + ChatColor.GOLD + player);
        }
        if(!(sender instanceof Player)) {
            return new CommandResponse(ChatColor.RED + "You cannot open a scoreboard from console!");
        }
        if(parameters.containsKey("duration")) {
            Integer duration = (Integer) parameters.get("duration");
            this.scoreboardManager.showScoreboard(name, (Player) sender);
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.scoreboardManager.hideScoreboard((Player) sender), duration * 20L);
            return new CommandResponse(ChatColor.GREEN + "Scoreboard " + ChatColor.GOLD + scoreboard.getScoreboardTitleWithColors() + ChatColor.GREEN +
                    " opened for " + ChatColor.GOLD + duration + ChatColor.GREEN + " seconds");
        }
        this.scoreboardManager.showScoreboard(name, (Player) sender);
        return new CommandResponse(ChatColor.GREEN + "Scoreboard " + ChatColor.GOLD + scoreboard.getScoreboardTitleWithColors() + ChatColor.GREEN + " opened");
    }
}
