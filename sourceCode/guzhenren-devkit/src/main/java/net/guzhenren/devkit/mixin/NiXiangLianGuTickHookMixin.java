package net.guzhenren.devkit.mixin;

import net.guzhenren.devkit.nixiangliangu.NiXiangLianGuRecipes;
import net.guzhenren.devkit.nixiangliangu.ExternalNiXiangLianGuRecipe;
import net.guzhenren.network.GuzhenrenModVariables;
import net.guzhenren.procedures.NiLianGuiDangGaiGUIDaKaiShiMeiKeFaShengProcedure;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 逆向炼蛊“每 tick”注入点。
 *
 * <p>主模组 {@code NiLianGuiDangGaiGUIDaKaiShiMeiKeFaShengProcedure} 是逆炼 GUI 打开期间每 tick 执行的过程
 * （原版会推进 vars.LianGuJinDu 并在满 100 时按 slot0 的蛊类型产出）。
 * 本 Mixin 在 HEAD 拦截：当 {@code vars.LianGu} 为真且有外部配方 {@code isActive} 命中时，
 * 调用其 {@code onTick} 并取消原版逻辑。
 */
@Mixin(NiLianGuiDangGaiGUIDaKaiShiMeiKeFaShengProcedure.class)
public class NiXiangLianGuTickHookMixin {
	/**
	 * DevKit 注入到目标过程的钩子；当外部处理器接管时取消原版逻辑。
	 */
	@Inject(method = "execute", at = @At("HEAD"), cancellable = true)
	private static void guzhenren_devkit$hook(LevelAccessor world, double x, double y, double z, Entity entity, CallbackInfo ci) {
		if (!(entity instanceof Player player)) {
			return;
		}
		if (player.level().isClientSide()) {
			return;
		}
		GuzhenrenModVariables.PlayerVariables vars = player.getData(GuzhenrenModVariables.PLAYER_VARIABLES);
		if (!vars.LianGu) {
			return;
		}
		ExternalNiXiangLianGuRecipe recipe = NiXiangLianGuRecipes.findActive(player);
		if (recipe == null) {
			return;
		}
		try {
			recipe.onTick(player);
		} catch (Throwable t) {
			System.err.println("[guzhenren-devkit] nixiang tick error: " + recipe.id() + ": " + t);
			t.printStackTrace();
			return;
		}
		ci.cancel();
	}
}
