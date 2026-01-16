package net.guzhenren.devkit;

import net.neoforged.fml.common.Mod;

@Mod(GuzhenrenDevKitMod.MODID)
public class GuzhenrenDevKitMod {
	public static final String MODID = "guzhenren_devkit";

	public GuzhenrenDevKitMod() {
		net.guzhenren.devkit.dizanghua.DiZangHuaTagRegistry.bootstrap();
	}
}
