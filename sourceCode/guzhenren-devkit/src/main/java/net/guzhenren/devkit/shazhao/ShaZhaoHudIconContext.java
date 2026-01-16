package net.guzhenren.devkit.shazhao;

import net.guzhenren.devkit.hook.HookResult;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class ShaZhaoHudIconContext {
	private final Player player;
	private final int keySlotIndex;
	private final double boundId;
	private final ItemStack boundItem;
	private ResourceLocation icon;

	public ShaZhaoHudIconContext(Player player, int keySlotIndex, double boundId, ItemStack boundItem) {
		this.player = player;
		this.keySlotIndex = keySlotIndex;
		this.boundId = boundId;
		this.boundItem = boundItem == null ? ItemStack.EMPTY : boundItem;
	}

	public Player player() {
		return player;
	}

	public int keySlotIndex() {
		return keySlotIndex;
	}

	public double boundId() {
		return boundId;
	}

	public ItemStack boundItem() {
		return boundItem;
	}

	public ResourceLocation icon() {
		return icon;
	}

	public void setIcon(ResourceLocation icon) {
		this.icon = icon;
	}

	public HookResult consumeWithIcon(ResourceLocation icon) {
		setIcon(icon);
		return HookResult.CONSUME;
	}
}
