package me.baruna.gatedetector.listeners;

import com.flowpowered.math.vector.Vector3i;
import me.baruna.gatedetector.GateDetector;
import me.baruna.gatedetector.config.Config;
import me.baruna.gatedetector.config.ConfigFile;
import me.baruna.gatedetector.config.Localization;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class BlockBreakListener {
    private final GateDetector plugin;
    private ConfigFile gates;

    public BlockBreakListener(GateDetector plugin) {
        this.plugin = plugin;
        this.gates = Config.getGates();
    }

    @Listener
    public void onBlockBreak(ChangeBlockEvent.Break event, @Root Player player) {
        if(event.getTransactions().get(0).getOriginal().getState().getType() != BlockTypes.STANDING_SIGN) {
            return;
        }

        Vector3i blockVector = event.getTransactions().get(0).getOriginal().getPosition();

        //check if coordinates exists in gates file, if yes remove it
        if(!gates.getNode().getNode(
                String.valueOf(blockVector.getX()),
                String.valueOf(blockVector.getZ()),
                String.valueOf(blockVector.getY())
        ).isVirtual()) {
            if(!player.hasPermission("gatedetector.break")) {
                player.sendMessage(Text.of(TextColors.RED, Localization.getText("noPermission")));
                event.setCancelled(true);
                return;
            }

            gates.getNode().getNode(
                    String.valueOf(blockVector.getX()),
                    String.valueOf(blockVector.getZ()),
                    String.valueOf(blockVector.getY())
            ).setValue(null);
            gates.save();

            player.sendMessage(Text.of(TextColors.GREEN, Localization.getText("signDestroyed")));
        }
    }
}
