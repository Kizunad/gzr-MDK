package net.guzhenren.devkit.hook;

import java.util.Objects;

public final class HookRegistration {
	private final String owner;
	private final int priority;

	public HookRegistration(String owner, int priority) {
		this.owner = Objects.requireNonNull(owner, "owner");
		this.priority = priority;
	}

	public String owner() {
		return owner;
	}

	public int priority() {
		return priority;
	}
}
