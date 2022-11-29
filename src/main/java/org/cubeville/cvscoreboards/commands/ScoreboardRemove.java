package org.cubeville.cvscoreboards.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.cubeville.commons.commands.Command;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterString;
import org.cubeville.commons.commands.CommandResponse;
import org.cubeville.cvscoreboards.CVScoreboards;
import org.cubeville.cvscoreboards.scoreboard.ScoreboardContainer;
import org.cubeville.cvscoreboards.scoreboard.ScoreboardManager;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScoreboardRemove extends Command {

    private final CVScoreboards plugin;
    private final ScoreboardManager scoreboardManager;

    public ScoreboardRemove(CVScoreboards cvScoreboards, ScoreboardManager scoreboardManager) {
        super("remove");
        addBaseParameter(new CommandParameterString());

        this.plugin = cvScoreboards;
        this.scoreboardManager = scoreboardManager;
    }

    @Override
    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters) throws CommandExecutionException {
        String name = ChatColor.translateAlternateColorCodes('&', (String) baseParameters.get(0));
        if(this.scoreboardManager.scoreboardExists(name)) {
            ScoreboardContainer scoreboard = this.scoreboardManager.getScoreboard(name);
            this.scoreboardManager.removeScoreboard(name);
            this.plugin.saveScoreboardManager();
            return new CommandResponse(ChatColor.GREEN + "Scoreboard " + ChatColor.GOLD + scoreboard.getScoreboardTitleWithColors() + ChatColor.GREEN + " removed!");
        } else {
            throw new CommandExecutionException(ChatColor.RED + "Scoreboard " + ChatColor.GOLD + baseParameters.get(0) + ChatColor.RED + " doesn't exist!");
        }
    }
}
