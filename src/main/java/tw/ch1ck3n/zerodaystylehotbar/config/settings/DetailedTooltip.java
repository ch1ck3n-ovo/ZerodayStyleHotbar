package tw.ch1ck3n.zerodaystylehotbar.config.settings;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class DetailedTooltip {
    
    @ConfigEntry.Gui.Excluded
    private static final String PREFIX = "text.autoconfig.zerodaystylehotbar.option.detailedTooltip.";

    public boolean status = true;

    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.DROPDOWN)
    public Armor armor = Armor.MAX_DAMAGE_REDUCTION;

    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.DROPDOWN)
    public Sword sword = Sword.ATTACK_DAMAGE;

    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.DROPDOWN)
    public Tool tool = Tool.MINING_EFFICIENCY;

    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.DROPDOWN)
    public Food food = Food.FOOD_LEVEL;

    public enum Armor {
        BOTH("both"),
        MAX_DAMAGE_REDUCTION("max-damage-reduction"),
        MIN_DAMAGE_REDUCTION("min-damage-reduction"),
        DISABLE("disable");

        final String key;
        Armor(String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return PREFIX + "armor." + this.key;
        }
    }

    public enum Sword {
        ATTACK_DAMAGE("attack-damage"),
        DISABLE("disable");

        final String key;
        Sword(String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return PREFIX + "sword." + this.key;
        }
    }

    public enum Tool {
        MINING_EFFICIENCY("mining-efficiency"),
        DISABLE("disable");

        final String key;
        Tool(String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return PREFIX + "tool." + this.key;
        }
    }

    public enum Food {
        BOTH("both"),
        FOOD_LEVEL("food-level"),
        SATURATION("saturation"),
        DISABLE("disable");

        final String key;
        Food(String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return PREFIX + "food." + this.key;
        }
    }
}
