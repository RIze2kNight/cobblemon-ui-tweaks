package ca.landonjw

import ca.landonjw.util.ReflectionUtils
import com.cobblemon.mod.common.api.gui.blitk
import com.cobblemon.mod.common.api.moves.MoveTemplate
import com.cobblemon.mod.common.api.moves.categories.DamageCategories
import com.cobblemon.mod.common.api.text.font
import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.client.CobblemonClient
import com.cobblemon.mod.common.client.CobblemonResources
import com.cobblemon.mod.common.client.battle.ClientBattle
import com.cobblemon.mod.common.client.render.drawScaledText
import com.cobblemon.mod.common.util.asTranslated
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.resources.language.I18n
import net.minecraft.locale.Language
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.contents.PlainTextContents
import net.minecraft.network.chat.contents.TranslatableContents
import net.minecraft.resources.ResourceLocation

object MoveHoverRenderer {

    val headerLeft = ResourceLocation.tryBuild(CobblemonUITweaks.MODID, "textures/battle/move/header/left.png")
    val headerMiddle = ResourceLocation.tryBuild(CobblemonUITweaks.MODID, "textures/battle/move/header/middle.png")
    val headerRight = ResourceLocation.tryBuild(CobblemonUITweaks.MODID, "textures/battle/move/header/right.png")
    val bodyLeftBorder = ResourceLocation.tryBuild(CobblemonUITweaks.MODID, "textures/battle/move/body/left_border.png")
    val bodyLeftCorner = ResourceLocation.tryBuild(CobblemonUITweaks.MODID, "textures/battle/move/body/left_corner.png")
    val bodyBottomBorder = ResourceLocation.tryBuild(CobblemonUITweaks.MODID, "textures/battle/move/body/bottom_border.png")
    val bodyRightCorner = ResourceLocation.tryBuild(CobblemonUITweaks.MODID, "textures/battle/move/body/right_corner.png")
    val bodyRightBorder = ResourceLocation.tryBuild(CobblemonUITweaks.MODID, "textures/battle/move/body/right_border.png")
    val bodyMiddle = ResourceLocation.tryBuild(CobblemonUITweaks.MODID, "textures/battle/move/body/middle.png")

    fun render(context: GuiGraphics, x: Float, y: Float, move: MoveTemplate, typeChanges: Map<String,String>) {
        val bodyWidth = 150
        val opacity = 0.95

        val basePowerText = if (move.power == 0.0) {
            "cobblemon_ui_tweaks.move.base_power.not_applicable".asTranslated()
        }
        else {
            "cobblemon_ui_tweaks.move.base_power".asTranslated(move.power)
        }

        val accuracyText = if (move.accuracy == -1.0) {
            "cobblemon_ui_tweaks.move.accuracy.cant_miss".asTranslated()
        }
        else {
            "cobblemon_ui_tweaks.move.accuracy".asTranslated("${move.accuracy}%")
        }

        val moveDescription = move.description.font(CobblemonResources.DEFAULT_LARGE)
        val descriptionLines = Minecraft.getInstance().font.splitter.splitLines(moveDescription, 150, moveDescription.style)
        val orderedLines = Language.getInstance().getVisualOrder(descriptionLines)

        val effectivenessText = getMoveEffectiveness(move, typeChanges)

        val bodyHeight = (orderedLines.size * 8) + 8 + if (effectivenessText != null) 8 else 0

        blitk(
            matrixStack = context.pose(),
            texture = headerLeft,
            x = x,
            y = y - bodyHeight - 34 - 4,
            height = 34,
            width = 2,
            alpha = opacity
        )

        blitk(
            matrixStack = context.pose(),
            texture = headerMiddle,
            x = x + 2,
            y = y - bodyHeight - 34 - 4,
            height = 34,
            width = bodyWidth,
            alpha = opacity
        )

        blitk(
            matrixStack = context.pose(),
            texture = headerRight,
            x = x + 2 + bodyWidth,
            y = y - bodyHeight - 34 - 4,
            height = 34,
            width = 2,
            alpha = opacity
        )

        blitk(
            matrixStack = context.pose(),
            texture = bodyLeftBorder,
            x = x,
            y = y - bodyHeight - 4,
            height = bodyHeight,
            width = 2,
            alpha = opacity
        )

        blitk(
            matrixStack = context.pose(),
            texture = bodyLeftCorner,
            x = x,
            y = y - 4,
            height = 4,
            width = 2,
            alpha = opacity
        )

        blitk(
            matrixStack = context.pose(),
            texture = bodyBottomBorder,
            x = x + 2,
            y = y - 4,
            height = 4,
            width = bodyWidth,
            alpha = opacity
        )

        blitk(
            matrixStack = context.pose(),
            texture = bodyRightCorner,
            x = x + bodyWidth + 2,
            y = y - 4,
            height = 4,
            width = 2,
            alpha = opacity
        )

        blitk(
            matrixStack = context.pose(),
            texture = bodyRightBorder,
            x = x + bodyWidth + 2,
            y = y - bodyHeight - 4,
            height = bodyHeight,
            width = 2,
            alpha = opacity
        )

        blitk(
            matrixStack = context.pose(),
            texture = bodyMiddle,
            x = x + 2,
            y = y - bodyHeight - 4,
            height = bodyHeight,
            width = bodyWidth,
            alpha = opacity
        )

        drawScaledText(
            context = context,
            font = CobblemonResources.DEFAULT_LARGE,
            text = move.displayName,
            x = x + 4,
            y = y - bodyHeight - 34 - 4 + 3,
            opacity = opacity
        )

        ScaleableTypeIcon(
            x = x + bodyWidth - 10,
            y = y - bodyHeight - 34 - 4 + 3,
            type = move.elementalType,
            small = true,
            opacity = opacity.toFloat()
        ).render(context)

        drawScaledText(
            context = context,
            font = CobblemonResources.DEFAULT_LARGE,
            text = basePowerText,
            scale = 0.85f,
            x = x + 4,
            y = y - bodyHeight - 34 - 4 + 3 + 14,
            opacity = opacity,
        )

        drawScaledText(
            context = context,
            font = CobblemonResources.DEFAULT_LARGE,
            text = accuracyText,
            scale = 0.85f,
            x = x + 4,
            y = y - bodyHeight - 34 - 4 + 3 + 22,
            opacity = opacity
        )

        orderedLines.forEachIndexed { index, line ->
            drawScaledText(
                context = context,
                text = line,
                scaleX = 0.85f,
                scaleY = 0.85f,
                x = x + 4,
                y = y - bodyHeight - 34 - 4 + 3 + 34 + (index * 8),
                opacity = opacity
            )
        }

        if (effectivenessText != null) {
            drawScaledText(
                context = context,
                font = CobblemonResources.DEFAULT_LARGE,
                text = effectivenessText,
                scale = 0.85f,
                x = x + 4,
                y = y - bodyHeight - 34 - 4 + 3 + 34 + (orderedLines.size * 8) + 4,
                opacity = opacity
            )
        }
    }

