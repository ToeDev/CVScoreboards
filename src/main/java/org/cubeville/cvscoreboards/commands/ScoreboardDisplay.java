package org.cubeville.cvscoreboards.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
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
        String name = (String) baseParameters.get(0);
        if(!this.scoreboardManager.scoreboardExists(name)) {
            throw new CommandExecutionException(ChatColor.RED + "Scoreboard " + ChatColor.GOLD + name + ChatColor.RED + " doesn't exist!");
        }
        ScoreboardContainer scoreboard = this.scoreboardManager.getScoreboard(name);
        if(parameters.containsKey("player")) {
            String player = (String) parameters.get("player");
            Player p = Bukkit.getPlayer(player);
            if(p == null) {
                throw new CommandExecutionException(ChatColor.GOLD + player + ChatColor.RED + " is not online!");
            }
            if(parameters.containsKey("duration")) {
                long duration = (long) parameters.get("duration");
                //TODO show scoreboard
                Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                    //todo remove scoreboard
                }, duration * 20);
                return new CommandResponse(ChatColor.GREEN + "Scoreboard " + ChatColor.GOLD + name + ChatColor.GREEN + " opened for " + ChatColor.GOLD + player +
                        ChatColor.GREEN + " for " +  ChatColor.GOLD + duration + ChatColor.GREEN + " seconds");
            }
            //TODO show scoreboard
            return new CommandResponse(ChatColor.GREEN + "Scoreboard " + ChatColor.GOLD + name + ChatColor.GREEN + " opened for " + ChatColor.GOLD + player);
        }
        if(!(sender instanceof Player)) {
            return new CommandResponse(ChatColor.RED + "You cannot open a scoreboard from console!");
        }
        if(parameters.containsKey("duration")) {
            Integer duration = (Integer) parameters.get("duration");
            //TODO show scoreboard
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                //todo remove scoreboard
            }, duration * 20L);
            return new CommandResponse(ChatColor.GREEN + "Scoreboard " + ChatColor.GOLD + name + ChatColor.GREEN + " opened for " +
                    ChatColor.GOLD + duration + ChatColor.GREEN + " seconds");
        }
        //TODO show scoreboard
        org.bukkit.scoreboard.ScoreboardManager sMan = Bukkit.getScoreboardManager();
        Scoreboard s = sMan.getNewScoreboard();
        for(Integer slot : scoreboard.getScoreboardRows().keySet()) {
            Objective o = s.registerNewObjective("test", "dummy");
            //o.setDisplayName(scoreboard.getScoreboardTitle());
            o.setDisplaySlot(DisplaySlot.SIDEBAR);
            Team t = s.registerNewTeam(scoreboard.getScoreboardRows().get(slot) + scoreboard.getScoreboardTitle());
            t.addEntry("");
            t.setPrefix(scoreboard.getScoreboardRows().get(slot));
            Score score = o.getScore(scoreboard.getScoreboardRows().get(slot) + scoreboard.getScoreboardTitle());
            score.setScore(slot);
        }
        ((Player) sender).setScoreboard(s);
        return new CommandResponse(ChatColor.GREEN + "Scoreboard " + ChatColor.GOLD + name + ChatColor.GREEN + " opened");
    }
}
