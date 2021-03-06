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

package me.baruna.gatedetector.commands;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;

import java.util.*;

public class CommandDataHolder {
    static Map<Player, Collection<ItemType>> itemsData = new HashMap<Player, Collection<ItemType>>();
    static List<Player> infoData = new ArrayList<Player>();

    public static Collection<ItemType> getData(Player player) {
        return itemsData.get(player);
    }

    public static void addItemsDataHolder(Player player, Collection<ItemType> items) {
        itemsData.put(player, items);
    }

    public static void removeItemDataHolder(Player player) {
        itemsData.remove(player);
    }

    public static boolean isItemDataExists(Player player) {
        return itemsData.containsKey(player);
    }

    public static void addInfoDataHolder(Player player) {
        infoData.add(player);
    }

    public static void removeInfoDataHolder(Player player) {
        infoData.remove(player);
    }

    public static boolean isInfoDataExists(Player player) {
        return infoData.contains(player);
    }
}
