package net.guzhenren.devkit.auction;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;

public final class PaiMaiSelectItemContext {
	private final LevelAccessor level;
	private final double x;
	private final double y;
	private final double z;
	private final Entity auctioneer;
	private ItemStack selectedItem;

	public PaiMaiSelectItemContext(LevelAccessor level, double x, double y, double z, Entity auctioneer, ItemStack selectedItem) {
		this.level = level;
		this.x = x;
		this.y = y;
		this.z = z;
		this.auctioneer = auctioneer;
		this.selectedItem = selectedItem == null ? ItemStack.EMPTY : selectedItem;
	}

	public LevelAccessor level() {
		return level;
	}

	public double x() {
		return x;
	}

	public double y() {
		return y;
	}

	public double z() {
		return z;
	}

	public Entity auctioneer() {
		return auctioneer;
	}

	public ItemStack selectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(ItemStack selectedItem) {
		this.selectedItem = selectedItem == null ? ItemStack.EMPTY : selectedItem;
	}
}
