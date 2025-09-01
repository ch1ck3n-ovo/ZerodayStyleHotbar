package tw.ch1ck3n.zerodaystylehotbar.util;

import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ItemComponentUtil {

    public static double getItemAttackDamage(ItemStack stack) {

        return ItemComponentUtil.getItemBaseAttackDamage(stack) + ItemComponentUtil.getItemEnchantmentBonusDamage(stack);
    }

    public static double getItemBaseAttackDamage(ItemStack stack) {

        AttributeModifiersComponent component = stack.get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
        if (component == null || component.modifiers().isEmpty()) return 1;

        for (AttributeModifiersComponent.Entry entry : component.modifiers()) {
            if (entry.modifier().idMatches(Identifier.of("minecraft:base_attack_damage"))) {
                return 1 + entry.modifier().value();
            }
        }
        return 1;
    }

    public static double getItemEnchantmentBonusDamage(ItemStack stack) {

        ItemEnchantmentsComponent component = stack.get(DataComponentTypes.ENCHANTMENTS);
        if (component == null || component.getEnchantments().isEmpty()) return 0;

        for (RegistryEntry<Enchantment> entry : component.getEnchantments()) {
            if (entry.matchesId(Identifier.of("minecraft:sharpness"))) {
                return EnchantmentHelper.getLevel(entry, stack) * 0.5D + 0.5D;
            }
        }
        return 0;
    }

    public static float getItemMiningSpeed(ItemStack stack) {

        return ItemComponentUtil.getItemDefaultMiningSpeed(stack) +
                ItemComponentUtil.getItemEnchantmentBonusMiningSpeed(stack);
    }

    public static float getItemDefaultMiningSpeed(ItemStack stack) {

        ToolComponent component = stack.get(DataComponentTypes.TOOL);
        if (component == null) return 0;
        Item item = stack.getItem();
        if (item instanceof AxeItem) return component.getSpeed(Blocks.OAK_LOG.getDefaultState());
        else if (item instanceof PickaxeItem) return component.getSpeed(Blocks.STONE.getDefaultState());
        else if (item instanceof ShovelItem) return component.getSpeed(Blocks.DIRT.getDefaultState());
        else if (item instanceof HoeItem) return component.getSpeed(Blocks.OAK_LEAVES.getDefaultState());
        else return 0;
    }

    public static int getItemEnchantmentBonusMiningSpeed(ItemStack stack) {

        ItemEnchantmentsComponent component = stack.get(DataComponentTypes.ENCHANTMENTS);
        if (component == null || component.getEnchantments().isEmpty()) return 0;

        for (RegistryEntry<Enchantment> entry : component.getEnchantments()) {
            if (entry.matchesId(Identifier.of("minecraft:efficiency"))) {
                return (int) (Math.pow(EnchantmentHelper.getLevel(entry, stack), 2) + 1);
            }
        }
        return 0;
    }

    public static double[] getItemDamageReduction(ItemStack stack) {

        double armor = ItemComponentUtil.getItemArmor(stack),
                armorToughness = ItemComponentUtil.getItemArmorToughness(stack),
                epfMultiplier = ItemComponentUtil.getItemEPFMultiplier(stack);

        return new double[] {
                ItemComponentUtil.getItemDamageReduction(armor, armorToughness, epfMultiplier, 100.0D),
                ItemComponentUtil.getItemDamageReduction(armor, armorToughness, epfMultiplier, 1.0D)
        };
    }

    public static double getItemDamageReduction(double armor, double armorToughness, double epfMultiplier, double damage) {

        return (damage - (damage * (1 - ItemComponentUtil.getRawReduction(armor, armorToughness, damage))) * epfMultiplier) / damage;
    }

    public static double getRawReduction(double armor, double toughness, double damage) {
        double base = Math.max(armor / 5.0, armor - (4.0 * damage) / (toughness + 8.0));
        return Math.min(20.0, base) / 25.0;
    }

    public static double getItemArmor(ItemStack stack) {

        AttributeModifiersComponent component = stack.get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
        if (component == null || component.modifiers().isEmpty()) return 0;
        for (AttributeModifiersComponent.Entry entry : component.modifiers()) {
            if (entry.attribute().matchesId(Identifier.of("minecraft:armor"))) {
                return entry.modifier().value();
            }
        }
        return 0;
    }

    public static double getItemArmorToughness(ItemStack stack) {

        AttributeModifiersComponent component = stack.get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
        if (component == null || component.modifiers().isEmpty()) return 0;
        for (AttributeModifiersComponent.Entry entry : component.modifiers()) {
            if (entry.attribute().matchesId(Identifier.of("minecraft:armor_toughness"))) {
                return entry.modifier().value();
            }
        }
        return 0;
    }

    public static double getItemEnchantmentProtection(ItemStack stack) {

        ItemEnchantmentsComponent component = stack.get(DataComponentTypes.ENCHANTMENTS);
        if (component == null || component.getEnchantments().isEmpty()) return 0;

        for (RegistryEntry<Enchantment> entry : component.getEnchantments()) {
            if (entry.matchesId(Identifier.of("minecraft:protection"))) {
                return EnchantmentHelper.getLevel(entry, stack);
            }
        }
        return 0;
    }

    public static double getItemEPFMultiplier(ItemStack stack) {

        return 1.0 - Math.min(ItemComponentUtil.getItemEnchantmentProtection(stack), 20) / 25.0;
    }

    public static float getItemSaturation(ItemStack stack) {

        FoodComponent component = stack.get(DataComponentTypes.FOOD);
        if (component == null) return 0.0F;
        return component.saturation();
    }

    public static int getItemNutrition(ItemStack stack) {

        FoodComponent component = stack.get(DataComponentTypes.FOOD);
        if (component == null) return 0;
        return component.nutrition();
    }

}
