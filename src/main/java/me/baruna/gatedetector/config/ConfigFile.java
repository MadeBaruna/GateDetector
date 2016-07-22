package me.baruna.gatedetector.config;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigFile {
    private Path configFile;
    private ConfigurationLoader configLoader;
    private ConfigurationNode configNode;

    public ConfigFile(Path configDir, String fileName) {
        configFile = Paths.get(configDir + File.separator + fileName);
    }

    public Path getConfigFile() {
        return configFile;
    }

    public void setConfigFile(Path configFile) {
        this.configFile = configFile;
    }

    public ConfigurationLoader getLoader() {
        return configLoader;
    }

    public void setLoader(ConfigurationLoader configLoader) {
        this.configLoader = configLoader;
    }

    public ConfigurationNode getNode() {
        return configNode;
    }

    public void setNode(ConfigurationNode configNode) {
        this.configNode = configNode;
    }

    public ConfigurationNode getValue(String path) {
        return getNode().getNode(path);
    }

    public void save() {
        try {
            getLoader().save(configNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
