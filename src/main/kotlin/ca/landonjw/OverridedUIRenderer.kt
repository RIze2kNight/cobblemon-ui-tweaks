package ca.landonjw

import com.cobblemon.mod.common.api.gui.blitk
import com.cobblemon.mod.common.client.gui.pc.PCGUI.Companion.PC_SPACER_HEIGHT
import com.cobblemon.mod.common.client.gui.pc.PCGUI.Companion.PC_SPACER_WIDTH
import com.cobblemon.mod.common.client.gui.pc.PCGUI.Companion.SCALE
import com.cobblemon.mod.common.util.cobblemonResource
import net.minecraft.client.gui.GuiGraphics

object OverridedUIRenderer {        // Feature added by RIze2kNight, 2024

    fun renderPC(context: GuiGraphics, x: Int, y: Int) {
        val matrices = context.pose()
        val topSpacerResource = cobblemonResource("textures/gui/pc/pc_spacer_top.png")
        val bottomSpacerResource = cobblemonResource("textures/gui/pc/pc_spacer_bottom.png")
        val rightSpacerResource = cobblemonResource("textures/gui/pc/pc_spacer_right.png")

        blitk(
            matrixStack = matrices,
            texture = topSpacerResource,
            x = (x + 86.5) / SCALE,
            y = (y + 13) / SCALE,
            width = PC_SPACER_WIDTH,
            height = PC_SPACER_HEIGHT,
            scale = SCALE
        )

        blitk(
            matrixStack = matrices,
            texture = bottomSpacerResource,
            x = (x + 86.5) / SCALE,
            y = (y + 189) / SCALE,
            width = PC_SPACER_WIDTH,
            height = PC_SPACER_HEIGHT,
            scale = SCALE
        )

        blitk(
            matrixStack = matrices,
            texture = rightSpacerResource,
            x = (x + 275.5) / SCALE,
            y = (y + 184) / SCALE,
            width = 64,
            height = 24,
            scale = SCALE
        )
    }
}