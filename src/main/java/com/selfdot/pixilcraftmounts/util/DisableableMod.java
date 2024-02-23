package com.selfdot.pixilcraftmounts.util;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;

public abstract class DisableableMod implements ModInitializer {

    private volatile boolean disabled = false;
    private final Logger LOGGER = LogUtils.getLogger();

    public boolean isDisabled() {
        return disabled;
    }

    public Logger getLogger() {
        return LOGGER;
    }

    public void disable() {
        this.disabled = true;
    }

}
