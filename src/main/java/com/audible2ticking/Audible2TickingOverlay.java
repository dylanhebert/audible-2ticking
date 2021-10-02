package com.audible2ticking;

import com.google.inject.Inject;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

public class Audible2TickingOverlay extends OverlayPanel
{
    private final Audible2TickingConfig config;

    @Inject
    private Audible2TickingOverlay(Audible2TickingConfig config)
    {
        this.config = config;

        panelComponent.getChildren().add(TitleComponent.builder()
                .text("Audible 2-Ticking")
                .color(Color.CYAN)
                .build());

        setPriority(OverlayPriority.LOW);
        setPosition(OverlayPosition.BOTTOM_LEFT);
        setClearChildren(false);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        return super.render(graphics);
    }
}
