package org.cubeville.cvscoreboards.scoreboard;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.cubeville.cvscoreboards.CVScoreboards;

import java.util.*;

@SerializableAs("ScoreboardManager")
public class ScoreboardManager implements ConfigurationSerializable {

    Map<String, ScoreboardContainer> scoreboards;

    CVScoreboards plugin;

    @SuppressWarnings("unchecked")
    public ScoreboardManager(Map<String, Object> config) {
        scoreboards = (Map<String, ScoreboardContainer>) config.get("scoreboards");
    }

    public Map<String, Object> serialize() {
        Map<String, Object> ret = new HashMap<>();
        ret.put("scoreboards", this.scoreboards);
        return ret;
    }

    public ScoreboardManager() {
        this.scoreboards = new HashMap<>();
    }

    public void setScoreboardManager(CVScoreboards plugin) {
        this.plugin = plugin;
    }

    public void createScoreboard(String name) {
        this.scoreboards.put(name, new ScoreboardContainer(name, new TreeMap<>()));
    }

    public void removeScoreboard(String name) {
        this.scoreboards.remove(name);
    }

    public boolean scoreboardExists(String name) {
        return this.scoreboards.containsKey(name);
    }

    public ScoreboardContainer getScoreboard(String name) {
        if(scoreboardExists(name)) {
            return this.scoreboards.get(name);
        }
        return null;
    }

    public List<String> getAllScoreboardNames() {
        return new ArrayList<>(this.scoreboards.keySet());
    }

    public void setScoreboardTitle(String name, String title) {
        ScoreboardContainer c = this.scoreboards.get(name);
        c.setScoreboardTitle(title);
        this.scoreboards.put(name, c);
    }

    public void addScoreboardRow(String name, String row) {
        ScoreboardContainer c = this.scoreboards.get(name);
        TreeMap<Integer, String> r = c.getScoreboardRows();
        r.put(r.size(), row);
        c.setScoreboardRows(r);
        this.scoreboards.put(name, c);
    }

    public void setScoreboardRow(String name, Integer slot, String row) {
        ScoreboardContainer c = this.scoreboards.get(name);
        TreeMap<Integer, String> r = c.getScoreboardRows();
        r.put(slot, row);
        c.setScoreboardRows(r);
        this.scoreboards.put(name, c);
    }

    public void removeScoreboardRow(String name, Integer slot) {
        ScoreboardContainer c = this.scoreboards.get(name);
        TreeMap<Integer, String> r = c.getScoreboardRows();
        r.remove(slot);
        TreeMap<Integer, String> newR = new TreeMap<>();
        int i = 0;
        for(String row : r.values()) {
            newR.put(i, row);
            i++;
        }
        c.setScoreboardRows(newR);
        this.scoreboards.put(name, c);
    }
}
