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

package me.baruna.gatedetector.listeners;

import com.flowpowered.math.vector.Vector3i;
import me.baruna.gatedetector.GateDetector;
import me.baruna.gatedetector.InventoryCheck;
import me.baruna.gatedetector.SignGate;
import me.baruna.gatedetector.SignUtil;
import me.baruna.gatedetector.config.Config;
import me.baruna.gatedetector.config.ConfigFile;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.entity.HealthData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlayerMoveListener {
    private final GateDetector plugin;
    private ConfigFile gates;

    public PlayerMoveListener(GateDetector plugin) {
        this.plugin = plugin;
        this.gates = Config.getGates();
    }

    @Listener
    public void onPlayerMove(MoveEntityEvent event, @Root Player player) {
        Vector3i fromLoc = event.getFromTransform().getLocation().getBlockPosition();
        Vector3i toLoc = event.getToTransform().getLocation().getBlockPosition();

        float moveLength = toLoc.sub(fromLoc).length();

        if (moveLength != 1) {
            return;
        }

        Vector3i aboveHeadLoc = fromLoc.add(0, 3, 0);
        if (event.getFromTransform().getLocation().getExtent().getBlock(aboveHeadLoc).getType()
                != BlockTypes.STANDING_SIGN) {
            return;
        }

        SignGate sign = SignUtil.getSign(aboveHeadLoc);

        if (sign == null) {
            return;
        }

        Location signLoc = sign.getLocation();
        String facing = sign.getFacing();

        if (player.getLocation().getExtent() == signLoc.getExtent()) {
            Vector3i signBlockPos = signLoc.getBlockPosition();
            switch (facing) {
                case "NORTH":
                    signBlockPos = signBlockPos.add(0, -3, 1);
                    break;
                case "SOUTH":
                    signBlockPos = signBlockPos.sub(0, 3, 1);
                    break;
                case "WEST":
                    signBlockPos = signBlockPos.add(1, -3, 0);
                    break;
                case "EAST":
                    signBlockPos = signBlockPos.sub(1, 3, 0);
                    break;
            }

            if(toLoc.equals(signBlockPos)) {
                if(InventoryCheck.checkInventory(player.getInventory(), sign)) {
                    action(sign, event, player);
                }
            }
        }
    }

    private boolean isLoadingPit = false;
    private void action(SignGate sign, MoveEntityEvent event, Player player) {
        Vector3i toLoc = event.getToTransform().getLocation().getBlockPosition();
        String type = sign.getType();
        switch (type) {
            case "BLOCK":
                event.setCancelled(true);
                break;
            case "PITHOLE":
                if(!isLoadingPit) {
                    isLoadingPit = true;
                    List<BlockState> blocks = new ArrayList<BlockState>();
                    for (int i = -2; i <= 2; i++) {
                        for (int j = -2; j <= 2; j++) {
                            Location blockUnder = sign.getLocation().sub(i, 4, j);
                            blocks.add(blockUnder.getBlock());
                            blockUnder.setBlockType(BlockTypes.AIR);
                        }
                    }

                    final SignGate s = sign;
                    Scheduler scheduler = Sponge.getScheduler();
                    Task.Builder taskBuilder = scheduler.createTaskBuilder();
                    Task task = taskBuilder.execute(new Runnable() {
                        public void run() {
                            int counter = 0;
                            for (int i = -2; i <= 2; i++) {
                                for (int j = -2; j <= 2; j++) {
                                    Location blockUnder = s.getLocation().sub(i, 4, j);
                                    blockUnder.setBlock(blocks.get(counter++));
                                }
                            }
                            isLoadingPit = false;
                        }
                    }).delay(1, TimeUnit.SECONDS).submit(plugin);
                }
                break;
            case "KILL":
                HealthData healthData = player.getHealthData();
                healthData.set(Keys.HEALTH, 0D);
                player.offer(healthData);
            case "REMOVE":
                InventoryCheck.removeItems(player, sign.getItems());
                break;
            default:
                event.setCancelled(true);
        }
    }
}