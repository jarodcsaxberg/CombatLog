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

package me.iiSnipez.CombatLog.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.iiSnipez.CombatLog.CombatLog;

public class PlayerCombatLogEvent extends Event implements Cancellable {

	private boolean cancelled;
	private Player player;
	CombatLog plugin;

	private static final HandlerList handlerList = new HandlerList();

	public static HandlerList getHandlerList() {
		return handlerList;
	}

	public HandlerList getHandlers() {
		return handlerList;
	}
	
	public PlayerCombatLogEvent(CombatLog plugin, Player player) {
		this.plugin = plugin;
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public long getTagTimeRemaining() {
		return (plugin.tagDuration - (plugin.getCurrentTime() - (Long) plugin.taggedPlayers.get(player.getName()).longValue()));
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean b) {
		this.cancelled = b;
	}
}