package com.yleayoy.pearlfix;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PearlFixMod implements ModInitializer {

    public static final String MOD_ID = "pearlfix";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("[PearlFix] Mod chargé - correctif de duplication des ender pearls en chambre de stase actif.");
    }
}
