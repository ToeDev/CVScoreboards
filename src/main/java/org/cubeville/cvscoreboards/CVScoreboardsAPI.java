package org.cubeville.cvscoreboards;

import org.bukkit.entity.Player;
import org.cubeville.cvscoreboards.scoreboard.ScoreboardManager;

public class CVScoreboardsAPI {

    public ScoreboardManager scoreboardManager;

    public CVScoreboardsAPI(ScoreboardManager scoreboardManager) {
        this.scoreboardManager = scoreboardManager;
    }

    public boolean doesScoreboardExist(String name) {
        return this.scoreboardManager.scoreboardExists(name);
    }

    public void showPlayerScoreboard(String name, Player player) {
        this.scoreboardManager.showScoreboard(name, player);
    }

    public void hidePlayerScoreboard(Player player) {
        this.scoreboardManager.hideScoreboard(player);
    }
}
