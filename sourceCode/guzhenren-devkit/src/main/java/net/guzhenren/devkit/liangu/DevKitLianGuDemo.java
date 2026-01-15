package net.guzhenren.devkit.liangu;

import net.guzhenren.devkit.liangu.spi.ExternalLianGuRecipe;

import net.guzhenren.init.GuzhenrenModItems;
import net.guzhenren.init.GuzhenrenModMenus;
import net.guzhenren.network.GuzhenrenModVariables;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class DevKitLianGuDemo {
	private DevKitLianGuDemo() {
	}

	public static void bootstrap() {
		LianGuRecipes.register(new ExternalLianGuRecipe() {
			@Override
			public String id() {
				return "guzhenren_devkit:demo";
			}

			@Override
			public boolean matches(Player player) {
				ItemStack stack = (player.containerMenu instanceof GuzhenrenModMenus.MenuAccessor m) ? m.getSlots().get(8).getItem() : ItemStack.EMPTY;
				return stack.getItem() == GuzhenrenModItems.BAI_SHI_GU_GU_FANG.get() && player.getData(GuzhenrenModVariables.PLAYER_VARIABLES).LianGu == false;
			}

			@Override
			public void onStart(Player player) {
				if (!(player.containerMenu instanceof GuzhenrenModMenus.MenuAccessor menu)) {
					return;
				}

				ItemStack slot0 = menu.getSlots().get(0).getItem();
				if (slot0 == null || slot0.isEmpty()) {
					return;
				}

				GuzhenrenModVariables.PlayerVariables vars = player.getData(GuzhenrenModVariables.PLAYER_VARIABLES);
				vars.LianGuJinDu = 0;
				vars.lg_ke = 0;
				vars.GuFang = net.guzhenren.devkit.demo.DevKitDemoMarkers.DEMO_LIANGU_ID;
				vars.LianGu = true;
				vars.markSyncDirty();

				menu.getSlots().get(10).set(slot0.copy());
				menu.getSlots().get(0).set(ItemStack.EMPTY);
				player.containerMenu.broadcastChanges();
			}

			@Override
			public void onTick(Player player) {
				if (!(player.containerMenu instanceof GuzhenrenModMenus.MenuAccessor menu)) {
					return;
				}

				GuzhenrenModVariables.PlayerVariables vars = player.getData(GuzhenrenModVariables.PLAYER_VARIABLES);
				vars.lg_ke = vars.lg_ke + 1;
				if (vars.lg_ke / 20 >= 1) {
					vars.lg_ke = 0;
					vars.LianGuJinDu = vars.LianGuJinDu + 1;
				}

				if (vars.LianGuJinDu >= 5) {
					vars.LianGuJinDu = 0;
					vars.GuFang = 0;
					vars.LianGu = false;
					vars.markSyncDirty();

					ItemStack kept = menu.getSlots().get(10).getItem();
					menu.getSlots().get(9).set(kept.copy());
					menu.getSlots().get(10).set(ItemStack.EMPTY);
					player.containerMenu.broadcastChanges();
					return;
				}

				vars.markSyncDirty();
			}

			@Override
			public boolean isActive(Player player) {
				return ExternalLianGuRecipe.isRunning(player, net.guzhenren.devkit.demo.DevKitDemoMarkers.DEMO_LIANGU_ID);
			}
		});
	}
}
