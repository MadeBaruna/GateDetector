/*
 * This file is part of GateDetector, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2016 Made Baruna
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
