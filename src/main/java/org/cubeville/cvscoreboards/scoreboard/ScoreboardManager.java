package org.cubeville.cvscoreboards.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.cubeville.cvscoreboards.CVScoreboards;

import java.util.*;

@SerializableAs("ScoreboardManager")
public class ScoreboardManager implements ConfigurationSerializable {

    Map<String, ScoreboardContainer> scoreboards;
    Map<UUID, ScoreboardContainer> playerScoreboards;

    CVScoreboards plugin;

    @SuppressWarnings("unchecked")
    public ScoreboardManager(Map<String, Object> config) {
        scoreboards = (Map<String, ScoreboardContainer>) config.get("scoreboards");
        playerScoreboards = new HashMap<>();
    }

    public Map<String, Object> serialize() {
        Map<String, Object> ret = new HashMap<>();
        ret.put("scoreboards", this.scoreboards);
        return ret;
    }

    public ScoreboardManager() {
        this.scoreboards = new HashMap<>();
        this.playerScoreboards = new HashMap<>();
    }

    public void setScoreboardManager(CVScoreboards plugin) {
        this.plugin = plugin;
    }

    public void createScoreboard(String name) {
        this.scoreboards.put(name, new ScoreboardContainer(name, new TreeMap<>()));
    }

    public void removeScoreboard(String name) {
        if(this.scoreboards.containsKey(name)) {
            this.scoreboards.remove(name);
        } else {
            for(ScoreboardContainer s : this.scoreboards.values()) {
                if(s.getScoreboardTitleWithoutColors().equals(ChatColor.stripColor(name))) {
                    this.scoreboards.remove(s.getScoreboardTitleWithColors());
                    return;
                }
            }
        }
    }

    public boolean scoreboardExists(String name) {
        if(this.scoreboards.containsKey(name)) return true;
        for(ScoreboardContainer s : this.scoreboards.values()) {
            System.out.println(s.getScoreboardTitleWithoutColors());
            System.out.println(name);
            if(s.getScoreboardTitleWithoutColors().equals(ChatColor.stripColor(name))) return true;
        }
        return false;
    }

    public ScoreboardContainer getScoreboard(String name) {
        if(this.scoreboards.get(name) != null) {
            return this.scoreboards.get(name);
        } else {
            for(ScoreboardContainer s : this.scoreboards.values()) {
                if(s.getScoreboardTitleWithoutColors().equals(ChatColor.stripColor(name))) {
                    return s;
                }
            }
        }
        return null;
    }

    public List<String> getAllScoreboardNames() {
        return new ArrayList<>(this.scoreboards.keySet());
    }

    public void setScoreboardTitle(String name, String title) { //TODO there may be problems?
        ScoreboardContainer c = this.scoreboards.get(name);
        if(c == null) {
            for(ScoreboardContainer s : this.scoreboards.values()) {
                if(s.getScoreboardTitleWithoutColors().equals(ChatColor.stripColor(name))) {
                    c = s;
                }
            }
        }
        c.setScoreboardTitle(title);
        this.scoreboards.put(c.getScoreboardTitleWithColors(), c);
    }

    public void addScoreboardRow(String name, String row) {
        ScoreboardContainer c = this.scoreboards.get(name);
        if(c == null) {
            for(ScoreboardContainer s : this.scoreboards.values()) {
                if(s.getScoreboardTitleWithoutColors().equals(ChatColor.stripColor(name))) {
                    c = s;
                }
            }
        }
        if(c != null) {
            TreeMap<Integer, String> r = c.getScoreboardRows();
            r.put(r.size(), row);
            c.setScoreboardRows(r);
            this.scoreboards.put(c.getScoreboardTitleWithColors(), c);
            this.updateAllOnlinePlayerScoreboards(c);
        }
    }

    public void setScoreboardRow(String name, Integer slot, String row) {
        ScoreboardContainer c = this.scoreboards.get(name);
        if(c == null) {
            for(ScoreboardContainer s : this.scoreboards.values()) {
                if(s.getScoreboardTitleWithoutColors().equals(ChatColor.stripColor(name))) {
                    c = s;
                }
            }
        }
        if(c != null) {
            TreeMap<Integer, String> r = c.getScoreboardRows();
            r.put(slot, row);
            c.setScoreboardRows(r);
            this.scoreboards.put(c.getScoreboardTitleWithColors(), c);
            this.updateAllOnlinePlayerScoreboards(c);
        }
    }

    public void removeScoreboardRow(String name, Integer slot) {
        ScoreboardContainer c = this.scoreboards.get(name);
        if(c == null) {
            for(ScoreboardContainer s : this.scoreboards.values()) {
                if(s.getScoreboardTitleWithoutColors().equals(ChatColor.stripColor(name))) {
                    c = s;
                }
            }
        }
        if(c != null) {
            TreeMap<Integer, String> r = c.getScoreboardRows();
            r.remove(slot);
            TreeMap<Integer, String> newR = new TreeMap<>();
            int i = 0;
            for(String row : r.values()) {
                newR.put(i, row);
                i++;
            }
            c.setScoreboardRows(newR);
            this.scoreboards.put(c.getScoreboardTitleWithColors(), c);
            this.updateAllOnlinePlayerScoreboards(c);
        }
    }

    public void updateAllOnlinePlayerScoreboards(ScoreboardContainer c) {
        Set<UUID> uuids = this.playerScoreboards.keySet();
        for(UUID uuid : uuids) {
            Player p = Bukkit.getPlayer(uuid);
            String s = this.playerScoreboards.get(uuid).getScoreboardTitleWithoutColors();
            if(p != null && s.equals(c.getScoreboardTitleWithoutColors())) {
                this.playerScoreboards.put(uuid, c);
                if(!this.getScoreboard(s).getScoreboardRows().isEmpty()) {
                    this.showScoreboard(s, p);
                }
            }
        }
    }

    public Map<UUID, ScoreboardContainer> getAllPlayerScoreboards() {
        return this.playerScoreboards;
    }

    public ScoreboardContainer getPlayerScoreboard(Player player) {
        return this.playerScoreboards.get(player.getUniqueId());
    }

    public void setPlayerScoreboard(ScoreboardContainer scoreboard, Player player) {
        this.playerScoreboards.put(player.getUniqueId(), scoreboard);
    }

    public void removePlayerScoreboard(Player player) {
        this.playerScoreboards.remove(player.getUniqueId());
    }

    public void showScoreboard(String name, Player player) {
        ScoreboardContainer sC = this.scoreboards.get(name);
        if(sC == null) {
            for(ScoreboardContainer s : this.scoreboards.values()) {
                if(s.getScoreboardTitleWithoutColors().equals(ChatColor.stripColor(name))) {
                    sC = s;
                }
            }
        }
        if(sC != null) {
            org.bukkit.scoreboard.ScoreboardManager sMan = Bukkit.getScoreboardManager();
            Scoreboard s = sMan.getNewScoreboard();
            Objective o = s.registerNewObjective("sidebar", "", sC.getScoreboardTitleWithColors());
            o.setDisplaySlot(DisplaySlot.SIDEBAR);
            o.setDisplayName(sC.getScoreboardTitleWithColors());
            for(Integer slot : sC.getScoreboardRows().keySet()) {
                Objective oL = s.getObjective("sidebar");
                String row = sC.getScoreboardRows().get(slot);
                row = plugin.parsePAPI(player, row);
                oL.getScore(row).setScore(slot);
            }
            player.setScoreboard(s);
            this.setPlayerScoreboard(sC, player);
        }
    }

    public void hideScoreboard(Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        this.removePlayerScoreboard(player);
    }
}
