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

public class ScoreboardCreate extends BaseCommand {

    private final CVScoreboards plugin;
    private final ScoreboardManager scoreboardManager;

    public ScoreboardCreate(CVScoreboards cvScoreboards, ScoreboardManager scoreboardManager) {
        super("create");
        addBaseParameter(new CommandParameterString());

        this.plugin = cvScoreboards;
        this.scoreboardManager = scoreboardManager;
    }

    @Override
    public CommandResponse execute(CommandSender sender, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters) throws CommandExecutionException {
        String name = ChatColor.translateAlternateColorCodes('&', (String) baseParameters.get(0));
        if(!this.scoreboardManager.scoreboardExists(name)) {
            this.scoreboardManager.createScoreboard(name);
            this.plugin.saveScoreboardManager();
            return new CommandResponse(ChatColor.GREEN + "Scoreboard " + ChatColor.GOLD + name + ChatColor.GREEN + " created!");
        } else {
            throw new CommandExecutionException(ChatColor.RED + "Scoreboard " + ChatColor.GOLD + name + ChatColor.RED + " already exists!");
        }
    }
}
