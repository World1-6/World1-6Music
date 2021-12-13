package com.andrew121410.mc.world16music.commands;

import com.andrew121410.mc.world16music.World16Music;
import com.andrew121410.mc.world16music.objects.MusicBoundingBox;
import com.andrew121410.mc.world16utils.World16Utils;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.utils.TabUtils;
import com.andrew121410.mc.world16utils.utils.Utils;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.UUID;

public class MusicManagerCMD implements CommandExecutor {

    private final World16Music plugin;

    public MusicManagerCMD(World16Music plugin) {
        this.plugin = plugin;
        this.plugin.getCommand("musicmanager").setExecutor(this);
        this.plugin.getCommand("musicmanager").setTabCompleter((sender, command, alias, args) -> {
            if (args.length == 1) {
                return Arrays.asList("create", "delete", "start");
            } else if (args.length == 2) {
                String sub = args[0];
                if (sub.equalsIgnoreCase("delete") || sub.equalsIgnoreCase("start")) {
                    return TabUtils.getContainsString(args[1], new ArrayList<>(this.plugin.getMusicBoundingBoxes().keySet()));
                }
            }
            return null;
        });
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (!player.hasPermission("world16music.musicmanager")) {
                player.sendMessage("You don't have permission to use this command!");
                return true;
            }

            if (args[0].equalsIgnoreCase("create")) {
                if (args.length == 1) {
                    player.sendMessage(Translate.color("&6/musicmanager create <name> <sound> <volume> <pitch>"));
                    player.sendMessage(Translate.color("&cAlso make sure you make a WorldEdit selection!"));
                    return true;
                } else if (args.length == 5) {
                    String name = args[1];
                    String sound = args[2];
                    String volume = args[3];
                    String pitch = args[4];

                    BoundingBox boundingBox = World16Utils.getInstance().getClassWrappers().getWorldEdit().getRegion(player);
                    if (boundingBox == null) {
                        player.sendMessage(Translate.color("&cPlease make a WorldEdit selection!"));
                        return true;
                    }

                    MusicBoundingBox musicBoundingBox = new MusicBoundingBox(name, boundingBox, sound, Utils.asFloatOrElse(volume, 1F), Utils.asFloatOrElse(pitch, 1F));
                    this.plugin.getMusicBoundingBoxes().put(name, musicBoundingBox);
                    player.sendMessage(Translate.color("&6The MusicBoundingBox has been created!"));
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("delete")) {
                if (args.length == 1) {
                    player.sendMessage(Translate.color("&6/musicmanager delete <name>"));
                    return true;
                } else if (args.length == 2) {
                    String name = args[1];

                    if (!this.plugin.getMusicBoundingBoxes().containsKey(name)) {
                        player.sendMessage(Translate.color("&cThat MusicBoundingBox doesn't exist"));
                        return true;
                    }

                    this.plugin.deleteMusicBoundingBox(name);
                    player.sendMessage(Translate.color("&6Successfully deleted " + name + " MusicBoundingBox"));
                    return true;
                }
            }
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("start")) {
            String name = args[1];

            MusicBoundingBox musicBoundingBox = this.plugin.getMusicBoundingBoxes().get(name);
            if (musicBoundingBox == null) {
                sender.sendMessage(Translate.color("&cThat MusicBoundingBox doesn't exist"));
                return true;
            }

            if (musicBoundingBox.isRunning()) {
                musicBoundingBox.setRunning(false);

                Iterator<UUID> iterator = musicBoundingBox.getPlayersInBoundingBoxList().iterator();
                while (iterator.hasNext()) {
                    UUID uuid = iterator.next();

                    Player player1 = this.plugin.getServer().getPlayer(uuid);
                    if (player1 == null) {
                        iterator.remove();
                        continue;
                    }

                    player1.stopSound(musicBoundingBox.getSound());
                }
                return true;
            } else {
                musicBoundingBox.setRunning(true);

                for (Player onlinePlayer : this.plugin.getServer().getOnlinePlayers()) {
                    if (musicBoundingBox.getBoundingBox().contains(onlinePlayer.getLocation().getBlock().getLocation().toVector())) {
                        musicBoundingBox.getPlayersInBoundingBoxList().add(onlinePlayer.getUniqueId());
                        onlinePlayer.playSound(onlinePlayer.getLocation(), musicBoundingBox.getSound(), SoundCategory.MASTER, musicBoundingBox.getVolume(), musicBoundingBox.getPitch());
                    }
                }
            }
        }
        return true;
    }
}