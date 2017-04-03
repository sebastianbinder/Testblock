package com.sebastianbinder.testblock;

import com.sk89q.worldedit.EditSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TestblockCommand implements CommandExecutor, TabExecutor {

  private final String permission;

  public TestblockCommand(JavaPlugin plugin, String command, String permission) {
    plugin.getCommand(command).setExecutor(this);
    this.permission = permission;
  }

  public boolean onCommand(CommandSender commandSender, Command command, String label,
      String[] args) {

    if (this.permission != null && !commandSender.hasPermission(this.permission)) {
      return false;
    }

    List<String> arguments = new ArrayList<>(Arrays.asList(args));

    if (arguments.size() == 1 && arguments.get(0).equalsIgnoreCase("help")) {
      this.sendUsage(commandSender);
      return true;
    }

    if (commandSender instanceof Player) {
      this.execute((Player) commandSender, arguments);
      return true;
    } else {
      commandSender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
      return true;
    }
  }

  public void execute(Player player, List<String> arguments) {
    if (arguments.size() > 0) {
      String subCommand = arguments.get(0);
      arguments.remove(0);

      if ("delete".equalsIgnoreCase(subCommand)) {

      } else if ("paste".equalsIgnoreCase(subCommand)) {
        TestblockHelper.pasteTestblock(player);
      } else if ("redo".equalsIgnoreCase(subCommand)) {
        EditSession editSession = TestblockPlugin.getInstance().getTestblockEditSession(player);
        editSession.redo(editSession);
      } else if ("saveblock".equalsIgnoreCase(subCommand)) {
        if (arguments.size() > 1) {
          TestblockPlugin.getInstance()
              .sendPluginMessage(player, ChatColor.RED
                  + "Der Name deines Testblocks darf keine Leerzeichen enthalten!");
        } else if (arguments.size() == 1) {
          TestblockHelper.saveTestblock(player, arguments.get(0));
          TestblockPlugin.getInstance().saveTestblockSelection(player, arguments.get(0));
        } else {
          TestblockHelper.saveTestblock(player, null);
          TestblockPlugin.getInstance().saveTestblockSelection(player, null);
        }
      } else if ("select".equalsIgnoreCase(subCommand)) {
        if (arguments.size() > 1) {
          TestblockPlugin.getInstance()
              .sendPluginMessage(player, ChatColor.RED
                  + "Der Name deines Testblocks darf keine Leerzeichen enthalten!");
        } else if (arguments.size() == 1) {
          if (TestblockHelper.getPlayerTestblockNames(player).contains(arguments.get(0))) {
            TestblockPlugin.getInstance().saveTestblockSelection(player, arguments.get(0));
            TestblockPlugin.getInstance()
                .sendPluginMessage(player, ChatColor.GREEN
                    + "Du hast den Testblock \"" + arguments.get(0) + "\" ausgewählt.");
          } else {
            TestblockPlugin.getInstance().saveTestblockSelection(player, null);
            TestblockPlugin.getInstance()
                .sendPluginMessage(player, ChatColor.GOLD
                    + "Der Testblock \"" + arguments.get(0)
                    + "\" wurde nicht gefunden!\nEs wurde daher der Standard-Testblock ausgewählt.");
          }
        } else {
          TestblockPlugin.getInstance().saveTestblockSelection(player, null);
          TestblockPlugin.getInstance()
              .sendPluginMessage(player, ChatColor.GREEN
                  + "Es wurde daher der Standard-Testblock ausgewählt.");
        }
      } else if ("setlocation".equalsIgnoreCase(subCommand)) {
        TestblockPlugin.getInstance().saveTestblockLocation(player);
        TestblockPlugin.getInstance()
            .sendPluginMessage(player, ChatColor.GREEN
                + "Der Ort deines Testblocks wurde erfolgreich gesetzt.");
      } else if ("teleport".equalsIgnoreCase(subCommand) || "tp".equalsIgnoreCase(subCommand)) {
        Location location = TestblockPlugin.getInstance().getTestblockLocation(player);
        if (location == null) {
          TestblockPlugin.getInstance()
              .sendPluginMessage(player, ChatColor.GOLD
                  + "Der Ort deines Testblocks wurde noch nicht gesetzt.");
        } else {
          location.setPitch(player.getLocation().getPitch());
          location.setYaw(player.getLocation().getYaw());
          player.teleport(location);
        }
      } else if ("undo".equalsIgnoreCase(subCommand)) {
        EditSession editSession = TestblockPlugin.getInstance().getTestblockEditSession(player);
        editSession.undo(editSession);
      }
    } else {
      this.sendUsage(player);
    }
  }

  @Override
  public List<String> onTabComplete(CommandSender commandSender, Command command, String alias,
      String[] args) {
    List<String> results = new ArrayList<>();
    if (!(commandSender instanceof Player)) {
      return results;
    }
    Player player = (Player) commandSender;
    TestblockPlugin.getInstance().getLogger().info(alias);
    List<String> arguments = new ArrayList<>(Arrays.asList(args));
    if (arguments.size() == 0 || arguments.size() == 1) {
      List<String> subCommands = Arrays
          .asList("delete", "paste", "redo", "saveblock", "select", "setlocation", "teleport", "tp",
              "undo");
      if (arguments.size() == 0) {
        results.addAll(subCommands);
      } else {
        for (String subcommand : subCommands) {
          if (subcommand.startsWith(arguments.get(0))) {
            results.add(subcommand);
          }
        }
      }
    } else if (arguments.size() == 2) {
      String subCommand = arguments.get(0);
      arguments.remove(0);
      if ("delete".equalsIgnoreCase(subCommand) || "select".equalsIgnoreCase(subCommand)) {
        List<String> testblockNames = TestblockHelper.getPlayerTestblockNames(player);
        for (String testblockName : testblockNames) {
          if (testblockName.startsWith(arguments.get(0))) {
            results.add(testblockName);
          }
        }
      }
    }
    return results;
  }

  private void sendUsage(CommandSender commandSender) {
    commandSender.sendMessage(ChatColor.RED + "Hilfe:");
  }

}
