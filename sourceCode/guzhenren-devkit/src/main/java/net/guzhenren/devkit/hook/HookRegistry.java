package net.guzhenren.devkit.hook;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class HookRegistry<C> {
	private static final Comparator<Entry<?>> ENTRY_ORDER = Comparator
			.<Entry<?>>comparingInt(e -> -e.registration.priority())
			.thenComparing(e -> e.registration.owner());

	private final CopyOnWriteArrayList<Entry<C>> entries = new CopyOnWriteArrayList<>();

	public HookRegistration register(HookRegistration registration, HookHandler<C> handler) {
		Entry<C> entry = new Entry<>(registration, handler);
		entries.add(entry);
		entries.sort(ENTRY_ORDER);
		return registration;
	}

	public List<Entry<C>> entries() {
		return List.copyOf(entries);
	}

	public HookResult dispatch(C context) {
		for (Entry<C> entry : entries) {
			HookResult result;
			try {
				result = entry.handler.handle(context);
			} catch (Throwable t) {
				System.err.println("[guzhenren-devkit] hook error from " + entry.registration.owner() + ": " + t);
				t.printStackTrace();
				result = HookResult.PASS;
			}
			if (result == HookResult.CONSUME) {
				return HookResult.CONSUME;
			}
		}
		return HookResult.PASS;
	}

	public static final class Entry<C> {
		private final HookRegistration registration;
		private final HookHandler<C> handler;

		private Entry(HookRegistration registration, HookHandler<C> handler) {
			this.registration = registration;
			this.handler = handler;
		}

		public HookRegistration registration() {
			return registration;
		}

		public HookHandler<C> handler() {
			return handler;
		}
	}
}
