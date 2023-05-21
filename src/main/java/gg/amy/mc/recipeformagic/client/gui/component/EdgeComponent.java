package gg.amy.mc.recipeformagic.client.gui.component;

import gg.amy.mc.recipeformagic.client.gui.framework.BoundingBox;
import gg.amy.mc.recipeformagic.client.gui.framework.Coordinate;
import gg.amy.mc.recipeformagic.client.render.RenderHelper;
import gg.amy.mc.recipeformagic.data.RecipeGraph;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import org.apache.commons.lang3.StringUtils;

import static gg.amy.mc.recipeformagic.client.gui.component.ItemComponent.SLOT_SIZE;

/**
 * @author amy
 * @since 5/20/23.
 */
public class EdgeComponent extends BasicComponent {
    private final ItemStack stack;
    private final RecipeGraph.RecipeMetadata recipe;
    private final int x2;
    private final int y2;
    
    public EdgeComponent(final ItemStack stack, final RecipeGraph.RecipeMetadata recipe, final int x, final int y, final int x2, final int y2) {
        super(x, y, x2 - x, y2 - y);
        this.stack = stack;
        this.recipe = recipe;
        this.x2 = x2;
        this.y2 = y2;
    }
    
    @Override
    public void render(final MatrixStack matrices, final Coordinate mouse, final float delta) {
        final var lineColour = recipe.isPrimary() ? 0xFF55FF55 : bounds().contains(mouse) ? 0xFFFFFF55 : 0xFFFF5555;
        RenderHelper.drawLine(matrices, x, y, x, halfway(y, y2), lineColour);
        RenderHelper.drawLine(matrices, x2, y2, x2, halfway(y, y2), lineColour);
        RenderHelper.drawLine(matrices, x, halfway(y, y2), x2, halfway(y, y2), lineColour);
        
        final var hintBounds = new BoundingBox(x2 - SLOT_SIZE / 2, halfway(y, y2) - SLOT_SIZE / 2,
                SLOT_SIZE, SLOT_SIZE);
        final var textHeight = MinecraftClient.getInstance().textRenderer.fontHeight;
        
        final var recipe = this.recipe.recipe();
        if(hintBounds.contains(mouse)) {
            final var typeHint = StringUtils.capitalize(recipe.getType().toString().replace('_', ' '));
            final var typeHintWidth = MinecraftClient.getInstance().textRenderer.getWidth(typeHint) + 2;
            MinecraftClient.getInstance().getItemRenderer().renderGuiItemIcon(recipe.createIcon(), hintBounds.x(), hintBounds.y());
            matrices.push();
            matrices.translate(hintBounds.x(), hintBounds.y(), 400);
            RenderHelper.drawRect(matrices, SLOT_SIZE, 0,
                    typeHintWidth + 2, SLOT_SIZE, 0x77000000);
            MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, typeHint,
                    2 + SLOT_SIZE, Math.abs(hintBounds.height() - textHeight) / 2 + 1,
                    0xFFFFFFFF);
            matrices.pop();
        } else {
            MinecraftClient.getInstance().getItemRenderer().renderGuiItemIcon(recipe.createIcon(), hintBounds.x(), hintBounds.y());
        }
    }
    
    private int halfway(final int a, final int b) {
        return Math.min(a, b) + Math.abs(a - b) / 2;
    }
}
