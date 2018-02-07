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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import me.iiSnipez.CombatLog.Events.PlayerUntagEvent;
import me.iiSnipez.CombatLog.Events.PlayerUntagEvent.UntagCause;
import me.iiSnipez.CombatLog.Listeners.EntityDamageByEntityListener;
import me.iiSnipez.CombatLog.Listeners.PlayerCommandPreprocessListener;
import me.iiSnipez.CombatLog.Listeners.PlayerDeathListener;
import me.iiSnipez.CombatLog.Listeners.PlayerDisguiseListener;
import me.iiSnipez.CombatLog.Listeners.PlayerInteractListener;
import me.iiSnipez.CombatLog.Listeners.PlayerJoinListener;
import me.iiSnipez.CombatLog.Listeners.PlayerKickListener;
import me.iiSnipez.CombatLog.Listeners.PlayerMoveListener;
import me.iiSnipez.CombatLog.Listeners.PlayerQuitListener;
import me.iiSnipez.CombatLog.Listeners.PlayerTagListener;
import me.iiSnipez.CombatLog.Listeners.PlayerTeleportListener;
import me.iiSnipez.CombatLog.Listeners.PlayerToggleFlightListener;
import me.iiSnipez.CombatLog.Listeners.PlayerUntagListener;
import me.iiSnipez.CombatLog.Listeners.PlayeriDisguiseListener;
import me.libraryaddict.disguise.DisguiseAPI;

public class CombatLog extends JavaPlugin {

	public Logger log = Logger.getLogger("Minecraft");
	public PluginFile clConfig;
	public CommandExec commandExec;
	public Variables vars;
	public ActionBar aBar;
	public BStatsMetrics bsm;
	public WorldGuardPlugin wg;
	public de.robingrether.idisguise.api.DisguiseAPI dAPI;
	public boolean usesLibsDisguise = false;
	public boolean usesiDisguise = false;
	public boolean usesFactions = false;
	public boolean usesWorldGuard = false;
	public Updater updater;
	public boolean updateCheckEnabled = false;
	public boolean updateAvailable = false;
	public boolean MOTDEnabled = false;
	public boolean broadcastEnabled = false;
	public int tagDuration = 10;
	public boolean useActionBar = false;
	public boolean useBossBar = false;
	public boolean versionSupported = false;
	public boolean removeFlyEnabled = false;
	public boolean removeDisguiseEnabled = false;
	public boolean removeTagOnKick = false;
	public boolean removeTagOnLagout = false;
	public boolean removeTagInSafeZone = false;
	public boolean removeTagInPvPDisabledArea = false;
	public boolean removeInvisPotion = false;
	public boolean blockCommandsEnabled = false;
	public List<String> commandNames = new ArrayList<String>();
	public boolean whitelistModeEnabled = false;
	public boolean executeCommandsEnabled = false;
	public List<String> executeCommandList = new ArrayList<String>();
	public boolean blockTeleportationEnabled = false;
	public List<String> disableWorldNames = new ArrayList<String>();
	public boolean killEnabled = false;
	public String updateCheckMessage = "";
	public boolean updateCheckMessageEnabled = false;
	public String MOTDMessage = "";
	public boolean MOTDMessageEnabled = false;
	public String broadcastMessage = "";
	public boolean broadcastMessageEnabled = false;
	public String taggerMessage = "";
	public boolean taggerMessageEnabled = false;
	public String taggedMessage = "";
	public boolean taggedMessageEnabled = false;
	public String untagMessage = "";
	public boolean untagMessageEnabled = false;
	public String tagTimeMessage = "";
	public boolean tagTimeMessageEnabled = false;
	public String notInCombatMessage = "";
	public boolean notInCombatMessageEnabled = false;
	public String actionBarInCombatMessage = "";
	public String actionBarUntagMessage = "";
	public String scoreboardInCombatMessage = "";
	public boolean scoreboardInCombatMessageEnabled = false;
	public String removeModesMessage = "";
	public boolean removeModesMessageEnabled = false;
	public String removeInvisMessage = "";
	public boolean removeInvisMessageEnabled = false;
	public String blockCommandsMessage = "";
	public boolean blockCommandsMessageEnabled = false;
	public String blockTeleportationMessage = "";
	public boolean blockTeleportationMessageEnabled = false;
	public String killMessage = "";
	public boolean killMessageEnabled = false;
	public HashMap<String, Long> taggedPlayers = new HashMap<String, Long>();
	public ArrayList<String> killPlayers = new ArrayList<String>();
	public boolean useNewFactions = false;
	public boolean useLegacyFactions = false;
	public String nmserver;
	public boolean newActionBar = false;
	
	public int combatlogs;

	public void onEnable() {
		checkForPlugins();
		initiateVars();
		loadSettings();
		updateCheck();
		initiateListeners();
		initiateCmds();
		LogHandler();
		enableTimer();
		if (clConfig.getCLConfig().getBoolean("Metrics")) {
			startMetrics();
			metricsTimer();
		}
		logInfo("[CombatLog] v" + getDescription().getVersion() + " has been enabled.");
	}

	public void onDisable() {
		taggedPlayers.clear();
		logInfo("[CombatLog] v" + getDescription().getVersion() + " has been disabled.");
	}

	public void updateCheck() {
		if (updateCheckEnabled) {
			updater.updateCheck();
		}
	}

