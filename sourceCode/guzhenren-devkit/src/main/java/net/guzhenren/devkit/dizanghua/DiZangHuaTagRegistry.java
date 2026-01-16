package net.guzhenren.devkit.dizanghua;

import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class DiZangHuaTagRegistry {
	private static final CopyOnWriteArrayList<Entry> ENTRIES = new CopyOnWriteArrayList<>();

	private DiZangHuaTagRegistry() {
	}

	public static void registerDropTag(ResourceLocation liupaiTagId, int rarityTier, TagKey<Item> additionalDropTag) {
		ENTRIES.add(new Entry(liupaiTagId, rarityTier, additionalDropTag));
	}

	public static void bootstrap() {
		DiZangHuaHooks.registerDrop("guzhenren_devkit", -1000, ctx -> {
			ResourceLocation liupaiTagId = ResourceLocation.parse("guzhenren:" + ctx.liupaiId());
			for (Entry entry : ENTRIES) {
				if (!entry.matches(liupaiTagId, ctx.rarityTier())) {
					continue;
				}
				if (!ctx.drop().is(entry.additionalDropTag())) {
					ctx.setDrop(newRandomItem(entry.additionalDropTag()));
				}
			}
			return net.guzhenren.devkit.hook.HookResult.PASS;
		});
	}

	private static ItemStack newRandomItem(TagKey<Item> tag) {
		return new ItemStack(BuiltInRegistries.ITEM.getOrCreateTag(tag).getRandomElement(RandomSource.create())
				.orElseGet(() -> BuiltInRegistries.ITEM.wrapAsHolder(Items.AIR)).value());
	}

	private record Entry(ResourceLocation liupaiTagId, int rarityTier, TagKey<Item> additionalDropTag) {
		boolean matches(ResourceLocation liupaiTagId, int rarityTier) {
			return this.liupaiTagId.equals(liupaiTagId) && this.rarityTier == rarityTier;
		}
	}
}
