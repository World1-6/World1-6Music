package com.andrew121410.mc.world16music;

import com.andrew121410.mc.world16music.commands.MusicManagerCMD;
import com.andrew121410.mc.world16music.listeners.OnPlayerMoveEvent;
import com.andrew121410.mc.world16music.listeners.OnPlayerQuitEvent;
import com.andrew121410.mc.world16music.objects.MusicBoundingBox;
import com.andrew121410.mc.world16utils.config.CustomYmlManager;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class World16Music extends JavaPlugin {

    static {
        ConfigurationSerialization.registerClass(MusicBoundingBox.class, "MusicBoundingBox");
    }

    @Getter
    private static World16Music instance;

    private CustomYmlManager dataYml;
    @Getter
    private Map<String, MusicBoundingBox> musicBoundingBoxes;

    @Override
    public void onEnable() {
        instance = this;

        this.dataYml = new CustomYmlManager(this, false);
        this.dataYml.setup("data.yml");
        this.dataYml.saveConfig();
        this.dataYml.reloadConfig();

        this.musicBoundingBoxes = new HashMap<>();
        loadAllMusicBoundingBoxes();

        registerListeners();
        registerCommands();
    }

    private void registerCommands() {
        new MusicManagerCMD(this);
    }

    private void registerListeners() {
        new OnPlayerMoveEvent(this);
        new OnPlayerQuitEvent(this);
    }

    @Override
    public void onDisable() {
        saveAllMusicBoundingBoxes();
    }

    public void loadAllMusicBoundingBoxes() {
        ConfigurationSection musicBoundingBoxesSection = getMusicBoundingBoxesConfigSection();
        for (String key : musicBoundingBoxesSection.getKeys(false)) {
            this.musicBoundingBoxes.put(key, (MusicBoundingBox) musicBoundingBoxesSection.get(key));
        }
    }

    public void saveAllMusicBoundingBoxes() {
        ConfigurationSection musicBoundingBoxesSection = getMusicBoundingBoxesConfigSection();
        this.musicBoundingBoxes.forEach(musicBoundingBoxesSection::set);
        this.dataYml.saveConfig();
    }

    public void deleteMusicBoundingBox(String name) {
        ConfigurationSection musicBoundingBoxesSection = getMusicBoundingBoxesConfigSection();
        musicBoundingBoxesSection.set(name, null);
        this.musicBoundingBoxes.remove(name);
        this.dataYml.saveConfig();
    }

    private ConfigurationSection getMusicBoundingBoxesConfigSection() {
        ConfigurationSection musicBoundingBoxesSection = this.dataYml.getConfig().getConfigurationSection("MusicBoundingBoxes");
        if (musicBoundingBoxesSection == null) {
            musicBoundingBoxesSection = this.dataYml.getConfig().createSection("MusicBoundingBoxes");
        }
        return musicBoundingBoxesSection;
    }
}
