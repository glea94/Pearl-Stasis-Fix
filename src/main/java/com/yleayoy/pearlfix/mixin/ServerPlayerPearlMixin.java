package com.yleayoy.pearlfix.mixin;

import com.yleayoy.pearlfix.PearlFixMod;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.Set;

/**
* Fixes a known vanilla bug (see github.com/PaperMC/Paper/issues/13964):
* ServerPlayer.enderPearls (Set<ThrownEnderpearl>) is never cleaned up when
* the actual pearl is removed from the world, causing it to "resurrect"
* upon every reconnection or server restart. 
*
* On every player tick, any pearl for which isRemoved() is true is removed
* from the Set. Once removed, it will no longer be saved in the player's
* data during the next server shutdown, and thus cannot be recreated. 
*/
@Mixin(ServerPlayer.class)
public abstract class ServerPlayerPearlMixin {

    @Shadow
    private Set<?> enderPearls;

    @Inject(method = "tick", at = @At("HEAD"), require = 0)
    private void pearlfix$pruneRemovedPearls(CallbackInfo ci) {
        if (this.enderPearls == null || this.enderPearls.isEmpty()) {
            return;
        }

        Iterator<?> it = this.enderPearls.iterator();
        int removedCount = 0;
        while (it.hasNext()) {
            Object obj = it.next();
            if (obj instanceof Entity entity && entity.isRemoved()) {
                it.remove();
                removedCount++;
            }
        }

        if (removedCount > 0) {
            PearlFixMod.LOGGER.debug(
                "[PearlFix] {} reference(s) de perle(s) fantome(s) retiree(s) de enderPearls.",
                removedCount
            );
        }
    }
}
