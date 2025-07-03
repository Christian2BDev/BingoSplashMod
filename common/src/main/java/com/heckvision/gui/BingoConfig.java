package com.heckvision.gui;

import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyCollector;
import gg.essential.vigilance.data.PropertyType;
import gg.essential.vigilance.data.SortingBehavior;

import java.io.File;
import java.util.Arrays;


public class BingoConfig extends Vigilant {

    @Property(
            type = PropertyType.SWITCH, name = "Bingo Mod",
            description = "Simple Toggle to disable all features.",
            category = "General", subcategory = "Main"
    )
    public static boolean enableBingoSplash = true;

    @Property(
            type = PropertyType.SWITCH, name = "Splash pings",
            description = "Toggle to enable",
            category = "Splashes", subcategory = "Splash"
    )
    public static boolean enableSplashPings = true;

    @Property(
            type = PropertyType.SWITCH, name = "Chat Message",
            description = "Display a chat message when a splash is announced",
            category = "Splashes", subcategory = "Splash"
    )
    public static boolean enableSplashPingsChat = true;

    @Property(
            type = PropertyType.SWITCH, name = "Title",
            description = "Display a title when a splash is announced",
            category = "Splashes", subcategory = "Splash"
    )
    public static boolean enableSplashPingsTitle = true;

    @Property(
            type = PropertyType.SWITCH, name = "Sound",
            description = "Play a sound when a splash is announced",
            category = "Splashes", subcategory = "Splash"
    )
    public static boolean enableSplashPingsSound = true;

    @Property(
            type = PropertyType.PERCENT_SLIDER, name = "Sound Volume",
            description = "Volume of the ping sound",
            category = "Splashes", subcategory = "Splash", maxF = 1.0f
    )
    public static float pingsSoundVolume = 1.0f;

//    @Property(
//            type = PropertyType.SWITCH, name = "Robot Automaton pings",
//            description = "Toggle to enable pings for Automaton parts",
//            category = "Automaton", subcategory = "Automaton"
//    )
    public static boolean enableAutomatonPartsPings = true;

//   @Property(
//           type = PropertyType.BUTTON, name = "Robot Automaton pings",
////            description = "Toggle to enable pings for Automaton parts",
////            category = "Notifications", subcategory = "Automaton",
////            options = {"","",""}
//   )
//    public int testAtribute = 0;




    public static BingoConfig INSTANCE = new BingoConfig("1.0");
    public BingoConfig(String VERSION) {
        super(new File("./config/BingoSplash.toml"), "BingoSplash (" + VERSION + ")"  );
        initialize();

        Arrays.asList(
                "enableSplashPingsChat", "enableSplashPingsTitle",
                "enableSplashPingsSound", "pingsSoundVolume"
        ).forEach(property -> addDependency(property, "enableSplashPings"));

        addDependency("pingsSoundVolume","enableSplashPingsSound");


    }
}
