package me.devi.tobiko.tasks;

import me.devi.tobiko.Tobiko;
import me.devi.tobiko.enums.GameState;
import me.devi.tobiko.managers.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class GameTask implements Runnable {


    private int countdownTime;
    private int bukkitTaskID;
    private boolean isRunning;

    public GameTask(int countdownSeconds) {
        this.isRunning = false;
        this.countdownTime = countdownSeconds;

    }

    public void start() {
        if (this.isRunning) return;
        this.bukkitTaskID = Bukkit.getScheduler().runTaskTimerAsynchronously(Tobiko.getInst(), this, 0, 20).getTaskId();
        this.isRunning = true;
        Tobiko.gameState = GameState.COUNTDOWN;
    }

    public int getTimeLeft() {
        return this.countdownTime;
    }


    public void stop() {
        if (this.isRunning) {
            Bukkit.getScheduler().cancelTask(this.bukkitTaskID);
        } else {
            new Exception("Cannot cancel Task [Task is not running]");
        }
    }

    public boolean isRunning() {
        return this.isRunning;
    }




    public void run() {
        if(this.countdownTime > 1) {
            GameManager.scoreboardUpdater();
        } else {
            stop();
            return;
        }
        this.countdownTime--;
    }


    String fix(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }


}
