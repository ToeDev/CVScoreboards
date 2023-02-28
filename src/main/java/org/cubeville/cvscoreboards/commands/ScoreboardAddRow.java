package org.cubeville.cvscoreboards.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.cubeville.commons.commands.BaseCommand;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterString;
import org.cubeville.commons.commands.CommandResponse;
import org.cubeville.cvscoreboards.CVScoreboards;
import org.cubeville.cvscoreboards.scoreboard.ScoreboardManager;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScoreboardAddRow extends BaseCommand {

    private final CVScoreboards plugin;
    private final ScoreboardManager scoreboardManager;

    public ScoreboardAddRow(CVScoreboards cvScoreboards, ScoreboardManager scoreboardManager) {
        super("addrow");
        addBaseParameter(new CommandParameterString());
        addBaseParameter(new CommandParameterString());

        this.plugin = cvScoreboards;
        this.scoreboardManager = scoreboardManager;
    }

    @Override
    public CommandResponse execute(CommandSender sender, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters) throws CommandExecutionException {
        String name = ChatColor.translateAlternateColorCodes('&', (String) baseParameters.get(0));
        String row = ChatColor.translateAlternateColorCodes('&', ((String) baseParameters.get(1)));
        if(this.scoreboardManager.scoreboardExists(name)) {
            if(this.scoreboardManager.getScoreboard(name).getScoreboardRows().containsValue(row)) {
                throw new CommandExecutionException(ChatColor.RED + "Scoreboard " + ChatColor.GOLD + name + ChatColor.RED + " already has a row with this text!");
            }
            this.scoreboardManager.addScoreboardRow(name, ChatColor.translateAlternateColorCodes('&', row));
            this.plugin.saveScoreboardManager();
            return new CommandResponse(ChatColor.GREEN + "Scoreboard row added to " + ChatColor.GOLD +
                    this.scoreboardManager.getScoreboard(name).getScoreboardTitleWithColors() + ChatColor.GREEN + "!");
        } else {
            throw new CommandExecutionException(ChatColor.RED + "Scoreboard " + ChatColor.GOLD + name + ChatColor.RED + " doesn't exist!");
        }
    }
}
