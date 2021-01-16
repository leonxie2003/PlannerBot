package com.github.leonxie2003.plannerbot;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

public class Main {
	public static void main(String[] args)
	{
		DiscordApiBuilder builder = new DiscordApiBuilder();
		DiscordApi api = builder.setToken("ODAwMDU2ODQzMzkyMTIyOTMx.YAMk_w.WyMtW6ek5bDFLz10upbqlLqFQuA").login().join();
		
		api.addMessageCreateListener(event -> {
		    if (event.getMessageContent().equalsIgnoreCase("!ping")) {
		        event.getChannel().sendMessage("Pong!");
		    }
		});
	}
}
