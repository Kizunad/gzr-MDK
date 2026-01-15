package net.guzhenren.devkit.liangu;

import net.guzhenren.init.GuzhenrenModMenus;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class LianGuMenuSlots {
	private LianGuMenuSlots() {
	}

	public static boolean isInLianGuMenu(Player player) {
		return player.containerMenu instanceof GuzhenrenModMenus.MenuAccessor;
	}

	public static ItemStack getSlot(Player player, int slotIndex) {
		if (!(player.containerMenu instanceof GuzhenrenModMenus.MenuAccessor menu)) {
			return ItemStack.EMPTY;
		}
		return menu.getSlots().get(slotIndex).getItem();
	}

	public static void setSlot(Player player, int slotIndex, ItemStack stack) {
		if (!(player.containerMenu instanceof GuzhenrenModMenus.MenuAccessor menu)) {
			return;
		}
		menu.getSlots().get(slotIndex).set(stack);
		player.containerMenu.broadcastChanges();
	}

	public static int findFirstMatchInInputs(Player player, java.util.function.Predicate<ItemStack> predicate) {
		if (!(player.containerMenu instanceof GuzhenrenModMenus.MenuAccessor menu)) {
			return -1;
		}
		for (int i = 0; i <= 7; i++) {
			ItemStack stack = menu.getSlots().get(i).getItem();
			if (predicate.test(stack)) {
				return i;
			}
		}
		return -1;
	}
}
