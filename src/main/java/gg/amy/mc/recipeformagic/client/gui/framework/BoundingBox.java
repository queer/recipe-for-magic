package gg.amy.mc.recipeformagic.client.gui.framework;

/**
 * @author amy
 * @since 5/20/23.
 */
public record BoundingBox(int x, int y, int width, int height) {
    public boolean contains(final int x, final int y) {
        return x >= this.x && y >= this.y && x <= this.x + width && y <= this.y + height;
    }
    
    public boolean contains(final Coordinate coordinate) {
        return contains(coordinate.x(), coordinate.y());
    }
}
