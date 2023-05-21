package gg.amy.mc.recipeformagic.client.gui.layout;

import gg.amy.mc.recipeformagic.client.RecipeForMagicClient;
import gg.amy.mc.recipeformagic.client.gui.component.EdgeComponent;
import gg.amy.mc.recipeformagic.client.gui.component.ItemComponent;
import gg.amy.mc.recipeformagic.client.gui.framework.BoundingBox;
import gg.amy.mc.recipeformagic.client.gui.framework.GuiComponent;
import gg.amy.mc.recipeformagic.client.gui.framework.Layout;
import gg.amy.mc.recipeformagic.data.RecipeGraph.LayerNode;
import gg.amy.mc.recipeformagic.data.RecipeGraph.RecipeMetadata;
import gg.amy.mc.recipeformagic.data.RecipeGraph.RecipeSearchResults;
import net.minecraft.item.Item;

import java.util.*;

import static gg.amy.mc.recipeformagic.client.gui.component.ItemComponent.SLOT_SIZE;

/**
 * @author amy
 * @since 5/20/23.
 */
public class SimpleLayout implements Layout {
    @Override
    public Collection<GuiComponent> apply(final RecipeSearchResults results, final BoundingBox bounds) {
        final var layers = results.layers();
        
        final List<GuiComponent> components = new LinkedList<>();
        final Map<Item, ItemComponent> componentsByItem = new HashMap<>();
        
        var y = bounds.y() + bounds.height() / 2 - SLOT_SIZE * layers.size();
        final var edges = new LinkedList<RecipeMetadata>();
        for(final List<LayerNode> layer : layers) {
            var x = bounds.x() + 4;
            for(final LayerNode node : layer) {
                edges.addAll(node.edges());
                final var component = new ItemComponent(node.node(), x, y);
                components.add(component);
                componentsByItem.put(node.node(), component);
                x += SLOT_SIZE + 4;
            }
            y += (SLOT_SIZE + 4) * 3;
        }
        for(final var edge : edges) {
            final var output = edge.recipe().getOutput();
            final var outputComponent = componentsByItem.get(output.getItem());
            for(final var ingredient : edge.recipe().getIngredients()) {
                for(final var stack : ingredient.getMatchingStacks()) {
                    final var stackComponent = componentsByItem.get(stack.getItem());
                    if(stackComponent == null) {
//                        RecipeForMagicClient.LOGGER.warn("WARNING: stackComponent for {} is null!!!", stack.getItem());
                        continue;
                    }
                    components.add(new EdgeComponent(
                            stack, edge,
                            outputComponent.x() + SLOT_SIZE / 2, outputComponent.y() + SLOT_SIZE / 2,
                            stackComponent.x() + SLOT_SIZE / 2, stackComponent.y() + SLOT_SIZE / 2
                    ));
                }
            }
        }
        
        Collections.reverse(components);
        
        return components;
    }
}
