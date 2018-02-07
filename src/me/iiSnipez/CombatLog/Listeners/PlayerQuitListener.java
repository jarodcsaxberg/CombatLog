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

package me.iiSnipez.CombatLog.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.iiSnipez.CombatLog.CombatLog;
import me.iiSnipez.CombatLog.Events.PlayerCombatLogEvent;
import me.iiSnipez.CombatLog.Events.PlayerUntagEvent;
import me.iiSnipez.CombatLog.Events.PlayerUntagEvent.UntagCause;

public class PlayerQuitListener implements Listener {

	CombatLog plugin;

	public PlayerQuitListener(CombatLog plugin) {
		this.plugin = plugin;
	}

	public static String disconnectMsg = "";

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerOuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (player.hasPermission("combatlog.bypass")) {
			return;
		}
		if (!player.hasPermission("combatlog.bypass") && plugin.taggedPlayers.containsKey(player.getName())) {
			if (plugin.removeTagOnLagout && !playerCombatLogged()) {
				PlayerUntagEvent event1 = new PlayerUntagEvent(player, UntagCause.LAGOUT);
				Bukkit.getServer().getPluginManager().callEvent(event1);
				return;
			}
			if (plugin.broadcastEnabled) {
				plugin.broadcastMsg(plugin.translateText(plugin.broadcastMessage.replace("<name>", player.getName())));
			}			
			if(plugin.executeCommandsEnabled){
				for(String s : plugin.executeCommandList){
					Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), s.replaceAll("<name>", player.getName()));
				}
			}
			if (plugin.killEnabled) {
				player.setHealth(0);
				plugin.killPlayers.add(player.getUniqueId().toString());
			}
			PlayerCombatLogEvent event1 = new PlayerCombatLogEvent(plugin, player);
			plugin.getServer().getPluginManager().callEvent(event1);
			PlayerUntagEvent event2 = new PlayerUntagEvent(player, UntagCause.COMBATLOG);
			Bukkit.getServer().getPluginManager().callEvent(event2);
			plugin.combatlogs++;
		}
	}

	public static void setDisconnectMsg(String msg) {
		disconnectMsg = msg;
	}

	public boolean playerCombatLogged() {
		if (!disconnectMsg.equalsIgnoreCase("disconnect.overflow")
				&& !disconnectMsg.equalsIgnoreCase("disconnect.genericreason")
				&& !disconnectMsg.equalsIgnoreCase("disconnect.timeout")) {
			return true;
		}
		return false;
	}
}