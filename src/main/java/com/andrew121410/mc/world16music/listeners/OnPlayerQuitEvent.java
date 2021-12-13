package com.andrew121410.mc.world16music.listeners;

import com.andrew121410.mc.world16music.World16Music;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerQuitEvent implements Listener {

    private final World16Music plugin;

    public OnPlayerQuitEvent(World16Music plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.plugin.getMusicBoundingBoxes().forEach((name, musicBoundingBox) -> musicBoundingBox.getPlayersInBoundingBoxList().remove(event.getPlayer().getUniqueId()));
    }
}
