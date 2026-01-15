package net.guzhenren.devkit.quest;

import net.guzhenren.devkit.hook.HookResult;
import net.guzhenren.devkit.util.PlayerVarAccess;

import net.guzhenren.network.GuzhenrenModVariables;

public final class DevKitQuestDemo {
	private DevKitQuestDemo() {
	}

	public static void bootstrap() {
		RenWuHooks.registerButton("guzhenren_devkit", 0, ctx -> {
			if (ctx.buttonId() != 0) {
				return HookResult.PASS;
			}
			GuzhenrenModVariables.PlayerVariables vars = PlayerVarAccess.vars(ctx.player());
			if (vars.renwu1 == net.guzhenren.devkit.demo.DevKitDemoMarkers.DEMO_RENWU_ID) {
				vars.rwss = net.guzhenren.devkit.demo.DevKitDemoMarkers.DEMO_RENWU_ID;
				vars.rwzbxs = 1;
				vars.markSyncDirty();
				return HookResult.CONSUME;
			}
			return HookResult.PASS;
		});
	}
}
