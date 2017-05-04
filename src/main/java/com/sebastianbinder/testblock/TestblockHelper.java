package com.sebastianbinder.testblock;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalPlayer;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.schematic.SchematicFormat;
import com.sk89q.worldedit.session.ClipboardHolder;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

class TestblockHelper {

  public static List<String> getPlayerTestblockNames(Player player) {
    File directory = getSchematicDirectory();

    String[] matchedSchematics = directory.list((dir, name) -> name
        .startsWith(player.getUniqueId().toString() + "-") && name.endsWith(".schematic"));

    List<String> filenames = new ArrayList<>();
    if (matchedSchematics != null) {
      for (String filename : matchedSchematics) {
        String testblockName = filename;
        Integer filenameLength = filename.length() - (
            player.getUniqueId().toString().length() + 1) - (".schematic"
            .length());
        filenames.add(
            filename
                .substring(player.getUniqueId().toString().length() + 1,
                    player.getUniqueId().toString().length() + 1 + filenameLength));
      }
    }
    return filenames;
  }

  static File getSchematicDirectory() {
    WorldEdit worldEdit = TestblockPlugin.getInstance().getWorldEdit();
    LocalConfiguration config = worldEdit.getConfiguration();

    File directory = worldEdit.getWorkingDirectoryFile(config.saveDir + "/testblocks");
    if (!directory.exists()) {
      directory.mkdirs();
    }
    return directory;
  }

  static void pasteTestblock(Player player) {
    if (TestblockPlugin.getInstance().isWorldEditEnabled()) {
      Location location = TestblockPlugin.getInstance().getTestblockLocation(player);
      if (location == null) {
        TestblockPlugin.getInstance()
            .sendPluginMessage(player, ChatColor.GOLD
                + "Bitte setze den Ort des Testblocks mit \"/testblock setlocation\"!");
        return;
      }
      try {
        File directory = getSchematicDirectory();

        String testblockName = TestblockPlugin.getInstance().getTestblockSelection(player);
        if (testblockName == null) {
          testblockName = "default";
        }

        File schematicFile = new File(directory,
            player.getUniqueId().toString() + "-" + testblockName + ".schematic");
        if (!schematicFile.exists()) {
          schematicFile = new File(directory, "testblock.schematic");
        }
        if (schematicFile.exists()) {
          EditSession editSession = TestblockPlugin.getInstance().getTestblockEditSession(player);
          editSession.enableQueue();

          CuboidClipboard clipboard = SchematicFormat.MCEDIT.load(schematicFile);

          Vector widthVector = clipboard.getOffset().add(clipboard.getWidth(), 0, 0);
          Vector lengthVector = clipboard.getOffset().add(0, 0, clipboard.getLength());
          Vector middleVector = Vector.getMidpoint(widthVector, lengthVector);
          org.bukkit.util.Vector bukkitVector = new org.bukkit.util.Vector(
              middleVector.getX(), middleVector.getY(),
              middleVector.getZ());

          Direction testblockDirection = Direction.getDirectionFromVector(bukkitVector);
          Direction pasteDirection = Direction.getDirectionFromVector(location.getDirection());

          clipboard.rotate2D(Direction.getOffset(pasteDirection, testblockDirection));
          clipboard.paste(editSession, BukkitUtil.toVector(location), false);
          editSession.flushQueue();
        }
      } catch (Exception exception) {
        exception.printStackTrace();
      }
    }
  }

  static void saveTestblock(Player player, String name) {
    if (TestblockPlugin.getInstance().isWorldEditEnabled()) {
      try {
        WorldEditPlugin worldEditPlugin = TestblockPlugin.getInstance().getWorldEditPlugin();
        WorldEdit worldEdit = TestblockPlugin.getInstance().getWorldEdit();

        File directory = getSchematicDirectory();

        if (name == null) {
          name = "default";
        }
        File schematicFile = new File(directory, player.getUniqueId().toString() + "-" +
            name + ".schematic");

        LocalPlayer localPlayer = worldEditPlugin.wrapPlayer(player);

        LocalSession localSession = worldEdit.getSession(localPlayer);

        ClipboardHolder clipboardSelection = null;
        try {
          clipboardSelection = localSession.getClipboard();
        } catch (Exception ignored) {
        }

        if (clipboardSelection == null) {
          TestblockPlugin.getInstance()
              .sendPluginMessage(player,
                  ChatColor.RED + "Bitte wähle einen Bereich aus und kopiere ihn mit \"//copy\".");
          return;
        }

        FileOutputStream stream = new FileOutputStream(schematicFile);
        ClipboardWriter writer = ClipboardFormat.SCHEMATIC.getWriter(stream);
        writer.write(clipboardSelection.getClipboard(), null);
        writer.close();

        TestblockPlugin.getInstance()
            .sendPluginMessage(player,
                ChatColor.GREEN + "Dein Testblock wurde erfolgreich gespeichert.");
      } catch (Exception exception) {
        exception.printStackTrace();
      }
    }
  }

}
