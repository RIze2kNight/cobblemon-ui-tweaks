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

        this.isFocused = false
    }

    override fun setFocused(bl: Boolean) {
        if (value != (storageWidget.box + 1).toString()){
            value = (storageWidget.box + 1).toString()
        }
        super.setFocused(bl)
    }

    private fun updateNewPCBox() {
        val newPCBox = this.value.toIntOrNull()
        if (newPCBox != null && newPCBox > 0 && newPCBox <= pc.boxes.size && newPCBox - 1 != storageWidget.box) {
            storageWidget.box = newPCBox - 1 // Update storage widget
            value = (newPCBox).toString()
        }
        else{
            this.value = (storageWidget.box + 1).toString()
        }
    }

    override fun renderWidget(context: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {
        if (cursorPosition != value.length) moveCursorToEnd(Screen.hasShiftDown())
        val currentBox = this.storageWidget.box + 1     // Dynamically fetch current box for rendering

        if (isFocused){
            drawScaledText(
                context = context,
                font = CobblemonResources.DEFAULT_LARGE,
                text = Component.translatable("cobblemon.ui.pc.box.title", if (isFocused) "$value|" else currentBox).bold(),
                x = x + 172 - 140,
                y = y,
                centered = true
            )
        }

    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        when(keyCode) {
            InputConstants.KEY_RETURN -> {
                updateNewPCBox()
                this.isFocused = false
                return true
            }
            InputConstants.KEY_ESCAPE, InputConstants.KEY_RIGHT, InputConstants.KEY_LEFT -> {
                this.isFocused = false
                return true
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers)
    }
}