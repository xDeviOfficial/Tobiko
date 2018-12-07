package me.devi.tobiko.scoreboard;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class ScoreboardManager {

    private HashMap<String, ScoreboardContainer> scoreboard = new HashMap<String, ScoreboardContainer>();

    public void registerPlayer(Player p) {
        ScoreboardContainer container = new ScoreboardContainer(p);
        scoreboard.put(p.getName(), container);
    }

    public ScoreboardContainer getScoreboardContainer(Player ac) {
        ScoreboardContainer container = scoreboard.get(ac.getName());
        return container;
    }

    public void update(Player ac, Map<Integer, String> lines) {
        if(ac == null) return;
        ScoreboardContainer container = getScoreboardContainer(ac);
        if(container == null) return;
        container.update(lines);
    }

    public void removePlayer(Player ac) {
        if(ac == null)
            return;
        ScoreboardContainer container = getScoreboardContainer(ac);
        if(container == null)
            return;
        container.resetScoreboard();
        scoreboard.remove(ac);
    }

}