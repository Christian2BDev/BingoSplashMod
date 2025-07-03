package heckvision.com.bingosplash.client.core;

import com.heckvision.gui.BingoConfig;
import com.heckvision.web.MessageManager;
import com.heckvision.web.WebSocketConnection;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public class ExecuteTasks {

    private final WebSocketConnection connection;

    public ExecuteTasks(){
        connection = new WebSocketConnection();
        MessageManager messageManager = connection.getMessageManager();

        //incoming messages
        messageManager.setSplashListener(this::Execute);
        messageManager.setAutomatonListener(this::Execute);

        //websocket connection
        ClientTickEvents.END_CLIENT_TICK.register(client -> connection.keepConnection());
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> connection.Shutdown());
    }

    private void Execute(String type,String message) {
        Text parsedMessage = Text.literal("§6§l[§r"+type+"§6§l]" +message);
        MinecraftClient client = MinecraftClient.getInstance();
        client.execute(() -> {
            if (client.player  != null) {
                ClientPlayerEntity player = client.player;
                if (BingoConfig.enableSplashPingsChat){
                    client.inGameHud.getChatHud().addMessage(parsedMessage);
                }
                if (BingoConfig.enableSplashPingsTitle){
                    client.inGameHud.setTitle(Text.literal(type));
                    client.inGameHud.setSubtitle(Text.literal(message));
                }
                if (BingoConfig.enableSplashPingsSound){
                    player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, BingoConfig.pingsSoundVolume , 1.0f);
                }

             }
        });
    }
}
