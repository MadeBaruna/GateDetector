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
import org.spongepowered.api.event.block.CollideBlockEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.filter.type.Exclude;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;

public class RightClickBlockListener {
    private final GateDetector plugin;

    public RightClickBlockListener(GateDetector plugin) {
        this.plugin = plugin;
    }

    @Listener
    @Exclude({CollideBlockEvent.class, MoveEntityEvent.class})
    public void onRightClickBlock(InteractBlockEvent.Secondary event, @Root Player player) {
        if(event.getTargetBlock().getState().getType() != BlockTypes.STANDING_SIGN) {
            return;
        }

        if(!CommandDataHolder.isExists(player)) {
            return;
        }

        Vector3i targetLoc = event.getTargetBlock().getPosition();
        SignGate sign = SignUtil.getSign(targetLoc);

        if(sign == null) {
            return;
        }

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
        CommandDataHolder.removeData(player);
    }
}
