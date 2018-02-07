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
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.iiSnipez.CombatLog.CombatLog;
import me.iiSnipez.CombatLog.Events.PlayerUntagEvent;
import me.iiSnipez.CombatLog.Events.PlayerUntagEvent.UntagCause;

public class PlayerDeathListener implements Listener {

	public CombatLog plugin;

	public PlayerDeathListener(CombatLog plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event){
		Player player = event.getEntity().getPlayer();
		if(plugin.taggedPlayers.containsKey(player.getName())){
			PlayerUntagEvent event1 = new PlayerUntagEvent(player, UntagCause.DEATH);
			Bukkit.getServer().getPluginManager().callEvent(event1);
		}
		if(plugin.killPlayers.contains(player.getUniqueId().toString())){
			plugin.killPlayers.remove(player.getUniqueId().toString());
		}
	}
}