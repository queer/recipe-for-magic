package gg.amy.mc.recipeformagic.client.gui.framework;

import gg.amy.mc.recipeformagic.data.RecipeGraph.RecipeSearchResults;

import java.util.Collection;

/**
 * @author amy
 * @since 5/20/23.
 */
public interface Layout {
    Collection<GuiComponent> apply(final RecipeSearchResults results, final BoundingBox bounds);
}
