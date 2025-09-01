package tw.ch1ck3n.zerodaystylehotbar.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.profiler.Profilers;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tw.ch1ck3n.zerodaystylehotbar.ZerodayStyleHotbar;
import tw.ch1ck3n.zerodaystylehotbar.util.DetailedStatusBarUtil;
import tw.ch1ck3n.zerodaystylehotbar.util.DetailedTooltipUtil;
import tw.ch1ck3n.zerodaystylehotbar.util.HotbarLayoutUtil;
import tw.ch1ck3n.zerodaystylehotbar.util.MinecraftInstance;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin implements MinecraftInstance {

    @Shadow @Final
    private static Identifier HOTBAR_ATTACK_INDICATOR_BACKGROUND_TEXTURE;

    @Shadow @Final
    private static Identifier HOTBAR_ATTACK_INDICATOR_PROGRESS_TEXTURE;

    @Shadow @Final
    private MinecraftClient client;

    @Shadow
    private int heldItemTooltipFade;

    @Shadow
    private ItemStack currentStack;

    @Shadow
    private int renderHealthValue;

    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    private void injectRenderHotbar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {

        if (ZerodayStyleHotbar.getInstance().getConfig().status) {
            PlayerEntity player = this.getCameraPlayer();
            if (player != null) {
                ItemStack offHandStack = player.getOffHandStack();
                Arm oppositeArm = player.getMainArm().getOpposite();

                int midX = context.getScaledWindowWidth() / 2, seed = 1, slot;
                int originX, originY = context.getScaledWindowHeight() - 16 - 3 - HotbarLayoutUtil.OFFSET;
                originY += this.getHeartRows(DetailedStatusBarUtil.getTotalRenderHealth(player, renderHealthValue)) * -9 + 9;

//                float currentSlot = player.getInventory().selectedSlot;
                float currentSlot = HotbarLayoutUtil.getCurrentSlot(player.getInventory().selectedSlot, tickCounter);

                for (slot = 0; slot < 9; slot++) {
                    originX = midX - 90 + slot * 20 + 2;

                    float size = HotbarLayoutUtil.getAnimatedSlotSize(currentSlot, slot, tickCounter);
                    int drawX = Math.round(HotbarLayoutUtil.getSlotX(originX, slot));
                    int drawY = Math.round(HotbarLayoutUtil.getSlotY(originY, size));

                    context.getMatrices().push();
                    HotbarLayoutUtil.doScaleAndTranslate(context.getMatrices(), drawX, drawY, size);
                    
                    if (!player.getInventory().main.get(slot).isEmpty()) {
                        this.renderHotbarItem(
                                context, drawX, drawY, tickCounter, player,
                                player.getInventory().main.get(slot), seed++
                        );
                    } else {
                        context.drawCenteredTextWithShadow(
                                this.getTextRenderer(), String.valueOf(slot + 1),
                                drawX + 8, drawY + 16 - this.getTextRenderer().fontHeight, -1
                        );
                    }
                    context.getMatrices().pop();
                }

                if (!offHandStack.isEmpty()) {
                    slot = HotbarLayoutUtil.getOffHandSlot(oppositeArm);
                    originX = HotbarLayoutUtil.getOffHandSlotX(oppositeArm, midX);

                    float size = HotbarLayoutUtil.getAnimatedSlotSize(currentSlot, (slot + 10) % 10, tickCounter);
                    int drawX = Math.round(HotbarLayoutUtil.getSlotX(originX, slot));
                    int drawY = Math.round(HotbarLayoutUtil.getSlotY(originY, size));

                    context.getMatrices().push();
                    HotbarLayoutUtil.doScaleAndTranslate(context.getMatrices(), drawX, drawY, size);

                    this.renderHotbarItem(
                            context, drawX, drawY, tickCounter, player,
                            offHandStack, seed++
                    );
                    context.getMatrices().pop();
                }

                if (this.client.options.getAttackIndicator().getValue() == AttackIndicator.HOTBAR) {
                    float f = this.client.player.getAttackCooldownProgress(0.0F);
                    int textWidth = Math.max(
                            this.getTextRenderer().getWidth("20.0"),
                            this.getTextRenderer().getWidth(
                                    String.valueOf(DetailedStatusBarUtil.getLevelExperience(this.client.player.experienceLevel))
                            ) + 2
                    );
                    if (f < 1.0F) {
                        originX = midX + 91 + 6;
                        originY = context.getScaledWindowHeight() - 20;
                        if (ZerodayStyleHotbar.getInstance().getConfig().detailedStatusBar.shouldShowSaturation() ||
                                ZerodayStyleHotbar.getInstance().getConfig().detailedStatusBar.shouldShowDetailedExperience()) {
                            originX += (textWidth - 2);
                        }

                        if (oppositeArm == Arm.RIGHT) {
                            originX = midX - 91 - 22;
                            if (ZerodayStyleHotbar.getInstance().getConfig().detailedStatusBar.shouldShowTotalHealth() ||
                                    ZerodayStyleHotbar.getInstance().getConfig().detailedStatusBar.shouldShowDetailedExperience()) {
                                originX -= textWidth;
                            }
                        }

                        int p = (int)(f * 19.0F);
                        context.drawGuiTexture(RenderLayer::getGuiTextured, HOTBAR_ATTACK_INDICATOR_BACKGROUND_TEXTURE, originX, originY, 18, 18);
                        context.drawGuiTexture(RenderLayer::getGuiTextured, HOTBAR_ATTACK_INDICATOR_PROGRESS_TEXTURE, 18, 18, 0, 18 - p, originX, originY + 18 - p, 18, p);
                    }
                }
            }
            ci.cancel();
        }
    }

    @Inject(method = "renderExperienceBar", at = @At("HEAD"))
    private void renderExperienceBar(DrawContext context, int x, CallbackInfo ci) {

        if (ZerodayStyleHotbar.getInstance().getConfig().detailedStatusBar.shouldShowDetailedExperience()) {
            int level = this.client.player.experienceLevel;
            int lastLevelUpExperience = level == 0 ? 0 : DetailedStatusBarUtil.getLevelExperience(level - 1);
            int nextLevelUpExperience = DetailedStatusBarUtil.getLevelExperience(level);
            int i = context.getScaledWindowWidth() / 2;
            int j = HotbarLayoutUtil.getBarY(context.getScaledWindowHeight() - 32 + 3) - 5 / 2;
            context.drawTextWithShadow(
                    this.getTextRenderer(), String.valueOf(lastLevelUpExperience),
                    i - 91 - this.getTextRenderer().getWidth(String.valueOf(lastLevelUpExperience)) - 3, j, -1
            );
            context.drawTextWithShadow(
                    this.getTextRenderer(), String.valueOf(nextLevelUpExperience),
                    i + 91 + 3, j, -1
            );
        }
    }

    @Inject(method = "renderHeldItemTooltip", at = @At("HEAD"), cancellable = true)
    private void injectRenderHeldItemTooltip(DrawContext context, CallbackInfo ci) {

        if (ZerodayStyleHotbar.getInstance().getConfig().status) {
            Profilers.get().push("selectedItemName");
            if (this.heldItemTooltipFade > 0 && !this.currentStack.isEmpty()) {
                MutableText mutableText = Text.empty().append(this.currentStack.getName()).formatted(this.currentStack.getRarity().getFormatting());
                if (this.currentStack.contains(DataComponentTypes.CUSTOM_NAME)) {
                    mutableText.formatted(Formatting.ITALIC);
                }

                int midX = context.getScaledWindowWidth() / 2;
                float slot = HotbarLayoutUtil.currentSlot;
                float originX = midX - 90 + slot * 20 + 2;
                float originY = context.getScaledWindowHeight() - 16 - 3 - HotbarLayoutUtil.OFFSET;
                originY += this.getHeartRows(DetailedStatusBarUtil.getTotalRenderHealth(this.getCameraPlayer(), renderHealthValue)) * -9 + 9;

                float drawX = Math.round(HotbarLayoutUtil.getSlotX(originX, (int) slot)) - 8;
                float drawY = Math.round(HotbarLayoutUtil.getSlotY(originY, 2)) - 20;

                int l = (int) ((float) this.heldItemTooltipFade * 256.0F / 10.0F);

                if (l == 1024) {
                    HotbarLayoutUtil.setTooltipX(drawX);
                }

                if (l > 255) {
                    l = 255;
                }

                slot = this.client.player.getInventory().selectedSlot;
                originX = midX - 90 + slot * 20 + 2;
                drawX = HotbarLayoutUtil.getTooltipDrawX(originX, slot);

                if (l > 0) {
                    float scale = 1.2F;
                    context.getMatrices().push();
                    context.getMatrices().scale(scale, scale, 1);
                    context.drawTextWithShadow(
                            this.getTextRenderer(), mutableText,
                            (int) (drawX / scale), (int) (drawY / scale),
                            ColorHelper.withAlpha(l, -1)
                    );
                    context.getMatrices().pop();

                    ItemStack stack = this.currentStack;
                    String tooltip = DetailedTooltipUtil.getItemDetailedTooltip(stack);
                    if (!tooltip.isEmpty()) {
                        context.drawTextWithShadow(
                                this.getTextRenderer(), Text.of(tooltip),
                                (int) drawX, (int) drawY + 11, ColorHelper.withAlpha(l, -1)
                        );
                    }

//                    System.out.println(saturation + ", " + ItemComponentUtil.getItemNutrition(stack) + ", "+
//                            this.getCameraPlayer().getHungerManager().getFoodLevel() + ", " +
//                            this.getCameraPlayer().getHungerManager().getSaturationLevel());

//                    this.currentStack.getTooltipData().ifPresent((datax) -> System.out.println(TooltipComponent.of(datax)));
//                    for (RegistryKey<ItemGroup> groupKey : Registries.ITEM_GROUP.getKeys()) {
//                        ItemGroup group = Registries.ITEM_GROUP.get(groupKey);
//                        System.out.println(group.getDisplayName());
//                        if (group.getDisplayStacks().contains(stack)) {
//                            System.out.println("Found in " + group.getDisplayName());
//                        }
//                    }
//                    Screen.getTooltipFromItem(client, currentStack).forEach(System.out::println);
                }
            }
            Profilers.get().pop();
            ci.cancel();
        }
    }

    @Redirect(
            method = "renderMountJumpBar",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowHeight()I")
    )
    private int modifyMountJumpBarY(DrawContext instance) {

        return HotbarLayoutUtil.getBarY(instance.getScaledWindowHeight());
    }

    @Redirect(
            method = "renderExperienceBar",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowHeight()I")
    )
    private int modifyExperienceBarY(DrawContext instance) {

        return HotbarLayoutUtil.getBarY(instance.getScaledWindowHeight());
    }

    @Redirect(
            method = "renderExperienceLevel",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowHeight()I")
    )
    private int modifyExperienceLevelY(DrawContext instance) {

        return HotbarLayoutUtil.getBarY(instance.getScaledWindowHeight());
    }

    @Shadow
    protected abstract PlayerEntity getCameraPlayer();

    @Shadow
    protected abstract int getHeartRows(int heartCount);

    @Redirect(
            method = "renderStatusBars",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowHeight()I")
    )
    private int modifyStatusBarsY(DrawContext instance) {

        return HotbarLayoutUtil.getBarY(instance.getScaledWindowHeight());
    }

    @Inject(method = "renderHealthBar", at = @At("HEAD"))
    private void renderHealthBar(DrawContext context, PlayerEntity player, int x, int y,
                                 int lines, int regeneratingHeartIndex, float maxHealth,
                                 int lastHealth, int health, int absorption,
                                 boolean blinking, CallbackInfo ci) {

        if (ZerodayStyleHotbar.getInstance().getConfig().detailedStatusBar.shouldShowTotalHealth()) {

            double total = DetailedStatusBarUtil.getTotalHealth(player);
            String text = String.format("%.1f", total);
            context.drawTextWithShadow(
                    this.getTextRenderer(), text,
                    x - this.getTextRenderer().getWidth(text) - 3, y, -1
            );
        }

    }

    @Inject(method = "renderFood", at = @At("HEAD"))
    private void renderFood(DrawContext context, PlayerEntity player, int top, int right, CallbackInfo ci) {

        if (ZerodayStyleHotbar.getInstance().getConfig().detailedStatusBar.shouldShowSaturation()) {
            double saturation = player.getHungerManager().getSaturationLevel();
            context.drawTextWithShadow(this.getTextRenderer(), String.format("%.1f", saturation), right + 3, top, -1);
        }
    }

    @Shadow
    protected abstract void renderHotbarItem(DrawContext context, int x, int y, RenderTickCounter tickCounter, PlayerEntity player, ItemStack stack, int seed);

    @Shadow
    public abstract TextRenderer getTextRenderer();
}