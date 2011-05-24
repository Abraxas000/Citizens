package com.fullwall.Citizens.Utils;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Constants;
import com.fullwall.Citizens.Economy.EconomyHandler;
import com.fullwall.Citizens.Economy.EconomyHandler.Operation;
import com.fullwall.Citizens.Economy.Payment;
import com.fullwall.Citizens.NPCTypes.Traders.ItemPrice;
import com.fullwall.Citizens.NPCTypes.Traders.Stockable;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Properties.Properties.UtilityProperties;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class MessageUtils {
	public static String noPermissionsMessage = ChatColor.RED
			+ "You don't have permission to use that command.";
	public static String notEnoughMoneyMessage = ChatColor.GRAY
			+ "You don't have enough money to do that.";
	public static String mustBeIngameMessage = "You must use this command ingame";
	public static String mustHaveNPCSelectedMessage = ChatColor.GRAY
			+ "You must have an NPC selected (right click).";
	public static String notOwnerMessage = ChatColor.RED
			+ "You are not the owner of this NPC.";

	public static String stripWhite(String check) {
		if (check.equals("�f"))
			return "";
		return check;
	}

	/**
	 * Parses a basic npc's text for sending.
	 * 
	 * @param npc
	 * @param player
	 * @param plugin
	 */
	public static void sendText(HumanNPC npc, Player player, Citizens plugin) {
		String text = getText(npc, player, plugin);
		if (!text.isEmpty())
			((Player) player).sendMessage(text);

	}

	/**
	 * Gets the text to be said for a basic npc.
	 * 
	 * @param npc
	 * @param player
	 * @param plugin
	 * @return
	 */
	public static String getText(HumanNPC npc, Player player, Citizens plugin) {
		String name = StringUtils.stripColour(npc.getStrippedName());
		int UID = npc.getUID();
		ArrayList<String> array = NPCManager.getText(UID);
		String text = "";
		if (array != null && array.size() > 0) {
			text = array.get(plugin.basicNPCHandler.ran.nextInt(array.size()));
		}
		if (text.isEmpty())
			text = UtilityProperties.getDefaultText();
		if (!text.isEmpty()) {
			if (Constants.useNPCColours) {
				text = Constants.chatFormat.replace("&", "�").replace("%name%",
						npc.getStrippedName())
						+ text;
			} else
				text = Constants.chatFormat.replace("%name%",
						Constants.NPCColour + name + ChatColor.WHITE).replace(
						"&", "�")
						+ text;
			return text;
		}
		return "";
	}

	/**
	 * Formats the not enough money message for an operation.
	 * 
	 * @param op
	 * @param player
	 * @return
	 */
	public static String getNoMoneyMessage(Operation op, Player player) {
		String message = "";
		message = ChatColor.RED
				+ "You need "
				+ StringUtils.wrap(
						EconomyHandler.getPaymentType(op,
								EconomyHandler.getRemainder(op, player),
								ChatColor.RED), ChatColor.RED)
				+ " more to do that.";
		return message;
	}

	/**
	 * Formats the paid message for an operation.
	 * 
	 * @param op
	 * @param paid
	 * @param npcName
	 * @param type
	 * @param useType
	 * @return
	 */
	public static String getPaidMessage(Operation op, double paid,
			String npcName, String type, boolean useType) {
		String message = "";
		message = ChatColor.GREEN
				+ "Paid "
				+ StringUtils.wrap(EconomyHandler.getPaymentType(op, "" + paid,
						ChatColor.GREEN)) + " for ";
		if (useType)
			message += StringUtils.wrap(npcName) + " to become a "
					+ StringUtils.wrap(type) + ".";
		else
			message += StringUtils.wrap(npcName) + ".";
		return message;
	}

	/**
	 * Formats the price message for an ItemPrice.
	 * 
	 * @param price
	 * @return
	 */
	public static String getPriceMessage(ItemPrice price, ChatColor colour) {
		String message = "";
		message += colour
				+ StringUtils.wrap(
						EconomyHandler.getCurrency(new Payment(price), colour),
						colour);
		return message;
	}

	private static String reverseStockableMessage(Stockable stockable,
			ChatColor colour) {
		return MessageUtils.getPriceMessage(stockable.getPrice(), colour)
				+ " for "
				+ StringUtils.wrap(stockable.getStocking().getAmount() + " "
						+ stockable.getStocking().getType().name(), colour)
				+ "(s)";
	}

	/**
	 * Formats the ItemStack contained in a stockable to a string.
	 * 
	 * @param s
	 * @param colour
	 * @return
	 */
	public static String getStockableMessage(Stockable s, boolean selling,
			ChatColor colour) {
		if (!selling)
			return StringUtils.wrap(s.getStocking().getAmount() + " "
					+ s.getStocking().getType().name(), colour)
					+ "(s) for "
					+ MessageUtils.getPriceMessage(s.getPrice(), colour);
		else
			return reverseStockableMessage(s, colour);
	}

	public static String getStackString(ItemStack stack, ChatColor colour) {
		return StringUtils.wrap(stack.getAmount() + " "
				+ stack.getType().name(), colour)
				+ "(s)";
	}

	public static String getStackString(ItemStack item) {
		return getStackString(item, ChatColor.YELLOW);
	}
}