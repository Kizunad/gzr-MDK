package net.guzhenren.devkit.shazhao.util;

import net.guzhenren.network.GuzhenrenModVariables;

public final class ShaZhaoCooldowns {
	private ShaZhaoCooldowns() {
	}

	public static double getCooldownTicks(GuzhenrenModVariables.PlayerVariables vars, int keySlotIndex) {
		if (keySlotIndex == 1) {
			return vars.sanzhao_CD;
		} else if (keySlotIndex == 2) {
			return vars.sanzhao_CD1;
		} else if (keySlotIndex == 3) {
			return vars.sanzhao_CD2;
		} else if (keySlotIndex == 4) {
			return vars.sanzhao_CD3;
		}
		return 0;
	}

	public static void setCooldownTicks(GuzhenrenModVariables.PlayerVariables vars, int keySlotIndex, double ticks) {
		if (keySlotIndex == 1) {
			vars.sanzhao_CD = ticks;
		} else if (keySlotIndex == 2) {
			vars.sanzhao_CD1 = ticks;
		} else if (keySlotIndex == 3) {
			vars.sanzhao_CD2 = ticks;
		} else if (keySlotIndex == 4) {
			vars.sanzhao_CD3 = ticks;
		}
	}
}
