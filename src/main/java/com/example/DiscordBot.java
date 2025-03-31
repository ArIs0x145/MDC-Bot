package com.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.IOException;
import javax.security.auth.login.LoginException;

public class DiscordBot {
    public static void main(String[] args) throws LoginException, IOException, InterruptedException{
        JDA bot = JDABuilder.createDefault(DefaultValue.Discord_Bot)
                .setActivity(Activity.customStatus("超越生死 Postmortal"))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .enableIntents(GatewayIntent.GUILD_MESSAGES)
                .enableIntents(GatewayIntent.DIRECT_MESSAGES)
                .build();
        
        bot.addEventListener(
            //new MessageListener(),
            new SlashListener()
        );

        bot.getRestPing().queue(ping -> System.out.println("Logged in with ping: " + ping));

        bot.awaitReady();
        System.out.println("Guilds: " + bot.getGuildCache().size());
    }
}
