package net.guzhenren.devkit.nixiangliangu;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.world.entity.player.Player;

/**
 * 逆向炼蛊（逆炼）外部配方注册表。镜像 {@code net.guzhenren.devkit.liangu.LianGuRecipes}。
 *
 * <p>DevKit 的两个逆炼 Mixin 通过本表查找配方：
 * <ul>
 *   <li>{@link #findMatch(Player)} —— 逆炼“开始”按钮点击时，取第一个 {@code matches} 命中的配方</li>
 *   <li>{@link #findActive(Player)} —— 逆炼 GUI 每 tick 时，取第一个 {@code isActive} 命中的配方</li>
 * </ul>
 */
public final class NiXiangLianGuRecipes {
	private static final CopyOnWriteArrayList<ExternalNiXiangLianGuRecipe> RECIPES = new CopyOnWriteArrayList<>();

	/**
	 * 构造方法。
	 */
	private NiXiangLianGuRecipes() {
	}

	/**
	 * 将该条目注册到 DevKit 注册表。
	 */
	public static void register(ExternalNiXiangLianGuRecipe recipe) {
		RECIPES.add(recipe);
	}

	/**
	 * 执行 all 操作。
	 */
	public static List<ExternalNiXiangLianGuRecipe> all() {
		return List.copyOf(RECIPES);
	}

	/**
	 * 查找匹配给定上下文的match。
	 */
	public static ExternalNiXiangLianGuRecipe findMatch(Player player) {
		for (ExternalNiXiangLianGuRecipe recipe : RECIPES) {
			try {
				if (recipe.matches(player)) {
					return recipe;
				}
			} catch (Throwable t) {
				System.err.println("[guzhenren-devkit] nixiang recipe error: " + recipe.id() + ": " + t);
				t.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 查找匹配给定上下文的active。
	 */
	public static ExternalNiXiangLianGuRecipe findActive(Player player) {
		for (ExternalNiXiangLianGuRecipe recipe : RECIPES) {
			try {
				if (recipe.isActive(player)) {
					return recipe;
				}
			} catch (Throwable t) {
				System.err.println("[guzhenren-devkit] nixiang recipe error: " + recipe.id() + ": " + t);
				t.printStackTrace();
			}
		}
		return null;
	}
}
