package gg.amy.mc.recipeformagic.client.gui.framework;

import net.minecraft.client.util.math.MatrixStack;

/**
 * @author amy
 * @since 5/20/23.
 */
public interface GuiComponent {
    int x();
    
    int y();
    
    default Coordinate coordinates() {
        return new Coordinate(x(), y());
    }
    
    void render(final MatrixStack matrices, final Coordinate mouse, final float delta);
    
    BoundingBox bounds();
}
