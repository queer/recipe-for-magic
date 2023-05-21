package gg.amy.mc.recipeformagic.client.gui.component;

import gg.amy.mc.recipeformagic.client.gui.framework.BoundingBox;
import gg.amy.mc.recipeformagic.client.gui.framework.Coordinate;
import gg.amy.mc.recipeformagic.client.render.RenderHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;

/**
 * @author amy
 * @since 5/20/23.
 */
public class ItemComponent extends BasicComponent {
    public static final int SLOT_SIZE = 16;
    
    protected final Item item;
    
    public ItemComponent(final Item item, final int x, final int y) {
        super(x, y, SLOT_SIZE, SLOT_SIZE);
        this.item = item;
    }
    
    @Override
    public int x() {
        return x;
    }
    
    @Override
    public int y() {
        return y;
    }
    
    @Override
    public void render(final MatrixStack matrices, final Coordinate mouse, final float delta) {
        RenderHelper.renderTooltipLike(matrices, x + 3, y + 3, SLOT_SIZE - 6, SLOT_SIZE - 6, 0, 0xFF777777, 0xFF555555);
        MinecraftClient.getInstance().getItemRenderer().renderGuiItemIcon(item.getDefaultStack(), x, y);
    }
    
    @Override
    public BoundingBox bounds() {
        return new BoundingBox(x, y, SLOT_SIZE, SLOT_SIZE);
    }
    
    public Item item() {
        return item;
    }
}
