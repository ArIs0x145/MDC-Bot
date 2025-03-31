package com.example;

import org.jetbrains.annotations.NotNull;

import com.example.SlashMethod.MinecraftManager;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.Command.Choice;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

public class SlashListener extends ListenerAdapter{
    
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        CommandListUpdateAction commands = event.getJDA().updateCommands();

        commands.addCommands(
            Commands.slash("綁定minecraft遊戲帳號", "添加至Server白名單 / 更新綁定資料")
                .addOptions(
                    new OptionData(OptionType.STRING, "minecraft平台", "Minecraft常用的平台", true)
                        .addChoices(
                            new Choice("java版", "JAVA"),
                            new Choice("基岩版", "BEDROCK")
                        ), 
                    new OptionData(OptionType.STRING, "minecraft玩家名稱", "Minecraft玩家ID", true)
                )
                .setGuildOnly(true)
        );

        commands.addCommands(
            Commands.slash("查看server白名單", "遍歷server白名單")
                .setGuildOnly(true)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_ROLES))
        );

        commands.addCommands(
            Commands.slash("查找玩家名", "查詢Minecraft綁定資訊")
                .addOptions(
                    new OptionData(OptionType.STRING, "minecraft玩家名稱", "Minecraft玩家ID", false), 
                    new OptionData(OptionType.STRING, "discord用戶", "Discord用戶名 / Discord用戶ID / Discord用戶UUID", false)
                )
                .setGuildOnly(true)
        );

        commands.addCommands(
            Commands.slash("查詢伺服器ip", "MinecraftServerIP")
                .setGuildOnly(true)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_ROLES))
        );

        commands.addCommands(
            Commands.slash("解除綁定minecraft遊戲帳號", "從Server白名單中剔除資料")
                .setGuildOnly(true)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_ROLES))
        );

        commands.addCommands(
            Commands.slash("refresh", "刷新Bot")
                .setGuildOnly(true)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_ROLES))
        );

        commands.queue();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getGuild() == null)
            return;
        MinecraftManager manager = new MinecraftManager();
        switch (event.getName()) {
            case "綁定minecraft遊戲帳號":
                manager.addMinecraftWhiteList(
                    event,
                    event.getOption("minecraft平台").getAsString(),
                    event.getOption("minecraft玩家名稱").getAsString()
                );
                break;
            case "查看server白名單":
                manager.traverseMinecraftWhiteList(event);
                break;
            case "查找玩家名" : 
                if (event.getOption("minecraft玩家名稱") != null) {
                    if (event.getOption("discord用戶") != null) {
                        event.reply("請勿同時輸入兩個ID").queue();
                        break;
                    }

                    manager.searchMinecraftWhiteList(
                        event, 
                        "MC_ID", 
                        event.getOption("minecraft玩家名稱").getAsString()
                    );
                }
                else if (event.getOption("discord用戶") != null) {
                    manager.searchMinecraftWhiteList(
                        event, 
                        "DC_ID_ALL", 
                        event.getOption("discord用戶").getAsString()
                    );
                }
                else {
                    event.reply("未輸入ID").queue();
                }
                break;
            case "查詢伺服器ip":
                event.reply(DefaultValue.MinecraftServerIP_Path).queue();
                break;
            case "解除綁定minecraft遊戲帳號":
                manager.deleteMinecraftWhiteList(event, event.getUser().getId());
                break;
            case "refresh":
                event.reply("刷新成功").queue();
                break;
            default:
                event.reply("錯誤/指令").setEphemeral(true).queue();
                break;
                
        }
    }
}
