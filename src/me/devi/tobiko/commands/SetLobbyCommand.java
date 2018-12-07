package me.devi.tobiko.commands;

import com.mojang.authlib.GameProfile;
import me.devi.tobiko.Tobiko;
import me.devi.tobiko.utils.LocationUtils;
import net.minecraft.server.v1_12_R1.EnumGamemode;
import net.minecraft.server.v1_12_R1.PacketPlayInArmAnimation;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLobbyCommand implements CommandExecutor{


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(cmd.getName().equalsIgnoreCase("tobiko")) {
            if(args.length == 0) {
                //TO DO HELP ALL COMMANDS
            } else if(args.length == 1 && args[0].equalsIgnoreCase("setlobby")) {
                Player p = (Player) sender;
                setLobby(p.getLocation());
                p.sendMessage(Tobiko.TAG + " &eLobby has been set successfully.");
            }
        }
        return false;
    }

    public void setLobby(Location loc) {
        Tobiko.getYamler().getCfg().set("lobby.location", LocationUtils.locationToString(loc));
        Tobiko.getYamler().save();
        Tobiko.getYamler().reload();
    }


}
