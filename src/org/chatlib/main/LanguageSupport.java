package org.chatlib.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class LanguageSupport{
	private final File file;
	private final FileConfiguration config;
	
	private ChatLib plugin;
	private String lang;
	
	private Queue<Double> doub = new LinkedList<Double>();
	private Queue<Integer> integer = new LinkedList<Integer>();
	private Queue<String> string = new LinkedList<String>();
	private Queue<Boolean> bool = new LinkedList<Boolean>();
	
	LanguageSupport(Plugin plugin, String lang) throws IOException, InvalidConfigurationException{
		Validate.notNull(lang);
		
		File folder = new File(plugin.getDataFolder(), "lang");
		if(!folder.exists())
			folder.mkdirs();
		
		file = new File(folder, lang+".yml");
		if(!file.exists())
			file.createNewFile();
		
		config = new YamlConfiguration();
		
		this.plugin = (ChatLib) plugin;
		this.lang = lang;
		
		config.load(file);
		
		fillIfEmpty();
	}
	
	private Object load(String key){
		return config.get(key);
	}
	
	private void save(String key, Object value) throws IOException{
		config.set(key, value);
		config.save(file);
	}
	
	private void fillIfEmpty(){
		int i = 0;
		for(final Languages lang : Languages.values()){
			String str = lang.toString();
			
			if(this.load(str) == null){
				try {
					this.save(str, new ArrayList<String>(){{
						add(lang.getDefault());
						}});
					Bukkit.getLogger().info("Added language setting "+str);
				} catch (IOException e) {
					Bukkit.getLogger().info("Could not fill empty string "+str);
					e.printStackTrace();
				}
			}
		}
		Bukkit.getLogger().info("Loaded " + i + " language statements");
	}
	
	@SuppressWarnings("unchecked")
	public String[] parseStrings(Languages lang){
		List<String> str = new ArrayList<String>();
		str.addAll((List<String>) this.load(lang.toString()));
		
		Validate.notNull(str);
		
		replaceVariables(str);
		
		for(int i=0;i<str.size();i++){
			str.set(i , lang + " is null");
		}
		
		return str.toArray(new String[str.size()]);
	}
	
	@SuppressWarnings("unchecked")
	public String parseFirstString(Languages lang){
		List<String> str = new ArrayList<String>();
		str.addAll((List<String>) this.load(lang.toString()));
		
		Validate.notNull(str);
		if(str.size() == 0){
			ChatLib.logDebug("str is empty?");
			return null;
		}
		
		if(str.size() > 1){
			for(int i=1;i<str.size();i++){
				str.remove(i);
			}
		}

		replaceVariables(str);
		
		ChatLib.logDebug("return: "+str.get(0));
		return str.get(0) == null ? lang + " is null" : str.get(0);
	}
	
	/**
	 * @author Hex_27
	 * @param msg
	 * @return
	 */
	public String colorize(String msg)
    {
        String coloredMsg = "";
        for(int i = 0; i < msg.length(); i++)
        {
            if(msg.charAt(i) == '&')
                coloredMsg += '§';//§
            else
                coloredMsg += msg.charAt(i);
        }
        return coloredMsg;
    }
	
	private void replaceVariables(List<String> strings){
		ChatLib.logDebug("replaceVar");
		for(int i = 0;i < strings.size(); i++){
			String str = strings.get(i);
			if(str == null) continue;
			
			ChatLib.logDebug("color trans before: "+str);
			str = colorize(str);
			ChatLib.logDebug("color trans after: "+str);
			
			if(str.contains("${")){
				int start = -1;
				int end = -1;
				
				while(!((start = str.indexOf("${")) == -1 || 
						(end = str.indexOf("}")) == -1)){
					
					String leftStr = str.substring(0, start);
					String rightStr = str.substring(end + 1, str.length());
					
					String varName = str.substring(start + 2, end);
					
					ChatLib.logDebug("left:"+leftStr+" right:"+rightStr+" var:"+varName);
					
					switch(varName){
					case "double":
						str = leftStr+String.valueOf(this.doub.poll())+rightStr;
						break;
					case "integer":
						str = leftStr+String.valueOf(this.integer.poll())+rightStr;
						break;
					case "string":
						str = leftStr+String.valueOf(this.string.poll())+rightStr;
						break;
					case "bool":
						str = leftStr+String.valueOf(this.bool.poll())+rightStr;
						break;
					case "commandname":
						str = leftStr+String.valueOf(CommandExecutor.MAINCOMMAND)+rightStr;
						break;
						default:
							str = leftStr+String.valueOf("[?]")+rightStr;
							break;
					}
				}	
			}
			
			strings.set(i, str);
		}
		
		this.doub.clear();
		this.integer.clear();
		this.string.clear();
		this.bool.clear();
	}
	
	public void addDouble(double doub){
		this.doub.add(Double.valueOf(doub));
	}
	
	public void addInteger(int integer){
		this.integer.add(Integer.valueOf(integer));
	}
	
	public void addString(String string){
		Validate.notNull(string);
		
		this.string.add(string);
	}
	
	public void addBoolean(boolean bool){
		this.bool.add(Boolean.valueOf(bool));
	}
	
	public static enum Languages{
		Plugin_NotEnabled("Plugin is not enabled. "),
		Plugin_SetEnableToTrue("Please check your setting at config.yml to make sure it's enabled."),
		Plugin_WillBeDisabled("Plugin will be disabled."),
		Plugin_VersionNotSupported("${string} is not supported by this plugin!"),
		
		Plugin_Vault_Init_Failed("Couldn't initialize Vault's ${string}."),
		
		General_NotANumber("&c${string} is not a number!"),
		General_OutOfBound("&c${string} is out of bound!"),
		General_OutOfBound_RangeIs("&crange: &6${integer} &7< &fvalue &7< &6${integer}"),
		
		Command_Main_Header("&7======== &6${string}&7 ========"), 
		Command_Main_NotEnoughPermission("&cYou do not have enough permission to use command &6${string}&c!"),
		Command_Main_NoSuchCommand("&cNo such command &6${string}&c."),
		Command_Main_NoSuchCommand_Admin("&cNo admin command &6${string}&c. Type &d/{commandname} admin&c to see the list."),
		Command_Main_ArgumentSizeNotMatch("&cSyntax error while executing &6${string}&c."),
		Command_Main_Usage("&cusages:"),
		Command_Main_Usage_Format("  ${string}"),
		
		Command_Main_Help_PageDescription("&6Page &7${integer}/${integer}"),
		Command_Main_Help_TypeHelpToSeeMore("&6Type &6/${commandname} help &7<page> &6to see next pages."), 
		Command_Main_Help_Format("&6${string} &7- &f${string}"),
		Command_Main_Help_Format_Admin("&d${string} &5- &7${string}"),  
		
		Command_User_Admin_Desc("Show list of admin commands."), 
		Command_User_Admin_Usage1("/${commandname} admin"),
		Command_User_Admin_Format("&d/${commandname} admin ${string} &5- &7${string}"), 
		
		HoverMesssage_Name("&6Name: ${string}"), 
		HoverMesssage_Group("&7Name: ${string}"), 
		HoverMesssage_ClickToWhisper("&d[Click to whisper]"), 
		HoverMesssage_Url_Warning("&cThis URL might contain malicious software. Please do not click it if it looks suspicious!"), 
		;
		
		private String defaultSring;
		
		private Languages(String defaultString){
			this.defaultSring = defaultString;
		}
		
		public String getDefault(){
			return this.defaultSring;
		}
	}
}
