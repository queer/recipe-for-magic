package gg.amy.mc.recipeformagic;

import gg.amy.mc.recipeformagic.data.RecipeGraph;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author amy
 * @since 5/20/23.
 */
public class RecipeForMagic implements ModInitializer {
    public static final String ID = "recipe-for-magic";
    
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);
    
    public static final RecipeGraph GRAPH = new RecipeGraph();
    
    public static Identifier id(final String path) {
        return new Identifier(ID, path);
    }
    
    @Override
    public void onInitialize() {
        LOGGER.info("henlo world");
    }
}
