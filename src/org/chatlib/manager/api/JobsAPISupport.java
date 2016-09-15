package org.chatlib.manager.api;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.chatlib.main.ChatLibAPI;

import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.api.JobsJoinEvent;
import com.gamingmesh.jobs.api.JobsLeaveEvent;
import com.gamingmesh.jobs.container.JobProgression;
import com.gamingmesh.jobs.container.JobsPlayer;

public class JobsAPISupport implements Listener{
	private final Plugin plugin;
	
	public JobsAPISupport(Plugin plugin) {
		this.plugin = plugin;
	}

	private static final String JOBS = "Jobs";
	
	@EventHandler
	public void onJoinJob(JobsJoinEvent e){
		JobsPlayer jplayer = e.getPlayer();
		putJobsInfo(jplayer);
	}
	
	@EventHandler
	public void onQuitJob(JobsLeaveEvent e){
		deleteJobsInfo(e.getPlayer().getPlayer());
	}
	
	public void putJobsInfo(Player player){
		JobsPlayer jplayer = Jobs.getPlayerManager().getJobsPlayer(player);
		putJobsInfo(jplayer);
	}
	
	private void putJobsInfo(final JobsPlayer jplayer){
		if(jplayer.getJobProgression().isEmpty())
			return;
		
		String jobsInfo = ChatColor.YELLOW+"Jobs: ";
		int count = 0;
		for(JobProgression prog : jplayer.getJobProgression()){
			jobsInfo += ChatColor.GRAY+"["+ChatColor.GOLD+prog.getJob().getName()+ChatColor.DARK_GREEN+" lv "+prog.getLevel()+ChatColor.GRAY+"]";
			jobsInfo += " ";
			count++;
			if(count % 5 == 0){
				jobsInfo += "\n  ";
			}
		}
		
		final String info = jobsInfo;
		Bukkit.getScheduler().runTask(plugin, new Runnable(){
			@Override
			public void run() {
				ChatLibAPI.getInfo(jplayer.getPlayer()).put(JOBS, info);
			}
		});
	}
	
	private void deleteJobsInfo(final Player player){
		Bukkit.getScheduler().runTask(plugin, new Runnable(){
			@Override
			public void run() {
				ChatLibAPI.getInfo(player).remove(JOBS);
			}
		});
	}
}
