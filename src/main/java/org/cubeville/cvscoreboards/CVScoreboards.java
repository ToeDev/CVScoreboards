package org.cubeville.cvscoreboards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.cubeville.commons.commands.CommandParser;
import org.cubeville.cvscoreboards.commands.*;
import org.cubeville.cvscoreboards.scoreboard.ScoreboardContainer;
import org.cubeville.cvscoreboards.scoreboard.ScoreboardManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CVScoreboards extends JavaPlugin implements Listener {

    private static CVScoreboards cvScoreboards;
    private PAPIHandler papiHandler;

    private ScoreboardManager scoreboardManager;
    private CommandParser commandParser;
    private Logger logger;

    public void onEnable() {
        this.logger = this.getLogger();
        cvScoreboards = this;

        final File dataDir = getDataFolder();
        if(!dataDir.exists()) {
            dataDir.mkdirs();
        }
        File configFile = new File(dataDir, "config.yml");
        if(!configFile.exists()) {
            try {
                configFile.createNewFile();
                final InputStream inputStream = this.getResource(configFile.getName());
                final FileOutputStream fileOutputStream = new FileOutputStream(configFile);
                final byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = Objects.requireNonNull(inputStream).read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch(IOException e) {
                logger.log(Level.WARNING, ChatColor.LIGHT_PURPLE + "Unable to generate config file", e);
                throw new RuntimeException(ChatColor.LIGHT_PURPLE + "Unable to generate config file", e);
            }
        }

        ConfigurationSerialization.registerClass(ScoreboardContainer.class, "ScoreboardContainer");
        ConfigurationSerialization.registerClass(ScoreboardManager.class, "ScoreboarcManager");

        papiHandler = new PAPIHandler();
        this.scoreboardManager = (ScoreboardManager) getConfig().get("ScoreboardManager");
        if(this.scoreboardManager == null) this.scoreboardManager = new ScoreboardManager();
        this.scoreboardManager.setScoreboardManager(this);

        this.commandParser = new CommandParser();
        this.commandParser.addCommand(new ScoreboardCreate(this, this.scoreboardManager));
        this.commandParser.addCommand(new ScoreboardRemove(this, this.scoreboardManager));
        this.commandParser.addCommand(new ScoreboardAddRow(this, this.scoreboardManager));
        this.commandParser.addCommand(new ScoreboardRemoveRow(this, this.scoreboardManager));
        this.commandParser.addCommand(new ScoreboardSetRow(this, this.scoreboardManager));
        this.commandParser.addCommand(new ScoreboardList(this.scoreboardManager));
        this.commandParser.addCommand(new ScoreboardDisplay(this, this.scoreboardManager));
        this.commandParser.addCommand(new ScoreboardHide(this, this.scoreboardManager));

        Bukkit.getPluginManager().registerEvents(this, this);
        logger.info(ChatColor.LIGHT_PURPLE + "Plugin Enabled Successfully");
        this.startPAPIUpdates();
    }

    public void saveScoreboardManager() {
        getConfig().set("ScoreboardManager", scoreboardManager);
        saveConfig();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("cvscoreboards")) {
            return commandParser.execute(sender, args);
        }
        return false;
    }

    public String parsePAPI(Player player, String var) {
        return this.papiHandler.parsePAPI(player, var);
    }

    @EventHandler
    public void onLogin(PlayerJoinEvent event) {

    }

    @EventHandler
    public void onLogout(PlayerQuitEvent event) {
        this.scoreboardManager.removePlayerScoreboard(event.getPlayer());
    }

    public void startPAPIUpdates() {
        this.getServer().getScheduler().runTaskTimer(this, () -> {
            Map<UUID, ScoreboardContainer> playerScoreboards = this.scoreboardManager.getAllPlayerScoreboards();
            if(playerScoreboards != null && !playerScoreboards.isEmpty()) {
                for(UUID uuid : playerScoreboards.keySet()) {
                    Player player = Bukkit.getPlayer(uuid);
                    if(player != null) {
                        ScoreboardContainer playerScoreboard = playerScoreboards.get(uuid);
                        for(Integer slot : playerScoreboard.getScoreboardRows().keySet()) {
                            ScoreboardContainer defaultScoreboard = this.scoreboardManager.getScoreboard(playerScoreboard.getScoreboardTitleWithColors());
                            String defaultRowValue = this.parsePAPI(player, defaultScoreboard.getScoreboardRows().get(slot));
                            String playerRowValue = playerScoreboard.getScoreboardRows().get(slot);
                            System.out.println(defaultRowValue);
                            System.out.println(playerRowValue);
                            if(!defaultRowValue.equals(playerRowValue)) {
                                this.scoreboardManager.setScoreboardRow(defaultScoreboard.getScoreboardTitleWithColors(), slot, defaultRowValue);
                            }
                        }
                    }
                }
            }
        }, 100L, 20L);
    }
}
