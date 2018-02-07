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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ActionBar {

	public String nmserver;
	public boolean old = false;
	CombatLog plugin;

	public ActionBar(CombatLog plugin) {
		this.plugin = plugin;
	}

	public void getBukkitVersion() {
		nmserver = Bukkit.getServer().getClass().getPackage().getName();
		nmserver = nmserver.substring(nmserver.lastIndexOf(".") + 1);

		if (nmserver.equalsIgnoreCase("v1_8_R1") || nmserver.startsWith("v1_7_")) {
			old = true;
		}
	}

	public void sendActionBarOld(Player player, String msg) {
		 if (!player.isOnline()) {
	            return;
	        }
	        try {
	            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + nmserver + ".entity.CraftPlayer");
	            Object craftPlayer = craftPlayerClass.cast(player);
	            Object ppoc;
	            Class<?> c4 = Class.forName("net.minecraft.server." + nmserver + ".PacketPlayOutChat");
	            Class<?> c5 = Class.forName("net.minecraft.server." + nmserver + ".Packet");
	            if (old) {
	                Class<?> c2 = Class.forName("net.minecraft.server." + nmserver + ".ChatSerializer");
	                Class<?> c3 = Class.forName("net.minecraft.server." + nmserver + ".IChatBaseComponent");
	                Method m3 = c2.getDeclaredMethod("a", String.class);
	                Object cbc = c3.cast(m3.invoke(c2, "{\"text\": \"" + msg + "\"}"));
	                ppoc = c4.getConstructor(new Class<?>[]{c3, byte.class}).newInstance(cbc, (byte) 2);
	            } else {
	                Class<?> c2 = Class.forName("net.minecraft.server." + nmserver + ".ChatComponentText");
	                Class<?> c3 = Class.forName("net.minecraft.server." + nmserver + ".IChatBaseComponent");
	                Object o = c2.getConstructor(new Class<?>[]{String.class}).newInstance(msg);
	                ppoc = c4.getConstructor(new Class<?>[]{c3, byte.class}).newInstance(o, (byte) 2);
	            }
	            Method m1 = craftPlayerClass.getDeclaredMethod("getHandle");
	            Object h = m1.invoke(craftPlayer);
	            Field f1 = h.getClass().getDeclaredField("playerConnection");
	            Object pc = f1.get(h);
	            Method m5 = pc.getClass().getDeclaredMethod("sendPacket", c5);
	            m5.invoke(pc, ppoc);
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	}
	
	public void sendActionBarNew(Player player, String msg) {
		if(!player.isOnline())
			return;
		
		try {
			Class<?> playerClass = Class.forName("org.bukkit.craftbukkit." + nmserver + ".entity.CraftPlayer");
			Object p = playerClass.cast(player);
			Object ppoc;
			Class<?> c2 = Class.forName("net.minecraft.server." + nmserver + ".PacketPlayOutChat");
            Class<?> c3 = Class.forName("net.minecraft.server." + nmserver + ".Packet");
            Class<?> c4 = Class.forName("net.minecraft.server." + nmserver + ".ChatComponentText");
            Class<?> c5 = Class.forName("net.minecraft.server." + nmserver + ".IChatBaseComponent");
            Class<?> chatMessageTypeClass = Class.forName("net.minecraft.server." + nmserver + ".ChatMessageType");
            Object[] chatMessageTypes = chatMessageTypeClass.getEnumConstants();
            Object chatMessageType = null;
            for (Object obj : chatMessageTypes) {
                if (obj.toString().equals("GAME_INFO")) {
                    chatMessageType = obj;
                }
            }
            Object o = c4.getConstructor(new Class<?>[]{String.class}).newInstance(plugin.translateText(msg));
            ppoc = c2.getConstructor(new Class<?>[]{c5, chatMessageTypeClass}).newInstance(o, chatMessageType);
            Method m1 = playerClass.getDeclaredMethod("getHandle");
            Object h = m1.invoke(p);
            Field f1 = h.getClass().getDeclaredField("playerConnection");
            Object pc = f1.get(h);
            Method m2 = pc.getClass().getDeclaredMethod("sendPacket", c3);
            m2.invoke(pc, ppoc);
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}