package com.andrew121410.mc.world16music.listeners;

import com.andrew121410.mc.world16music.World16Music;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class OnPlayerMoveEvent implements Listener {

    private final World16Music plugin;

    public OnPlayerMoveEvent(World16Music plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        this.plugin.getMusicBoundingBoxes().forEach((name, musicBoundingBox) -> {
            if (!musicBoundingBox.getBoundingBox().contains(player.getLocation().getBlock().getLocation().toVector())) {
                if (musicBoundingBox.getPlayersInBoundingBoxList().contains(player.getUniqueId())) {
                    musicBoundingBox.getPlayersInBoundingBoxList().remove(player.getUniqueId());
                    player.stopSound(musicBoundingBox.getSound());
                }
            }
        });
    }
}
