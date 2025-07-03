package com.heckvision.web;

import com.heckvision.gui.BingoConfig;

public class MessageManager implements MessageListener {
    private SplashListener splashListener;
    private AutomatonListener automatonListener;

    public interface SplashListener {
        void onSplashMessage(String Type,String Splash);
    }

    public interface AutomatonListener {
        void onAutomatonMessage(String Type ,String Automaton);
    }


    public void setSplashListener(SplashListener listener) {
        this.splashListener = listener;
    }

    public void setAutomatonListener(AutomatonListener listener) {
        this.automatonListener = listener;
    }

    @Override
    public void onMessage(String message) {
        String parsedMessage = message.substring(message.indexOf("]") + 2, message.length() - 1);
        if (message.contains("[Splash]") && BingoConfig.enableSplashPings){
            splashListener.onSplashMessage("§6Splash"," §r"+parsedMessage);
        }
        if (message.contains("[Automaton]") && BingoConfig.enableAutomatonPartsPings) {
            automatonListener.onAutomatonMessage("§2Automaton",parsedMessage);
        }
    }


}
