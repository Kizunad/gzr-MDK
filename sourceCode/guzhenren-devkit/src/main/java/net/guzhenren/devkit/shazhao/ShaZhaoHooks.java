package net.guzhenren.devkit.shazhao;

import java.util.Objects;

import net.guzhenren.devkit.hook.HookRegistration;
import net.guzhenren.devkit.hook.HookRegistry;

public final class ShaZhaoHooks {
	private ShaZhaoHooks() {
	}

	public static final HookRegistry<ShaZhaoKeyContext> KEY_PRESS = new HookRegistry<>();
	public static final HookRegistry<ShaZhaoBindContext> BIND = new HookRegistry<>();
	public static final HookRegistry<ShaZhaoTickContext> TICK = new HookRegistry<>();

	public static HookRegistration registerKeyPress(String owner, int priority, ShaZhaoKeyHandler handler) {
		return KEY_PRESS.register(new HookRegistration(Objects.requireNonNull(owner, "owner"), priority), handler);
	}

	public static HookRegistration registerKeyPress(String owner, int priority, java.util.Set<Integer> keySlots, ShaZhaoKeyHandler handler) {
		Objects.requireNonNull(keySlots, "keySlots");
		Objects.requireNonNull(handler, "handler");
		return registerKeyPress(owner, priority, ctx -> keySlots.contains(ctx.slotIndex()) ? handler.handle(ctx) : net.guzhenren.devkit.hook.HookResult.PASS);
	}

	public static HookRegistration registerBind(String owner, int priority, ShaZhaoBindHandler handler) {
		return BIND.register(new HookRegistration(Objects.requireNonNull(owner, "owner"), priority), handler);
	}

	public static HookRegistration registerBind(String owner, int priority, java.util.Set<Integer> keySlots, ShaZhaoBindHandler handler) {
		Objects.requireNonNull(keySlots, "keySlots");
		Objects.requireNonNull(handler, "handler");
		return registerBind(owner, priority, ctx -> keySlots.contains(ctx.keySlotIndex()) ? handler.handle(ctx) : net.guzhenren.devkit.hook.HookResult.PASS);
	}

	public static HookRegistration registerTick(String owner, int priority, ShaZhaoTickHandler handler) {
		return TICK.register(new HookRegistration(Objects.requireNonNull(owner, "owner"), priority), handler);
	}

	public static HookRegistration registerTick(String owner, int priority, java.util.Set<Integer> keySlots, ShaZhaoTickHandler handler) {
		Objects.requireNonNull(keySlots, "keySlots");
		Objects.requireNonNull(handler, "handler");
		return registerTick(owner, priority, ctx -> keySlots.contains(ctx.keySlotIndex()) ? handler.handle(ctx) : net.guzhenren.devkit.hook.HookResult.PASS);
	}
}
