package gg.amy.mc.recipeformagic.client;

import gg.amy.mc.recipeformagic.RecipeForMagic;
import gg.amy.mc.recipeformagic.data.RecipeGraph.RecipeSearchResults;
import gg.amy.mc.recipeformagic.mixin.HandledScreenAccessorMixin;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil.Type;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * @author amy
 * @since 5/20/23.
 */
public class RecipeForMagicClient implements ClientModInitializer {
    public static final String ID = "recipe-for-magic";
    
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);
    public static final KeyBinding SWITCH_ACTIVE_RECIPE = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.recipeformagic.view",
            Type.KEYSYM, GLFW.GLFW_KEY_BACKSPACE, "category.recipeformagic.keybindings"));
    
    @SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "PublicField"})
    public static Optional<RecipeSearchResults> RECIPE_SEARCH_RESULTS = Optional.empty();
    
    public static Identifier id(final String path) {
        return new Identifier(ID, path);
    }
    
    @Override
    public void onInitializeClient() {
        LOGGER.info("henlo client!");
    }
}
