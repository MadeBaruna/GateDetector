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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CommandDataHolder {
    static Map<Player, Collection<ItemType>> data = new HashMap<Player, Collection<ItemType>>();

    public static Collection<ItemType> getData(Player player) {
        return data.get(player);
    }

    public static void addData(Player player, Collection<ItemType> items) {
        data.put(player, items);
    }

    public static void removeData(Player player) {
        data.remove(player);
    }

    public static boolean isExists(Player player) {
        return data.containsKey(player);
    }
}
