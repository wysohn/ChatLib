package org.chatlib.utils.nms.v1_9_R2;

import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.chatlib.utils.nms.IPacketSender;

import net.minecraft.server.v1_9_R2.IChatBaseComponent;
import net.minecraft.server.v1_9_R2.PacketPlayOutChat;
import net.minecraft.server.v1_9_R2.IChatBaseComponent.ChatSerializer;

public class PacketSender extends IPacketSender {

	@Override
	public void sendPlayOutChat(Player player, String json, boolean dummy) {
		IChatBaseComponent icc = ChatSerializer.a(json);
		
		CraftPlayer cp = (CraftPlayer) player;
		PacketPlayOutChat packet = new PacketPlayOutChat(icc);
		cp.getHandle().playerConnection.sendPacket(packet);
	}

}
