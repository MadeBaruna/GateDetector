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
