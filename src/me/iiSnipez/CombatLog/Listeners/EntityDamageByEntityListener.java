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
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.TippedArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.iiSnipez.CombatLog.CombatLog;
import me.iiSnipez.CombatLog.Events.PlayerTagEvent;

public class EntityDamageByEntityListener implements Listener {

	CombatLog plugin;

	public EntityDamageByEntityListener(CombatLog plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (!event.isCancelled()) {
			Entity damagee = event.getEntity(); // being damaged
			Entity damager = event.getDamager(); // causing damage
			if (damagee instanceof Player && damager instanceof Player) {
				if (((Player) damager).hasPermission("combatlog.bypass")
						|| ((Player) damagee).hasPermission("combatlog.bypass")) {
					return;
				}
				PlayerTagEvent event1 = new PlayerTagEvent((Player) damager, (Player) damagee, plugin.tagDuration);
				plugin.getServer().getPluginManager().callEvent(event1);
			}
			if (damagee instanceof Player && damager instanceof Projectile
					&& ((Projectile) damager).getShooter() instanceof Player) {
				if (((Player) ((Projectile) damager).getShooter()).hasPermission("combatlog.bypass")
						|| ((Player) ((Projectile) damager).getShooter()).hasPermission("combatlog.bypass")) {
					return;
				}
				if (Bukkit.getVersion().contains("1.12") || Bukkit.getVersion().contains("1.11")
						|| Bukkit.getVersion().contains("1.10") || Bukkit.getVersion().contains("1.9")) {
					if (damager instanceof TippedArrow) {
						PlayerTagEvent event1 = new PlayerTagEvent((Player) ((Projectile) damager).getShooter(),
								(Player) damagee, plugin.tagDuration);
						plugin.getServer().getPluginManager().callEvent(event1);
					}
				}
				if (damager instanceof Arrow) {
					PlayerTagEvent event1 = new PlayerTagEvent((Player) ((Projectile) damager).getShooter(),
							(Player) damagee, plugin.tagDuration);
					plugin.getServer().getPluginManager().callEvent(event1);
				} else if (damager instanceof Snowball) {
					PlayerTagEvent event1 = new PlayerTagEvent((Player) ((Projectile) damager).getShooter(),
							(Player) damagee, plugin.tagDuration);
					plugin.getServer().getPluginManager().callEvent(event1);
				} else if (damager instanceof Egg) {
					PlayerTagEvent event1 = new PlayerTagEvent((Player) ((Projectile) damager).getShooter(),
							(Player) damagee, plugin.tagDuration);
					plugin.getServer().getPluginManager().callEvent(event1);
				} else if (damager instanceof ThrownPotion) {
					PlayerTagEvent event1 = new PlayerTagEvent((Player) ((Projectile) damager).getShooter(),
							(Player) damagee, plugin.tagDuration);
					plugin.getServer().getPluginManager().callEvent(event1);
				} else if (damager instanceof EnderPearl) {
					return;
				}
			}
		}
	}
}