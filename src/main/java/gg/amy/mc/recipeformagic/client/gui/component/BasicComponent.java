package gg.amy.mc.recipeformagic.client.gui.component;

import gg.amy.mc.recipeformagic.client.gui.framework.BoundingBox;
import gg.amy.mc.recipeformagic.client.gui.framework.GuiComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;

/**
 * @author amy
 * @since 5/20/23.
 */
public abstract class BasicComponent implements GuiComponent {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    
    public BasicComponent(final int x, final int y, final int width, final int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
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
    public BoundingBox bounds() {
        return new BoundingBox(x, y, width, height);
    }
}
