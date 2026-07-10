package net.guzhenren.devkit.util;

import net.minecraft.world.entity.Entity;

import net.guzhenren.network.GuzhenrenModVariables;

/**
 * 玩家变量（{@code GuzhenrenModVariables.PlayerVariables}）的安全访问入口。
 *
 * <p>本体（mod本体_魂道智道更新）新增字段已全部在此暴露。字段类型：
 * 除 {@code maomin_gc_1..16} 为 boolean 外，其余新增字段均为 double。</p>
 */
public final class PlayerVarAccess {
	private PlayerVarAccess() {
	}

	public static GuzhenrenModVariables.PlayerVariables vars(Entity entity) {
		return entity.getData(GuzhenrenModVariables.PLAYER_VARIABLES);
	}

	public static void markDirty(Entity entity) {
		vars(entity).markSyncDirty();
	}

	// ───────────────────────── 魂珀 ─────────────────────────
	public static double getHunPoCapacity(Entity e) {
		return vars(e).hunpochengzaijixian;
	}

	public static void setHunPoCapacity(Entity e, double v) {
		vars(e).hunpochengzaijixian = v;
	}

	public static double getHunPoRatioReduced(Entity e) {
		return vars(e).hunpo_baifenbi_jianshang;
	}

	public static void setHunPoRatioReduced(Entity e, double v) {
		vars(e).hunpo_baifenbi_jianshang = v;
	}

	// ──────────────────────── 九道遗藏 ────────────────────────
	public static double getJiuDaoYiCang(Entity e) {
		return vars(e).jiudaoyicang;
	}

	public static void setJiuDaoYiCang(Entity e, double v) {
		vars(e).jiudaoyicang = v;
	}

	public static double getJiuDaoYiCangBranch(Entity e) {
		return vars(e).jiudaoyicang_fenzhitiaojian;
	}

	public static void setJiuDaoYiCangBranch(Entity e, double v) {
		vars(e).jiudaoyicang_fenzhitiaojian = v;
	}

	// ──────────────────────── 树明 ────────────────────────
	public static double getHuaShiMing(Entity e) {
		return vars(e).huashiming;
	}

	public static void setHuaShiMing(Entity e, double v) {
		vars(e).huashiming = v;
	}

	public static double getHuaShiMingItem(Entity e) {
		return vars(e).huashiming_wupin;
	}

	public static void setHuaShiMingItem(Entity e, double v) {
		vars(e).huashiming_wupin = v;
	}

	// ──────────────────────── 灵牌 ────────────────────────
	public static double getLingPaiJiSha(Entity e) {
		return vars(e).lingpai_jisha;
	}

	public static void setLingPaiJiSha(Entity e, double v) {
		vars(e).lingpai_jisha = v;
	}

	public static double getLingPaiRenShou(Entity e) {
		return vars(e).lingpai_renshou;
	}

	public static void setLingPaiRenShou(Entity e, double v) {
		vars(e).lingpai_renshou = v;
	}

	// ──────────────────────── 主线 / 剧情标记 ────────────────────────
	public static double getZhuXian(Entity e) {
		return vars(e).zhuxian;
	}

	public static void setZhuXian(Entity e, double v) {
		vars(e).zhuxian = v;
	}

	public static double getAnQing(Entity e) {
		return vars(e).anqing;
	}

	public static void setAnQing(Entity e, double v) {
		vars(e).anqing = v;
	}

	public static double getKongXiaoHua(Entity e) {
		return vars(e).kongxiaohua;
	}

	public static void setKongXiaoHua(Entity e, double v) {
		vars(e).kongxiaohua = v;
	}

	public static double getLingDongZi(Entity e) {
		return vars(e).lingdongzi;
	}

	public static void setLingDongZi(Entity e, double v) {
		vars(e).lingdongzi = v;
	}

	public static double getLiuTianMing(Entity e) {
		return vars(e).liutianming;
	}

