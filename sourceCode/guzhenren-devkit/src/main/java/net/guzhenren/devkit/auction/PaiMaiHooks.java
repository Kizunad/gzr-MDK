package net.guzhenren.devkit.auction;

import java.util.Objects;

import net.guzhenren.devkit.hook.HookRegistration;
import net.guzhenren.devkit.hook.HookRegistry;

public final class PaiMaiHooks {
	private PaiMaiHooks() {
	}

	public static final HookRegistry<PaiMaiSelectItemContext> SELECT_ITEM = new HookRegistry<>();

	public static HookRegistration registerSelectItem(String owner, int priority, PaiMaiSelectItemHandler handler) {
		return SELECT_ITEM.register(new HookRegistration(Objects.requireNonNull(owner, "owner"), priority), handler);
	}
}
