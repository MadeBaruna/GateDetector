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

package me.baruna.gatedetector;

import com.google.inject.Inject;
import me.baruna.gatedetector.commands.Commands;
import me.baruna.gatedetector.config.Config;
import me.baruna.gatedetector.listeners.BlockBreakListener;
import me.baruna.gatedetector.listeners.PlayerMoveListener;
import me.baruna.gatedetector.listeners.RightClickBlockListener;
import me.baruna.gatedetector.listeners.SignEventListener;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameLoadCompleteEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.plugin.Plugin;

import java.nio.file.Path;

@Plugin(id = "gatedetector", name = "Gate Detector", version = "1.0")
public class GateDetector {
    @Inject
    private Game game;

    @Inject
    private Logger logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDir;

    private Config config;

    public Logger getLogger() {
        return this.logger;
    }

    public Config getConfig() {
        return config;
    }

    @Listener
    public void onPreInitialization(GamePreInitializationEvent event) {
        config = new Config(configDir, this);
        config.init();
    }

    @Listener
    public void onInitialization(GameInitializationEvent event) {
        //register listner
        Sponge.getEventManager().registerListeners(this, new SignEventListener(this));
        Sponge.getEventManager().registerListeners(this, new BlockBreakListener(this));
        Sponge.getEventManager().registerListeners(this, new PlayerMoveListener(this));
        Sponge.getEventManager().registerListeners(this, new RightClickBlockListener(this));

        new InventoryCheck(game, this);
    }

    @Listener
    public void onServerStarting(GameStartingServerEvent event) {
        //register commands
        new Commands(this);
    }

    @Listener
    public void onLoadComplete(GameLoadCompleteEvent event) {
        getLogger().info("Plugin loaded v1.0");
    }
}
