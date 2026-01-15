package net.guzhenren.devkit.util;

import net.minecraft.world.entity.Entity;

import net.guzhenren.network.GuzhenrenModVariables;

public final class PlayerVarAccess {
	private PlayerVarAccess() {
	}

	public static GuzhenrenModVariables.PlayerVariables vars(Entity entity) {
		return entity.getData(GuzhenrenModVariables.PLAYER_VARIABLES);
	}

	public static void markDirty(Entity entity) {
		vars(entity).markSyncDirty();
	}
}
