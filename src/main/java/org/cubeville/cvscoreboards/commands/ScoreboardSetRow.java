package org.cubeville.cvscoreboards.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.cubeville.commons.commands.*;
import org.cubeville.cvscoreboards.CVScoreboards;
import org.cubeville.cvscoreboards.scoreboard.ScoreboardContainer;
import org.cubeville.cvscoreboards.scoreboard.ScoreboardManager;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ScoreboardSetRow extends Command {

    private final CVScoreboards plugin;
    private final ScoreboardManager scoreboardManager;

    public ScoreboardSetRow(CVScoreboards cvScoreboards, ScoreboardManager scoreboardManager) {
        super("setrow");
        addBaseParameter(new CommandParameterString());
        addBaseParameter(new CommandParameterInteger());
        addBaseParameter(new CommandParameterString());
        addParameter("player", true, new CommandParameterString());

        this.plugin = cvScoreboards;
        this.scoreboardManager = scoreboardManager;
    }

    @Override
    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters) throws CommandExecutionException {
        String name = ChatColor.translateAlternateColorCodes('&', (String) baseParameters.get(0));
        Integer slot = (Integer) baseParameters.get(1);
        String row = ChatColor.translateAlternateColorCodes('&', ((String) baseParameters.get(2)));
        if(this.scoreboardManager.scoreboardExists(name)) {
            if(this.scoreboardManager.getScoreboard(name).getScoreboardRows().get(slot) != null) {
                if(this.scoreboardManager.getScoreboard(name).getScoreboardRows().containsValue(row)) {
                    throw new CommandExecutionException(ChatColor.RED + "Scoreboard " + ChatColor.GOLD + name + ChatColor.RED + " already has a row with this text!");
                }
                if(parameters.containsKey("player")) {
                    Player p = Bukkit.getPlayer((String) parameters.get("player"));
                    if(p != null) {
                        if(this.scoreboardManager.getPlayerScoreboard(p) != null) {
                            ScoreboardContainer sC = this.scoreboardManager.getPlayerScoreboard(p);
                            TreeMap<Integer, String> rows = sC.getScoreboardRows();
                            rows.put(slot, row);
                            sC.setScoreboardRows(rows);
                            sC.setPlayerSpecific(true);
                            this.scoreboardManager.setPlayerScoreboard(sC, player);
                            this.scoreboardManager.showPlayerSpecificScoreboard(player);
                            return new CommandResponse(ChatColor.GREEN + "Scoreboard row added to " + ChatColor.GOLD +
                                    this.scoreboardManager.getScoreboard(name).getScoreboardTitleWithColors() + ChatColor.GREEN + " for player " +
                                    ChatColor.GOLD + p.getName() + ChatColor.GREEN + " only!");
                        }
                        throw new CommandExecutionException(ChatColor.RED + "Player " + ChatColor.GOLD + parameters.get("player") + ChatColor.RED +
                                " does not currently have a sidebar displayed to them!");
                    }
                    throw new CommandExecutionException(ChatColor.RED + "Player " + ChatColor.GOLD + parameters.get("player") + ChatColor.RED + " is not online!");
                }
                this.scoreboardManager.setScoreboardRow(name, slot, ChatColor.translateAlternateColorCodes('&', row));
                this.plugin.saveScoreboardManager();
                return new CommandResponse(ChatColor.GREEN + "Scoreboard row added to " + ChatColor.GOLD +
                        this.scoreboardManager.getScoreboard(name).getScoreboardTitleWithColors() + ChatColor.GREEN + "!");
            } else {
                throw new CommandExecutionException(ChatColor.RED + "Scoreboard row doesn't exist in " + ChatColor.GOLD +
                        this.scoreboardManager.getScoreboard(name).getScoreboardTitleWithColors() + ChatColor.RED + "!");
            }
        } else {
            throw new CommandExecutionException(ChatColor.RED + "Scoreboard " + ChatColor.GOLD + name + ChatColor.RED + " doesn't exist!");
        }
    }
}
