package ca.landonjw

import com.cobblemon.mod.common.CobblemonSounds
import com.cobblemon.mod.common.client.gui.pc.PCGUI
import com.cobblemon.mod.common.client.gui.summary.Summary
import com.cobblemon.mod.common.pokemon.Pokemon
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.sounds.SimpleSoundInstance
import net.minecraft.sounds.SoundEvent

object GUIHandler {

    private var PC: PCGUI? = null
    var hoveredPokemon: Pokemon? = null
    var hoveredPokemonType: String? = null

    fun onSummaryPressFromPC(pc: PCGUI) {
        if (hoveredPokemon != null) {
            PC = pc
            Summary.open(listOf(hoveredPokemon, null, null, null, null, null), false)
            playSound(CobblemonSounds.GUI_CLICK)
        }
    }

    fun onSummaryClose() {
        if (PC != null) {
            Minecraft.getInstance().setScreen(PC)
            PC = null
        }
    }

    private fun playSound(soundEvent: SoundEvent) {
        Minecraft.getInstance().soundManager.play(SimpleSoundInstance.forUI(soundEvent, 1.0F))
    }

}