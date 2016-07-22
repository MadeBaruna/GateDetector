package me.baruna.gatedetector.config;

public class Localization {
    public static String getText(String node) {
        return Config.getLang().getValue(node).getString();
    }
}
