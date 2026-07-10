package net.guzhenren.devkit.auction;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;

/**
 * 拍卖行拍品选择 Hook 的上下文。
 *
 * <p>新版拍卖行为世界级别（由 {@code GuzhenrenModVariables.MapVariables} 驱动），
 * 不再绑定某个 NPC 实体，因此上下文只携带世界与当前拍品。</p>
 *
 * <p>附属模组在 Handler 中调用 {@link #setSelectedItem(ItemStack)} 提供替换拍品，
 * 并返回 {@link net.guzhenren.devkit.hook.HookResult#CONSUME} 即可生效；
 * 返回 {@link net.guzhenren.devkit.hook.HookResult#PASS} 则保留原拍品。</p>
 *
 * <p><b>关于价格：</b>主模组的 {@code PaiMaiHangJiaGeProcedure} 只对属于
 * {@code guzhenren:paimaihang_jiage_XXw} 系列标签的拍品赋价，对标签之外的物品
 * （例如原版物品或未加价格标签的自定义拍品）不会设置价格，导致起拍价/竞价为 0。
 * 因此当替换成标签之外的物品时，需通过 {@link #setPrice(double, double)} 或
 * {@link #setPrice(double)} 主动指定价格，Mixin 会在算价流程之前写入世界变量，
 * 由于该物品不匹配任何价格标签，主模组算价流程不会覆盖此处设定的价格。</p>
 */
public final class PaiMaiSelectItemContext {
	private final LevelAccessor level;
	private ItemStack selectedItem;

	/** 起拍价（对应 {@code MapVariables.PaiMaiHang_jiage}）；<0 表示未覆盖，交由主模组标签逻辑处理。 */
	private double price = -1.0;
	/** 每次竞价加价幅度（对应 {@code MapVariables.PaiMaiHang_JingJia}）；<0 表示未覆盖。 */
	private double bidIncrement = -1.0;

	/**
	 * 构造方法。
	 */
	public PaiMaiSelectItemContext(LevelAccessor level, ItemStack selectedItem) {
		this.level = level;
		this.selectedItem = selectedItem == null ? ItemStack.EMPTY : selectedItem;
	}

	public LevelAccessor level() {
		return level;
	}

	/**
	 * 执行 selected item 操作。
	 */
	public ItemStack selectedItem() {
		return selectedItem;
	}

	/**
	 * 设置 selected item 的值。
	 */
	public void setSelectedItem(ItemStack selectedItem) {
		this.selectedItem = selectedItem == null ? ItemStack.EMPTY : selectedItem;
	}

	/** 起拍价；<0 表示未覆盖。 */
	public double price() {
		return price;
	}

	/** 竞价加价幅度；<0 表示未覆盖。 */
	public double bidIncrement() {
		return bidIncrement;
	}

	/** 是否指定了价格覆盖（起拍价 >= 0 即视为覆盖）。 */
	public boolean hasPriceOverride() {
		return price >= 0.0;
	}

	/**
	 * 仅设置起拍价，竞价幅度按起拍价的 10% 自动推算（对齐主模组默认 价格:竞价 = 10:1 的比例）。
	 *
	 * @param price 起拍价，须 >= 0
	 */
	/**
	 * 设置 price 的值。
	 */
	public void setPrice(double price) {
		this.price = price;
		this.bidIncrement = price >= 0.0 ? price / 10.0 : -1.0;
	}

	/**
	 * 同时设置起拍价与每次竞价加价幅度。
	 *
	 * @param price        起拍价，须 >= 0
	 * @param bidIncrement 每次加价幅度，须 >= 0
	 */
	/**
	 * 设置 price 的值。
	 */
	public void setPrice(double price, double bidIncrement) {
		this.price = price;
		this.bidIncrement = bidIncrement;
	}

	/**
	 * 设置 bid increment 的值。
	 */
	public void setBidIncrement(double bidIncrement) {
		this.bidIncrement = bidIncrement;
	}
}
