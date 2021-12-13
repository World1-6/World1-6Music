package com.andrew121410.mc.world16music.objects;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Getter
@Setter
public class MusicBoundingBox implements ConfigurationSerializable {

    //Do not save
    private boolean running;
    private List<UUID> playersInBoundingBoxList;

    private String name;

    private BoundingBox boundingBox;
    private String sound;
    private Float volume;
    private Float pitch;

    public MusicBoundingBox(String name, BoundingBox boundingBox, String sound, Float volume, Float pitch) {
        this.running = false;
        this.playersInBoundingBoxList = new ArrayList<>();

        this.name = name;
        this.boundingBox = boundingBox;
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("Name", this.name);
        map.put("BoundingBox", this.boundingBox);
        map.put("Sound", this.sound);
        map.put("Volume", this.volume);
        map.put("Pitch", this.pitch);
        return map;
    }

    public static MusicBoundingBox deserialize(Map<String, Object> map) {
        return new MusicBoundingBox((String) map.get("Name"), (BoundingBox) map.get("BoundingBox"), (String) map.get("Sound"), ((Double) map.get("Volume")).floatValue(), ((Double) map.get("Pitch")).floatValue());
    }
}
