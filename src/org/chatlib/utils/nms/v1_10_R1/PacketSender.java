package org.chatlib.utils.nms.v1_10_R1;

import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.chatlib.utils.nms.IPacketSender;

import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_10_R1.PacketPlayOutChat;

public class PacketSender extends IPacketSender {

	@Override
	protected void sendPlayOutChat(Player player, String json, boolean dummy) {
		IChatBaseComponent icc = ChatSerializer.a(json);
		
		CraftPlayer cp = (CraftPlayer) player;
		PacketPlayOutChat packet = new PacketPlayOutChat(icc);
		cp.getHandle().playerConnection.sendPacket(packet);
	}

}
