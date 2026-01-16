package net.guzhenren.devkit.shop;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class ShouGouSellContext {
	private final Player player;
	private final ItemStack input;
	private final ItemStack currencyStack;
	private final int price;
	private boolean allow;

	public ShouGouSellContext(Player player, ItemStack input, ItemStack currencyStack, int price, boolean allow) {
		this.player = player;
		this.input = input == null ? ItemStack.EMPTY : input;
		this.currencyStack = currencyStack == null ? ItemStack.EMPTY : currencyStack;
		this.price = price;
		this.allow = allow;
	}

	public Player player() {
		return player;
	}

	public ItemStack input() {
		return input;
	}

	public ItemStack currencyStack() {
		return currencyStack;
	}

	public int price() {
		return price;
	}

	public boolean allow() {
		return allow;
	}

	public void setAllow(boolean allow) {
		this.allow = allow;
	}
}
