package gg.amy.mc.recipeformagic.mixin;

import gg.amy.mc.recipeformagic.RecipeForMagic;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author amy
 * @since 5/20/23.
 */
@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    @Inject(at = @At("HEAD"), method = "init()V")
    private void init(final CallbackInfo info) {
        RecipeForMagic.LOGGER.info("This line is printed by an example mod mixin!");
    }
}
