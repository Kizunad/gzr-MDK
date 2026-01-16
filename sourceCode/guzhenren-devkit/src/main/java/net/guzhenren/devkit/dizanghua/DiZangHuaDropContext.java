package net.guzhenren.devkit.dizanghua;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;

public final class DiZangHuaDropContext {
	private final LevelAccessor level;
	private final BlockPos pos;
	private final Player player;
	private final String liupaiId;
	private final int rarityTier;
	private final double qiyun;
	private ItemStack drop;

	public DiZangHuaDropContext(LevelAccessor level, BlockPos pos, Player player, String liupaiId, int rarityTier, double qiyun, ItemStack drop) {
		this.level = level;
		this.pos = pos;
		this.player = player;
		this.liupaiId = liupaiId;
		this.rarityTier = rarityTier;
		this.qiyun = qiyun;
		this.drop = drop == null ? ItemStack.EMPTY : drop;
	}

	public LevelAccessor level() {
		return level;
	}

	public BlockPos pos() {
		return pos;
	}

	public Player player() {
		return player;
	}

	public String liupaiId() {
		return liupaiId;
	}

	public int rarityTier() {
		return rarityTier;
	}

	public double qiyun() {
		return qiyun;
	}

	public ItemStack drop() {
		return drop;
	}

	public void setDrop(ItemStack drop) {
		this.drop = drop == null ? ItemStack.EMPTY : drop;
	}
}
