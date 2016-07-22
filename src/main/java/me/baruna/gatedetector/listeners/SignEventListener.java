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
import com.google.common.reflect.TypeToken;
import me.baruna.gatedetector.GateDetector;
import me.baruna.gatedetector.SignGate;
import me.baruna.gatedetector.config.Config;
import me.baruna.gatedetector.config.ConfigFile;
import me.baruna.gatedetector.config.Localization;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.tileentity.SignData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.tileentity.ChangeSignEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SignEventListener {
    private final GateDetector plugin;
    private ConfigFile gates;
    private static final List<String> AVAILABLE_TYPES = Arrays.asList(
            "BLOCK",
            "PITHOLE",
            "KILL",
            "REMOVE"
    );

    public SignEventListener(GateDetector plugin) {
        this.plugin = plugin;
        this.gates = Config.getGates();
    }

    //Listener for change sign event
    @Listener
    public void onChangeSignEvent(ChangeSignEvent event, @Root Player player) {
        //check if standing sign
        if(event.getTargetTile().getBlock().getType() != BlockTypes.STANDING_SIGN) {
            return;
        }

        SignData sign = event.getText();

        //Get first line text
        Optional<Text> firstLineText = sign.get(0);
        Optional<Text> secondLineText = sign.get(1);
        if(!firstLineText.isPresent()) {
            return;
        }
        String firstLine = firstLineText.get().toPlain();

        //check if player has create permission
        if(!player.hasPermission("gatedetector.create")) {
            return;
        }

        String direction = event.getTargetTile().getBlock().get(Keys.DIRECTION).get().toString();

        //compare the first line string
        if(firstLine.equals("[GateDetector]")) {
            //ignore diagonal sign placement
            switch (direction) {
                case "SOUTHWEST":
                case "NORTHWEST":
                case "NORTHEAST":
                case "SOUTHEAST":
                    player.sendMessage(Text.of(TextColors.RED, Localization.getText("diagonalSign")));
                    return;
            }

            if(!secondLineText.isPresent()) {
                player.sendMessage(Text.of(TextColors.RED, Localization.getText("noType")));
                return;
            }

            String secondLine = secondLineText.get().toPlain().toUpperCase();
            if(!AVAILABLE_TYPES.contains(secondLine)) {
                player.sendMessage(Text.of(TextColors.RED, Localization.getText("wrongType")));
                return;
            }

            //create signGate object
            SignGate signGate = new SignGate(player.getUniqueId(),
                    event.getTargetTile().getLocation(),
                    direction, secondLine);

            Vector3i blockVector = event.getTargetTile().getLocation().getBlockPosition();

            //save to gates file
            try {
                gates.getNode().getNode(
                        String.valueOf(blockVector.getX()))
                        .getNode(String.valueOf(blockVector.getZ()))
                        .getNode(String.valueOf(blockVector.getY()))
                        .setValue(TypeToken.of(SignGate.class), signGate);
            } catch (ObjectMappingException e) {
                e.printStackTrace();
            }
            gates.save();

            sign.setElement(0, Text.of(TextColors.GREEN, "[GateDetector]"));
            sign.setElement(1, Text.of(secondLine));
            player.sendMessage(Text.of(TextColors.GREEN, Localization.getText("signCreated")));
        }
    }
}

