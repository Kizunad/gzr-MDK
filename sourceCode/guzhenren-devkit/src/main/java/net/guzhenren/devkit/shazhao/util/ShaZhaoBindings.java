package net.guzhenren.devkit.shazhao.util;

import net.guzhenren.network.GuzhenrenModVariables;

import net.minecraft.world.item.ItemStack;

public final class ShaZhaoBindings {
	private ShaZhaoBindings() {
	}

	public static double getBoundId(GuzhenrenModVariables.PlayerVariables vars, int keySlotIndex) {
		if (keySlotIndex == 1) {
			return vars.sanzhao1;
		} else if (keySlotIndex == 2) {
			return vars.sanzhao2;
		} else if (keySlotIndex == 3) {
			return vars.sanzhao3;
		} else if (keySlotIndex == 4) {
			return vars.sanzhao4;
		}
		return 0;
	}

	public static void setBoundId(GuzhenrenModVariables.PlayerVariables vars, int keySlotIndex, double id) {
		if (keySlotIndex == 1) {
			vars.sanzhao1 = id;
		} else if (keySlotIndex == 2) {
			vars.sanzhao2 = id;
		} else if (keySlotIndex == 3) {
			vars.sanzhao3 = id;
		} else if (keySlotIndex == 4) {
			vars.sanzhao4 = id;
		}
	}

	public static ItemStack getBoundItem(GuzhenrenModVariables.PlayerVariables vars, int keySlotIndex) {
		if (keySlotIndex == 1) {
			return vars.ShaZhao1;
		} else if (keySlotIndex == 2) {
			return vars.ShaZhao2;
		} else if (keySlotIndex == 3) {
			return vars.ShaZhao3;
		} else if (keySlotIndex == 4) {
			return vars.ShaZhao4;
		}
		return ItemStack.EMPTY;
	}
}
