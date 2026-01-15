package net.guzhenren.devkit.mixin;

import net.guzhenren.devkit.hook.HookResult;
import net.guzhenren.devkit.quest.RenWuButtonContext;
import net.guzhenren.devkit.quest.RenWuHooks;
import net.guzhenren.network.RenWuGuiButtonMessage;

import net.minecraft.world.entity.player.Player;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenWuGuiButtonMessage.class)
public class RenWuGuiButtonHookMixin {
	@Inject(method = "handleButtonAction", at = @At("HEAD"), cancellable = true)
	private static void guzhenren_devkit$hook(Player entity, int buttonID, int x, int y, int z, CallbackInfo ci) {
		if (entity == null) {
			return;
		}
		HookResult result = RenWuHooks.BUTTON.dispatch(new RenWuButtonContext(entity, buttonID, x, y, z));
		if (result == HookResult.CONSUME) {
			ci.cancel();
		}
	}
}
