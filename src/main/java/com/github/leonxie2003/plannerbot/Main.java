package com.github.leonxie2003.plannerbot;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

public class Main {
	public static void main(String[] args)
	{
		DiscordApiBuilder builder = new DiscordApiBuilder();
		DiscordApi api = builder.setToken("ODAwMDU2ODQzMzkyMTIyOTMx.YAMk_w.Vpj0t_h8VgU3kZNldznePw_oWeo").login().join();
		
		Plan plan = new Plan();
		
		api.addListener(new CommandListener(api, plan));
	}
}
