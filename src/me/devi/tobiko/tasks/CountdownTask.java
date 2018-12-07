package me.devi.tobiko.tasks;

import me.devi.tobiko.Tobiko;
import me.devi.tobiko.enums.GameState;
import me.devi.tobiko.managers.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class CountdownTask implements Runnable{

    private int countdownTime;
    private int bukkitTaskID;
    private boolean isRunning;

    public CountdownTask(int countdownSeconds) {
        this.isRunning = false;
        this.countdownTime = countdownSeconds;

    }

    public void start() {
        if(this.isRunning) return;
        this.bukkitTaskID = Bukkit.getScheduler().runTaskTimerAsynchronously(Tobiko.getInst(), this, 0, 20).getTaskId();
        this.isRunning = true;
        Tobiko.gameState = GameState.COUNTDOWN;
    }


    public void stop() {
        if(this.isRunning) {
            Bukkit.getScheduler().cancelTask(this.bukkitTaskID);
        } else {
            new Exception("Cannot cancel Task [Task is not running]");
        }
    }

    public boolean isRunning() {
        return this.isRunning;
    }


    public void run() {
        if(this.countdownTime == 0) {
            GameManager.startGame();
            stop();
            this.countdownTime = 30;
            return;
        } else if(this.countdownTime == 30 || this.countdownTime == 15 || this.countdownTime <= 10) {
            Bukkit.broadcastMessage(fix(Tobiko.TAG + "&fGra rozpocznie sie za &c" + this.countdownTime + " &fsekund!"));
        }
        this.countdownTime--;
    }

    String fix(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
