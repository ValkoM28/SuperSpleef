package me.iamnany.superspleef;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.scoreboard.*;

public class ScoreboardHandler {
    public Scoreboard scoreboard;
    public Objective objective;

    private final World spleefWorld;

    public ScoreboardHandler(SuperSpleef plugin, World spleefWorld) {
        this.spleefWorld = spleefWorld;
        this.setupScoreboard();
    }
    private void setupScoreboard() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        scoreboard = manager.getNewScoreboard();
        objective = scoreboard.registerNewObjective("alivePlayers", "dummy", "Players Alive");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        updateScoreboard();
    }


    public void updateScoreboard() {
        int alivePlayers = (int) spleefWorld.getPlayers().stream()
                .filter(player -> player.getGameMode() != GameMode.SPECTATOR)
                .count();
        Score score = objective.getScore("Alive:");
        score.setScore(alivePlayers);
    }
}
