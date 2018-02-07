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

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class PluginFile extends YamlConfiguration {

	public CombatLog plugin;

	public PluginFile(CombatLog plugin) {
		this.plugin = plugin;
	}

	public FileConfiguration clConfig = null;
	public File clConfigFile = null;

	public void reloadCLConfig() {
		if (clConfigFile == null) {
			clConfigFile = new File(plugin.getDataFolder(), "config.yml");
		}
		clConfig = YamlConfiguration.loadConfiguration(clConfigFile);
	}
	
	public FileConfiguration getCLConfig() {
		if (clConfig == null) {
			reloadCLConfig();
		}
		return clConfig;
	}

	public void saveCLConfig() {
		if ((clConfig == null) || (clConfigFile == null)) {
			return;
		}
		try {
			getCLConfig().save(clConfigFile);
		} catch (IOException ex) {
			plugin.log.warning("Could not save config to " + clConfigFile);
			plugin.log.warning(ex.getMessage());
		}
	}

	public void saveDefault() {
		if (!clConfigFile.exists()) {
			plugin.saveResource("config.yml", false);
		}
	}
}