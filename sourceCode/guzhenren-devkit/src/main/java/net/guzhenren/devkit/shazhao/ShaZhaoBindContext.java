package net.guzhenren.devkit.shazhao;

import net.guzhenren.init.GuzhenrenModMenus;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record ShaZhaoBindContext(Player player, int keySlotIndex, ItemStack shazhaoItem) {
	public boolean isInShaZhaoMenu() {
		return player.containerMenu instanceof GuzhenrenModMenus.MenuAccessor;
	}
}
