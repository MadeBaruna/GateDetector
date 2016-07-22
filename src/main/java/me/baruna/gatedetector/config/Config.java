package me.baruna.gatedetector.config;

import me.baruna.gatedetector.GateDetector;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {
    private final GateDetector plugin;
    private Path configDir;
    private static ConfigFile langFile;
    private static ConfigFile configFile;
    private static ConfigFile gatesFile;

    public Config(Path configDir, GateDetector plugin) {
        this.configDir = configDir;
        this.plugin = plugin;

        langFile = new ConfigFile(configDir, "lang.conf");
        configFile = new ConfigFile(configDir, "config.conf");
        gatesFile = new ConfigFile(configDir, "gates.conf");
    }

    public void init() {
        //create config directory if doesn't exists
        if(!Files.exists(configDir)) {
            try {
                Files.createDirectory(configDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        setupConfig(langFile);
        setupConfig(configFile);
        setupConfig(gatesFile);
    }

    private void setupConfig(ConfigFile configFile) {
        //create config if not exists
        Path pathName = configFile.getConfigFile();
        if(!Files.exists(pathName)) {
            try{
                Files.createFile(pathName);
            } catch(IOException e) {
                e.printStackTrace();
            }
        } else {
            loadConfig(configFile);
            return;
        }

        URL langDefault = plugin.getClass().getClassLoader().getResource(pathName.getFileName().toString());
        ConfigurationLoader langDefaultLoader = HoconConfigurationLoader.builder().setURL(langDefault).build();
        ConfigurationNode defaults = null;
        try {
            defaults = langDefaultLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!Files.exists(pathName)) {
            try {
                Files.createFile(pathName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        configFile.setLoader(HoconConfigurationLoader.builder().setPath(pathName).build());
        ConfigurationLoader loader = configFile.getLoader();
        try {
            configFile.setNode(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        configFile.getNode().mergeValuesFrom(defaults);
        try {
            loader.save(configFile.getNode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadConfig(ConfigFile configFile) {
        Path pathName = configFile.getConfigFile();
        configFile.setLoader(HoconConfigurationLoader.builder().setPath(pathName).build());
        ConfigurationLoader loader = configFile.getLoader();
        try {
            configFile.setNode(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ConfigFile getLang() {
        return langFile;
    }

    public static ConfigFile getConfig() {
        return configFile;
    }

    public static ConfigFile getGates() {
        return gatesFile;
    }
}
