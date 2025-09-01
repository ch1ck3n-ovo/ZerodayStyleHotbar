package tw.ch1ck3n.zerodaystylehotbar.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import tw.ch1ck3n.zerodaystylehotbar.ZerodayStyleHotbar;
import tw.ch1ck3n.zerodaystylehotbar.config.settings.DetailedTooltip;
import tw.ch1ck3n.zerodaystylehotbar.config.settings.DetailedStatusBar;

@Config(name = ZerodayStyleHotbar.MOD_ID)
public class ZerodayStyleHotbarConfig implements ConfigData {

    public boolean status = true;

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public DetailedTooltip detailedTooltip = new DetailedTooltip();

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public DetailedStatusBar detailedStatusBar = new DetailedStatusBar();
}

//        @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
//        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.DROPDOWN)
//        @ConfigEntry.BoundedDiscrete(min = 0L, max = 10L)
//        @ConfigEntry.Gui.Tooltip
