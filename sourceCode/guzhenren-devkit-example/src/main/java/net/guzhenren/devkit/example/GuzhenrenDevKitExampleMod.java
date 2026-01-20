package net.guzhenren.devkit.example;

import com.mojang.logging.LogUtils;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.guzhenren.devkit.liangu.LianGuMenuSlots;
import net.guzhenren.devkit.liangu.LianGuRecipes;
import net.guzhenren.devkit.liangu.spi.ExternalLianGuRecipe;
import net.guzhenren.init.GuzhenrenModItems;
import net.guzhenren.network.GuzhenrenModVariables;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

// 示例附属模组：演示如何使用 guzhenren-devkit 的注入扩展点。
//
// 这个文件刻意写得"可教学"：你可以直接复制它，改动很少就能做出自己的配方/杀招。
//
// 测试目标：
// 1) 炼蛊（多材料配方）：
//    - slot8 放 kong_bai_gu_fang
//    - slot0-7 需放入：钻石×1、金锭×2、绿宝石×1、红石×4
//    - 产出：下界之星×1
//    - 炼制时间：8 秒
// 2) 杀招：按 Z 键（slotIndex=1）时触发一个可观察的效果（此处用变量标记演示）。
@Mod(GuzhenrenDevKitExampleMod.MODID)
public class GuzhenrenDevKitExampleMod {

    public static final String MODID = "guzhenren_devkit_example";

    private static final Logger LOGGER = LogUtils.getLogger();

    // 外部配方 ID：主模组内置 GuFang 通常是较小整数；示例约定 addon 从 100000 起，避免冲突。
    private static final double EXAMPLE_GUFANG_ID = 100000d;

    // 炼蛊耗时：进度变量每秒 +1，这里 8 秒完成（多材料配方理应更久）。
    private static final double LIANGU_SECONDS = 8d;

    // 多材料配方定义：材料 -> 需求数量
    // 使用 LinkedHashMap 保持顺序，方便日志输出和调试
    private static final Map<Item, Integer> RECIPE_INGREDIENTS =
        new LinkedHashMap<>();

    static {
        RECIPE_INGREDIENTS.put(Items.DIAMOND, 1); // 钻石 ×1
        RECIPE_INGREDIENTS.put(Items.GOLD_INGOT, 2); // 金锭 ×2
        RECIPE_INGREDIENTS.put(Items.EMERALD, 1); // 绿宝石 ×1
        RECIPE_INGREDIENTS.put(Items.REDSTONE, 4); // 红石 ×4
    }

    // 产出物品
    private static final ItemStack RECIPE_OUTPUT = new ItemStack(
        Items.NETHER_STAR,
        1
    );

    public GuzhenrenDevKitExampleMod() {
        LOGGER.info("[{}] loaded", MODID);
        LOGGER.info(
            "[{}] usage(liangu): slot8=kong_bai_gu_fang, inputs=DIAMOND×1+GOLD_INGOT×2+EMERALD×1+REDSTONE×4 -> output=NETHER_STAR",
            MODID
        );

        net.guzhenren.devkit.tizhi.TizhiNames.register(100001, "示例体质");

        ExampleShaZhaoKey.bootstrap(MODID);
        ExampleShaZhaoBind.bootstrap(MODID);
        ExampleShaZhaoLifecycle.bootstrap(MODID);
        ExampleShaZhaoHudIcon.bootstrap(MODID);
        ExampleDiZangHua.bootstrap(MODID);
        ExamplePaiMai.bootstrap(MODID);
        ExampleShop.bootstrap(MODID);
        bootstrapLianGu();
    }

