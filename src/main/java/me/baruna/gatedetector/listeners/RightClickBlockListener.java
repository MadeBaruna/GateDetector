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
import me.baruna.gatedetector.SignUtil;
import me.baruna.gatedetector.commands.CommandDataHolder;
import me.baruna.gatedetector.config.Config;
import me.baruna.gatedetector.config.Localization;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RightClickBlockListener {
    private final GateDetector plugin;

    public RightClickBlockListener(GateDetector plugin) {
        this.plugin = plugin;
    }

    @Listener
    public void onRightClickBlock(InteractBlockEvent.Secondary event, @Root Player player) {
        if(event.getTargetBlock().getState().getType() != BlockTypes.STANDING_SIGN) {
            return;
        }

        Vector3i targetLoc = event.getTargetBlock().getPosition();
        SignGate sign = SignUtil.getSign(targetLoc);

        if(sign == null) {
            return;
        }

        if(CommandDataHolder.isItemDataExists(player)) {
            add(sign, player);
        }

        if(CommandDataHolder.isInfoDataExists(player)) {
            info(sign, player);
        }
    }

    private void add(SignGate sign, Player player) {
        List<ItemType> items = sign.getItems();
        List<ItemType> newItems = new ArrayList<ItemType>(CommandDataHolder.getData(player));
        items.addAll(newItems);

        sign.setItems(items);

        Vector3i loc = sign.getLocation().getBlockPosition();

        try {
            Config.getGates().getNode().getNode(
                    String.valueOf(loc.getX()))
                    .getNode(String.valueOf(loc.getZ()))
                    .getNode(String.valueOf(loc.getY()))
                    .setValue(TypeToken.of(SignGate.class), sign);
        } catch (ObjectMappingException e) {
            e.printStackTrace();
        }

        Config.getGates().save();

        player.sendMessage(Text.of(TextColors.GREEN, Localization.getText("itemsAdded")));
        CommandDataHolder.removeItemDataHolder(player);
    }

    private void info(SignGate sign, Player player) {
        List<ItemType> items = sign.getItems();
        Optional<Player> playerCreator = plugin.getGame().getServer().getPlayer(sign.getPlayerUUID());

        if(playerCreator.isPresent()) {
            player.sendMessage(
                    Text.of(TextColors.GREEN, "Gate Creator: ",
                            TextColors.AQUA, playerCreator.get().getName())
            );
        }
        player.sendMessage(Text.of(TextColors.GREEN, "Gate Type: ", TextColors.AQUA, sign.getType()));
        Text.Builder itemsList = Text.builder()
                .append(Text.of(TextColors.GREEN, "Detection list: ")).
                color(TextColors.AQUA);
        for(ItemType i : items) {
            itemsList.append(Text.of(i.getName(), " "));
        }
        player.sendMessage(itemsList.build());

        CommandDataHolder.removeInfoDataHolder(player);
    }
}
