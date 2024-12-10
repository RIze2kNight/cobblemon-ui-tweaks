package ca.landonjw

import ca.landonjw.util.ReflectionUtils
import com.cobblemon.mod.common.api.battles.model.actor.ActorType
import com.cobblemon.mod.common.api.gui.blitk
import com.cobblemon.mod.common.api.text.font
import com.cobblemon.mod.common.client.CobblemonClient.battle
import com.cobblemon.mod.common.client.CobblemonResources
import com.cobblemon.mod.common.client.battle.ActiveClientBattlePokemon
import com.cobblemon.mod.common.client.gui.TypeIcon
import com.cobblemon.mod.common.client.gui.battle.BattleOverlay
import com.cobblemon.mod.common.client.gui.battle.BattleOverlay.Companion.PORTRAIT_DIAMETER
import com.cobblemon.mod.common.client.gui.battle.BattleOverlay.Companion.PORTRAIT_OFFSET_X
import com.cobblemon.mod.common.client.gui.battle.BattleOverlay.Companion.PORTRAIT_OFFSET_Y
import com.cobblemon.mod.common.client.gui.battle.BattleOverlay.Companion.TILE_WIDTH
import com.cobblemon.mod.common.client.render.drawScaledText
import com.cobblemon.mod.common.pokemon.FormData
import com.cobblemon.mod.common.pokemon.Species
import com.cobblemon.mod.common.util.asTranslated
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation
import kotlin.math.max

object BattlePortraitHoverRenderer {

    val left = ResourceLocation.tryBuild(CobblemonUITweaks.MODID, "textures/battle/pokemon/summary/left.png")
    val flippedLeft = ResourceLocation.tryBuild(CobblemonUITweaks.MODID, "textures/battle/pokemon/summary/flipped/left.png")
    val middle = ResourceLocation.tryBuild(CobblemonUITweaks.MODID, "textures/battle/pokemon/summary/middle.png")
    val right = ResourceLocation.tryBuild(CobblemonUITweaks.MODID, "textures/battle/pokemon/summary/right.png")
    val flippedRight = ResourceLocation.tryBuild(CobblemonUITweaks.MODID, "textures/battle/pokemon/summary/flipped/right.png")

    fun render(context: GuiGraphics, mouseX: Int, mouseY: Int) {
        val battle = battle ?: return

        val playerUUID = Minecraft.getInstance().player?.uuid ?: return

        val side1 = if (battle.side1.actors.any { it.uuid == playerUUID }) battle.side1 else battle.side2
        val side2 = if (side1 == battle.side1) battle.side2 else battle.side1

        side1.activeClientBattlePokemon.forEachIndexed { index, activeClientBattlePokemon ->
            renderIfHovered(context, mouseX, mouseY, activeClientBattlePokemon, true, index)
        }

        side2.activeClientBattlePokemon.forEachIndexed { index, activeClientBattlePokemon ->
            renderIfHovered(context, mouseX, mouseY, activeClientBattlePokemon, false, index)
        }
    }

