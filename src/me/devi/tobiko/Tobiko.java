package me.devi.tobiko;

import me.devi.tobiko.commands.SetLobbyCommand;
import me.devi.tobiko.enums.GameState;
import me.devi.tobiko.listeners.PlayerListener;
import me.devi.tobiko.map.MapLoader;
import me.devi.tobiko.tasks.CountdownTask;
import me.devi.tobiko.tasks.GameTask;
import me.devi.tobiko.yaml.Yamler;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Tobiko extends JavaPlugin{

    private static Tobiko inst;
    private static MapLoader mapLoader;
    private static Yamler yaml;
    public static final String TAG = "&9&lTobiko &e» ";
    public static CountdownTask countdownTask;
    public static GameState gameState;

    public static Yamler getYamler() {
        return yaml;
    }

    public static Tobiko getInst() {
        return inst;
    }

    public static CountdownTask getCountdownTask() {
        return countdownTask;
    }


    @Override
    public void onEnable() {
        inst = this;
        this.loadConfig();
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerListener(), this);
        mapLoader = new MapLoader();
        setupWorlds();
        this.registerCommands();
        initlializeTasks();
        gameState = GameState.WAITING;
    }

    @Override
    public void onDisable() {
        //mapLoader.unloadWorld(Bukkit.getWorld("world_temo"));
        for(Player p : Bukkit.getOnlinePlayers()) {
            p.kickPlayer("Serwer sie restartuje");
        }
        mapLoader.deleteWorld(new File(getDataFolder().getParentFile().getParentFile(), "temp_world"));
    }


    public void setupWorlds() {
        //World source = Bukkit.getWorld("tobiko");
        //File sourceFolder = source.getWorldFolder();
        File sourceTobiko = new File(getDataFolder().getParentFile().getParentFile(), "tobiko");
        //Kopiowanie
        Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[INFO] " + ChatColor.YELLOW + "The world " + sourceTobiko.getName() + " is coppyuing");
        mapLoader.copyWorld(sourceTobiko, new File(getDataFolder().getParentFile().getParentFile(), "temp_world"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[INFO] " + ChatColor.YELLOW + "The world " + sourceTobiko.getName() + " has been copied successfully");
        //Ladowanie
        Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[INFO] " + ChatColor.YELLOW + "Loading world temp_world");
        //new WorldCreator("temp_world").createWorld();
        //Bukkit.getWorlds().add(Bukkit.getWorld("temp_world"));
        //mapLoader.addWorld("temp_world", World.Environment.NORMAL, null, WorldType.NORMAL, null, null, false);
        getServer().createWorld(new WorldCreator("temp_world"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[INFO] " + ChatColor.YELLOW + "Loaded successfully");
    }

    private void loadConfig() {
        File f = new File(getDataFolder(), "config.yml");
        if(f.exists()) {
            yaml = new Yamler(f);
        } else {
            yaml = new Yamler(f);
            yaml.save();
        }
    }


    public void registerCommands() {
        getCommand("tobiko").setExecutor(new SetLobbyCommand());
    }

    public void initlializeTasks() {
        countdownTask = new CountdownTask(5);
    }
}
