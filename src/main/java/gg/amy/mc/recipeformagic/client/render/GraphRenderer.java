package gg.amy.mc.recipeformagic.client.render;

import gg.amy.mc.recipeformagic.client.gui.framework.BoundingBox;
import gg.amy.mc.recipeformagic.client.gui.framework.Coordinate;
import gg.amy.mc.recipeformagic.client.gui.layout.SimpleLayout;
import gg.amy.mc.recipeformagic.data.RecipeGraph.RecipeSearchResults;
import net.minecraft.client.util.math.MatrixStack;

/**
 * @author amy
 * @since 5/20/23.
 */
public class GraphRenderer {
    public static void renderGraph(final RecipeSearchResults results, final MatrixStack matrices,
                                   final Coordinate mouse, final BoundingBox bounds, final float delta) {
        final var layout = new SimpleLayout();
        final var components = layout.apply(results, bounds);
        for(final var component : components) {
            component.render(matrices, mouse, delta);
        }
    }
}
