package tw.ch1ck3n.zerodaystylehotbar.config.settings;

import me.shedaniel.autoconfig.annotation.ConfigEntry;


public class DetailedStatusBar {
    
//    @ConfigEntry.Gui.Excluded
//    private static final String PREFIX = "text.autoconfig.zerodaystylehotbar.option.detailedStatusBar.";

    public boolean status = true;

    public boolean showDetailedExperience = true;

    public boolean showSaturation = true;

    public boolean showTotalHealth = true;

    public boolean shouldShowDetailedExperience() {
        return this.status && this.showDetailedExperience;
    }

    public boolean shouldShowSaturation() {
        return this.status && this.showSaturation;
    }

    public boolean shouldShowTotalHealth() {
        return this.status && this.showTotalHealth;
    }
}
