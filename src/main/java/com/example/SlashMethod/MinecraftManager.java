package com.example.SlashMethod;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import com.example.DefaultValue;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class MinecraftManager {
    public void addMinecraftWhiteList(SlashCommandInteractionEvent event, String MC_Edit, String MC_ID) {
        TxtToolMC tool = new TxtToolMC();
        String caller_DC_UUID = event.getUser().getId();
        if(!tool.getTagValue("DC_UUID", caller_DC_UUID, "DC_UUID").isEmpty()) {
            List<String> caller_MC_Edit = tool.getTagValue("DC_UUID", caller_DC_UUID, "MC_Edit");
            List<String> caller_MC_ID = tool.getTagValue("DC_UUID", caller_DC_UUID, "MC_ID");
            if (
                !MC_Edit.equals(caller_MC_Edit.get(0))
                || 
                !MC_ID.equals(caller_MC_ID.get(0))
                ) {
                
                if (getUuid(event, MC_ID).isEmpty()) {
                    event.reply(
                        "平台: " + MC_Edit + '\n' +
                        "ID: " + MC_ID + '\n' +
                        " -> 更新失敗" + '\n' +
                        "原因: " + "錯誤ID"
                    ).queue();
                    return;
                }
                else {
                    event.reply(
                        "平台: " + MC_Edit + '\n' +
                        "ID: " + MC_ID + '\n' +
                        " -> 更新成功"
                    ).queue();
                    return;
                }
            }
            else {
                event.reply(
                    "平台: " + MC_Edit + '\n' +
                    "ID: " + MC_ID + '\n' +
                    " -> 綁定失敗" + '\n' +
                    "原因: " + "重複綁定"
                ).queue();
                return;
            } 
        }
        String listMajangUesrINFO = getUuid(event, MC_ID);
        if (listMajangUesrINFO.isEmpty()) {
            event.reply(
                "平台: " + MC_Edit + '\n' +
                "ID: " + MC_ID + '\n' +
                " -> 綁定失敗" + '\n' +
                "原因: " + "錯誤ID"
            ).queue();
            return;
        }
        JSONObject objJson = new JSONObject(listMajangUesrINFO); 

        // Write txt
        try {
            BufferedWriter osWriter = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(DefaultValue.MinecraftWhiteListTxt_Path, true),
                "UTF-8"
                )
            );

            osWriter.write(
                "DiscordName : " + event.getUser().getGlobalName() + '\n' +
                "DiscordID : " + event.getUser().getName() + '\n' +
                "DiscordUUID : " + event.getUser().getId() + '\n' +
                "MinecraftEdit : " + MC_Edit + '\n' +
                "MinecraftID : " + MC_ID + '\n' +
                "MinecraftUUID : " + objJson.getString("id") + "\n\n"
            );
            osWriter.close();

            event.reply(
            "平台: " + MC_Edit + '\n' +
            "ID: " + MC_ID + '\n' +
            "-> 綁定成功" + '\n' +
            "ServerIP: " + DefaultValue.MinecraftServerIP_Path
            ).queue();
        }
        catch (IOException erro) {
            erro.printStackTrace();
        }
        
        // Write json
        try {
            RandomAccessFile osWriter = new RandomAccessFile(DefaultValue.MinecraftWhiteListJson_Path, "rw");

            JSONObject listJson = new JSONObject();
            listJson.put("uuid", objJson.getString("id"));
            listJson.put("name", objJson.getString("name"));
            
            String str = "[\n " + listJson.toString() + "\n]";

            if (osWriter.length() != 0) {
                str = ",\n " + listJson.toString() + "\n]";
                osWriter.seek(osWriter.length() - 2);
            }
            osWriter.write(str.getBytes());
            osWriter.close();
        }
        catch (IOException erro) {
            erro.printStackTrace();
        }
    }

    private String getUuid(SlashCommandInteractionEvent event, String MC_ID) {
        try {
            URL apiUrl = new URL(DefaultValue.MinecraftMajangAPI_Url + MC_ID);

            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader osReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            StringBuilder text = new StringBuilder();
            while ((line = osReader.readLine()) != null) {
                text.append(line + '\n');
            }
            osReader.close();

            return text.toString();

        } catch (IOException erro) {
            //erro.printStackTrace();
            return new String();
        }
    }


    public void traverseMinecraftWhiteList(SlashCommandInteractionEvent event) {
        try {
            BufferedReader osReader = new BufferedReader(new FileReader(DefaultValue.MinecraftWhiteListJson_Path));
            String line;
            String text = new String();
            while ((line = osReader.readLine()) != null) {
                text += line + '\n';
            }

            osReader.close();

            if (text.equals(new String())) {
                event.reply("無資料").queue();
            }
            else {
                event.reply(text).queue();
            }
        }
        catch (IOException erro) {
            erro.printStackTrace();
        }
    }
    public void searchMinecraftWhiteList(SlashCommandInteractionEvent event, String attribute , String value) {
        List<String> valueList= new ArrayList<>();
        TxtToolMC txt = new TxtToolMC();
        switch (attribute) {
            case "MC_ID":
                if (!(valueList = txt.getTagValue("MC_ID", value, "User_Simple")).isEmpty()) {
                    event.reply(
                        "Minecraft玩家名: " + valueList.get(3) + "\n" +
                        "Discord用戶名: " + valueList.get(0) + "\n" +
                        "Discord用戶ID: " + valueList.get(1) + "\n" +
                        "Discord用戶UUID: " + valueList.get(2)
                    ).queue();
                }
                else {
                    event.reply("該用戶未綁定").queue();
                }
                break;
            case "DC_ID_ALL":
                if (!(valueList = txt.getTagValue("DC_ID_ALL", value, "User_Simple")).isEmpty()) {
                    event.reply(
                        "Minecraft玩家名: " + valueList.get(3) + "\n" +
                        "Discord用戶名: " + valueList.get(0) + "\n" +
                        "Discord用戶ID: " + valueList.get(1) + "\n" +
                        "Discord用戶UUID: " + valueList.get(2)
                    ).queue();
                }
                else {
                    event.reply("該用戶未綁定").queue();
                }
                break;
            default:
                System.out.println("Methon of searchMinecraftWhiteList was Erro!!!");
                break;
        }
    }

    public void deleteMinecraftWhiteList(SlashCommandInteractionEvent event, String DC_UUID) {
        TxtToolMC tool = new TxtToolMC();
        List<List<String>> mcList = tool.toList();
        if (mcList.isEmpty()) {
            event.reply("白名單為空，無法解綁").queue();
            return;
        }

        List<String> caller_DC_Name = tool.getTagValue("DC_UUID", DC_UUID, "DC_Name");
        String mcArrayListCopy = "";
        for (int i = 0; i < mcList.size(); i++) {
            if (! mcList.get(i).isEmpty()) {
                if (mcList.get(i).get(0).equals("DiscordName") && mcList.get(i).get(2).equals(caller_DC_Name.get(0))) {
                    i += tool.attributeNum;
                }
                else {
                    mcArrayListCopy += mcList.get(i).get(0) + " " + mcList.get(i).get(1) + " " + mcList.get(i).get(2) + "\n";
                }  
            }
            else {
                mcArrayListCopy += "\n";
            }
        }
        tool.wirrteInTxt(mcArrayListCopy);
        event.reply("用戶已解綁成功").queue();;
    }
}
