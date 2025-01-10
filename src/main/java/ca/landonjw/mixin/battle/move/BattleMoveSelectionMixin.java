package ca.landonjw.mixin.battle.move;

import ca.landonjw.MoveHoverRenderer;
import ca.landonjw.ResizeableTextQueue;
import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.client.gui.battle.subscreen.BattleMoveSelection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(BattleMoveSelection.class)
public class BattleMoveSelectionMixin {

    @Shadow(remap = false) private List<BattleMoveSelection.MoveTile> moveTiles;

    @Unique private boolean isSubscribed = false;
    @Unique final Map<String,String> battleTypeChanges = new HashMap<>();

    @Inject(method = "renderWidget", at = @At("TAIL"))
    public void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        ensureSubscribed();
        this.moveTiles.forEach(tile -> {
            if (tile.isHovered(mouseX, mouseY)) {
                context.pose().pushPose();
                context.pose().translate(0.0F, 0.0F, 400.0F);

                var guiScale = Minecraft.getInstance().options.guiScale().get();
                var tooltipScale = getTooltipScale(guiScale);

                context.pose().scale(tooltipScale, tooltipScale, 1.0f);
                MoveHoverRenderer.INSTANCE.render(context, tile.getX() / tooltipScale, tile.getY() / tooltipScale, tile.getMoveTemplate(), battleTypeChanges);
                context.pose().popPose();
            }
        });
    }

    @Unique
    private float getTooltipScale(float guiScale) {
        if (guiScale == 5f) return 0.9f;
        if (guiScale == 4f) return 1f;
        if (guiScale == 3f) return 1.3f;
        if (guiScale == 2f) return 1.6f;
        if (guiScale == 1f) return 2.0f;
        return 1f;
    }

    private void ensureSubscribed() {
        if (!isSubscribed) {
            isSubscribed = true;
            ResizeableTextQueue queueWithBattleMessages = (ResizeableTextQueue) (Object) CobblemonClient.INSTANCE.getBattle().getMessages();
            queueWithBattleMessages.cobblemon_ui_tweaks$subscribe(message -> {

                if(message.getContents() instanceof TranslatableContents contents
                        && "cobblemon.battle.start.typechange".equals(contents.getKey())){
                    Object[] args = contents.getArgs();

                    if(args.length >= 2){
                        String owner = null;
                        String pokemonName = null;
                        String newType = args[1].toString(); // Second argument is the type

                        // Extract owner and Pokémon name from the first argument
                        if (args[0] instanceof MutableComponent PokemonComponent
                                && PokemonComponent.getContents() instanceof TranslatableContents PokemonContents) {

                            if("cobblemon.battle.owned_pokemon".equals(PokemonContents.getKey())){
                                Object[] pokeArgs = PokemonContents.getArgs();
                                owner = pokeArgs[0].toString(); // Owner name
                                pokemonName = pokeArgs[1].toString();
                            }
                            else {
                                owner = "WILD";
                                pokemonName = PokemonContents.toString();
                            }
                        }

                        if (owner != null && pokemonName != null && !Objects.equals(owner, Minecraft.getInstance().getUser().getName())) {
                            // Create a unique key based on owner and Pokémon name
                            String key = owner + ":" + pokemonName;
                            battleTypeChanges.put(key, newType); // Update or add the new type
                        }
                    }
                }
            });
        }
    }
}
