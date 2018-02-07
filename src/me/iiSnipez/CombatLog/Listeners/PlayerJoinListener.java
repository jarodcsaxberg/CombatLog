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

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.iiSnipez.CombatLog.CombatLog;

public class PlayerJoinListener implements Listener {

	CombatLog plugin;

	public PlayerJoinListener(CombatLog plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (plugin.updateCheckEnabled && player.hasPermission("combatlog.update") && plugin.updateAvailable) {
			if (plugin.updateCheckMessageEnabled) {
				player.sendMessage(plugin.translateText(plugin.updateCheckMessage));
			}
		}
		if(plugin.MOTDEnabled && plugin.MOTDMessageEnabled){
			player.sendMessage(plugin.translateText(plugin.MOTDMessage));
		}
		if(plugin.killMessageEnabled && plugin.killPlayers.contains(player.getUniqueId().toString())){
			player.sendMessage(plugin.translateText(plugin.killMessage));
			plugin.killPlayers.remove(player.getUniqueId().toString());
		}
	}
}
