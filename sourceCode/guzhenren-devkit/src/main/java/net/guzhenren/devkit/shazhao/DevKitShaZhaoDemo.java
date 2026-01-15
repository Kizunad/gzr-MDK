package net.guzhenren.devkit.shazhao;

import net.guzhenren.devkit.hook.HookResult;
import net.guzhenren.devkit.util.PlayerVarAccess;

import net.guzhenren.network.GuzhenrenModVariables;

public final class DevKitShaZhaoDemo {
	private DevKitShaZhaoDemo() {
	}

	public static void bootstrap() {
		ShaZhaoHooks.registerKeyPress("guzhenren_devkit", 0, ctx -> {
			if (ctx.slotIndex() != 1) {
				return HookResult.PASS;
			}
			GuzhenrenModVariables.PlayerVariables vars = PlayerVarAccess.vars(ctx.entity());
			if (vars.sanzhao1 == net.guzhenren.devkit.demo.DevKitDemoMarkers.DEMO_SHAZHAO_ID) {
				vars.sanzhao_CD = 1;
				vars.markSyncDirty();
				return HookResult.CONSUME;
			}
			return HookResult.PASS;
		});
	}
}
