package net.guzhenren.devkit.liangu.spi;

import net.minecraft.world.entity.player.Player;

public interface ExternalLianGuRecipe {
	String id();

	boolean matches(Player player);

	void onStart(Player player);

	void onTick(Player player);

	boolean isActive(Player player);

	static boolean isRunning(Player player, double guFangId) {
		net.guzhenren.network.GuzhenrenModVariables.PlayerVariables vars = player.getData(net.guzhenren.network.GuzhenrenModVariables.PLAYER_VARIABLES);
		return vars.LianGu && vars.GuFang == guFangId;
	}
}
