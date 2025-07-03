package heckvision.com.bingosplash.client;

import heckvision.com.bingosplash.client.core.ExecuteTasks;
import heckvision.com.bingosplash.client.gui.BingoOpenConfig;
import heckvision.com.bingosplash.client.utils.ChatCommand;
import net.fabricmc.api.ClientModInitializer;

public class BingosplashClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        //register commands
        ChatCommand bingoCommand = new ChatCommand("Bingo");
        bingoCommand.createBingoCommand("", client -> BingoOpenConfig.openConfig());
        bingoCommand.createBingoCommand("config", client -> BingoOpenConfig.openConfig());
        bingoCommand.registerBingoCommands();

        new ExecuteTasks();



    }
}

