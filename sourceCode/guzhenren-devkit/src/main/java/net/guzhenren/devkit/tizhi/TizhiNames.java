package net.guzhenren.devkit.tizhi;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class TizhiNames {
	private static final Map<Integer, String> NAMES = new ConcurrentHashMap<>();

	private TizhiNames() {
	}

	public static void register(int id, String name) {
		if (id <= 0) {
			throw new IllegalArgumentException("tizhi id must be positive: " + id);
		}
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("tizhi name must be non-blank");
		}
		NAMES.put(id, name);
	}

	public static String get(int id) {
		return NAMES.get(id);
	}
}
