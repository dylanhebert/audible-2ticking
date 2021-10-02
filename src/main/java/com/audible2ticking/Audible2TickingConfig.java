package com.audible2ticking;

import net.runelite.api.SoundEffectVolume;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup("audible2ticking")
public interface Audible2TickingConfig extends Config
{
	int VOLUME_MAX = SoundEffectVolume.HIGH;

	@Range(
			max = VOLUME_MAX
	)
	@ConfigItem(
			keyName = "tickVolume",
			name = "NPC Splash Volume",
			description = "Configures the volume of the splash sound (like a rat hitting you). A value of 0 will disable the sounds.",
			position = 1
	)
	default int tickVolume()
	{
		return SoundEffectVolume.MEDIUM_HIGH;
	}

	@Range(
			max = 16
	)
	@ConfigItem(
			keyName = "openSlotsLeftCount",
			name = "Empty Slots Count",
			description = "Play a notification sound with this many slots left open in inventory.",
			position = 2
	)
	default int openSlotsLeftCount()
	{
		return 4;
	}

	@Range(
			max = VOLUME_MAX
	)
	@ConfigItem(
			keyName = "inventoryVolume",
			name = "Low Inventory Volume",
			description = "Configures the volume of the low inventory slots sound. A value of 0 will disable the sound.",
			position = 3
	)
	default int inventoryVolume()
	{
		return SoundEffectVolume.MEDIUM_HIGH;
	}
}
