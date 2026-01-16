package net.guzhenren.devkit.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class GuzhenrenTags {
	private GuzhenrenTags() {
	}

	public static final class Auction {
		private Auction() {
		}

		public static TagKey<Item> pool() {
			return TagKeyUtil.itemTag("guzhenren", "paimaihang");
		}
	}

	public static final class Shop {
		private Shop() {
		}

		public static TagKey<Item> currency() {
			return TagKeyUtil.itemTag("guzhenren", "yuanshi");
		}

		public static List<TagKey<Item>> priceTags() {
			List<TagKey<Item>> tags = new ArrayList<>();
			int[] prices = new int[] { 1, 5, 10, 50, 100, 200, 400, 600, 800, 1000, 1200, 1500 };
			for (int price : prices) {
				tags.add(TagKeyUtil.itemTag("guzhenren", "jiage_" + price));
			}
			tags.add(TagKeyUtil.itemTag(ResourceLocation.parse("guzhenren:jiiage_5000")));
			return tags;
		}
	}

	public static final class Dizanghua {
		private Dizanghua() {
		}

		public static TagKey<Item> rarityZ1() {
			return TagKeyUtil.itemTag("guzhenren", "z1");
		}

		public static TagKey<Item> rarityZ2() {
			return TagKeyUtil.itemTag("guzhenren", "z2");
		}

		public static TagKey<Item> rarityZ3() {
			return TagKeyUtil.itemTag("guzhenren", "z3");
		}

		public static TagKey<Item> rarityZ4() {
			return TagKeyUtil.itemTag("guzhenren", "z4");
		}
	}
}
