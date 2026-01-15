package net.guzhenren.devkit.shazhao;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;

public record ShaZhaoKeyContext(LevelAccessor level, double x, double y, double z, Entity entity, int slotIndex) {
}
