package net.guzhenren.devkit.shop;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class ShangDianStockContext {
	private final Player player;
	private final int shopSlotIndex;
	private ItemStack currentStock;
	private boolean overrideAllowed;

	public ShangDianStockContext(Player player, int shopSlotIndex, ItemStack currentStock) {
		this.player = player;
		this.shopSlotIndex = shopSlotIndex;
		this.currentStock = currentStock == null ? ItemStack.EMPTY : currentStock;
	}

	public Player player() {
		return player;
	}

	public int shopSlotIndex() {
		return shopSlotIndex;
	}

	public ItemStack currentStock() {
		return currentStock;
	}

	public void setCurrentStock(ItemStack currentStock) {
		this.currentStock = currentStock == null ? ItemStack.EMPTY : currentStock;
		this.overrideAllowed = true;
	}

	public boolean overrideAllowed() {
		return overrideAllowed;
	}
}
