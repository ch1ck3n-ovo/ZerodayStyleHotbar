package tw.ch1ck3n.zerodaystylehotbar.util;

import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import tw.ch1ck3n.zerodaystylehotbar.ZerodayStyleHotbar;

public class HotbarLayoutUtil implements MinecraftInstance {

    public static final int OFFSET = -(16 + 3) + 42 + 16;

    public static final float[] SLOT_SIZES = {1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F};

    public static float currentSlot = 0;

    public static float tooltipX = 0;

    public static int getBarY(int originY) {

        if (ZerodayStyleHotbar.getInstance().getConfig().status) {
            return originY + 22 - 5;
        }

        return originY;
    }

    public static float getSlotX(int originX, int slot) {

        return getSlotX((float) originX, slot);
    }

    public static float getSlotX(float originX, int slot) {

        return originX + (slot - 4) * 10;
    }

    public static float getSlotY(int originY, float size) {

        return getSlotY((float) originY, size);
    }

    public static float getSlotY(float originY, float size) {

        return originY + 16 - (16 * size) + 2;
    }

    public static float getSlotSize(int main, int current) {

        return HotbarLayoutUtil.getSlotSize((float) main, current);
    }

    public static float getSlotSize(float main, int current) {

        float diff = Math.abs(main - current);
        if (diff >= 4) {
            return 1;
        }

        return (float) (13.2005 / (diff + 5.4961) - 0.3944);
    }

    public static float getAnimatedSlotSize(int main, int current, RenderTickCounter tickCounter) {

        return HotbarLayoutUtil.getAnimatedSlotSize((float) main, current, tickCounter);
    }

    public static float getAnimatedSlotSize(float main, int current, RenderTickCounter tickCounter) {

        HotbarLayoutUtil.SLOT_SIZES[current] = HotbarLayoutUtil.lerp(
                HotbarLayoutUtil.SLOT_SIZES[current], HotbarLayoutUtil.getSlotSize(main, current),
                0.2F, tickCounter.getTickDelta(false)
        );
        return HotbarLayoutUtil.SLOT_SIZES[current];
    }

    public static int getOffHandSlot(Arm arm) {

        return (arm == Arm.LEFT ? -1 : 9);
    }

    public static int getOffHandSlotX(Arm arm, int midX) {

        return midX - 90 + HotbarLayoutUtil.getOffHandSlot(arm) * 20 + (arm == Arm.LEFT ? -2 : 2);
    }

    public static float getCurrentSlot(int main, RenderTickCounter tickCounter) {

        currentSlot = HotbarLayoutUtil.moveTowards(
                currentSlot, main,
                0.12F, tickCounter.getTickDelta(false)
        );
        return currentSlot;
    }

    public static void setTooltipX(float value) {
        HotbarLayoutUtil.tooltipX = value;
    }

    public static float getTooltipDrawX(float originX, float slot) {
        originX = Math.round(HotbarLayoutUtil.getSlotX(originX, (int) slot)) - 8;
        HotbarLayoutUtil.setTooltipX(
                HotbarLayoutUtil.lerp(
                        HotbarLayoutUtil.tooltipX, originX, 0.04F, mc.getRenderTickCounter().getTickDelta(false)
                )
        );
        return Math.round(HotbarLayoutUtil.tooltipX);
    }

    public static void doScaleAndTranslate(MatrixStack matrices, int drawX, int drawY, float size) {
        matrices.translate(drawX + 8, drawY, 0);
        matrices.scale(size, size, 1);
        matrices.translate(-(drawX + 8), -(drawY), 0);
    }

    public static float lerp(float current, float target, float progress, float tickDelta) {

        return current + (target - current) * progress * tickDelta;
    }

    public static float moveTowards(float current, float target, float progress, float tickDelta) {

        float direction = (target - current > 0 ? 1 : -1);
        float distance = Math.abs(target - current);
        return current + Math.min(distance, progress) * direction * tickDelta;
    }

}
