package com.selfdot.pixilcraftmounts.command;

import com.cobblemon.mod.common.CobblemonEntities;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.command.argument.PartySlotArgumentType;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.selfdot.pixilcraftmounts.imixin.IPokemonEntityMixin;
import com.selfdot.pixilcraftmounts.util.TaskScheduler;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class MountCommand implements Command<ServerCommandSource> {

    @Override
    public int run(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null) return 0;
        Pokemon pokemon = PartySlotArgumentType.Companion.getPokemon(context, "pokemon").clone(false, false);
        System.out.println(pokemon.getIvs().get(Stats.SPEED));
        PokemonEntity pokemonEntity = new PokemonEntity(player.getWorld(), pokemon, CobblemonEntities.POKEMON);
        player.getWorld().spawnEntity(pokemonEntity);
        ((IPokemonEntityMixin)(Object)pokemonEntity).pixilCraftMounts$setIsMount(true);
        pokemonEntity.setPosition(player.getPos());
        TaskScheduler.getInstance().schedule(() -> player.startRiding(pokemonEntity), 5);
        return SINGLE_SUCCESS;
    }

}
