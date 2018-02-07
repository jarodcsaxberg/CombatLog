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
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import me.iiSnipez.CombatLog.CombatLog;

public class PlayerTeleportListener implements Listener {

	CombatLog plugin;

	public PlayerTeleportListener(CombatLog plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		if (plugin.taggedPlayers.containsKey(player.getName())) {
			if (plugin.blockTeleportationEnabled) {
				if (Bukkit.getVersion().contains("1.12") || Bukkit.getVersion().contains("1.11") || Bukkit.getVersion().contains("1.10")
						|| Bukkit.getVersion().contains("1.9")) {
					if (event.getCause().equals(TeleportCause.COMMAND)
							|| event.getCause().equals(TeleportCause.END_GATEWAY)
							|| event.getCause().equals(TeleportCause.END_PORTAL)
							|| event.getCause().equals(TeleportCause.ENDER_PEARL)
							|| event.getCause().equals(TeleportCause.NETHER_PORTAL)
							|| event.getCause().equals(TeleportCause.PLUGIN)
							|| event.getCause().equals(TeleportCause.SPECTATE)
							|| event.getCause().equals(TeleportCause.UNKNOWN)
							|| event.getCause().equals(TeleportCause.CHORUS_FRUIT)) {
						event.setCancelled(true);
						if (plugin.blockTeleportationMessageEnabled) {
							player.sendMessage(plugin.translateText(plugin.blockTeleportationMessage));
						}
					}
				} else {
					if (event.getCause().equals(TeleportCause.COMMAND)
							|| event.getCause().equals(TeleportCause.END_PORTAL)
							|| event.getCause().equals(TeleportCause.ENDER_PEARL)
							|| event.getCause().equals(TeleportCause.NETHER_PORTAL)
							|| event.getCause().equals(TeleportCause.PLUGIN)
							|| event.getCause().equals(TeleportCause.SPECTATE)
							|| event.getCause().equals(TeleportCause.UNKNOWN)) {
						event.setCancelled(true);
						if (plugin.blockTeleportationMessageEnabled) {
							player.sendMessage(plugin.translateText(plugin.blockTeleportationMessage));
						}
					}
				}
			}
		}
	}
}