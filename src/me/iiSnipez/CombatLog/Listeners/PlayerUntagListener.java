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
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.iiSnipez.CombatLog.CombatLog;
import me.iiSnipez.CombatLog.Events.PlayerUntagEvent;
import me.iiSnipez.CombatLog.Events.PlayerUntagEvent.UntagCause;

public class PlayerUntagListener implements Listener {

	CombatLog plugin;

	public PlayerUntagListener(CombatLog plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onUntag(PlayerUntagEvent event) {
		Player player = event.getPlayer();
		String name = event.getPlayer().getName();
		if (event.getCause().equals(UntagCause.TIME_EXPIRE)) {
			if (plugin.useActionBar) {
				if (plugin.newActionBar) {
					plugin.aBar.sendActionBarNew(player, "" + plugin.actionBarUntagMessage);
				} else {
					plugin.aBar.sendActionBarOld(player, "" + plugin.actionBarUntagMessage);
				}
			}
			if (plugin.untagMessageEnabled) {
				player.sendMessage(plugin.translateText(plugin.untagMessage));
			}
			plugin.taggedPlayers.remove(name);
		} else if (event.getCause().equals(UntagCause.COMBATLOG)) {
			plugin.taggedPlayers.remove(name);
		} else if (event.getCause().equals(UntagCause.DEATH)) {
			if (plugin.untagMessageEnabled) {
				player.sendMessage(plugin.translateText(plugin.untagMessage));
			}
			plugin.taggedPlayers.remove(name);
		} else if (event.getCause().equals(UntagCause.KICK)) {
			plugin.taggedPlayers.remove(name);
		} else if (event.getCause().equals(UntagCause.LAGOUT)) {
			plugin.taggedPlayers.remove(name);
		} else if (event.getCause().equals(UntagCause.SAFE_AREA)) {
			if (plugin.untagMessageEnabled) {
				player.sendMessage(plugin.translateText(plugin.untagMessage));
			}
			if (plugin.useActionBar) {
				if (plugin.newActionBar) {
					plugin.aBar.sendActionBarNew(player, "" + plugin.actionBarUntagMessage);
				} else {
					plugin.aBar.sendActionBarOld(player, "" + plugin.actionBarUntagMessage);
				}
			}
			plugin.taggedPlayers.remove(name);
		}
	}
}