package org.cubeville.cvscoreboards.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cubeville.commons.commands.BaseCommand;
import org.cubeville.commons.commands.CommandResponse;
import org.cubeville.cvscoreboards.scoreboard.ScoreboardManager;

import java.util.*;

public class ScoreboardList extends BaseCommand {

    private final ScoreboardManager scoreboardManager;

    public ScoreboardList(ScoreboardManager scoreboardManager) {
        super("list");

        this.scoreboardManager = scoreboardManager;
    }

    @Override
    public CommandResponse execute(CommandSender sender, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters) {
        List<String> scoreboards = this.scoreboardManager.getAllScoreboardNames();
        Collections.sort(scoreboards);
        if(sender instanceof Player) {
            List<TextComponent> out = new ArrayList<>();
            out.add(new TextComponent(ChatColor.DARK_GREEN + "------------------" + ChatColor.GREEN + "Scoreboards" + ChatColor.DARK_GREEN + "------------------"));
            TextComponent scoreboardList = new TextComponent("");
            int i = scoreboards.size();
            for (String scoreboard : scoreboards) {
                TextComponent s = new TextComponent(scoreboard);
                s.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cvscoreboards display " + "\"" + ChatColor.stripColor(scoreboard) + "\"" + " duration:5"));
                s.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Display " + scoreboard)));
                i--;
                if(i >= 1) s.addExtra(ChatColor.BLUE + " || ");
                scoreboardList.addExtra(s);
            }
            out.add(scoreboardList);
            for(TextComponent o : out) {
                sender.spigot().sendMessage(o);
            }
        } else {
            List<String> out = new ArrayList<>();
            out.add(ChatColor.DARK_GREEN + "------------------" + ChatColor.GREEN + "Scoreboards" + ChatColor.DARK_GREEN + "------------------");
            String list = "";
            int i = scoreboards.size();
            for(String scoreboard : scoreboards) {
                i--;
                if(i >= 1) scoreboard = scoreboard.concat(ChatColor.BLUE + " || ");
                list = list.concat(ChatColor.GOLD + scoreboard);
            }
            out.add(list);
            for(String o : out) {
                sender.sendMessage(o);
            }
        }
        return new CommandResponse("");
    }
}
