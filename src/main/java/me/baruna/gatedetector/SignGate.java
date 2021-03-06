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

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.world.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ConfigSerializable
public class SignGate {
    @Setting(value="player")
    private UUID playerUUID;

    @Setting
    private Location location;

    @Setting
    private String facing;

    @Setting
    private String type;

    @Setting
    private List<ItemType> items;

    public SignGate() {}

    public SignGate(UUID playerUUID, Location location, String facing, String type) {
        this.playerUUID = playerUUID;
        this.location = location;
        this.facing = facing;
        this.type = type;
        items = new ArrayList<ItemType>();
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public Location getLocation() {
        return location;
    }

    public String getFacing() {
        return facing;
    }

    public List<ItemType> getItems() {
        return items;
    }

    public String getType() {
        return type;
    }

    public void setItems(List<ItemType> items) {
        this.items = items;
    }
}
