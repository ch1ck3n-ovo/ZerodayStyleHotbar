package tw.ch1ck3n.zerodaystylehotbar.util;

import net.minecraft.item.*;
import net.minecraft.text.Text;
import tw.ch1ck3n.zerodaystylehotbar.ZerodayStyleHotbar;
import tw.ch1ck3n.zerodaystylehotbar.config.settings.DetailedTooltip;

public class DetailedTooltipUtil {

    public static String getItemDetailedTooltip(ItemStack stack) {

        Item item = stack.getItem();
        int nutrition = ItemComponentUtil.getItemNutrition(stack);
        if (nutrition != 0 && DetailedTooltipUtil.shouldRenderFoodDetailed()) {
            return DetailedTooltipUtil.getItemFoodTooltip(stack);
        } else if (item instanceof ArmorItem && DetailedTooltipUtil.shouldRenderArmorDetailed()) {
            return DetailedTooltipUtil.getItemArmorTooltip(stack);
        } else if (item instanceof MiningToolItem && DetailedTooltipUtil.shouldRenderToolDetailed()) {
            return DetailedTooltipUtil.getItemToolTooltip(stack);
        } else if ((item instanceof SwordItem || item == Items.TRIDENT || item == Items.MACE) &&
                DetailedTooltipUtil.shouldRenderSwordDetailed()) {
            return DetailedTooltipUtil.getItemSwordTooltip(stack);
        }
        return "";
    }

    public static boolean shouldRenderSwordDetailed() {

        return ZerodayStyleHotbar.getInstance().getConfig().detailedTooltip.status &&
                ZerodayStyleHotbar.getInstance().getConfig().detailedTooltip.sword != DetailedTooltip.Sword.DISABLE;
    }

    public static String getItemSwordTooltip(ItemStack stack) {

        return DetailedTooltipUtil.getItemAttackDamageString(stack);
    }

    public static String getItemAttackDamageString(ItemStack stack) {

        return "§2+" + String.format("%.1f", ItemComponentUtil.getItemAttackDamage(stack)) + " " + Text.translatable("text.zerodaystylehotbar.detailedTooltip.attack-damage").getString();
//        return "§2+" + String.format("%.1f", ItemComponentUtil.getItemAttackDamage(stack)) + " Attack Damage";
    }

    public static boolean shouldRenderArmorDetailed() {

        return ZerodayStyleHotbar.getInstance().getConfig().detailedTooltip.status &&
                ZerodayStyleHotbar.getInstance().getConfig().detailedTooltip.armor != DetailedTooltip.Armor.DISABLE;
    }

    public static String getItemArmorTooltip(ItemStack stack) {

        DetailedTooltip detailed = ZerodayStyleHotbar.getInstance().getConfig().detailedTooltip;
        String tooltip = DetailedTooltipUtil.getItemMaxDamageReductionString(stack);
        if (detailed.armor == DetailedTooltip.Armor.BOTH) {
            tooltip += " (" + DetailedTooltipUtil.getItemMinDamageReductionString(stack) + ")";
        } else if (detailed.armor == DetailedTooltip.Armor.MIN_DAMAGE_REDUCTION) {
            tooltip = DetailedTooltipUtil.getItemMinDamageReductionString(stack);
        }

        return tooltip;
    }

    public static String getItemMaxDamageReductionString(ItemStack stack) {

        return "+" + String.format("%.1f", ItemComponentUtil.getItemDamageReduction(stack)[1] * 100.0D) + "% " +
                Text.translatable("text.zerodaystylehotbar.detailedTooltip.max-damage-reduction").getString();
    }

    public static String getItemMinDamageReductionString(ItemStack stack) {

        return "+" + String.format("%.1f", ItemComponentUtil.getItemDamageReduction(stack)[0] * 100.0D) + "% " +
                Text.translatable("text.zerodaystylehotbar.detailedTooltip.min-damage-reduction").getString();
    }

    public static boolean shouldRenderToolDetailed() {

        return ZerodayStyleHotbar.getInstance().getConfig().detailedTooltip.status &&
                ZerodayStyleHotbar.getInstance().getConfig().detailedTooltip.tool != DetailedTooltip.Tool.DISABLE;
    }

    public static String getItemToolTooltip(ItemStack stack) {

        return DetailedTooltipUtil.getItemMiningSpeedString(stack);
    }

    public static String getItemMiningSpeedString(ItemStack stack) {

        float miningSpeed = ItemComponentUtil.getItemMiningSpeed(stack);
        return "§9+" + String.format("%.1f", miningSpeed) + " " +
                Text.translatable("text.zerodaystylehotbar.detailedTooltip.mining-efficiency").getString();
    }

    public static boolean shouldRenderFoodDetailed() {

        return ZerodayStyleHotbar.getInstance().getConfig().detailedTooltip.status &&
                ZerodayStyleHotbar.getInstance().getConfig().detailedTooltip.food != DetailedTooltip.Food.DISABLE;
    }

    public static String getItemFoodTooltip(ItemStack stack) {

        DetailedTooltip detailed = ZerodayStyleHotbar.getInstance().getConfig().detailedTooltip;
        String tooltip = DetailedTooltipUtil.getItemNutritionString(stack);
        if (detailed.food == DetailedTooltip.Food.BOTH) {
            tooltip += " (" + DetailedTooltipUtil.getItemSaturationString(stack) + ")";
        } else if (detailed.food == DetailedTooltip.Food.SATURATION) {
            tooltip = DetailedTooltipUtil.getItemSaturationString(stack);
        }

        return tooltip;
    }

    public static String getItemSaturationString(ItemStack stack) {

        return "+" + String.format("%.1f", ItemComponentUtil.getItemSaturation(stack)) + " " +
                Text.translatable("text.zerodaystylehotbar.detailedTooltip.saturation").getString();
    }

    public static String getItemNutritionString(ItemStack stack) {

        return "+" + String.format("%d", ItemComponentUtil.getItemNutrition(stack)) + " " +
                Text.translatable("text.zerodaystylehotbar.detailedTooltip.nutrition").getString();
    }

}
