package net.guzhenren.devkit.shazhao.util;

import net.guzhenren.init.GuzhenrenModMenus;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class ShaZhaoMenuSlots {
	private ShaZhaoMenuSlots() {
	}

	public static boolean isInShaZhaoMenu(Player player) {
		return player.containerMenu instanceof GuzhenrenModMenus.MenuAccessor;
	}

	public static ItemStack getSlot(Player player, int slotIndex) {
		if (!(player.containerMenu instanceof GuzhenrenModMenus.MenuAccessor menu)) {
			return ItemStack.EMPTY;
		}
		return menu.getSlots().get(slotIndex).getItem();
	}
}
