# Minecraft Discord Bot

一個用於管理Minecraft伺服器白名單的Discord機器人，可讓玩家透過Discord綁定他們的Minecraft帳號。

## 功能特色

- 透過Discord斜線命令管理Minecraft伺服器白名單
- 允許玩家將他們的Discord帳號與Minecraft帳號綁定
- 自動驗證Minecraft玩家名稱
- 提供白名單查詢功能
- 支援Java版和基岩版的Minecraft帳號

## 技術棧

- Java 17
- Maven
- JDA (Java Discord API) 5.0.0-beta.24
- SLF4J
- JSON

## 使用前準備

### 必要條件

- Java 17 或更高版本
- Maven
- Discord Bot Token
- Discord Guild ID

### 安裝步驟

1. 克隆此倉庫
   ```
   git clone https://github.com/YourUsername/minecraft-discord-bot.git
   cd minecraft-discord-bot
   ```

2. 配置 `DefaultValue.java` 文件
   - 重命名 `src/main/java/com/example/DefaultValue.java.example` 為 `DefaultValue.java`（如果存在）
   - 或者創建新文件並填入以下內容：
   ```java
   package com.example;

   public class DefaultValue {
       public static final String  Discord_Bot = "YOUR_DISCORD_BOT_TOKEN";
       public static final String  Guild = "YOUR_GUILD_ID";
       public static final String  MinecraftWhiteListTxt_Path = "demo\\src\\main\\resource\\MinecraftWhiteList.txt";
       public static final String  MinecraftWhiteListJson_Path = "demo\\src\\main\\resource\\MinecraftWhiteList.json";
       public static final String  MinecraftMajangAPI_Url = "https://api.mojang.com/users/profiles/minecraft/";
       public static final String  MinecraftServerIP_Path = "YOUR_SERVER_IP";
   }
   ```

3. 使用Maven編譯
   ```
   mvn clean package
   ```

4. 運行Bot
   ```
   java -jar target/demo-1.0-SNAPSHOT.jar
   ```

## 使用說明

### 可用的斜線命令

- `/綁定minecraft遊戲帳號` - 添加Minecraft玩家至白名單或更新綁定資料
- `/查看server白名單` - 查看所有綁定的玩家
- `/查找玩家名` - 查詢特定玩家的綁定資訊
- `/查詢伺服器ip` - 獲取Minecraft伺服器IP地址
- `/解除綁定minecraft遊戲帳號` - 從白名單中移除玩家
- `/refresh` - 刷新Bot
