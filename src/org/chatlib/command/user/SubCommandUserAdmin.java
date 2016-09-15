package org.chatlib.command.user;

import java.util.Queue;
import java.util.function.Consumer;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.chatlib.command.SubCommand;
import org.chatlib.main.ChatLib;
import org.chatlib.main.CommandExecutor;
import org.chatlib.main.LanguageSupport.Languages;

public class SubCommandUserAdmin extends SubCommandUser {

	public SubCommandUserAdmin() {
		super("admin");
		
		this.setArguments(-1);
		this.setPermission(CommandExecutor.MAINCOMMAND+".admin");
		this.setDescription(ChatLib.getLang().parseFirstString(Languages.Command_User_Admin_Desc));
		
		ChatLib.getLang().addString("/admin");
		ChatLib.getLang().addString("");
		this.setUsage(new String[]{
				ChatLib.getLang().parseFirstString(Languages.Command_User_Admin_Usage1)
		});
	}

	@Override
	protected boolean executeConsole(CommandSender sender, Queue<String> args) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean executeOp(Player op, Queue<String> args) {
		return executeUser(op, args);
	}

	@Override
	protected boolean executeUser(Player op, Queue<String> args) {
		showAllCommands(op);
		return true;
	}

	private static String[] adminCommands;
	private void showAllCommands(CommandSender sender){
		if(adminCommands == null) {
			adminCommands = new String[CommandExecutor.getAdmincommands().size()];
			for(int i=0;i<CommandExecutor.getAdmincommands().size(); i++){
				ChatLib.getLang().addString("/"+CommandExecutor.getAdmincommands().get(i).getName());
				ChatLib.getLang().addString(CommandExecutor.getAdmincommands().get(i).getDescription());
				adminCommands[i] = ChatLib.getLang().parseFirstString(Languages.Command_User_Admin_Format);
			}
		}
		
		sender.sendMessage(adminCommands);
	}
}
