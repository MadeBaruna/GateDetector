package me.baruna.gatedetector;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
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

    public static boolean checkInventory(Inventory inventory, SignGate sign) {
        Iterable<Slot> slots = getSlots(inventory);
        List<Slot> slotsList = Lists.newArrayList(slots);
        List<ItemType> itemDetectionList = sign.getItems();

        for (int i = 0; i < slotsList.size(); i++) {
            Optional<ItemStack> optStack = slotsList.get(i).peek();
            if (optStack.isPresent()) {
                {
                    if(itemDetectionList.contains(optStack.get().getItem())) {
                        return true;
                    }
                }
            }
        }

        //TODO check off-hand item
        //plugin.getLogger().info(player.getItemInHand(HandTypes.OFF_HAND).get().getItem().getType().getName());

        return false;
    }

    public static void removeItems(Player player, List<ItemType> items) {
        Iterable<Slot> slots = player.getInventory().slots();
        List<Slot> slotsList = Lists.newArrayList(slots);

        for (int i = 0; i < slotsList.size(); i++) {
            Optional<ItemStack> optStack = slotsList.get(i).peek();
            if (optStack.isPresent()) {

                if(items.contains(optStack.get().getItem())) {
                    Inventory item = player.getInventory().query(optStack.get());
                    item.poll();
                }
            }
        }
    }
}
