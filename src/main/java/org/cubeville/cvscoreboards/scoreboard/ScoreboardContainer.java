package org.cubeville.cvscoreboards.scoreboard;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@SerializableAs("ScoreboardContainer")
public class ScoreboardContainer implements ConfigurationSerializable {

    private String scoreboardTitle;
    private TreeMap<Integer, String> scoreboardRows;

    @SuppressWarnings("unchecked")
    public ScoreboardContainer(Map<String, Object> config) {
        this.scoreboardTitle = (String) config.get("title");
        this.scoreboardRows = new TreeMap<>((Map<Integer, String>) config.get("rows"));
    }

    public Map<String, Object> serialize() {
        Map<String, Object> ret = new HashMap<>();
        ret.put("title", this.scoreboardTitle);
        ret.put("rows", this.scoreboardRows);
        return ret;
    }

    public ScoreboardContainer(String title, TreeMap<Integer, String> rows) {
        this.scoreboardTitle = title;
        this.scoreboardRows = rows;
    }

    public String getScoreboardTitle() {
        return this.scoreboardTitle;
    }

    public TreeMap<Integer, String> getScoreboardRows() {
        return this.scoreboardRows;
    }

    public void setScoreboardTitle(String title) {
        this.scoreboardTitle = title;
    }

    public void setScoreboardRows(TreeMap<Integer, String> rows) {
        this.scoreboardRows = rows;
    }
}
