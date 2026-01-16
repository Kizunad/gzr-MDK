package net.guzhenren.devkit.dizanghua;

import java.util.Objects;

import net.guzhenren.devkit.hook.HookRegistration;
import net.guzhenren.devkit.hook.HookRegistry;

public final class DiZangHuaHooks {
	private DiZangHuaHooks() {
	}

	public static final HookRegistry<DiZangHuaDropContext> DROP = new HookRegistry<>();

	public static HookRegistration registerDrop(String owner, int priority, DiZangHuaDropHandler handler) {
		return DROP.register(new HookRegistration(Objects.requireNonNull(owner, "owner"), priority), handler);
	}
}
