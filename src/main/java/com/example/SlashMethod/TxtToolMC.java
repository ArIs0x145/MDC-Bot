package com.example.SlashMethod;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.example.DefaultValue;

public class TxtToolMC {
    private ArrayList<String> arrList = new ArrayList<>();
    private Integer length = 0;
    public final Integer attributeNum = 6;

    public TxtToolMC() {
        try {
            BufferedReader osReader = new BufferedReader(new FileReader(DefaultValue.MinecraftWhiteListTxt_Path));
            String line;
            while ((line = osReader.readLine()) != null) {
                arrList.add(line);
                length++;
            }
            osReader.close();
        }
        catch (IOException erro) {
            erro.printStackTrace();
        }
    }

    public List<List<String>> toList() {
        List<List<String>>  mcList = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            mcList.add(new ArrayList<>());
        }

        for (int i = 0; i < length; i++) {
            String[] arrStirng = traverseArrayList().get(i).toString().split(" ");
            for (String s : arrStirng) {
                mcList.get(i).add(s);
            }
        }
        return mcList;
    }

    public ArrayList<String> traverseArrayList() {
        try {
            BufferedReader osReader = new BufferedReader(new FileReader(DefaultValue.MinecraftWhiteListTxt_Path));
            String line;
            ArrayList<String> newArrayList= new ArrayList<>();
            Integer len = 0;
            while ((line = osReader.readLine()) != null) {
                newArrayList.add(line);
                len++;
            }
            osReader.close();
            this.length = len;
            this.arrList = newArrayList;
            return  arrList;
        }
        catch (IOException erro) {
            erro.printStackTrace();
            return  null;
        }
    }

    public List<String> getTagValue(String tag, String value, String tagReturn) {
        List<List<String>> userInfoList = getUserInfo(tag, value);
        if (userInfoList.isEmpty())    return new ArrayList<String>();

        List<String> returnList = new ArrayList<>();
        for (int col = 0; col < userInfoList.size(); col++) {
            String tagInList = userInfoList.get(col).get(0);
            if (tagEquals(tagReturn, tagInList)) {
                returnList.add(userInfoList.get(col).get(2));
            }
        }

        return returnList;
    }

    public List<List<String>> getUserInfo(String tag, String value) {
        List<List<String>> mcList = toList();
        if (mcList.equals(null))   return null;

        Integer index = 0;
        for (int i = 0; i < length; i ++) {
            // index = step (0 ~ tag_num)
            String tagInList = mcList.get(i).get(0);
            if (tagInList.isEmpty()) {
                index = 0;
                continue;
            }
            
            String valueInList = mcList.get(i).get(2);

            if (tagEquals(tag, tagInList) && value.equals(valueInList)) {
                List<List<String>> userInfoList= new ArrayList<>();
                for (int j = i - index; j < i - index + 6; j++) {
                    userInfoList.add(mcList.get(j));
                }
                return userInfoList;
            }
            index++;
        }
        return new ArrayList<>();
    }

    public boolean tagEquals(String tag1, String tag2) {
        switch (tag1) {
            case "DC_Name":
                switch (tag2) {
                    case "DC_Name":
                        return true;
                    case "DiscordName":
                        return true;
                    default:
                        return false;
                }
            case "DC_ID":
                switch (tag2) {
                    case "DC_ID":
                        return true;
                    case "DiscordID":
                        return true;
                    default:
                        return false;
                }
            case "DC_UUID":
                switch (tag2) {
                    case "DC_UUID":
                        return true;
                    case "DiscordUUID":
                        return true;
                    default:
                        return false;
                }
            case "MC_ID":
                switch (tag2) {
                    case "MC_ID":
                        return true;
                    case "MinecraftID":
                        return true;
                    default:
                        return false;
                }
            case "MC_Edit":
                switch (tag2) {
                    case "MC_Edit":
                        return true;
                    case "MinecraftEdit":
                        return true;
                    default:
                        return false;
                }
            case "MC_UUID":
                switch (tag2) {
                    case "MC_UUID":
                        return true;
                    case "MinecraftUUID":
                        return true;
                    default:
                        return false;
                }
            case "DC_ID_ALL":
                return tag2.equals("DC_ID_ALL") || tagEquals("DC_Name", tag2) || tagEquals("DC_ID", tag2) || tagEquals("DC_UUID", tag2);   
            case "User_Simple":
                return tag2.equals("User_Simple") || tagEquals("DC_Name", tag2) || tagEquals("DC_ID", tag2) || tagEquals("DC_UUID", tag2) || tagEquals("MC_ID", tag2);
            default:
                return false;
        }
    }

    public void wirrteInTxt(String mcList) {
        try {
            BufferedWriter osWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(DefaultValue.MinecraftWhiteListTxt_Path, false),
                    "UTF-8"
                    )
            );

            osWriter.write(mcList);
            osWriter.close();
        }
        catch (IOException erro) {
            erro.printStackTrace();
        }
    }
}