package net.guzhenren.devkit.liangu;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.guzhenren.devkit.liangu.spi.ExternalLianGuRecipe;

import net.minecraft.world.entity.player.Player;

public final class LianGuRecipes {
	private static final CopyOnWriteArrayList<ExternalLianGuRecipe> RECIPES = new CopyOnWriteArrayList<>();

	private LianGuRecipes() {
	}

	public static void register(ExternalLianGuRecipe recipe) {
		RECIPES.add(recipe);
	}

	public static List<ExternalLianGuRecipe> all() {
		return List.copyOf(RECIPES);
	}

	public static ExternalLianGuRecipe findMatch(Player player) {
		for (ExternalLianGuRecipe recipe : RECIPES) {
			try {
				if (recipe.matches(player)) {
					return recipe;
				}
			} catch (Throwable t) {
				System.err.println("[guzhenren-devkit] liangu recipe error: " + recipe.id() + ": " + t);
				t.printStackTrace();
			}
		}
		return null;
	}

	public static ExternalLianGuRecipe findActive(Player player) {
		for (ExternalLianGuRecipe recipe : RECIPES) {
			try {
				if (recipe.isActive(player)) {
					return recipe;
				}
			} catch (Throwable t) {
				System.err.println("[guzhenren-devkit] liangu recipe error: " + recipe.id() + ": " + t);
				t.printStackTrace();
			}
		}
		return null;
	}
}
