package heckvision.com.bingosplash.client.gui;

import com.heckvision.gui.BingoConfig;
import gg.essential.universal.UScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class BingoOpenConfig {
    private static long targetTime = -1;
    private static boolean actionDone = false;

    public static void openConfig() {
        long delayMillis = 1;
        targetTime = System.currentTimeMillis() + delayMillis;
        actionDone = false;

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!actionDone && targetTime > 0 && System.currentTimeMillis() >= targetTime) {
                client.execute(() -> {
                    UScreen.displayScreen(BingoConfig.INSTANCE.gui());

                });
                actionDone = true;
                targetTime = -1;
            }
        });
    }
}