	public void checkForPlugins() {
		if (getServer().getPluginManager().getPlugin("LibsDisguises") == null) {
			usesLibsDisguise = false;
		} else {
			logInfo("[CombatLog] LibsDisguises plugin found! Disguise removal will work.");
			usesLibsDisguise = true;
		}
		if (getServer().getPluginManager().getPlugin("iDisguise") == null) {
			usesiDisguise = false;
		} else {
			usesiDisguise = true;
			logInfo("[CombatLog] iDisguise plugin found! Disguise removal will work.");
		}
		if (getServer().getPluginManager().getPlugin("Factions") == null) {
			usesFactions = false;
		} else {
			usesFactions = true;
			String version = getServer().getPluginManager().getPlugin("Factions").getDescription().getVersion();
			if (version.substring(0, 1).equalsIgnoreCase("2")) {
				useNewFactions = true;
				logInfo("[CombatLog] New Factions plugin v" + version + " found! Safezone regions are now detected.");
			} else if (version.substring(0, 1).equalsIgnoreCase("1")) {
				useLegacyFactions = true;
				logInfo("[CombatLog] Legacy Factions plugin v" + version
						+ " found! Safezone regions are now detected.");
			}
		}
		if (getServer().getPluginManager().getPlugin("WorldGuard") == null) {
			usesWorldGuard = false;
		} else {
			usesWorldGuard = true;
			logInfo("[CombatLog] WorldGuard plugin found! PvP regions are now detected.");
			wg = (WorldGuardPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		}
	}

	public void startMetrics() {
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		bsm = new BStatsMetrics(this);

		bsm.addCustomChart(new BStatsMetrics.SingleLineChart("combatlogs", new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				return combatlogs;
			}
		}));
	}

	public void enableTimer() {
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				Iterator<Map.Entry<String, Long>> iter = taggedPlayers.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry<String, Long> c = iter.next();
					Player player = getServer().getPlayer(c.getKey());
					if (useActionBar) {
						if (newActionBar) {
							aBar.sendActionBarNew(player, "" + "" + actionBarInCombatMessage.replaceAll("<time>",
									"" + tagTimeRemaining(player.getName())));
						} else {
							aBar.sendActionBarOld(player, "" + "" + actionBarInCombatMessage.replaceAll("<time>",
									"" + tagTimeRemaining(player.getName())));
						}
					}
					if (getCurrentTime() - (long) c.getValue().longValue() >= tagDuration) {
						iter.remove();
						PlayerUntagEvent event = new PlayerUntagEvent(player, UntagCause.TIME_EXPIRE);
						getServer().getPluginManager().callEvent(event);
					}
				}
			}
		}, 0L, 20L);
	}

	private void metricsTimer() {
		final Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				combatlogs = 0;
			}
		}, 1005 * 60 * 5, 1000 * 60 * 30);
	}

	public void LogHandler() {
		log.addHandler(new Handler() {
			public void publish(LogRecord logRecord) {
				String s = logRecord.getMessage();
				if (s.contains(" lost connection: ")) {
					String[] a = s.split(" ");
					String DisconnectMsg = a[3];
					PlayerQuitListener.setDisconnectMsg(DisconnectMsg);
				}
			}

			public void flush() {
			}

			public void close() throws SecurityException {
			}
		});
	}

	public void initiateCmds() {
		getCommand("combatlog").setExecutor(commandExec);
		getCommand("tag").setExecutor(commandExec);
	}

	public void initiateListeners() {
		getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerCommandPreprocessListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerKickListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerMoveListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerTagListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerTeleportListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerToggleFlightListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerUntagListener(this), this);
		if (usesLibsDisguise) {
			getServer().getPluginManager().registerEvents(new PlayerDisguiseListener(this), this);
		}
		if (usesiDisguise) {
			Bukkit.getServer().getPluginManager().registerEvents(new PlayeriDisguiseListener(this), this);
		}
	}

	public void loadSettings() {
		clConfig.getCLConfig().options().copyDefaults(true);
		clConfig.saveDefault();
		clConfig.reloadCLConfig();

		vars.getValues();
	}

	public void initiateVars() {
		clConfig = new PluginFile(this);
		updater = new Updater(this);
		commandExec = new CommandExec(this);
		vars = new Variables(this);
		aBar = new ActionBar(this);
		aBar.getBukkitVersion();
		if (usesiDisguise) {
			dAPI = vars.initDis();
		}
	}

	public void removeDisguise(Player player) {
		if (usesLibsDisguise && removeDisguiseEnabled && DisguiseAPI.isDisguised(player)) {
			DisguiseAPI.undisguiseToAll(player);
			if (removeModesMessageEnabled) {
				player.sendMessage(translateText(removeModesMessage.replaceAll("<mode>", "disguise")));
			}
		}
		if (usesiDisguise && removeDisguiseEnabled && dAPI.isDisguised((OfflinePlayer) player)) {
			dAPI.undisguise((OfflinePlayer) player);
			if (removeModesMessageEnabled)
				player.sendMessage(translateText(removeModesMessage.replaceAll("<mode>", "disguise")));
		}
	}

	public void removeFly(Player player) {
		if (player.isFlying() && removeFlyEnabled) {
			player.setFlying(false);
			if (removeModesMessageEnabled) {
				player.sendMessage(translateText(removeModesMessage.replaceAll("<mode>", "flight")));
			}
		}
	}

	public String tagTimeRemaining(String id) {
		return "" + (tagDuration - (getCurrentTime() - (Long) taggedPlayers.get(id).longValue()));
	}

	public long tagTime(String id) {
		return tagDuration - (getCurrentTime() - (Long) taggedPlayers.get(id).longValue());
	}

	public long getCurrentTime() {
		return System.currentTimeMillis() / 1000L;
	}

	public void broadcastMsg(String string) {
		getServer().broadcastMessage(translateText(string));
	}

	public void logInfo(String string) {
		log.info(translateText(string));
	}

	public String translateText(String string) {
		return ChatColor.translateAlternateColorCodes('&', "" + string);
	}
}