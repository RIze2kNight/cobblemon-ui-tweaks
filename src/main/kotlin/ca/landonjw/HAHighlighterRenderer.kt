package ca.landonjw

import com.cobblemon.mod.common.api.text.bold
import com.cobblemon.mod.common.client.gui.pc.PCGUI.Companion.SCALE
import com.cobblemon.mod.common.client.gui.summary.widgets.screens.info.InfoOneLineWidget
import com.cobblemon.mod.common.client.render.drawScaledText
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.pokemon.abilities.HiddenAbility
import com.cobblemon.mod.common.util.asTranslated
import com.cobblemon.mod.common.util.lang
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Style
import net.minecraft.network.chat.TextColor
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object HAHighlighterRenderer {      // Feature added by RIze2kNight, 2024
    private val goldStyle: Style = Style.EMPTY.withColor(TextColor.fromRgb(0xFFD700))

    fun renderPC(context: GuiGraphics, x: Int, y: Int, pokemon: Pokemon) {
        if(hasHiddenAbility(pokemon)) {
            drawScaledText(
                context = context,
                text = pokemon.ability.displayName.asTranslated().withStyle(goldStyle),
                x = x + 39,
                y = y + 154,
                centered = true,
                shadow = true,
                scale = SCALE
            )
        }
    }

    fun renderSummary(x: Int, y: Int, ROW_HEIGHT: Int, width: Int, pokemon: Pokemon): InfoOneLineWidget? {
        if(hasHiddenAbility(pokemon)) {
            val abilityWidget = InfoOneLineWidget(
                pX = x,
                pY = y + 5 * ROW_HEIGHT,
                width = width,
                label = lang("ui.info.ability").bold(),
                value = pokemon.ability.displayName.asTranslated().bold().withStyle(goldStyle),
            )
            return abilityWidget
        }
        return null
    }

    private fun hasHiddenAbility(pokemon: Pokemon): Boolean = pokemon.form.abilities
        .filterIsInstance<HiddenAbility>()
        .any { ability -> pokemon.ability.template == ability.template }
}