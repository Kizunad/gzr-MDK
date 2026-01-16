package net.guzhenren.devkit.shop;

import java.util.Objects;

import net.guzhenren.devkit.hook.HookRegistration;
import net.guzhenren.devkit.hook.HookRegistry;

public final class ShopHooks {
	private ShopHooks() {
	}

	public static final HookRegistry<ShouGouSellContext> SHOU_GOU_SELL = new HookRegistry<>();
	public static final HookRegistry<ShangDianStockContext> SHANG_DIAN_STOCK = new HookRegistry<>();

	public static HookRegistration registerShouGouSell(String owner, int priority, ShouGouSellHandler handler) {
		return SHOU_GOU_SELL.register(new HookRegistration(Objects.requireNonNull(owner, "owner"), priority), handler);
	}

	public static HookRegistration registerShangDianStock(String owner, int priority, ShangDianStockHandler handler) {
		return SHANG_DIAN_STOCK.register(new HookRegistration(Objects.requireNonNull(owner, "owner"), priority), handler);
	}
}
