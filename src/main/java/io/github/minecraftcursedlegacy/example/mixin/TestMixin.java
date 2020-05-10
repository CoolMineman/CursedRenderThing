package io.github.minecraftcursedlegacy.example.mixin;

import org.jglrxavpok.glutils.TessellatorModel;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.entity.tile.SignRenderer;
import net.minecraft.entity.SignEntity;

@Mixin(SignRenderer.class)
public class TestMixin {
    TessellatorModel model = new TessellatorModel("/assets/modid/models/MMSEV.obj");

    @Inject(at = @At("HEAD"), method = "method_1809", cancellable = true)
    public void method_1809(SignEntity arg, double d, double d1, double d2, float f, CallbackInfo bruh) {
        GL11.glPushMatrix();
        GL11.glTranslated(d, d1, d2);
        model.render();
        GL11.glPopMatrix();
        //bruh.cancel();
    }
}