package tw.ch1ck3n.zerodaystylehotbar.util;

import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

public class DetailedStatusBarUtil {

    public static int getLevelExperience(int experienceLevel) {
        if (experienceLevel >= 30) {
            return 112 + (experienceLevel - 30) * 9;
        } else {
            return experienceLevel >= 15 ? 37 + (experienceLevel - 15) * 5 : 7 + experienceLevel * 2;
        }
    }

    public static double getTotalHealth(PlayerEntity player) {

        return DetailedStatusBarUtil.getHealth(player) + DetailedStatusBarUtil.getAbsorption(player);
    }

    public static float getHealth(PlayerEntity player) {

        return player.getHealth();
    }

    public static int getTotalRenderHealth(PlayerEntity player, int renderHealthValue) {

        return MathHelper.ceil((double) DetailedStatusBarUtil.getBaseRenderHealth(player, renderHealthValue) / 2) +
                MathHelper.ceil((double) DetailedStatusBarUtil.getAbsorption(player) / 2);
    }

    public static float getBaseRenderHealth(PlayerEntity player, int renderHealthValue) {

        return Math.max(
                (float) player.getAttributeValue(EntityAttributes.MAX_HEALTH),
                Math.max(renderHealthValue, player.getHealth())
        );
    }

    public static float getAbsorption(PlayerEntity player) {

        return MathHelper.ceil(player.getAbsorptionAmount());
    }

}
