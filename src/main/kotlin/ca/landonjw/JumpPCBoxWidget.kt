/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ca.landonjw

import com.cobblemon.mod.common.api.text.bold
import com.cobblemon.mod.common.client.CobblemonResources
import com.cobblemon.mod.common.client.gui.pc.StorageWidget
import com.cobblemon.mod.common.client.render.drawScaledText
import com.cobblemon.mod.common.client.storage.ClientPC
import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import org.slf4j.LoggerFactory

open class JumpPCBoxWidget(     // Feature added by RIze2kNight, 2024
    private var storageWidget: StorageWidget,
    private var pc: ClientPC,
    x: Int,
    y: Int, width: Int, height: Int, pcBoxText: Component
): EditBox(
    Minecraft.getInstance().font,
    x, y, width, height, pcBoxText
) {
    private val logger = LoggerFactory.getLogger("cobblemonuitweaks")

    init {
        this.setFilter { input -> input.isEmpty() || input.all { it.isDigit() } }
        logger.info("CobblemonUITweaks JumpPCBoxWidget init")

        setSelectedPCBox(storageWidget.box + 1)
    }

    override fun setFocused(focused: Boolean) {
        super.setFocused(focused)
        this.value = (storageWidget.box + 1).toString()
    }

    private fun setSelectedPCBox(pcBox: Int) {
        if (isFocused) {
            isFocused = false
        }
        value = pcBox.toString() // Set the value directly
    }

    private fun updateNewPCBox() {
        val newPCBox = this.value.toIntOrNull()
        if (newPCBox != null && newPCBox > 0 && newPCBox <= pc.boxes.size && newPCBox != storageWidget.box) {
            storageWidget.box = newPCBox - 1 // Update storage widget
            value = (newPCBox).toString()
        }
        else{
            this.value = storageWidget.box.toString()
        }
    }

    override fun renderWidget(context: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {
        if (cursorPosition != value.length) moveCursorToEnd(Screen.hasShiftDown())
        val currentBox = this.storageWidget.box + 1     // Dynamically fetch current box for rendering

        drawScaledText(
            context = context,
            font = CobblemonResources.DEFAULT_LARGE,
            text = Component.translatable("cobblemon.ui.pc.box.title", if (isFocused) "$value|" else currentBox).bold(),
            x = x + 172 - 140,
            y = y,
            centered = true
        )
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (keyCode == InputConstants.KEY_RETURN) { // Enter key
            updateNewPCBox()
            this.isFocused = false
            return true
        } else if (keyCode == InputConstants.KEY_ESCAPE) { // Escape key to cancel changes
            this.value = storageWidget.box.toString()
            this.isFocused = false
            return true
        }
        return super.keyPressed(keyCode, scanCode, modifiers)
    }
}