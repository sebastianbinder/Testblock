package com.sebastianbinder.testblock;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import java.util.HashMap;
import java.util.UUID;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TestblockPlugin extends JavaPlugin {

  @Getter
  private static TestblockPlugin instance;
  private HashMap<UUID, Location> testblockLocations = new HashMap<>();
  private HashMap<UUID, String> testblockSelections = new HashMap<>();
  private HashMap<UUID, EditSession> testblockEditSessions = new HashMap<>();

  public void onEnable() {
    instance = this;
    new TestblockCommand(this, "testblock", null);
  }

  public void onDisable() {
  }

  boolean isWorldEditEnabled() {
    return TestblockPlugin.getInstance().getServer().getPluginManager()
        .isPluginEnabled("WorldEdit");
  }

  WorldEditPlugin getWorldEditPlugin() {
    return (WorldEditPlugin) TestblockPlugin.getInstance().getServer().getPluginManager()
        .getPlugin("WorldEdit");
  }

  WorldEdit getWorldEdit() {
    return this.getWorldEditPlugin().getWorldEdit();
  }

  void sendPluginMessage(CommandSender recipient, String message) {
    recipient.sendMessage(ChatColor.GRAY + "[TESTBLOCK] " + ChatColor.RESET + message);
  }

  Location getTestblockLocation(Player player) {
    if (this.testblockLocations.containsKey(player.getUniqueId())) {
      return this.testblockLocations.get(player.getUniqueId());
    }
    return null;
  }

  void saveTestblockLocation(Player player) {
    if (this.testblockLocations.containsKey(player.getUniqueId())) {
      this.testblockLocations.replace(player.getUniqueId(), player.getLocation());
    } else {
      this.testblockLocations.put(player.getUniqueId(), player.getLocation());
    }
  }

  String getTestblockSelection(Player player) {
    if (this.testblockSelections.containsKey(player.getUniqueId())) {
      return this.testblockSelections.get(player.getUniqueId());
    }
    return null;
  }

  public void saveTestblockSelection(Player player, String name) {
    if (this.testblockSelections.containsKey(player.getUniqueId())) {
      this.testblockSelections.replace(player.getUniqueId(), name);
    } else {
      this.testblockSelections.put(player.getUniqueId(), name);
    }
  }

  EditSession getTestblockEditSession(Player player) {
    if (this.testblockEditSessions.containsKey(player.getUniqueId())) {
      return this.testblockEditSessions.get(player.getUniqueId());
    }
    EditSession editSession = new EditSession(
        new BukkitWorld(player.getLocation().getWorld()), 999999999);
    this.saveTestblockEditSession(player, editSession);
    return editSession;
  }

  public void saveTestblockEditSession(Player player, EditSession editSession) {
    if (this.testblockEditSessions.containsKey(player.getUniqueId())) {
      this.testblockEditSessions.replace(player.getUniqueId(), editSession);
    } else {
      this.testblockEditSessions.put(player.getUniqueId(), editSession);
    }
  }
}
