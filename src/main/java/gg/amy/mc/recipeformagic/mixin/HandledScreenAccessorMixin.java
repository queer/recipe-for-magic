package gg.amy.mc.recipeformagic.mixin;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author amy
 * @since 5/20/23.
 */
@Mixin(HandledScreen.class)
public interface HandledScreenAccessorMixin {
    @Accessor
    Slot getFocusedSlot();
}
