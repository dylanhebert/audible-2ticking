package com.audible2ticking;

import com.google.inject.Provides;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import javax.inject.Inject;

@PluginDescriptor(
		name = "Audible 2-Ticking",
		description = "Plays sounds to aid with some 2-ticking allowing the player to at least look away from the screen.",
		tags = {"skilling", "tick", "timers", "damage", "zero", "sound", "fishing"},
		enabledByDefault = false
)
@Slf4j
public class Audible2TickingPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private Notifier notifier;

	@Getter(AccessLevel.PACKAGE)
	private int inventoryCount;

	@Getter(AccessLevel.PACKAGE)
	private int inventoryPreviousCount;

	int maxInventoryCount = 28;

	@Inject
	private Audible2TickingConfig config;

	@Inject
	private Audible2TickingOverlay overlay;

	@Inject
	private OverlayManager overlayManager;

	@Provides
	Audible2TickingConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(Audible2TickingConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
		reset();
		clientThread.invokeLater(() ->
		{
			final ItemContainer container = client.getItemContainer(InventoryID.INVENTORY);

			if (container != null)
			{
				calculateInventory(container.getItems());
			}
		});
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
		reset();
	}

	private void reset()
	{
		inventoryCount = 0;
		inventoryPreviousCount = 0;
	}

	@Subscribe
	public void onHitsplatApplied(HitsplatApplied hitsplatApplied)
	{
		// As playSoundEffect only uses the volume argument when the in-game volume isn't muted, sound effect volume
		// needs to be set to the value desired for ticks or tocks and afterwards reset to the previous value.
		Preferences preferences = client.getPreferences();
		int previousVolume = preferences.getSoundEffectVolume();

		Actor target = hitsplatApplied.getActor();
		Hitsplat hitsplat = hitsplatApplied.getHitsplat();

		// Ignore all hitsplats other than mine
		if (target != client.getLocalPlayer())
		{
			return;
		}

		if (hitsplat.getAmount() == 0)
		{
			if (config.tickVolume() > 0)
			{
				preferences.setSoundEffectVolume(config.tickVolume());
				client.playSoundEffect(SoundEffectID.MINING_TINK, config.tickVolume());
			}
		}

		preferences.setSoundEffectVolume(previousVolume);
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		Preferences preferences = client.getPreferences();
		int previousVolume = preferences.getSoundEffectVolume();

		final ItemContainer container = event.getItemContainer();

		if (container != client.getItemContainer(InventoryID.INVENTORY))
		{
			return;
		}

		calculateInventory(container.getItems());

		int openSlots = maxInventoryCount - inventoryCount;
		log.debug("Inventory count: {}", inventoryCount);
		log.debug("Open Inventory slots: {}", openSlots);
		log.debug("Previous Inventory slots: {}", inventoryPreviousCount);
		if (openSlots <= config.openSlotsLeftCount())
		{
			if (inventoryCount > inventoryPreviousCount)
			{
				preferences.setSoundEffectVolume(config.inventoryVolume());
				client.playSoundEffect(SoundEffectID.TOWN_CRIER_BELL_DONG, config.inventoryVolume());
			}
		}

		inventoryPreviousCount = inventoryCount;
		preferences.setSoundEffectVolume(previousVolume);
	}

	private void calculateInventory(Item[] inv)
	{
		inventoryCount = 0;

		for (Item item : inv)
		{
			if (item.getId() != -1)
			{
				inventoryCount += 1;
			}
		}
	}
}
