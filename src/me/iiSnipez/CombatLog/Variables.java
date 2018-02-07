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

package me.iiSnipez.CombatLog;

import org.bukkit.Bukkit;

import de.robingrether.idisguise.api.DisguiseAPI;

public class Variables {

	CombatLog plugin;

	public Variables(CombatLog plugin) {
		this.plugin = plugin;
	}
	
	public void getValues() {
		plugin.logInfo("[CombatLog] Loading config.yml");
		// configuration
		plugin.updateCheckEnabled = plugin.clConfig.getCLConfig().getBoolean("UpdateCheck");
		plugin.MOTDEnabled = plugin.clConfig.getCLConfig().getBoolean("MOTD");
		plugin.broadcastEnabled = plugin.clConfig.getCLConfig().getBoolean("Broadcast");
		plugin.tagDuration = plugin.clConfig.getCLConfig().getInt("Tag-Duration");
		if (plugin.clConfig.getCLConfig().getStringList("Remove-Modes").contains("fly")) {
			plugin.removeFlyEnabled = true;
		}
		if (plugin.clConfig.getCLConfig().getStringList("Remove-Modes").contains("disguise")) {
			plugin.removeDisguiseEnabled = true;
		}
		plugin.removeTagOnKick = plugin.clConfig.getCLConfig().getBoolean("Remove-Tag.onKick");
		plugin.removeTagOnLagout = plugin.clConfig.getCLConfig().getBoolean("Remove-Tag.onLagout");
		plugin.removeTagInSafeZone = plugin.clConfig.getCLConfig().getBoolean("Remove-Tag.inSafeZone");
		plugin.removeTagInPvPDisabledArea = plugin.clConfig.getCLConfig().getBoolean("Remove-Tag.inPvPDisabledArea");
		plugin.removeInvisPotion = plugin.clConfig.getCLConfig().getBoolean("Remove-Invis-Potion");
		plugin.useActionBar = plugin.clConfig.getCLConfig().getBoolean("ActionBar");
		if(Bukkit.getServer().getVersion().contains("1.7")) {
			plugin.useActionBar = false;
		}
		plugin.useBossBar = plugin.clConfig.getCLConfig().getBoolean("BossBar");
		plugin.blockCommandsEnabled = plugin.clConfig.getCLConfig().getBoolean("Block-Commands");
		plugin.whitelistModeEnabled = plugin.clConfig.getCLConfig().getBoolean("Whitelist-Mode");
		plugin.commandNames = plugin.clConfig.getCLConfig().getStringList("Commands");
		plugin.executeCommandsEnabled = plugin.clConfig.getCLConfig().getBoolean("Execute-Commands");
		plugin.executeCommandList = plugin.clConfig.getCLConfig().getStringList("ExecutedCommands");
		plugin.blockTeleportationEnabled = plugin.clConfig.getCLConfig().getBoolean("Block-Teleportation");
		plugin.disableWorldNames = plugin.clConfig.getCLConfig().getStringList("Disabled-Worlds");
		plugin.killEnabled = plugin.clConfig.getCLConfig().getBoolean("Kill");
		// messages
		plugin.updateCheckMessage = plugin.clConfig.getCLConfig().getString("UpdateCheckMessage");
		if (!plugin.updateCheckMessage.equalsIgnoreCase("false")) {
			plugin.updateCheckMessageEnabled = true;
		} else {
			plugin.updateCheckMessageEnabled = false;
		}
		plugin.MOTDMessage = plugin.clConfig.getCLConfig().getString("MOTDMessage");
		if (!plugin.MOTDMessage.equalsIgnoreCase("false")) {
			plugin.MOTDMessageEnabled = true;
		} else {
			plugin.MOTDMessageEnabled = false;
		}
		plugin.broadcastMessage = plugin.clConfig.getCLConfig().getString("BroadcastMessage");
		if (!plugin.broadcastMessage.equalsIgnoreCase("false")) {
			plugin.broadcastMessageEnabled = true;
		} else {
			plugin.broadcastMessageEnabled = false;
		}
		plugin.taggerMessage = plugin.clConfig.getCLConfig().getString("TaggerMessage");
		if (!plugin.taggerMessage.equalsIgnoreCase("false")) {
			plugin.taggerMessageEnabled = true;
		} else {
			plugin.taggerMessageEnabled = false;
		}
		plugin.taggedMessage = plugin.clConfig.getCLConfig().getString("TaggedMessage");
		if (!plugin.taggedMessage.equalsIgnoreCase("false")) {
			plugin.taggedMessageEnabled = true;
		} else {
			plugin.taggedMessageEnabled = false;
		}
		plugin.untagMessage = plugin.clConfig.getCLConfig().getString("UntagMessage");
		if (!plugin.untagMessage.equalsIgnoreCase("false")) {
			plugin.untagMessageEnabled = true;
		} else {
			plugin.untagMessageEnabled = false;
		}
		plugin.tagTimeMessage = plugin.clConfig.getCLConfig().getString("InCombatMessage");
		if (!plugin.tagTimeMessage.equalsIgnoreCase("false")) {
			plugin.tagTimeMessageEnabled = true;
		} else {
			plugin.tagTimeMessageEnabled = false;
		}
		plugin.notInCombatMessage = plugin.clConfig.getCLConfig().getString("NotInCombatMessage");
		if (!plugin.notInCombatMessage.equalsIgnoreCase("false")) {
			plugin.notInCombatMessageEnabled = true;
		} else {
			plugin.notInCombatMessageEnabled = false;
		}
		plugin.actionBarInCombatMessage = plugin.clConfig.getCLConfig().getString("ActionBarInCombatMessage");
		plugin.actionBarUntagMessage = plugin.clConfig.getCLConfig().getString("ActionBarUntagMessage");
		plugin.removeModesMessage = plugin.clConfig.getCLConfig().getString("RemoveModesMessage");
		if (!plugin.removeModesMessage.equalsIgnoreCase("false")) {
			plugin.removeModesMessageEnabled = true;
		} else {
			plugin.removeModesMessageEnabled = false;
		}
		plugin.removeInvisMessage = plugin.clConfig.getCLConfig().getString("RemoveInvisMessage");
		if (!plugin.removeInvisMessage.equalsIgnoreCase("false")) {
			plugin.removeInvisMessageEnabled = true;
		} else {
			plugin.removeInvisMessageEnabled = false;
		}
		plugin.blockCommandsMessage = plugin.clConfig.getCLConfig().getString("BlockCommandsMessage");
		if (!plugin.blockCommandsMessage.equalsIgnoreCase("false")) {
			plugin.blockCommandsMessageEnabled = true;
		} else {
			plugin.blockCommandsMessageEnabled = false;
		}
		plugin.blockTeleportationMessage = plugin.clConfig.getCLConfig().getString("BlockTeleportationMessage");
		if (!plugin.blockTeleportationMessage.equalsIgnoreCase("false")) {
			plugin.blockTeleportationMessageEnabled = true;
		} else {
			plugin.blockTeleportationMessageEnabled = false;
		}
		plugin.killMessage = plugin.clConfig.getCLConfig().getString("KillMessage");
		if (!plugin.killMessage.equalsIgnoreCase("false")) {
			plugin.killMessageEnabled = true;
		} else {
			plugin.killMessageEnabled = false;
		}
		if (Bukkit.getVersion().contains("1.12")) {
			plugin.newActionBar = true;
		} else {
			plugin.newActionBar = false;
		}
	}
	
	public DisguiseAPI initDis() {
		return Bukkit.getServer().getServicesManager().getRegistration(DisguiseAPI.class).getProvider();
	}	
}