package net.guzhenren.devkit.client;

import net.guzhenren.devkit.hook.HookResult;
import net.guzhenren.devkit.shazhao.ShaZhaoHudIconContext;
import net.guzhenren.devkit.shazhao.ShaZhaoHooks;
import net.guzhenren.devkit.shazhao.util.ShaZhaoBindings;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

@EventBusSubscriber(value = Dist.CLIENT)
public final class ShaZhaoHudIconOverlay {
	private ShaZhaoHudIconOverlay() {
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void eventHandler(RenderGuiEvent.Pre event) {
		Player player = Minecraft.getInstance().player;
		if (player == null) {
			return;
		}
		if (!net.guzhenren.procedures.UIZhuangtaitiaoXianShiYouXiNeiDieJiaCengProcedure.execute(player)) {
			return;
		}

		int screenHeight = event.getGuiGraphics().guiHeight();

		drawSlot(event, player, 1, 5, screenHeight - 37);
		drawSlot(event, player, 2, 41, screenHeight - 37);
		drawSlot(event, player, 3, 77, screenHeight - 37);
		drawSlot(event, player, 4, 113, screenHeight - 37);
	}

	private static void drawSlot(RenderGuiEvent.Pre event, Player player, int keySlotIndex, int x, int y) {
		net.guzhenren.network.GuzhenrenModVariables.PlayerVariables vars = player.getData(net.guzhenren.network.GuzhenrenModVariables.PLAYER_VARIABLES);
		double boundId = ShaZhaoBindings.getBoundId(vars, keySlotIndex);
		net.minecraft.world.item.ItemStack boundItem = ShaZhaoBindings.getBoundItem(vars, keySlotIndex);

		ShaZhaoHudIconContext ctx = new ShaZhaoHudIconContext(player, keySlotIndex, boundId, boundItem);
		HookResult result = ShaZhaoHooks.HUD_ICON.dispatch(ctx);
		if (result != HookResult.CONSUME) {
			return;
		}
		ResourceLocation icon = ctx.icon();
		if (icon == null) {
			return;
		}

		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.enableBlend();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderSystem.setShaderColor(1, 1, 1, 1);

		event.getGuiGraphics().blit(icon, x, y, 0, 0, 32, 32, 32, 32);

		RenderSystem.depthMask(true);
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		RenderSystem.disableBlend();
		RenderSystem.setShaderColor(1, 1, 1, 1);
	}
}
