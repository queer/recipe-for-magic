package gg.amy.mc.recipeformagic.mixin;

import gg.amy.mc.recipeformagic.RecipeForMagic;
import gg.amy.mc.recipeformagic.client.RecipeForMagicClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.SynchronizeRecipesS2CPacket;
import net.minecraft.network.packet.s2c.play.SynchronizeTagsS2CPacket;
import net.minecraft.recipe.RecipeManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

/**
 * @author amy
 * @since 5/20/23.
 */

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Shadow
    @Final
    private RecipeManager recipeManager;
    
    @Inject(method = "onSynchronizeRecipes", at = @At("TAIL"))
    private void handleUpdateRecipes(SynchronizeRecipesS2CPacket recipeSync, CallbackInfo ci) {
        RecipeForMagicClient.LOGGER.info("building recipe graph...");
        RecipeForMagic.GRAPH.reset().buildGraph(recipeManager);
        RecipeForMagicClient.RECIPE_SEARCH_RESULTS = Optional.empty();
        RecipeForMagicClient.LOGGER.info("built recipe graph");
    }
    
    @Inject(method = "onSynchronizeTags", at = @At("TAIL"))
    private void handleUpdateTags(SynchronizeTagsS2CPacket packet, CallbackInfo ci) {
        // TODO
    }
}
