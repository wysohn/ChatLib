package org.chatlib.manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.Plugin;
import org.chatlib.main.ChatLib;

/**
 * Managers must extend this class.
 * implement Listner as needed, but they will be registered automatically (which means do not call
 * PluginManager.registerEvents() manually)
 * however, it is not required to implement Listener unless you want to handle events
 * @author wysohn
 *
 */
public abstract class Manager {
	private static List<Manager> modules = new ArrayList<Manager>();
	
	protected ChatLib plugin;
	/**
	 * All managers
	 * @param plugin kingdoms plugin instance
	 */
	protected Manager(Plugin plugin){
		this.plugin = (ChatLib) plugin;

		modules.add(this);
	}
	
	/**
	 * get all managers
	 * @return list of managers
	 */
	public static List<Manager> getModules() {
		return modules;
	}
	
	/**
	 * perform appropriate actions on plugin disable depends on managers
	 */
	public abstract void onDisable();
}
