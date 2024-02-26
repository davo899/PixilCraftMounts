package com.selfdot.pixilcraftmounts;

import com.mojang.brigadier.CommandDispatcher;
import com.selfdot.pixilcraftmounts.command.MountsCommandTree;
import com.selfdot.pixilcraftmounts.util.DisableableMod;
import com.selfdot.pixilcraftmounts.util.TaskScheduler;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class PixilCraftMountsMod extends DisableableMod {

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(this::registerCommands);
        TaskScheduler.getInstance().register();
    }

    private void registerCommands(
        CommandDispatcher<ServerCommandSource> dispatcher,
        CommandRegistryAccess commandRegistryAccess,
        CommandManager.RegistrationEnvironment registrationEnvironment
    ) {
        MountsCommandTree.register(dispatcher);
    }

}
