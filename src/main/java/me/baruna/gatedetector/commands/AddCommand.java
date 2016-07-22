package me.baruna.gatedetector.commands;

import me.baruna.gatedetector.config.Localization;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Collection;

public class AddCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(src instanceof Player) {
            Player player = (Player) src;
            Collection<ItemType> items = args.<ItemType>getAll(Text.of("Item Type"));
            CommandDataHolder.addData(player, items);
            ((Player) src).sendMessage(Text.of(TextColors.BLUE, Localization.getText("addItems")));
        } else {
            src.sendMessage(Text.of(TextColors.RED, Localization.getText("notPlayer")));
        }

        return CommandResult.success();
    }
}
