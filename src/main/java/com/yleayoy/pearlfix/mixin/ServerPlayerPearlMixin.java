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
 * Corrige un bug vanilla connu (voir github.com/PaperMC/Paper/issues/13964) :
 * ServerPlayer.enderPearls (Set<ThrownEnderpearl>) n'est jamais nettoye quand
 * la perle reelle est supprimee du monde, ce qui la fait "ressusciter" a
 * chaque reconnexion/redemarrage du serveur.
 *
 * A chaque tick du joueur, on retire du Set toute perle dont isRemoved() est
 * vrai. Une fois retiree, elle ne sera plus sauvegardee dans les donnees du
 * joueur au prochain arret serveur, et ne pourra donc plus etre recreee.
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
