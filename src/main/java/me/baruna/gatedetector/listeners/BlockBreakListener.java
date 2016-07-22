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
import me.baruna.gatedetector.config.Config;
import me.baruna.gatedetector.config.ConfigFile;
import me.baruna.gatedetector.config.Localization;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class BlockBreakListener {
    private final GateDetector plugin;
    private ConfigFile gates;

    public BlockBreakListener(GateDetector plugin) {
        this.plugin = plugin;
        this.gates = Config.getGates();
    }

    @Listener
    public void onBlockBreak(ChangeBlockEvent.Break event, @Root Player player) {
        if(event.getTransactions().get(0).getOriginal().getState().getType() != BlockTypes.STANDING_SIGN) {
            return;
        }

        Vector3i blockVector = event.getTransactions().get(0).getOriginal().getPosition();

        //check if coordinates exists in gates file, if yes remove it
        if(!gates.getNode().getNode(
                String.valueOf(blockVector.getX()),
                String.valueOf(blockVector.getZ()),
                String.valueOf(blockVector.getY())
        ).isVirtual()) {
            if(!player.hasPermission("gatedetector.break")) {
                player.sendMessage(Text.of(TextColors.RED, Localization.getText("noPermission")));
                event.setCancelled(true);
                return;
            }

            gates.getNode().getNode(
                    String.valueOf(blockVector.getX()),
                    String.valueOf(blockVector.getZ()),
                    String.valueOf(blockVector.getY())
            ).setValue(null);
            gates.save();

            player.sendMessage(Text.of(TextColors.GREEN, Localization.getText("signDestroyed")));
        }
    }
}
