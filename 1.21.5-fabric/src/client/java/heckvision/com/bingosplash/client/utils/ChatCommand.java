package heckvision.com.bingosplash.client.utils;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ChatCommand {


    private final String mainCommand;

    public ChatCommand(String mainCommand) {
        this.mainCommand = mainCommand;
    }
    private final Map<String, Consumer<MinecraftClient>> subcommands = new LinkedHashMap<>();
    private Consumer<MinecraftClient> rootCommand = null;

    /**
     * Registers a client-side command with the given command literal and action.
     *
     * @param commandName The literal name of the command (without slash).
     * @param action The void method to run when command executes (receives MinecraftClient instance).
     */
    public void createBingoCommand(String commandName, Consumer<MinecraftClient> action) {
        if (commandName.isEmpty()){
            rootCommand = action;
        }else{
            subcommands.put(commandName, action);
        }
    }

    public void registerBingoCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            var root = getFabricClientCommandSourceLiteralArgumentBuilder();

            SuggestionProvider<FabricClientCommandSource> subcommandSuggestions = (context, builder) -> {
                String remaining = builder.getRemainingLowerCase();
                for (String sub : subcommands.keySet()) {
                    if (sub.startsWith(remaining)) {
                        builder.suggest(sub);
                    }
                }
                return builder.buildFuture();
            };

            root.then(ClientCommandManager.argument("sub", StringArgumentType.word()).suggests(subcommandSuggestions).executes(ctx -> {
                String sub = StringArgumentType.getString(ctx, "sub");
                Consumer<MinecraftClient> action = subcommands.get(sub);
                MinecraftClient client = MinecraftClient.getInstance();

                if (action != null) {
                    action.accept(client);
                } else {
                    assert client.player != null;
                    client.player.sendMessage(Text.literal("Unknown subcommand: " + sub), false);
                }
                return 1;
            }));

            dispatcher.register(root);
        });
    }

    private @NotNull LiteralArgumentBuilder<FabricClientCommandSource> getFabricClientCommandSourceLiteralArgumentBuilder() {
        var root = ClientCommandManager.literal(mainCommand);

        //Bingo command
        root.executes(ctx -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (rootCommand != null) {
                rootCommand.accept(client);
            } else {
                assert client.player != null;
                client.player.sendMessage(Text.literal("Usage: /Bingo <" + String.join("|", subcommands.keySet()) + ">"), false);
            }
            return 1;
        });
        return root;
    }

}
