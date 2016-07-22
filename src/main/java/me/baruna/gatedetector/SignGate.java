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
