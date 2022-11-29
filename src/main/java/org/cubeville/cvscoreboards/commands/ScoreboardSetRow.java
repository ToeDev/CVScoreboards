package org.cubeville.cvscoreboards.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.cubeville.commons.commands.*;
import org.cubeville.cvscoreboards.CVScoreboards;
import org.cubeville.cvscoreboards.scoreboard.ScoreboardManager;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScoreboardSetRow extends Command {

    private final CVScoreboards plugin;
    private final ScoreboardManager scoreboardManager;

    public ScoreboardSetRow(CVScoreboards cvScoreboards, ScoreboardManager scoreboardManager) {
        super("setrow");
        addBaseParameter(new CommandParameterString());
        addBaseParameter(new CommandParameterInteger());
        addBaseParameter(new CommandParameterString());

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
