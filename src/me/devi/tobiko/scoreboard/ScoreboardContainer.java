package me.devi.tobiko.scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.devi.tobiko.managers.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import net.md_5.bungee.api.ChatColor;

public class ScoreboardContainer {

    public Scoreboard sb;
    public Objective obj;
    public Objective hp;
    boolean second = false;
    public HashMap<String, Score> scores = new HashMap<String, Score>();
    public HashMap<String, Team> teams = new HashMap<String, Team>();
    public HashMap<Integer, Line> lines = new HashMap<Integer, Line>();
    private Player account;
    private List<Line> toDelete = new ArrayList<Line>();
    private boolean use18;
    private boolean init = false;

    public ScoreboardContainer(Player ac) {
        this.account = ac;
        use18 = true;
        //if(((CraftPlayer) ac.getPlayer()).getHandle().playerConnection.networkManager.getVersion() >= 47)
        //	use18 = true;

    }


    String fix(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public void init() {
        if(sb != null) resetScoreboard();

        deleteOlds();

        sb = Bukkit.getScoreboardManager().getNewScoreboard();
        obj = sb.registerNewObjective("obj", "dummy");
        hp = sb.registerNewObjective("hp", "dummy");
        hp.setDisplaySlot(DisplaySlot.BELOW_NAME);
        hp.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&bLevel"));


        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(fix("&9&lTobiko: " + GameManager.gameTask.getTimeLeft()%60));



        addLine(7,"");
        addLine(6,fix("&c&lZycie Tobiko"));
        addLine(5,"20");
        addLine(4," ");
        addLine(3,fix("&a&lZywi Gracze"));
        addLine(2,"" + GameManager.players.size());
        addLine(1,"   ");

        account.getPlayer().setScoreboard(sb);
        init = true;
    }

    public boolean isInit() {
        return init;
    }

    public void update(Map<Integer, String> lines) {
        deleteOlds();
        for(int i : lines.keySet()) {
            updateLine(i, lines.get(i));
        }
    }

    public void setDisplayName(String name) {
        if(obj != null)
            obj.setDisplayName(name);
    }

    public void resetScoreboard() {
        deleteOlds();

        hp.unregister();
        obj.unregister();
        sb = null;
        obj = null;
        hp = null;
        scores.clear();
        teams.clear();
        lines.clear();
        account.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());

    }

    private void deleteOlds() {
        if(!use18)
            return;

        for(Line old : toDelete) {
            if(sb == null || obj == null) {
                scores.remove(old.main);
                continue;
            }
            obj.getScoreboard().resetScores(old.main);
            if(old.usedTeam) {
                if(obj.getScoreboard().getTeam(old.main) != null)
                    obj.getScoreboard().getTeam(old.main).unregister();
            }

            scores.remove(old.main);
        }

        toDelete.clear();
    }

    public void registerTeam(String prefix,String text,String sufix) {
        Team team = sb.registerNewTeam(text);
        teams.put(text, team);
        teams.get(text).addEntry(text);
        teams.get(text).setPrefix(
                prefix);
        teams.get(text).setSuffix(
                sufix);
    }

    private void updateLine(int index,String text) {
        Line old = lines.get(index);
        Line line = new Line(index, text);

        if(old.text.equalsIgnoreCase(text))
            return;
        scores.put(line.main, obj.getScore(line.main));

        if(line.usedTeam) {
            registerTeam(line.prefix, line.main, line.sufix);
        }
        lines.put(line.score, line);

        scores.get(line.main).setScore(line.score);
        if(use18) {
            scores.get(old.main).setScore(-(scores.get(old.main).getScore()));
            toDelete.add(old);
        } else {
            obj.getScoreboard().resetScores(old.main);
            if(old.usedTeam) {
                if(obj.getScoreboard().getTeam(old.main) != null)
                    obj.getScoreboard().getTeam(old.main).unregister();
            }

            scores.remove(old.main);
        }

    }

    public void addLine(int score,String text) {
        Line line = new Line(score, text);
        scores.put(line.main, obj.getScore(line.main));
        scores.get(line.main).setScore(line.score);
        if(line.usedTeam) {
            registerTeam(line.prefix, line.main, line.sufix);
        }
        lines.put(line.score, line);
    }

    public void unregisterLine(Line line) {
        obj.getScoreboard().resetScores(line.main);
        if(line.usedTeam) {
            obj.getScoreboard().getTeam(line.main).unregister();
        }
        scores.remove(line.main);
        lines.remove(line.score);
    }
}