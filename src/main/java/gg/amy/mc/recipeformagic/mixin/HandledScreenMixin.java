package gg.amy.mc.recipeformagic.mixin;

import gg.amy.mc.recipeformagic.RecipeForMagic;
import gg.amy.mc.recipeformagic.client.RecipeForMagicClient;
import gg.amy.mc.recipeformagic.client.gui.framework.BoundingBox;
import gg.amy.mc.recipeformagic.client.gui.framework.Coordinate;
import gg.amy.mc.recipeformagic.client.render.GraphRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

/**
 * @author amy
 * @since 5/20/23.
 */
@Mixin(HandledScreen.class)
public class HandledScreenMixin<T extends ScreenHandler> extends Screen implements ScreenHandlerProvider<T> {
    @Shadow
    protected int backgroundWidth;
    
    @Shadow
    protected int x;
    
    @Shadow
    protected int y;
    
    @Shadow
    protected int backgroundHeight;
    
    protected HandledScreenMixin(final Text title) {
        super(title);
    }
    
    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V",
                    shift = Shift.AFTER))
    private void injectRender(final MatrixStack matrices, final int mouseX, final int mouseY, final float delta, final CallbackInfo ci) {
        if(RecipeForMagicClient.RECIPE_SEARCH_RESULTS.isEmpty()) {
            return;
        }
        GraphRenderer.renderGraph(
                RecipeForMagicClient.RECIPE_SEARCH_RESULTS.get(),
                matrices, new Coordinate(mouseX, mouseY),
                new BoundingBox(x + backgroundWidth, 0, width - (x + backgroundWidth), height),
                delta
        );
    }
    
    @Inject(method = "keyPressed", at = @At("HEAD"))
    private void injectKeyPressed(final int keyCode, final int scanCode, final int modifiers, final CallbackInfoReturnable<Boolean> cir) {
        if(RecipeForMagicClient.SWITCH_ACTIVE_RECIPE.matchesKey(keyCode, scanCode)) {
            RecipeForMagicClient.LOGGER.info("Key pressed!");
            final var slot = ((HandledScreenAccessorMixin) self()).getFocusedSlot();
            if(slot != null) {
                final var internalItemName = Registry.ITEM.getId(slot.getStack().getItem());
                RecipeForMagicClient.LOGGER.info("Switching search to: {}", internalItemName);
                RecipeForMagicClient.RECIPE_SEARCH_RESULTS = Optional.of(RecipeForMagic.GRAPH.queryGraphWithDepth(internalItemName, 5));
            }
        }
    }
    
    @Override
    public T getScreenHandler() {
        return self().getScreenHandler();
    }
    
    @SuppressWarnings("unchecked")
    private HandledScreen<T> self() {
        return (HandledScreen<T>) (Object) this;
    }
}