    private fun getMoveEffectiveness(move: MoveTemplate, typeChanges: Map<String, String>): MutableComponent? {
        val battle = CobblemonClient.battle ?: return null
        val opponent = battle.side2.activeClientBattlePokemon.firstOrNull()?.battlePokemon ?: return null
        val aspects: Set<String> = ReflectionUtils.getPrivateField(opponent, "aspects") ?: return null

        val opponentForm = opponent.species.getForm(aspects)
        if (move.damageCategory == DamageCategories.STATUS) return null

        val typeChangeList = getTypeChanges(battle,typeChanges)
        var primaryType: ElementalType? = opponentForm.primaryType
        var secondaryType: ElementalType? = opponentForm.secondaryType

        if (typeChangeList.isNotEmpty()) {
            primaryType = typeChangeList.getOrNull(0) ?: primaryType // Use the first element if available, otherwise keep the original type
            secondaryType = typeChangeList.getOrNull(1) ?: secondaryType // Use the second element if available, otherwise keep the original type
        }

        return MoveEffectivenessCalculator.getMoveEffectiveness(move.elementalType, primaryType!!, secondaryType)
    }

    private fun getTypeChanges(battle: ClientBattle, typeChanges: Map<String, String>): List<ElementalType?>{
        val typeChangeList = mutableListOf<ElementalType?>()
        val ownerName = when (val owner = battle.side2.actors[0].displayName.contents){
            is TranslatableContents -> I18n.get(owner.key)
            is PlainTextContents -> I18n.get(owner.text())
            else -> owner.toString()
        }
        val opponentName = when (val opponent = battle.side2.activeClientBattlePokemon.firstOrNull()?.battlePokemon?.displayName?.contents){
            is TranslatableContents -> I18n.get(opponent.key)
            is PlainTextContents -> I18n.get(opponent.text())
            else -> opponent.toString()
        }

        var currentType: List<String> = emptyList()
        if(battle.side2.actors[0].type.name == "WILD"){
            if(typeChanges["WILD:$opponentName"] != null){
                currentType = typeChanges["WILD:$opponentName"]!!.split("/")
            }
        }
        else if(typeChanges["$ownerName:$opponentName"] != null){
            currentType = typeChanges["$ownerName:$opponentName"]!!.split("/")
        }

        for(element in currentType){
            typeChangeList.add(ElementalTypes.get(element))
        }

        return typeChangeList
    }
}
