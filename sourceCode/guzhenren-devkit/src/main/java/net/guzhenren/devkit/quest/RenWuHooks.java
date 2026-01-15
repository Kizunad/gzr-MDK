package net.guzhenren.devkit.quest;

import java.util.Objects;

import net.guzhenren.devkit.hook.HookRegistration;
import net.guzhenren.devkit.hook.HookRegistry;

public final class RenWuHooks {
	private RenWuHooks() {
	}

	public static final HookRegistry<RenWuButtonContext> BUTTON = new HookRegistry<>();

	public static HookRegistration registerButton(String owner, int priority, RenWuButtonHandler handler) {
		return BUTTON.register(new HookRegistration(Objects.requireNonNull(owner, "owner"), priority), handler);
	}
}
