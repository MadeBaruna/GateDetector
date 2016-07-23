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

    // /gatedetector help
    CommandSpec helpCmd = CommandSpec.builder()
            .description(Text.of("Check a gate detector information"))
            .permission("gatedetector")
            .executor(new HelpCommand())
            .build();

    // /gatedetector info
    CommandSpec infoCmd = CommandSpec.builder()
            .description(Text.of("Check a gate detector information"))
            .permission("gatedetector.info")
            .executor(new InfoCommand())
            .build();

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
            .child(infoCmd, "info")
            .child(helpCmd, "help")
            .executor(new GateDetectorCommand())
            .build();
}
