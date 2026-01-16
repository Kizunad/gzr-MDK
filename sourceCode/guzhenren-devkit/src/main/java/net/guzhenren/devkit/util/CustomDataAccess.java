package net.guzhenren.devkit.util;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.core.component.DataComponents;

public final class CustomDataAccess {
	private CustomDataAccess() {
	}

	public static double getDouble(ItemStack stack, String key) {
		if (stack == null || stack.isEmpty()) {
			return 0;
		}
		return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble(key);
	}

	public static void setDouble(ItemStack stack, String key, double value) {
		if (stack == null || stack.isEmpty()) {
			return;
		}
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putDouble(key, value));
	}

	public static ItemStack copyWithDouble(ItemStack original, String key, double value) {
		if (original == null || original.isEmpty()) {
			return ItemStack.EMPTY;
		}
		ItemStack copy = original.copy();
		setDouble(copy, key, value);
		return copy;
	}

	public static ItemStack copyWithOptionalCustomData(ItemStack original, HolderLookup.Provider lookupProvider) {
		if (original == null || original.isEmpty()) {
			return ItemStack.EMPTY;
		}
		return original.copy();
	}
}
