package net.mysticrealms.fireworks.capslock;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class CapsLockListener implements Listener {

	private CapsLock plugin;

	public CapsLockListener(CapsLock capslock) {
		plugin = capslock;
	}


	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player p;
		String message;		
		
		message = event.getMessage();
		p = event.getPlayer();
		
		if(p.hasPermission("capslock.ignore")){
			return;
		}
		
		if (plugin.enableNoCaps) {

			int[] newMessage = checkCaps(message);

			if (percentageCaps(newMessage) >= plugin.totalCapsPercentage || checkCapsInRow(newMessage) >= plugin.capsInRow) {

				if (plugin.enableForceLowercase == false && plugin.enableMessageOverride == false) {
					event.setCancelled(true);
				}

				if (plugin.enableMessageOverride) {
					event.setMessage(plugin.overrideMessage);
				} else if (plugin.enableForceLowercase) {
					String[] parts = message.split(" ");
					boolean allowedCaps = false;
					for (int i = 0; i < parts.length; i++) {
						boolean whitelisted = false;
						for (String whiteWord : plugin.whitelist) {

							if (whiteWord.equalsIgnoreCase(parts[i])) {
								whitelisted = true;
								allowedCaps = true;
								break;
							}
						}

						if (whitelisted) {
							continue;
						} else if (!allowedCaps) {
							char firstChar = parts[i].charAt(0);
							parts[i] = firstChar + parts[i].toLowerCase().substring(1);
						} else {
							parts[i] = parts[i].toLowerCase();
						}

						allowedCaps = !(parts[i].endsWith(".") || parts[i].endsWith("!"));

					}

					event.setMessage(StringUtils.join(parts, " "));

				} else {
					event.setCancelled(true);
				}

				if (plugin.enablePlayerMessage)
					p.sendMessage(ChatColor.RED + plugin.playerMessage);
			}
		}

	}

	public int[] checkCaps(String message) {
		int[] newMessage = new int[message.length()];
		for (int i = 0; i < message.length(); i++) {
			if (Character.isUpperCase(message.charAt(i)) && Character.isLetter(message.charAt(i))) {
				newMessage[i] = 1;
			} else {
				newMessage[i] = 0;
			}
		}
		return newMessage;
	}

	public int percentageCaps(int[] caps) {
		int sum = 0;
		for (int i = 0; i < caps.length; i++) {
			sum += caps[i];
		}
		double ratioCaps = (double) sum / (double) caps.length;
		int percentCaps = (int) (100 * (ratioCaps));
		return percentCaps;
	}

	public int checkCapsInRow(int[] caps) {
		int sum = 0;
		int sumTemp = 0;

		for (int i : caps) {
			if (i == 1) {
				sumTemp++;
				sum = Math.max(sum, sumTemp);
			} else {
				sumTemp = 0;
			}
		}
		return sum;
	}
}
