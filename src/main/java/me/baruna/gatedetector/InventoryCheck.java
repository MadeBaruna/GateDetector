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

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.spongepowered.api.Game;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class InventoryCheck {
    private static Game game;
    private static GateDetector plugin;

    public InventoryCheck(Game game, GateDetector plugin) {
        this.game = game;
        this.plugin = plugin;
    }

    private static Iterable<Slot> getSlots(Inventory inventory) {
        if (inventory instanceof Slot) {
            return Collections.emptyList();
        }
        Iterable<Slot> slots = inventory.slots();
        for (Inventory subInventory : inventory) {
            Iterables.concat(slots, getSlots(subInventory));
        }
        return slots;
    }

    public static boolean checkInventory(Player player, SignGate sign) {
        Inventory inventory = player.getInventory();
        Iterable<Slot> slots = getSlots(inventory);
        List<Slot> slotsList = Lists.newArrayList(slots);
        List<ItemType> itemDetectionList = sign.getItems();

        Optional<ItemStack> offHandItemOpt = player.getItemInHand(HandTypes.OFF_HAND);
        ItemType offHandItem = ItemTypes.NONE;
        if(offHandItemOpt.isPresent()) {
            offHandItem = offHandItemOpt.get().getItem().getType();
        }

        for (int i = 0; i < slotsList.size(); i++) {
            Optional<ItemStack> optStack = slotsList.get(i).peek();
            if (optStack.isPresent()) {
                {
                    ItemType type = optStack.get().getItem().getType();
                    if(itemDetectionList.contains(type) || itemDetectionList.contains(offHandItem)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static void removeItems(Player player, List<ItemType> items) {
        Iterable<Slot> slots = player.getInventory().slots();
        List<Slot> slotsList = Lists.newArrayList(slots);

        Optional<ItemStack> offHandItemOpt = player.getItemInHand(HandTypes.OFF_HAND);
        ItemStack offHandItem = null;
        if(offHandItemOpt.isPresent()) {
            offHandItem = offHandItemOpt.get();
        }

        for (int i = 0; i < slotsList.size(); i++) {
            Optional<ItemStack> optStack = slotsList.get(i).peek();
            if (optStack.isPresent()) {

                if(items.contains(optStack.get().getItem())) {
                    Inventory item = player.getInventory().query(optStack.get());
                    item.poll();
                }

                if(offHandItem != null) {
                    if(items.contains(offHandItem.getItem())) {
                        player.setItemInHand(HandTypes.OFF_HAND, null);
                    }
                }
            }
        }
    }
}
