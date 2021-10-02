package com.audible2ticking;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class Audible2TickingTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(Audible2TickingPlugin.class);
		RuneLite.main(args);
	}
}