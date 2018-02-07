/*
 *  CombatLog is a plugin for the popular game Minecraft that strives to
 *  make PvP combat in the game more fair
 *  
 *  Copyright (C) 2018 Jarod Saxberg (iiSnipez)
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.iiSnipez.CombatLog;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class CommandExec implements CommandExecutor {

	public CombatLog plugin;

	public CommandExec(CombatLog plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getLabel().equalsIgnoreCase("combatlog") || cmd.getLabel().equalsIgnoreCase("cl")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (args.length == 0) {
					player.sendMessage(plugin.translateText("&8[&4CombatLog&8]&7 Originally developed by &cJackProehl&7."));
					player.sendMessage(plugin.translateText("&8[&4CombatLog&8]&7 Update developed and maintained by &ciiSnipez&7."));
					player.sendMessage(plugin.translateText("&8[&4CombatLog&8]&7 This server's tag duration is: &c" + plugin.tagDuration + " seconds&7."));
					player.sendMessage(plugin.translateText("&cUse &7/cl help &cto view the commands."));
				} else if (args.length == 1) {
					if (args[0].equalsIgnoreCase("help")) {
						player.sendMessage(plugin.translateText("&8[&4CombatLog&8]&7Use &c/tag &7or &c/ct &7to view if you are in combat."));
						if (player.hasPermission("combatlog.reload") || player.isOp()) {
							player.sendMessage(plugin.translateText("&8[&4CombatLog&8]&7Use &c/cl reload &7to reload the configuration."));
						}
					} else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
						if (player.hasPermission("combatlog.reload") || player.isOp()) {
							plugin.loadSettings();
							player.sendMessage(plugin.translateText("&8[&4CombatLog&8] &aConfiguration reloaded."));
						} else {
							player.sendMessage(plugin.translateText("&4You do not have permission to use this command."));
						}
					} else if (args[0].equalsIgnoreCase("update") && player.hasPermission("combatlog.update")) {
						if(plugin.updateAvailable)
							player.sendMessage(plugin.translateText("&8[&4CombatLog&8] &aUpdate available! http://bit.ly/CL-DL "));
						else
							player.sendMessage(plugin.translateText("&8[&4CombatLog&8] &7No update was detected."));
					}
				}
			} else if (sender instanceof ConsoleCommandSender) {
				ConsoleCommandSender console = (ConsoleCommandSender) sender;
				if (args.length == 0) {
					console.sendMessage("[CombatLog] Use '/cl help' to view all of the commands.");
				} else if (args.length == 1) {
					if (args[0].equalsIgnoreCase("help")) {
						console.sendMessage("[CombatLog] /cl reload - reloads the configuration.");
					} else if (args[0].equalsIgnoreCase("reload")) {
						plugin.loadSettings();
						console.sendMessage("[CombatLog] Configuration reloaded.");
					} else if (args[0].equalsIgnoreCase("update")) {
						if(plugin.updateAvailable)
							console.sendMessage(ChatColor.GREEN + "[CombatLog] Update available! http://bit.ly/CL-DL ");
						else
							console.sendMessage(ChatColor.RED + "[CombatLog] No update was detected.");
					}
				}
			}
		} else if (cmd.getLabel().equalsIgnoreCase("tag") || cmd.getLabel().equalsIgnoreCase("ct")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				String id = player.getName();
				if (plugin.taggedPlayers.containsKey(id) && plugin.tagDuration 
						- (plugin.getCurrentTime() - (Long) plugin.taggedPlayers.get(id).longValue()) >= 1L) {
					player.sendMessage(plugin.translateText(plugin.tagTimeMessage.replaceAll("<time>", "" + plugin.tagTimeRemaining(id))));
				} else if (plugin.taggedPlayers.containsKey(id) && plugin.tagDuration
						- (plugin.getCurrentTime() - (Long) plugin.taggedPlayers.get(id).longValue()) < 1L) {
					player.sendMessage(plugin.translateText(plugin.tagTimeMessage.replaceAll("<time>", "" + plugin.tagTimeRemaining(id))));
				}else if (!plugin.taggedPlayers.containsKey(id)) {
					player.sendMessage(plugin.translateText(plugin.notInCombatMessage));
				}
			}else if(sender instanceof ConsoleCommandSender){
				ConsoleCommandSender console = (ConsoleCommandSender) sender;
				console.sendMessage("[CombatLog] The console cannot use this command.");
			}
		}
		return false;
	}
}