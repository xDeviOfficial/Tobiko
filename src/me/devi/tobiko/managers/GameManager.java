package me.devi.tobiko.managers;

import me.devi.tobiko.Tobiko;
import me.devi.tobiko.enums.GameState;
import me.devi.tobiko.scoreboard.Line;
import me.devi.tobiko.scoreboard.ScoreboardContainer;
import me.devi.tobiko.scoreboard.ScoreboardManager;
import me.devi.tobiko.tasks.GameTask;
import me.devi.tobiko.utils.LocationUtils;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class GameManager {

    private int maxPlayers;
    public static String tobikoPlayer;
    public static boolean tobikoAssigned = false;
    public static GameTask gameTask = new GameTask(300);

    public static List<String> players = new ArrayList<>();
    public static List<String> spectators = new ArrayList<>();

    private static ScoreboardManager sm = new ScoreboardManager();
    private static Map<Integer, String> lines = new HashMap<Integer, String>();


    /*



        addLine(15,"");
        addLine(14,fix("&c&lZycie Tobiko"));
        addLine(13,"20");
        addLine(12," ");
        addLine(11,fix("&a&lZywi Gracze"));
        addLine(10,"" + GameManager.players.size());
        addLine(9,"   ");


     */


    public static void addSpectator(String s) {
        if(players.contains(s)) {
            players.remove(s);
            spectators.add(s);
        }
    }






    public static void createScoreboard(Player p) {
        Bukkit.getScheduler().runTask(Tobiko.getInst(), new Runnable() {
            @Override
            public void run() {
                sm.registerPlayer(p);
                sm.getScoreboardContainer(p).init();
            }
        });
        lines.put(1,"");
        lines.put(2,fix("&c&lZycie Tobiko"));
        lines.put(3,fix("" + Bukkit.getPlayer(tobikoPlayer).getHealth()));
        lines.put(4,fix(" "));
        lines.put(5,fix("&a&lZywi Gracze"));
        lines.put(6,fix("" + players.size()));
        lines.put(7,fix("   "));

    }

    public static void scoreboardUpdater() {
       // lines.put(3, String.valueOf(Bukkit.getPlayer(tobikoPlayer).getHealth()));

        for(Player p : Bukkit.getOnlinePlayers()) {
            //sm.getScoreboardContainer(p).resetScoreboard();

             //sm.getScoreboardContainer(p).addLine(3, String.valueOf(Bukkit.getPlayer(tobikoPlayer).getHealth()));
            //sm.update(p, lines);
          //  sm.getScoreboardContainer(p).update(lines);
            for(int i = 0; i < lines.size(); i++) {
                p.sendMessage("Linia: " + i + ":" + lines.get(i));

            }
              sm.getScoreboardContainer(p).update(lines);
              //sm.update(p, lines);







            //Bukkit.broadcastMessage("3: " + sm.getScoreboardContainer(p).lines.get(3));
           // sm.update(p, lines);


            //sm.getScoreboardContainer(p).lines.get(0).update(" ");
            //sm.getScoreboardContainer(p).lines.get(1).update(fix("&c&lZycie Tobiko"));
            //sm.getScoreboardContainer(p).lines.get(2).update("" + Bukkit.getPlayer(tobikoPlayer).getHealth());
            //sm.getScoreboardContainer(p).lines.get(3).update(fix(" "));
            //sm.getScoreboardContainer(p).lines.get(4).update(fix("&a&lZywi Gracze"));
            //sm.getScoreboardContainer(p).lines.get(5).update(fix("" + players.size()));



            //sm.getScoreboardContainer(p).update(lines);
        }

    }

    public static void setSpectator(Player p) {
        addSpectator(p.getName());
        p.setGameMode(GameMode.SPECTATOR);
        p.getInventory().clear();
    }


    public static void assignTobiko() {
        Random r = new Random();
        tobikoPlayer = players.get(r.nextInt(players.size()));
        Bukkit.broadcastMessage(fix(Tobiko.TAG + "&eGracz &c" + tobikoPlayer + " &ezostal Tobiko!"));
        tobikoAssigned = true;
    }

    public static void joinLobby(Player p) {
        players.add(p.getName());
        resetPlayer(p);
        teleportToLobby(p);
    }

    public static void resetPlayer(Player p) {
        p.setGameMode(GameMode.ADVENTURE);
        p.setHealth(20);
        p.setExp(0);
        p.setFlying(false);
        p.setAllowFlight(false);
    }

    public static void teleportToLobby(Player p) {
        p.teleport(LocationUtils.stringToLocation(Tobiko.getYamler().getCfg().getString("lobby.location")));
    }


    public static void startGame() {
        if(GameManager.tobikoAssigned) {
            return;
        }
        if(Tobiko.gameState == GameState.COUNTDOWN){
            assignTobiko();
            Bukkit.getScheduler().runTask(Tobiko.getInst(), new Runnable() {
                @Override
                public void run() {
                    teleportPlayersToGame();
                }
            });
            Tobiko.gameState = GameState.INGAME;
        }
        for(Player p : Bukkit.getOnlinePlayers()) {
            createScoreboard(p);
        }
        gameTask.start();

    }

    public static void teleportPlayersToGame() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(p.getName().equalsIgnoreCase(tobikoPlayer)) {
                p.teleport(new Location(p.getLocation().getWorld(), p.getLocation().getX(), p.getLocation().getY()+20, + p.getLocation().getZ()));
                p.setAllowFlight(true);
                p.setFlying(true);
                equipTobiko(p);
            } else {
                equitPlayerSet(p);
            }
        }
    }



    public static void equitPlayerSet(Player p) {
        ItemStack bow = new ItemStack(Material.BOW, 1);
        ItemMeta bowMeta = bow.getItemMeta();
        bowMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
        bow.setItemMeta(bowMeta);

        ItemStack arrow = new ItemStack(Material.ARROW, 1);
        p.getInventory().addItem(bow, arrow);

    }

    public static void equipTobiko(Player p) {
        ItemStack hoe = new ItemStack(Material.DIAMOND_HOE);
        p.getInventory().addItem(hoe);
    }

    static String fix(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
