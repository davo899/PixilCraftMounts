package com.selfdot.pixilcraftmounts.command;

import com.cobblemon.mod.common.command.argument.PartySlotArgumentType;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;

public class MountsCommandTree {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>
            literal("mount")
            .requires(ServerCommandSource::isExecutedByPlayer)
            .then(RequiredArgumentBuilder.<ServerCommandSource, Integer>
                argument("pokemon", PartySlotArgumentType.Companion.partySlot())
                .executes(new MountCommand())
            )
        );
    }

}
