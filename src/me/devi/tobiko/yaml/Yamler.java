package me.devi.tobiko.yaml;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Yamler {

    public static HashMap<String, FileConfiguration> data = new HashMap<>();
    private File f;

    public Yamler(File f) {
        this.f = f;
        if(!data.containsKey(f.getAbsolutePath())) {
            data.put(f.getAbsolutePath(), (FileConfiguration)YamlConfiguration.loadConfiguration(f));
        }
    }

    public FileConfiguration getCfg() {
        return data.get(f.getAbsolutePath());
    }

    public void save() {
        try {
            data.get(f.getAbsolutePath()).save(f);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public Set<String> getSection(String section) {
        return getCfg().getKeys(false);
    }

    public void reload() {
        data.remove(f.getAbsolutePath());
        data.put(f.getAbsolutePath(), (FileConfiguration)YamlConfiguration.loadConfiguration(f));
    }

}