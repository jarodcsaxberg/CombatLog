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

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.ps.PS;

import me.iiSnipez.CombatLog.CombatLog;
import me.iiSnipez.CombatLog.Events.PlayerTagEvent;

public class PlayerTagListener implements Listener {

	CombatLog plugin;
	Faction faction;

	public PlayerTagListener(CombatLog plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onTagEvent(PlayerTagEvent event) {
		Player damager = event.getDamager();
		Player damagee = event.getDamagee();
		tagDamager(damager, damagee);
		tagDamagee(damager, damagee);
	}

	private void tagDamager(Entity damager, Entity damagee) {
		if (damager instanceof Player) {
			Player p = (Player) damager;
			if (!p.hasPermission("combatlog.bypass")) {
				if (!plugin.disableWorldNames.contains(p.getWorld().getName())) {
					Location l = p.getLocation();
					if (plugin.usesFactions) {
						if (plugin.useNewFactions) {
							faction = BoardColl.get().getFactionAt(PS.valueOf(l));
							if (faction.getName().equalsIgnoreCase("SafeZone")) {
								return;
							}
						}
						if (plugin.useLegacyFactions
								&& Board.getInstance().getFactionAt(new FLocation(l)).isSafeZone()) {
							return;
						}
					}
					if (!plugin.taggedPlayers.containsKey(p.getName())) {
						plugin.taggedPlayers.put(p.getName(), plugin.getCurrentTime());
						if (plugin.taggerMessageEnabled) {
							p.sendMessage(plugin.translateText(
									plugin.taggerMessage.replaceAll("<name>", ((Player) damagee).getName())));
						}
						if (plugin.useActionBar) {
							if (plugin.newActionBar) {
								plugin.aBar.sendActionBarNew(p, "" + plugin.actionBarInCombatMessage
										.replaceAll("<time>", "" + plugin.tagTimeRemaining(p.getName())));
							} else {
								plugin.aBar.sendActionBarOld(p, "" + plugin.actionBarInCombatMessage
										.replaceAll("<time>", "" + plugin.tagTimeRemaining(p.getName())));
							}
						}
						if (plugin.usesLibsDisguise && plugin.removeDisguiseEnabled)
							plugin.removeDisguise(p);
						if (plugin.removeFlyEnabled)
							plugin.removeFly(p);
						if (plugin.removeInvisPotion) {
							removePotion(p);
							removePotion((Player) damagee);
						}
					} else {
						plugin.taggedPlayers.remove(p.getName());
						plugin.taggedPlayers.put(p.getName(), plugin.getCurrentTime());
						if (plugin.removeDisguiseEnabled)
							plugin.removeDisguise(p);
						if (plugin.removeFlyEnabled)
							plugin.removeFly(p);
						if (plugin.removeInvisPotion) {
							removePotion(p);
							removePotion((Player) damagee);
						}
					}
				}
			}
		}
	}

	private void tagDamagee(Entity damager, Entity damagee) {
		if (damagee instanceof Player) {
			Player p = (Player) damagee;
			Location l = p.getLocation();
			if (!p.hasPermission("combatlog.bypass")) {
				if (!plugin.disableWorldNames.contains(p.getWorld().getName())) {
					if (plugin.usesFactions) {
						if (plugin.useNewFactions) {
							faction = BoardColl.get().getFactionAt(PS.valueOf(l));
							if (faction.getName().equalsIgnoreCase("SafeZone")) {
								return;
							}
						}
						if (plugin.useLegacyFactions
								&& Board.getInstance().getFactionAt(new FLocation(l)).isSafeZone()) {
							return;
						}
					}
					if (!plugin.taggedPlayers.containsKey(p.getName())) {
						plugin.taggedPlayers.put(p.getName(), plugin.getCurrentTime());
						if (plugin.taggedMessageEnabled) {
							p.sendMessage(plugin.translateText(
									plugin.taggedMessage.replaceAll("<name>", ((Player) damager).getName())));
						}
						if (plugin.useActionBar) {
							if (plugin.newActionBar) {
								plugin.aBar.sendActionBarNew(p, "" + plugin.actionBarInCombatMessage
										.replaceAll("<time>", "" + plugin.tagTimeRemaining(p.getName())));
							} else {
								plugin.aBar.sendActionBarOld(p, "" + plugin.actionBarInCombatMessage
										.replaceAll("<time>", "" + plugin.tagTimeRemaining(p.getName())));
							}
						}
						if (plugin.usesLibsDisguise && plugin.removeDisguiseEnabled)
							plugin.removeDisguise(p);
						if (plugin.removeFlyEnabled)
							plugin.removeFly(p);
						if (plugin.removeInvisPotion) {
							removePotion(p);
							removePotion((Player) damager);
						}
					} else {
						plugin.taggedPlayers.remove(p.getName());
						plugin.taggedPlayers.put(p.getName(), plugin.getCurrentTime());
						if (plugin.removeDisguiseEnabled)
							plugin.removeDisguise(p);
						if (plugin.removeFlyEnabled)
							plugin.removeFly(p);
						if (plugin.removeInvisPotion) {
							removePotion(p);
							removePotion((Player) damager);
						}
					}
				}
			}
		}
	}

	private void removePotion(Player player) {
		for (PotionEffect potion : player.getActivePotionEffects()) {
			if (potion.getType().equals(PotionEffectType.INVISIBILITY)) {
				player.removePotionEffect(PotionEffectType.INVISIBILITY);
				if (plugin.removeInvisMessageEnabled)
					player.sendMessage(plugin.translateText(plugin.removeInvisMessage));
			}
		}
	}
}