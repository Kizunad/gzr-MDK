package net.guzhenren.devkit.damage;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public final class GuzhenrenDamageTypeTags {
	private GuzhenrenDamageTypeTags() {
	}

	public static TagKey<DamageType> dao(String daoId) {
		if (daoId == null || daoId.isBlank()) {
			throw new IllegalArgumentException("daoId must be non-blank");
		}
		if (!daoId.endsWith("_dao")) {
			throw new IllegalArgumentException("daoId must end with '_dao': " + daoId);
		}
		return TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("guzhenren", daoId));
	}

	public static final TagKey<DamageType> XING_DAO = dao("xing_dao");
}
