package net.guzhenren.devkit.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class TagKeyUtil {
	private TagKeyUtil() {
	}

	public static TagKey<Item> itemTag(String namespace, String path) {
		return TagKey.create(net.minecraft.core.registries.Registries.ITEM, ResourceLocation.fromNamespaceAndPath(namespace, path));
	}

	public static TagKey<Item> itemTag(ResourceLocation id) {
		return TagKey.create(net.minecraft.core.registries.Registries.ITEM, id);
	}
}
