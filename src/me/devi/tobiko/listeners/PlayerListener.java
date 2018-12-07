package me.devi.tobiko.listeners;

import me.devi.tobiko.Tobiko;
import me.devi.tobiko.enums.GameState;
import me.devi.tobiko.managers.GameManager;
import me.devi.tobiko.scoreboard.ScoreboardContainer;
import me.devi.tobiko.scoreboard.ScoreboardManager;
import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.*;
import org.bukkit.util.Vector;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.HashMap;

public class PlayerListener implements Listener {







    @EventHandler
    public void onItemClick(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_AIR) {
            if(event.getItem().getType().equals(Material.DIAMOND_HOE)) {
                event.getPlayer().launchProjectile(Egg.class);
            }
        }
    }



    @EventHandler
    public void onHitEvent(ProjectileHitEvent event) {
        if(event.getEntity() instanceof  Egg) {
            if(event.getHitBlock() != null) {
                Location l = event.getHitBlock().getLocation();
                int range = 2;
                int minX = l.getBlockX() - range / 2;
                int minY = l.getBlockY() - range / 2;
                int minZ = l.getBlockZ() - range / 2;

                for(int x = minX; x < minX + range; x++)
                {
                    for(int y = minY; y < minY + range; y++)
                    {
                        for(int z = minZ; z < minZ + range; z++)
                        {
                            // world.getBlockAt(x,y,z) ...
                            //Bukkit.getWorlds().get(0).getBlockAt(x,y,z).breakNaturally();
                            event.getEntity().getWorld().createExplosion(l, 1);

                        }
                    }
                }
            }
        }


    }

    @EventHandler
    public void bowshoot(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            if(event.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) {
                event.setCancelled(true);
            }
        } else {
            return;
        }
    }

    @EventHandler
    public void playerQuitEvent(PlayerQuitEvent event) {
        if(Bukkit.getOnlinePlayers().size()-1 == 0) {
            Tobiko.getCountdownTask().stop();
            Bukkit.broadcastMessage("Odliczanie anulowane, zbyt malo graczy");
        }
    }

    @EventHandler
    public void entityDamageEntity(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof  Arrow && event.getEntity() instanceof Player) {
            LivingEntity shooter = (LivingEntity) ((Arrow) event.getDamager()).getShooter();
            Player shotPlayer = (Player) event.getEntity();
            if(shotPlayer.getName().equalsIgnoreCase(GameManager.tobikoPlayer)) {
                Bukkit.broadcastMessage(fix(Tobiko.TAG + "&f&lGracz " + shooter.getName() + " &ctrafil Tobiko! &e[+1 Moneta]"));
            }
        }

    }


    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if(event.getPlayer().getName().equalsIgnoreCase(GameManager.tobikoPlayer)) {
            if(event.getTo().getY() <= 55) {
                //TODO kick player above - set velocity for Y
                event.getPlayer().teleport(event.getFrom());
                event.getPlayer().sendMessage("Nie mozesz zejsc nizej");
            }
        }
    }

    @EventHandler
    public void onDeaths(EntityDeathEvent event) {
        if(event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            if(GameManager.players.size() == 0) {
                Bukkit.broadcastMessage(fix("Tobiko wygral!"));
                //TODO EFFECTS FIREWORKS SOUNDS ETC
             } else {
                if(event.getEntity().getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.VOID)) {
                    Bukkit.broadcastMessage(fix(Tobiko.TAG + "&f&l" + p.getName() + " &czostal WYELIMINOWANY!"));
                }
            }

        }
    }


    @EventHandler
    public void onSpawn(PlayerRespawnEvent event) {
        if(GameManager.players.contains(event.getPlayer().getName())) {
            GameManager.setSpectator(event.getPlayer());
            event.getPlayer().teleport(Bukkit.getPlayer(GameManager.tobikoPlayer).getLocation());
        }
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        GameManager.joinLobby(event.getPlayer());
        Bukkit.broadcastMessage(fix(Tobiko.TAG + "&f&l" + event.getPlayer().getName() + " &6dolaczyl do gry!"));
        event.setJoinMessage(null);
        if(Bukkit.getOnlinePlayers().size() >= 1) {
            Tobiko.getCountdownTask().start();
        }
    }

    @EventHandler
    public void preLoginEvent(AsyncPlayerPreLoginEvent event) {
        if(Tobiko.gameState == GameState.INGAME ) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Gra juz trwa, nie mozesz dolaczyc");
        } else if(Tobiko.gameState == GameState.RESTARTING) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Arena sie restartuje, prosze poczekac i dolaczyc za chwilke");
        }

    }

    String fix(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }


}
