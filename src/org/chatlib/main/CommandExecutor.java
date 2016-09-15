package org.chatlib.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.chatlib.command.SubCommand;
import org.chatlib.command.admin.SubCommandAdmin;
import org.chatlib.command.user.SubCommandUser;
import org.chatlib.command.user.SubCommandUserAdmin;
import org.chatlib.main.LanguageSupport.Languages;

public class CommandExecutor {
	/**
	 * must be same as it is defined in the plugin.yml
	 */
	public static final String MAINCOMMAND = "maincommand";
	
	private static final List<SubCommand> commands = new ArrayList<SubCommand>(){{
		add(new SubCommandUserAdmin());
	}};
	private static final List<SubCommand> admincommands = new ArrayList<SubCommand>(){{
		
	}};	
	public static List<SubCommand> getCommands() {
		return commands;
	}
	public static List<SubCommand> getAdmincommands() {
		return admincommands;
	}

	private static final int MAXLINES = 6;
	
	public static String PLUGINNAME;
	
	private ChatLib plugin;

	CommandExecutor(Plugin plugin){
		this.plugin = (ChatLib) plugin;
		
		PLUGINNAME = plugin.getDescription().getFullName();
	}
	
	public void onCommand(final CommandSender sender, Command cmd, String label, String[] args){
		if(!cmd.getName().equalsIgnoreCase(MAINCOMMAND)) return;
		
		Player player = null;
		if(sender instanceof Player) player = (Player) sender;
		
		if(args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("help"))){
			final Map<String, String> list = new HashMap<String, String>();
			commands.forEach(new Consumer<SubCommand>() {
				@Override
				public void accept(SubCommand c) {
					if(c.canUse(sender)) list.put(c.getName(), c.getDescription());
				}
			});
			
			admincommands.forEach(new Consumer<SubCommand>(){
				@Override
				public void accept(SubCommand c) {
					if(c.canUse(sender)) list.put("admin "+c.getName(), c.getDescription());
				}
			});
			
			ChatLib.getLang().addString(PLUGINNAME);
			sender.sendMessage(ChatLib.getLang().parseFirstString(Languages.Command_Main_Header));
			
			int index = 0;
			for(Map.Entry<String, String> entry : list.entrySet()){
				if(index >= MAXLINES) break;
				
				String c = entry.getKey();
				String desc = entry.getValue();
				
				if(player != null){
					ChatLib.getLang().addString("/" + MAINCOMMAND + " " + c);
					ChatLib.getLang().addString(desc);
					if(c.contains("admin")){
						sender.sendMessage(ChatLib.getLang().parseFirstString(Languages.Command_Main_Help_Format_Admin));
					}else{
						sender.sendMessage(ChatLib.getLang().parseFirstString(Languages.Command_Main_Help_Format));
					}
				}else{
					sender.sendMessage("/"+MAINCOMMAND+" "+c+" "+desc);
				}
				
				index++;
			}
			
			sender.sendMessage(ChatColor.LIGHT_PURPLE +"");
			
			ChatLib.getLang().addInteger(1);
			if(list.size()%MAXLINES==0){
				ChatLib.getLang().addInteger(list.size()/MAXLINES);
			}else{
				ChatLib.getLang().addInteger(list.size()/(MAXLINES+1));
			}
			sender.sendMessage(ChatLib.getLang().parseFirstString(Languages.Command_Main_Help_PageDescription));
			
			sender.sendMessage(ChatLib.getLang().parseFirstString(Languages.Command_Main_Help_TypeHelpToSeeMore));
			sender.sendMessage(ChatColor.GRAY +"");
			return;
		}else if(args.length == 2 && args[0].equalsIgnoreCase("help")){
			int page = 0;
			try{
				page = Integer.parseInt(args[1]);
			}catch(NumberFormatException e){
				ChatLib.getLang().addString(args[1]);
				sender.sendMessage(ChatLib.getLang().parseFirstString(Languages.General_NotANumber));
				return;
			}
			
			page -= 1;
			
			if(page*MAXLINES >= commands.size()+admincommands.size() || page < 0){
				ChatLib.getLang().addString(args[1]);
				sender.sendMessage(ChatLib.getLang().parseFirstString(Languages.General_OutOfBound));
				
				ChatLib.getLang().addInteger(1);
				ChatLib.getLang().addInteger(commands.size()+admincommands.size());
				sender.sendMessage(ChatLib.getLang().parseFirstString(Languages.General_OutOfBound_RangeIs));
				return;
			}
			
			final Map<String, String> list = new HashMap<String, String>();
			commands.forEach(new Consumer<SubCommand>() {
				@Override
				public void accept(SubCommand c) {
					if(c.canUse(sender)) list.put(c.getName(), c.getDescription());
				}
			});
			
			admincommands.forEach(new Consumer<SubCommand>(){
				@Override
				public void accept(SubCommand c) {
					if(c.canUse(sender)) list.put("admin "+c.getName(), c.getDescription());
				}
			});
			
			ChatLib.getLang().addString(PLUGINNAME);
			sender.sendMessage(ChatLib.getLang().parseFirstString(Languages.Command_Main_Header));
			
			List<Map.Entry<String, String>> entries = new ArrayList<Map.Entry<String, String>>();
			entries.addAll(list.entrySet());
			
			for(int i=page*MAXLINES;i<(page+1)*MAXLINES;i++){
				if(i >= list.size()) break;
				
				String c = entries.get(i).getKey();
				String desc = entries.get(i).getValue();

				if(player != null){
					ChatLib.getLang().addString("/" + MAINCOMMAND + " " + c);
					ChatLib.getLang().addString(desc);
					if(c.contains("admin")){
						sender.sendMessage(ChatLib.getLang().parseFirstString(Languages.Command_Main_Help_Format_Admin));
					}else{
						sender.sendMessage(ChatLib.getLang().parseFirstString(Languages.Command_Main_Help_Format));
					}
				}else{
					sender.sendMessage("/"+MAINCOMMAND+" "+c+" "+desc);
				}
			}
			
			sender.sendMessage(ChatColor.LIGHT_PURPLE +"");
			
			ChatLib.getLang().addInteger(page+1);
			if(list.size()%MAXLINES==0){
				ChatLib.getLang().addInteger(list.size()/MAXLINES);
			}else{
				ChatLib.getLang().addInteger(list.size()/(MAXLINES+1));
			}
			sender.sendMessage(ChatLib.getLang().parseFirstString(Languages.Command_Main_Help_PageDescription));
			
			ChatLib.getLang().addString(MAINCOMMAND);
			sender.sendMessage(ChatLib.getLang().parseFirstString(Languages.Command_Main_Help_TypeHelpToSeeMore));
			sender.sendMessage(ChatColor.GRAY +"");
			return;
		}else{
			//TODO: sub commands
			Queue<String> argsLeft = new LinkedList<String>();
			for(int i=1;i<args.length;i++){
				argsLeft.add(args[i]);
			}
			
			if(argsLeft.size() > 1 && argsLeft.peek().equalsIgnoreCase("admin")){
				argsLeft.poll(); //consume admin part
				
				String subCmd = argsLeft.poll();
				for(SubCommand c : admincommands){
					if(!c.getName().equalsIgnoreCase(subCmd) && !c.getAliases().contains(subCmd)) continue;
					
					if(!c.canUse(sender)){
						ChatLib.getLang().addString(subCmd);
						sender.sendMessage(ChatLib.getLang().parseFirstString(Languages.Command_Main_NotEnoughPermission));
						return;
					}
					
					if(c.getArguments() != -1 && c.getArguments() != argsLeft.size()){
						ChatLib.getLang().addString(subCmd);
						sender.sendMessage(ChatLib.getLang().parseFirstString(Languages.Command_Main_ArgumentSizeNotMatch));
						
						sender.sendMessage(ChatLib.getLang().parseFirstString(Languages.Command_Main_Usage));
						for(String usage : c.getUsage()) {
							ChatLib.getLang().addString(usage);
							sender.sendMessage(ChatLib.getLang().parseFirstString(Languages.Command_Main_Usage_Format));
						}
						return;
					}
					
					c.execute(sender, argsLeft);
					
					return;
				}
				
				//could not found command matching
				ChatLib.getLang().addString(subCmd);
				sender.sendMessage(ChatLib.getLang().parseFirstString(Languages.Command_Main_NoSuchCommand_Admin));
			}else{	
				String subCmd = argsLeft.poll();
				for(SubCommand c : commands){
					if(!c.getName().equalsIgnoreCase(subCmd) && !c.getAliases().contains(subCmd)) continue;
					
					if(!c.canUse(sender)){
						ChatLib.getLang().addString(subCmd);
						sender.sendMessage(ChatLib.getLang().parseFirstString(Languages.Command_Main_NotEnoughPermission));
						return;
					}
					
					if(c.getArguments() != -1 && c.getArguments() != argsLeft.size()){
						ChatLib.getLang().addString(subCmd);
						sender.sendMessage(ChatLib.getLang().parseFirstString(Languages.Command_Main_ArgumentSizeNotMatch));
						
						sender.sendMessage(ChatLib.getLang().parseFirstString(Languages.Command_Main_Usage));
						for(String usage : c.getUsage()) {
							ChatLib.getLang().addString(usage);
							sender.sendMessage(ChatLib.getLang().parseFirstString(Languages.Command_Main_Usage_Format));
						}
						return;
					}
					
					c.execute(sender, argsLeft);
					
					return;
				}
				
				//could not found command matching
				ChatLib.getLang().addString(subCmd);
				sender.sendMessage(ChatLib.getLang().parseFirstString(Languages.Command_Main_NoSuchCommand));
			}
		}
	}
}
