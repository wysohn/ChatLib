package org.chatlib.utils.nms.v1_8_R3;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.chatlib.utils.nms.IPacketSender;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class PacketSender extends IPacketSender {

	@Override
	public void sendPlayOutChat(Player player, String json, boolean dummy) {
		IChatBaseComponent icc = ChatSerializer.a(json);
		
		CraftPlayer cp = (CraftPlayer) player;
		PacketPlayOutChat packet = new PacketPlayOutChat(icc);
		cp.getHandle().playerConnection.sendPacket(packet);
	}

}
