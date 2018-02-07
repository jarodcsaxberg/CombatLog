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
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.events.DisguiseEvent;

public class PlayerDisguiseListener implements Listener{
	
	CombatLog plugin;
	
	public PlayerDisguiseListener(CombatLog plugin){
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDisguise(DisguiseEvent event){
		if(event.getEntity() instanceof Player){
			Player player = (Player) event.getEntity();
			if(plugin.usesiDisguise && plugin.removeDisguiseEnabled && plugin.taggedPlayers.containsKey(player.getName())){
				DisguiseAPI.undisguiseToAll(player);
				event.setCancelled(true);
				if(plugin.removeModesMessageEnabled)
					player.sendMessage(plugin.translateText(plugin.removeModesMessage.replaceAll("<mode>", "disguise")));
			}
		}
	}
}