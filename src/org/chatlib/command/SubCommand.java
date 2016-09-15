package org.chatlib.command;

import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.chatlib.main.CommandExecutor;

public abstract class SubCommand{
	private String name;
	private Set<String> aliases = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
	private String permission = null;
	private String description = "";
	private String[] usage = {};
	private int arguments  = -1;
	
	protected SubCommand(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Set<String> getAliases() {
		return aliases;
	}

	/**
	 * 
	 * @param alias
	 * @return true if not already contain the specified alias
	 */
	protected boolean addAlias(String alias){
		return aliases.add(alias);
	}
	
	/**
	 * 
	 * @param alias
	 * @return true if contained the specified alias
	 */
	protected boolean removeAlias(String alias){
		return aliases.remove(alias);
	}

	public String getPermission() {
		return permission;
	}

	protected void setPermission(String permission) {
		this.permission = permission;
	}
	
	public String getDescription() {
		return description;
	}

	protected void setDescription(String description) {
		this.description = description;
	}

	public String[] getUsage() {
		return usage;
	}

	protected void setUsage(String[] usage) {
		this.usage = usage;
	}

	public int getArguments() {
		return arguments;
	}

	protected void setArguments(int arguments) {
		this.arguments = arguments;
	}

	public boolean canUse(CommandSender sender){
		if(sender == null) return true;
		
		Player player = (Player) sender;
		if(player.isOp()) return true;
		
		return permission == null ? true : player.hasPermission(permission);
	}

	public void execute(CommandSender sender, Queue<String> args) {	
		if(sender == null){
			if(!executeConsole(sender, args)){
				for(String str : getUsage()){
					Bukkit.getLogger().info(ChatColor.stripColor(str));
				}
			}
		}else{
			Player player = (Player) sender;
			if(player.isOp()){
				if(!executeOp(player, args)){
					for(String str : getUsage()){
						sender.sendMessage(str);
					}
				}
			}else{
				if(!executeUser(player, args)){
					for(String str : getUsage()){
						sender.sendMessage(str);
					}
				}
			}
		}
	}
	
	protected abstract boolean executeConsole(CommandSender sender, Queue<String> args);
	protected abstract boolean executeOp(Player op, Queue<String> args);
	protected abstract boolean executeUser(Player op, Queue<String> args);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SubCommand other = (SubCommand) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
