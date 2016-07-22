package me.baruna.gatedetector.commands;

import me.baruna.gatedetector.GateDetector;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class Commands {
    public Commands(GateDetector plugin) {
        Sponge.getCommandManager().register(plugin, mainCmd, "gatedetector", "gd");
    }

    // /gatedetector add
    CommandSpec addCmd = CommandSpec.builder()
            .description(Text.of("AddCommand item to gate detection list"))
            .permission("gatedetector.create.add")
            .arguments(
                    GenericArguments.allOf(
                            GenericArguments.catalogedElement(Text.of("Item Type"), CatalogTypes.ITEM_TYPE)
                    )
            )
            .executor(new AddCommand())
            .build();

    // /gatedetector
    CommandSpec mainCmd = CommandSpec.builder()
            .description(Text.of("Gate Detector Main Command"))
            .permission("gatedetector")
            .child(addCmd, "add")
            .build();
}
