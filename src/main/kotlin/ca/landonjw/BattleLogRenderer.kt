package ca.landonjw

import com.cobblemon.mod.common.api.gui.blitk
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation

object BattleLogRenderer {

    val top = ResourceLocation.tryBuild(CobblemonUITweaks.MODID, "textures/battle/log/top.png")
    val bottom = ResourceLocation.tryBuild(CobblemonUITweaks.MODID, "textures/battle/log/bottom.png")
    val left = ResourceLocation.tryBuild(CobblemonUITweaks.MODID, "textures/battle/log/left.png")
    val right = ResourceLocation.tryBuild(CobblemonUITweaks.MODID, "textures/battle/log/right.png")
    val topLeft = ResourceLocation.tryBuild(CobblemonUITweaks.MODID, "textures/battle/log/top_left.png")
    val topRight = ResourceLocation.tryBuild(CobblemonUITweaks.MODID, "textures/battle/log/top_right.png")
    val bottomLeft = ResourceLocation.tryBuild(CobblemonUITweaks.MODID, "textures/battle/log/bottom_left.png")
    val bottomRight = ResourceLocation.tryBuild(CobblemonUITweaks.MODID, "textures/battle/log/bottom_right.png")
    val center = ResourceLocation.tryBuild(CobblemonUITweaks.MODID, "textures/battle/log/center.png")

    fun render(context: GuiGraphics, x: Int, y: Int, height: Int, width: Int, opacity: Float) {
        blitk(
            matrixStack = context.pose(),
            texture = topLeft,
            x = x,
            y = y,
            height = 8,
            width = 7,
            textureHeight = 8,
            textureWidth = 7,
            alpha = opacity
        )

        blitk(
            matrixStack = context.pose(),
            texture = topRight,
            x = x + width - 13,
            y = y,
            height = 8,
            width = 13,
            textureHeight = 8,
            textureWidth = 13,
            alpha = opacity
        )

        blitk(
            matrixStack = context.pose(),
            texture = bottomLeft,
            x = x,
            y = y + height - 5,
            height = 5,
            width = 7,
            textureHeight = 5,
            textureWidth = 7,
            alpha = opacity
        )

        blitk(
            matrixStack = context.pose(),
            texture = bottomRight,
            x = x + width - 13,
            y = y + height - 10,
            height = 10,
            width = 13,
            textureHeight = 10,
            textureWidth = 13,
            alpha = opacity
        )

        blitk(
            matrixStack = context.pose(),
            texture = top,
            x = x + 7,
            y = y,
            height = 7,
            width = width - 7 - 13,
            textureHeight = 7,
            textureWidth = 1,
            alpha = opacity
        )

        blitk(
            matrixStack = context.pose(),
            texture = bottom,
            x = x + 7,
            y = y + height - 4,
            height = 4,
            width = width - 7 - 12,
            textureHeight = 4,
            textureWidth = 1,
            alpha = opacity
        )

        blitk(
            matrixStack = context.pose(),
            texture = left,
            x = x,
            y = y + 8,
            height = height - 8 - 5,
            width = 7,
            textureHeight = 1,
            textureWidth = 7,
            alpha = opacity
        )

        blitk(
            matrixStack = context.pose(),
            texture = right,
            x = x + width - 13,
            y = y + 8,
            height = height - 18,
            width = 13,
            textureHeight = 1,
            textureWidth = 13,
            alpha = opacity
        )

        blitk(
            matrixStack = context.pose(),
            texture = center,
            x = x + 7,
            y = y + 7,
            height = height - 11,
            width = width - 20,
            textureHeight = height - 11,
            textureWidth = width - 20,
            alpha = opacity
        )
    }

}