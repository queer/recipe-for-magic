package gg.amy.mc.recipeformagic.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.*;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

/**
 * @author amy
 * @since 5/20/23.
 */
public final class RenderHelper {
    private RenderHelper() {
    }
    
    public static void drawRect(final MatrixStack matrices, final int x, final int y, final int width, final int height, final int color) {
        DrawableHelper.fill(matrices, x, y, x + width, y + height, color);
    }
    
    public static void drawLine(final MatrixStack matrices, final int x1, final int y1, final int x2, final int y2, final int color) {
        final var a = color >> 24 & 0xFF;
        final var r = color >> 16 & 0xFF;
        final var g = color >> 8 & 0xFF;
        final var b = color & 0xFF;
        
        final var tessellator = Tessellator.getInstance();
        final var bufferBuilder = tessellator.getBuffer();
        final var positionMatrix = matrices.peek().getPositionMatrix();
        
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferBuilder.begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(positionMatrix, x1, y1, 0).color(r, g, b, a).next();
        bufferBuilder.vertex(positionMatrix, x2, y2, 0).color(r, g, b, a).next();
        
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        
        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
        
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }
    
    public static void renderTooltip(final MatrixStack matrices, final int x, final int y, final int w, final int h) {
        final Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        drawGradient(matrix4f, x - 3, y - 4, x + w + 3, y - 3, 0x505000FF, 0x505000FF, 0);
        drawGradient(matrix4f, x - 3, y + h + 3, x + w + 3, y + h + 4, 0x505000FF, 0x505000FF, 0);
        drawGradient(matrix4f, x - 3, y - 3, x + w + 3, y + h + 3, 0x505000FF, 0x505000FF, 0);
        drawGradient(matrix4f, x - 4, y - 3, x - 3, y + h + 3, 0x505000FF, 0x505000FF, 0);
        drawGradient(matrix4f, x + w + 3, y - 3, x + w + 4, y + h + 3, 0x505000FF, 0x505000FF, 0);
        drawGradient(matrix4f, x - 3, y - 3 + 1, x - 3 + 1, y + h + 3 - 1, 0x505000FF, 0x505000FF, 0);
        drawGradient(matrix4f, x + w + 2, y - 3 + 1, x + w + 3, y + h + 3 - 1, 0x505000FF, 0x505000FF, 0);
        drawGradient(matrix4f, x - 3, y - 3, x + w + 3, y - 3 + 1, 0x505000FF, 0x505000FF, 0);
        drawGradient(matrix4f, x - 3, y + h + 2, x + w + 3, y + h + 3, 0x505000FF, 0x505000FF, 0);
    }
    
    public static void renderTooltipLike(final MatrixStack matrices, final int x, final int y, final int w, final int h, final int z, final int c1, final int c2) {
        final Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        drawGradient(matrix4f, x - 3, y - 4, x + w + 3, y - 3, c1, c2, z);
        drawGradient(matrix4f, x - 3, y + h + 3, x + w + 3, y + h + 4, c1, c2, z);
        drawGradient(matrix4f, x - 3, y - 3, x + w + 3, y + h + 3, c1, c2, z);
        drawGradient(matrix4f, x - 4, y - 3, x - 3, y + h + 3, c1, c2, z);
        drawGradient(matrix4f, x + w + 3, y - 3, x + w + 4, y + h + 3, c1, c2, z);
        drawGradient(matrix4f, x - 3, y - 3 + 1, x - 3 + 1, y + h + 3 - 1, c1, c2, z);
        drawGradient(matrix4f, x + w + 2, y - 3 + 1, x + w + 3, y + h + 3 - 1, c1, c2, z);
        drawGradient(matrix4f, x - 3, y - 3, x + w + 3, y - 3 + 1, c1, c2, z);
        drawGradient(matrix4f, x - 3, y + h + 2, x + w + 3, y + h + 3, c1, c2, z);
    }
    
    public static void drawGradient(final Matrix4f positionMatrix, final int x1, final int y1, final int x2, final int y2, final int c1, final int c2, final int z) {
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        final var a1 = c1 >> 24 & 0xFF;
        final var r1 = c1 >> 16 & 0xFF;
        final var g1 = c1 >> 8 & 0xFF;
        final var b1 = c1 & 0xFF;
        final var a2 = c2 >> 24 & 0xFF;
        final var r2 = c2 >> 16 & 0xFF;
        final var g2 = c2 >> 8 & 0xFF;
        final var b2 = c2 & 0xFF;
        bufferBuilder.vertex(positionMatrix, x2, y1, z).color(r1, g1, b1, a1).next();
        bufferBuilder.vertex(positionMatrix, x1, y1, z).color(r1, g1, b1, a1).next();
        bufferBuilder.vertex(positionMatrix, x1, y2, z).color(r2, g2, b2, a2).next();
        bufferBuilder.vertex(positionMatrix, x2, y2, z).color(r2, g2, b2, a2).next();
        tessellator.draw();
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
    }
}