    /**
     * 多材料炼蛊配方注册。
     *
     * <p>槽位布局（炼蛊界面）：
     * <ul>
     *   <li>slot 0-7：8 个输入槽（可放置材料）</li>
     *   <li>slot 8：蛊方槽位（本例要求 kong_bai_gu_fang）</li>
     *   <li>slot 9：输出槽</li>
     *   <li>slot 10：只读槽（显示当前消耗的材料预览）</li>
     * </ul>
     * </p>
     *
     * <p>多材料配方匹配逻辑：
     * <ul>
     *   <li>遍历 slot0-7，统计各材料的总数量</li>
     *   <li>检查是否满足配方所需的所有材料及数量</li>
     *   <li>材料可以分散在不同槽位，只要总数足够即可</li>
     * </ul>
     * </p>
     */
    private static void bootstrapLianGu() {
        LianGuRecipes.register(
            new ExternalLianGuRecipe() {
                @Override
                public String id() {
                    return MODID + ":liangu_multi_ingredient";
                }

                @Override
                public boolean matches(Player player) {
                    // 已经处于炼蛊中就不允许重新开始。
                    if (
                        player.getData(
                            GuzhenrenModVariables.PLAYER_VARIABLES
                        ).LianGu
                    ) {
                        return false;
                    }

                    // 必须在炼蛊界面里。
                    if (!LianGuMenuSlots.isInLianGuMenu(player)) {
                        return false;
                    }

                    // 炼蛊界面 slot8 是"蛊方槽位"。本例要求放入 kong_bai_gu_fang。
                    ItemStack slot8 = LianGuMenuSlots.getSlot(player, 8);
                    if (
                        slot8.isEmpty() ||
                        slot8.getItem() !=
                        GuzhenrenModItems.KONG_BAI_GU_FANG.get()
                    ) {
                        return false;
                    }

                    // 检查多材料配方是否满足
                    return checkIngredientsAvailable(player);
                }

                @Override
                public void onStart(Player player) {
                    // 收集并消耗所有材料
                    List<ConsumedIngredient> consumed = consumeIngredients(
                        player
                    );
                    if (consumed.isEmpty()) {
                        return; // 材料不足，不应该发生（matches 已检查）
                    }

                    GuzhenrenModVariables.PlayerVariables vars = player.getData(
                        GuzhenrenModVariables.PLAYER_VARIABLES
                    );
                    vars.LianGuJinDu = 0;
                    vars.lg_ke = 0;
                    vars.GuFang = EXAMPLE_GUFANG_ID;
                    vars.LianGu = true;
                    vars.markSyncDirty();

                    // 把第一个被消耗的材料放到 slot10（只读槽）作为预览
                    // 注意：slot10 只能显示一个物品，多材料时只显示第一个作为代表
                    if (!consumed.isEmpty()) {
                        LianGuMenuSlots.setSlot(
                            player,
                            10,
                            consumed.get(0).stack().copy()
                        );
                    }

                    if (!player.level().isClientSide()) {
                        StringBuilder sb = new StringBuilder();
                        for (ConsumedIngredient ci : consumed) {
                            if (sb.length() > 0) sb.append(", ");
                            sb
                                .append(ci.stack().getCount())
                                .append("x ")
                                .append(ci.stack().getItem());
                            sb.append(" from slot").append(ci.slots());
                        }
                        LOGGER.info(
                            "[{}] liangu start ok: consumed [{}]",
                            MODID,
                            sb
                        );
                    }
                }

                @Override
                public void onTick(Player player) {
                    GuzhenrenModVariables.PlayerVariables vars = player.getData(
                        GuzhenrenModVariables.PLAYER_VARIABLES
                    );

                    // lg_ke 是 tick 计数器（20 tick = 1 秒）。
                    vars.lg_ke = vars.lg_ke + 1;
                    if (vars.lg_ke / 20 >= 1) {
                        vars.lg_ke = 0;
                        vars.LianGuJinDu = vars.LianGuJinDu + 1;
                    }

                    // 进度达到目标秒数 -> 产出。
                    if (vars.LianGuJinDu >= LIANGU_SECONDS) {
                        vars.LianGuJinDu = 0;
                        vars.GuFang = 0;
                        vars.LianGu = false;
                        vars.markSyncDirty();

                        LianGuMenuSlots.setSlot(
                            player,
                            9,
                            RECIPE_OUTPUT.copy()
                        );
                        LianGuMenuSlots.setSlot(player, 10, ItemStack.EMPTY);

                        if (!player.level().isClientSide()) {
                            LOGGER.info(
                                "[{}] liangu finish ok: produced {}x {}",
                                MODID,
                                RECIPE_OUTPUT.getCount(),
                                RECIPE_OUTPUT.getItem()
                            );
                        }
                        return;
                    }

                    vars.markSyncDirty();
                }

                @Override
                public boolean isActive(Player player) {
                    return ExternalLianGuRecipe.isRunning(
                        player,
                        EXAMPLE_GUFANG_ID
                    );
                }

                /**
                 * 检查 slot0-7 中是否有足够的材料满足配方需求。
                 */
                private boolean checkIngredientsAvailable(Player player) {
                    Map<Item, Integer> available = collectAvailableIngredients(
                        player
                    );

                    for (Map.Entry<
                        Item,
                        Integer
                    > required : RECIPE_INGREDIENTS.entrySet()) {
                        int have = available.getOrDefault(required.getKey(), 0);
                        if (have < required.getValue()) {
                            return false;
                        }
                    }
                    return true;
                }

                /**
                 * 统计 slot0-7 中各物品的总数量。
                 */
                private Map<Item, Integer> collectAvailableIngredients(
                    Player player
                ) {
                    Map<Item, Integer> result = new LinkedHashMap<>();
                    for (int i = 0; i <= 7; i++) {
                        ItemStack stack = LianGuMenuSlots.getSlot(player, i);
                        if (!stack.isEmpty()) {
                            result.merge(
                                stack.getItem(),
                                stack.getCount(),
                                Integer::sum
                            );
                        }
                    }
                    return result;
                }

                /**
                 * 消耗配方所需的材料，返回实际消耗的列表（用于日志）。
                 *
                 * <p>消耗策略：从 slot0 开始，依次扣减需要的材料数量。
                 * 如果一个槽位不够，继续从下一个槽位扣减。</p>
                 */
                private List<ConsumedIngredient> consumeIngredients(
                    Player player
                ) {
                    List<ConsumedIngredient> result = new ArrayList<>();

                    // 复制一份需求，边消耗边扣减
                    Map<Item, Integer> remaining = new LinkedHashMap<>(
                        RECIPE_INGREDIENTS
                    );

                    for (
                        int slot = 0;
                        slot <= 7 && !remaining.isEmpty();
                        slot++
                    ) {
                        ItemStack stack = LianGuMenuSlots.getSlot(player, slot);
                        if (stack.isEmpty()) {
                            continue;
                        }

                        Item item = stack.getItem();
                        Integer needed = remaining.get(item);
                        if (needed == null || needed <= 0) {
                            continue;
                        }

                        int toConsume = Math.min(needed, stack.getCount());

                        // 记录消耗信息
                        ItemStack consumedStack = stack.copy();
                        consumedStack.setCount(toConsume);
                        result.add(
                            new ConsumedIngredient(consumedStack, List.of(slot))
                        );

                        // 扣减槽位中的物品
                        ItemStack newStack = stack.copy();
                        newStack.shrink(toConsume);
                        LianGuMenuSlots.setSlot(
                            player,
                            slot,
                            newStack.isEmpty() ? ItemStack.EMPTY : newStack
                        );

                        // 更新剩余需求
                        int newNeeded = needed - toConsume;
                        if (newNeeded <= 0) {
                            remaining.remove(item);
                        } else {
                            remaining.put(item, newNeeded);
                        }
                    }

                    // 如果还有未满足的材料，说明配方不完整（不应该发生）
                    if (!remaining.isEmpty()) {
                        LOGGER.warn(
                            "[{}] liangu consumeIngredients: some ingredients not satisfied: {}",
                            MODID,
                            remaining
                        );
                        return List.of();
                    }

                    return result;
                }
            }
        );
    }

    /**
     * 记录被消耗的材料信息（用于日志输出）。
     */
    private record ConsumedIngredient(ItemStack stack, List<Integer> slots) {}
}
