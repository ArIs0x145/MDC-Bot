package com.example;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter{
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event){
        User author = event.getAuthor();
        MessageChannelUnion channel = event.getChannel();
        Message message = event.getMessage();
        
        if (author.isBot()) {
            return;
        }

        channel.sendMessage(author.getEffectiveName()
            + " : "
            + message.getContentRaw()).queue();

        if (event.isFromGuild()) {
            System.out.printf("[%s] [%#s] %#s: %s\n",
                event.getGuild().getName(),
                channel,
                author,
                message.getContentDisplay()
            );
        }
        else {
            System.out.printf("[direct] %#s: %s\n",
                author,
                message.getContentDisplay()
            );
        }
        
        if (channel.getType() == ChannelType.TEXT) {
            System.out.println("The channel topic is " + channel.asTextChannel().getTopic());
        }

        if (channel.getType().isThread()) {
            System.out.println("This thread is part of channel #" +
                channel.asThreadChannel()
                       .getParentChannel()
                       .getName()
            );
        }
    }
}
