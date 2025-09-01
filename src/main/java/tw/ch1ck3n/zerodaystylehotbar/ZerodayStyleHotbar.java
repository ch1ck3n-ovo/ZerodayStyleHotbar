package tw.ch1ck3n.zerodaystylehotbar;

import lombok.Getter;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;

import tw.ch1ck3n.zerodaystylehotbar.config.ZerodayStyleHotbarConfig;

@Getter
public class ZerodayStyleHotbar implements ModInitializer {

	@Getter
	public static ZerodayStyleHotbar instance;

	public static final String MOD_ID = "zerodaystylehotbar";
	private ZerodayStyleHotbarConfig config;

	@Override
	public void onInitialize() {
		instance = this;

		AutoConfig.register(ZerodayStyleHotbarConfig.class, JanksonConfigSerializer::new);
		this.config = AutoConfig.getConfigHolder(ZerodayStyleHotbarConfig.class).getConfig();
	}
}