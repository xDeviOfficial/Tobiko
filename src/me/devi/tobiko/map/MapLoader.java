package me.devi.tobiko.map;

import me.devi.tobiko.Tobiko;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

public class MapLoader {

    private World world;

    public MapLoader() {
    }

    public void unloadWorld(World world) {
        this.world = Bukkit.getWorld("world");
        if(!world.equals(null)) {
            Bukkit.getServer().unloadWorld(world, true);
        }
    }

    public boolean deleteWorld(File path) {
        if(path.exists()) {
            File files[] = path.listFiles();
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteWorld(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return(path.delete());
    }

    public void copyWorld(File source, File target){
        try {
            ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat"));
            if(!ignore.contains(source.getName())) {
                if(source.isDirectory()) {
                    if(!target.exists())
                        target.mkdirs();
                    String files[] = source.list();
                    for (String file : files) {
                        File srcFile = new File(source, file);
                        File destFile = new File(target, file);
                        copyWorld(srcFile, destFile);
                    }
                } else {
                    InputStream in = new FileInputStream(source);
                    OutputStream out = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0)
                        out.write(buffer, 0, length);
                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {

        }
    }


    public boolean addWorld(String name, World.Environment env, String seedString, WorldType type, Boolean generateStructures,
                            String generator, boolean useSpawnAdjust) {
        if (name.equalsIgnoreCase("plugins") || name.equalsIgnoreCase("logs")) {
            return false;
        }
        Long seed = null;
        WorldCreator c = new WorldCreator(name);
        if (seedString != null && seedString.length() > 0) {
            try {
                seed = Long.parseLong(seedString);
            } catch (NumberFormatException numberformatexception) {
                seed = (long) seedString.hashCode();
            }
            c.seed(seed);
        }

        // TODO: Use the fancy kind with the commandSender
        if (generator != null && generator.length() != 0) {
            c.generator(generator);
        }
        c.environment(env);
        if (type != null) {
            c.type(type);
        }
        if (generateStructures != null) {
            c.generateStructures(generateStructures);
        }


        StringBuilder builder = new StringBuilder();
        builder.append("Loading World & Settings - '").append(name).append("'");
        builder.append(" - Env: ").append(env);
        builder.append(" - Type: ").append(type);
        if (seed != null) {
            builder.append(" & seed: ").append(seed);
        }
        if (generator != null) {
            builder.append(" & generator: ").append(generator);
        }
        System.out.print(builder.toString());



        // set generator (special case because we can't read it from org.bukkit.World)

        return true;
    }







}
