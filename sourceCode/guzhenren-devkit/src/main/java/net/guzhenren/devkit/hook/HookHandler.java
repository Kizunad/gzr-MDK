package net.guzhenren.devkit.hook;

public interface HookHandler<C> {
	HookResult handle(C context);
}