	public static void setLiuTianMing(Entity e, double v) {
		vars(e).liutianming = v;
	}

	public static double getJiWuJiu(Entity e) {
		return vars(e).jiwujiu;
	}

	public static void setJiWuJiu(Entity e, double v) {
		vars(e).jiwujiu = v;
	}

	public static double getQianShuiYuan(Entity e) {
		return vars(e).qianshuiyuan;
	}

	public static void setQianShuiYuan(Entity e, double v) {
		vars(e).qianshuiyuan = v;
	}

	public static double getXuanXiZi(Entity e) {
		return vars(e).xuanxizi;
	}

	public static void setXuanXiZi(Entity e, double v) {
		vars(e).xuanxizi = v;
	}

	public static double getChengZhenLaHei(Entity e) {
		return vars(e).chengzhenlahei;
	}

	public static void setChengZhenLaHei(Entity e, double v) {
		vars(e).chengzhenlahei = v;
	}

	public static double getTongJi(Entity e) {
		return vars(e).tongji;
	}

	public static void setTongJi(Entity e, double v) {
		vars(e).tongji = v;
	}

	public static double getPaiMaiDuiHuan(Entity e) {
		return vars(e).paimaiduihuan;
	}

	public static void setPaiMaiDuiHuan(Entity e, double v) {
		vars(e).paimaiduihuan = v;
	}

	// ──────────────────────── 貌民 ────────────────────────
	public static double getMaominCs(Entity e) {
		return vars(e).maomin_cs;
	}

	public static void setMaominCs(Entity e, double v) {
		vars(e).maomin_cs = v;
	}

	public static boolean getMaominGc(Entity e, int index) {
		return switch (index) {
			case 1 -> vars(e).maomin_gc_1;
			case 2 -> vars(e).maomin_gc_2;
			case 3 -> vars(e).maomin_gc_3;
			case 4 -> vars(e).maomin_gc_4;
			case 5 -> vars(e).maomin_gc_5;
			case 6 -> vars(e).maomin_gc_6;
			case 7 -> vars(e).maomin_gc_7;
			case 8 -> vars(e).maomin_gc_8;
			case 9 -> vars(e).maomin_gc_9;
			case 10 -> vars(e).maomin_gc_10;
			case 11 -> vars(e).maomin_gc_11;
			case 12 -> vars(e).maomin_gc_12;
			case 13 -> vars(e).maomin_gc_13;
			case 14 -> vars(e).maomin_gc_14;
			case 15 -> vars(e).maomin_gc_15;
			case 16 -> vars(e).maomin_gc_16;
			default -> throw new IllegalArgumentException("maomin_gc index must be 1..16, got " + index);
		};
	}

	public static void setMaominGc(Entity e, int index, boolean v) {
		switch (index) {
			case 1 -> vars(e).maomin_gc_1 = v;
			case 2 -> vars(e).maomin_gc_2 = v;
			case 3 -> vars(e).maomin_gc_3 = v;
			case 4 -> vars(e).maomin_gc_4 = v;
			case 5 -> vars(e).maomin_gc_5 = v;
			case 6 -> vars(e).maomin_gc_6 = v;
			case 7 -> vars(e).maomin_gc_7 = v;
			case 8 -> vars(e).maomin_gc_8 = v;
			case 9 -> vars(e).maomin_gc_9 = v;
			case 10 -> vars(e).maomin_gc_10 = v;
			case 11 -> vars(e).maomin_gc_11 = v;
			case 12 -> vars(e).maomin_gc_12 = v;
			case 13 -> vars(e).maomin_gc_13 = v;
			case 14 -> vars(e).maomin_gc_14 = v;
			case 15 -> vars(e).maomin_gc_15 = v;
			case 16 -> vars(e).maomin_gc_16 = v;
			default -> throw new IllegalArgumentException("maomin_gc index must be 1..16, got " + index);
		}
	}
}
