package tw.ch1ck3n.zerodaystylehotbar.mixin;

import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import tw.ch1ck3n.zerodaystylehotbar.util.MinecraftInstance;

@Mixin(DrawContext.class)
public abstract class DrawContextMixin implements MinecraftInstance {

    @ModifyArg(
            method = "drawStackCount",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;IIIZ)I"),
            index = 2
    )
    private int modifyX(int x) {

        return x;
    }
}