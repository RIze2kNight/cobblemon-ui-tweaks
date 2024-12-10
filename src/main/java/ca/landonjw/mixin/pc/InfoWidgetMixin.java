package ca.landonjw.mixin.pc;

import com.cobblemon.mod.common.client.gui.summary.widgets.SoundlessWidget;
import com.cobblemon.mod.common.client.gui.summary.widgets.screens.info.InfoWidget;
import com.cobblemon.mod.common.pokemon.Pokemon;
import ca.landonjw.HAHighlighterRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InfoWidget.class)        // Feature added by RIze2kNight, 2024
public abstract class InfoWidgetMixin extends SoundlessWidget {
    @Shadow(remap = false) @Final private static int ROW_HEIGHT;
    @Shadow(remap = false) @Final private Pokemon pokemon;

    public InfoWidgetMixin(int pX, int pY, int pWidth, int pHeight, @NotNull Component component) {
        super(pX, pY, pWidth, pHeight, component);
    }

    @Inject(
        method = "renderWidget",
        at = @At(
                value = "RETURN"
        )
    )
    protected void renderWidget(GuiGraphics context, int pMouseX, int pMouseY, float pPartialTicks, CallbackInfo ci) {
        if(pokemon != null){
            var abilityWidget = HAHighlighterRenderer.INSTANCE.renderSummary(this.getX(),this.getY(), ROW_HEIGHT,this.width,pokemon);
            if(abilityWidget != null){
                abilityWidget.render(context, pMouseX, pMouseY, pPartialTicks);
            }
        }
    }


}
