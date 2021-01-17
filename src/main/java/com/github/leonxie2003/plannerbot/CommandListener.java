package com.github.leonxie2003.plannerbot;

import java.util.StringTokenizer;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class CommandListener implements MessageCreateListener {
	private DiscordApi api;
	private Plan plan;
	
	public CommandListener(DiscordApi api, Plan p) {
		this.api = api;
		plan = p;
	}
	
	@Override
	public void onMessageCreate(MessageCreateEvent event) {
		String message = event.getMessageContent();
		StringTokenizer st = new StringTokenizer(message);
		String command = st.nextToken();
		TextChannel channel = event.getChannel();
		
		if (command.equals("!ping")) {
			channel.sendMessage("pong!");
	    } else if (command.equals("!newplan")) {
			executeNewPlan(plan, channel);
		} else if (command.equals("!add")) { 
			executeAdd(plan, channel, st);
		} else if (command.equals("!plan")) {
			executePlan(plan, channel);
		} else if (command.equals("!addbreak")) {
			executeAddBreak(plan, channel, st);
		} else if (command.equals("!start")) {
			executeStart(api, plan, channel);
		} else if (command.equals("!undo")) {
			executeUndo(plan, channel);
		} else if (command.equals("!end")) {
			executeEnd(plan, channel);
		}
	}
	
	private static void executeNewPlan(Plan plan, TextChannel channel) {
		plan.reset();
		channel.sendMessage("Creating new plan...");
	}
	
	private static void executeAdd(Plan plan, TextChannel channel, StringTokenizer st) {
		String name = st.nextToken();
		String t = st.nextToken();
		try {
			double time = Double.parseDouble(t);
			plan.addTask(name, time);
			channel.sendMessage("Adding " + name + ", lasting " + time + " minutes.");
		} catch(NumberFormatException e) {
			channel.sendMessage("Please enter a number for the time in minutes.");
		}
	}
	
	private static void executePlan(Plan plan, TextChannel channel) {
		MessageBuilder mb = new MessageBuilder();
		mb.setEmbed(new EmbedBuilder()
				.setTitle("Plan")
				.setDescription(plan.toString()));
		mb.send(channel);
	}
	
	private static void executeAddBreak(Plan plan, TextChannel channel, StringTokenizer st) {
		String t = st.nextToken();
		try {
			double time = Double.parseDouble(t);
			plan.addBreak(time);
			channel.sendMessage("Adding a break lasting " + time + " minutes.");
		} catch(NumberFormatException e) {
			channel.sendMessage("Please enter a number for the time in minutes.");
		}
	}

	private static void executeStart(DiscordApi api, Plan plan, TextChannel channel) {
		ScheduledExecutorService scheduler = api.getThreadPool().getScheduler();
		long timeElapsed = 0;
		for(int i = 0; i < plan.taskListSize(); i++) {
			Task task = plan.getTask(i);
			String name = task.getName();
			long time = (long) (task.getTime() * 60);
			scheduler.schedule(new MessageSender(name + " is starting...", channel), timeElapsed, TimeUnit.SECONDS);
			scheduler.schedule(new MessageSender("You should still be working on: " + name, channel), time/2 + timeElapsed, TimeUnit.SECONDS);
			scheduler.schedule(new MessageSender(name + " is complete!", channel), time + timeElapsed, TimeUnit.SECONDS);
			timeElapsed += time;
		}
		channel.sendMessage("Your plan has been completed!");
	}
	
	private static void executeUndo(Plan plan, TextChannel channel) {
		plan.removeTask(plan.getTask(plan.taskListSize()).getName());
		channel.sendMessage("Undo completed!");
	}
	
	private static void executeEnd(Plan plan, TextChannel channel) {
		plan.reset();
		channel.sendMessage("Your plan ended early!");
	}
}
