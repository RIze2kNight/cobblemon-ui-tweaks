package ca.landonjw.mixin.pc;

import ca.landonjw.GUIHandler;
import com.cobblemon.mod.common.client.gui.pc.PCGUI;
import com.cobblemon.mod.common.client.gui.pc.StorageWidget;
import com.cobblemon.mod.common.client.keybind.CobblemonKeyBinds;
import com.cobblemon.mod.common.client.storage.ClientPC;
import com.cobblemon.mod.common.mixin.accessor.KeyBindingAccessor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PCGUI.class)
public abstract class PCGUIMixin extends Screen {

    @Shadow(remap = false) private StorageWidget storageWidget;
    @Final @Shadow(remap = false) private ClientPC pc;

    protected PCGUIMixin(Component component) {
        super(component);
    }

    @Inject(method = "keyPressed", at = @At("HEAD"))
    private void cobblemon_ui_tweaks$keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        var summaryKey = (KeyBindingAccessor) CobblemonKeyBinds.INSTANCE.getSUMMARY();
        if (keyCode == summaryKey.boundKey().getValue()) {
            GUIHandler.INSTANCE.onSummaryPressFromPC((PCGUI)(Object)this);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount, double verticalAmount) {
        var pastureWidget = storageWidget.getPastureWidget();
        if (pastureWidget != null && pastureWidget.getPastureScrollList().isMouseOver(mouseX, mouseY)) {
            pastureWidget.getPastureScrollList().mouseScrolled(mouseX, mouseY, amount, verticalAmount);
        }
        else {
            var newBox = (storageWidget.getBox() - (int)amount) % this.pc.getBoxes().size();
            storageWidget.setBox(newBox);
        }

        return super.mouseScrolled(mouseX, mouseY, amount, verticalAmount);
    }

    @Inject(method = "closeNormally", at = @At("TAIL"), remap = false)
    private void cobblemon_ui_tweaks$closeNormally(CallbackInfo ci) {
        GUIHandler.INSTANCE.onPCClose();
    }

    @Inject(method = "keyPressed", at = @At(value = "INVOKE", target = "Lcom/cobblemon/mod/common/client/gui/pc/PCGUI;playSound(Lnet/minecraft/sounds/SoundEvent;)V"))
    private void cobblemon_ui_tweaks$keyPressedToClosePC(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        GUIHandler.INSTANCE.onPCClose();
    }

}
