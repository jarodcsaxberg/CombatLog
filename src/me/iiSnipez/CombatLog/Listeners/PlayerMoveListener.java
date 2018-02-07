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
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.massivecore.ps.PS;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;

import me.iiSnipez.CombatLog.CombatLog;
import me.iiSnipez.CombatLog.Events.PlayerUntagEvent;
import me.iiSnipez.CombatLog.Events.PlayerUntagEvent.UntagCause;

public class PlayerMoveListener implements Listener {

	CombatLog plugin;
	Faction factionIn;

	public PlayerMoveListener(CombatLog plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location l = player.getLocation();
				
		if (plugin.taggedPlayers.containsKey(player.getName())) {
			if (plugin.usesFactions && plugin.removeTagInSafeZone) {
				if (plugin.useNewFactions) {
					factionIn = BoardColl.get().getFactionAt(PS.valueOf(l));
					if (factionIn.equals(FactionColl.get().getSafezone())) {
						PlayerUntagEvent event1 = new PlayerUntagEvent(player, UntagCause.SAFE_AREA);
						Bukkit.getServer().getPluginManager().callEvent(event1);
						return;
					}
				} else if (plugin.useLegacyFactions && Board.getInstance().getFactionAt(new FLocation(l)).isSafeZone()) {
					PlayerUntagEvent event1 = new PlayerUntagEvent(player, UntagCause.SAFE_AREA);
					Bukkit.getServer().getPluginManager().callEvent(event1);
				}
			}
			if(plugin.usesWorldGuard && plugin.removeTagInPvPDisabledArea) {
				ApplicableRegionSet ars = WGBukkit.getPlugin().getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation());
				if(ars.queryState(null, DefaultFlag.PVP) == StateFlag.State.DENY) {
					PlayerUntagEvent event1 = new PlayerUntagEvent(player, UntagCause.SAFE_AREA);
					Bukkit.getServer().getPluginManager().callEvent(event1);
				}
			}
			if (plugin.removeFlyEnabled) {
				plugin.removeFly(player);
			}
			if (plugin.removeDisguiseEnabled) {
				plugin.removeDisguise(player);
			}
		}
	}
}