    private fun renderIfHovered(context: GuiGraphics, mouseX: Int, mouseY: Int, activeBattlePokemon: ActiveClientBattlePokemon, left: Boolean, rank: Int) {
        // Prevent render if pokemon is currently being swapped out
        if (activeBattlePokemon.animations.peek() !== null) return

        val x = activeBattlePokemon.xDisplacement
        val y = BattleOverlay.VERTICAL_INSET + rank * BattleOverlay.VERTICAL_SPACING

        val x0 = x + if (left) PORTRAIT_OFFSET_X else { TILE_WIDTH - PORTRAIT_DIAMETER - PORTRAIT_OFFSET_Y }
        val x1 = x0 + PORTRAIT_DIAMETER
        val y0 = y + PORTRAIT_OFFSET_Y
        val y1 = y0 + PORTRAIT_DIAMETER + PORTRAIT_OFFSET_Y

        if (mouseX < x0 || mouseX > x1) return
        if (mouseY < y0 || mouseY > y1) return


        val battlePokemon = activeBattlePokemon.battlePokemon ?: return

        val aspects: Set<String>? = ReflectionUtils.getPrivateField(battlePokemon, "aspects")

        val species = activeBattlePokemon.battlePokemon?.species ?: return
        val form = activeBattlePokemon.battlePokemon?.species?.getForm(aspects ?: setOf()) ?: return

        val formText = getFormText(species, form)
        val trainerText = getTrainerText(activeBattlePokemon)

        val formTextWidth = getTextWidth(formText, CobblemonResources.DEFAULT_LARGE)
        val typeWidth = if (form.secondaryType != null) 38 else 18
        val totalFormTextWidth = formTextWidth + typeWidth

        val trainerTextWidth = getTextWidth(trainerText, CobblemonResources.DEFAULT_LARGE)

        val largestTextWidth = max(totalFormTextWidth, trainerTextWidth)

        val width = max(100, largestTextWidth + 4)

        if (left) {
            blitk(
                matrixStack = context.pose(),
                texture = this.left,
                x = x,
                y = y + 40,
                height = 30,
                width = 2
            )

            blitk(
                matrixStack = context.pose(),
                texture = this.middle,
                x = x + 2,
                y = y + 40,
                height = 30,
                width = width
            )

            blitk(
                matrixStack = context.pose(),
                texture = this.right,
                x = x + 2 + width,
                y = y + 40,
                height = 30,
                width = 2
            )

            drawScaledText(
                context = context,
                font = CobblemonResources.DEFAULT_LARGE,
                text = formText,
                x = x + 4,
                y = y + 40 + 4
            )

            drawScaledText(
                context = context,
                font = CobblemonResources.DEFAULT_LARGE,
                text = trainerText,
                x = x + 4,
                y = y + 40 + 17
            )

            TypeIcon(
                x = x + 4 + formTextWidth + 4,
                y = y + 40 + 4,
                type = form.primaryType,
                secondaryType = form.secondaryType,
                secondaryOffset = 10f,
                small = true,
                centeredX = false
            ).render(context)
        }
        else {
            blitk(
                matrixStack = context.pose(),
                texture = this.flippedLeft,
                x = x + 129 - width - 2,
                y = y + 40,
                height = 30,
                width = 2
            )

            blitk(
                matrixStack = context.pose(),
                texture = this.middle,
                x = x + 129 - width,
                y = y + 40,
                height = 30,
                width = width
            )

            blitk(
                matrixStack = context.pose(),
                texture = this.flippedRight,
                x = x + 129,
                y = y + 40,
                height = 30,
                width = 2
            )

            drawScaledText(
                context = context,
                font = CobblemonResources.DEFAULT_LARGE,
                text = formText,
                x = x + 129 - width - 2 + 4,
                y = y + 40 + 4
            )

            drawScaledText(
                context = context,
                font = CobblemonResources.DEFAULT_LARGE,
                text = trainerText,
                x = x + 129 - width - 2 + 4,
                y = y + 40 + 17
            )

            TypeIcon(
                x = x + 129 - width - 2 + 4 + formTextWidth + 4,
                y = y + 40 + 4,
                type = form.primaryType,
                secondaryType = form.secondaryType,
                secondaryOffset = 10f,
                small = true,
                centeredX = false
            ).render(context)
        }
    }

    private fun getTextWidth(text: MutableComponent, font: ResourceLocation? = null): Int {
        return Minecraft.getInstance().font.width(if (font != null) text.font(font) else text)
    }

    private fun getFormText(species: Species, form: FormData): MutableComponent {
        val speciesName = species.name
        val formName = form.name
        val name = if (formName == "Normal") speciesName else "$speciesName-$formName"

        return "cobblemon_ui_tweaks.portrait.pokemon.form".asTranslated(name)
    }

    private fun getTrainerText(pokemon: ActiveClientBattlePokemon): MutableComponent {
        if (pokemon.actor.type == ActorType.PLAYER) {
            return "cobblemon_ui_tweaks.portrait.pokemon.trainer".asTranslated(pokemon.actor.displayName)
        }
        else {
            return "cobblemon_ui_tweaks.portrait.pokemon.trainer.not_applicable".asTranslated()
        }
    }

}
