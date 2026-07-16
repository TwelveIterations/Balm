package net.blay09.mods.balm.platform.compatibility.config.internal;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import net.blay09.mods.balm.platform.config.schema.ConfigControlBinding;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ClothConfigWidgetConfigListEntry<T> extends AbstractConfigListEntry<T> {
    private final ConfigControlBinding<T> binding;
    private final AbstractWidget widget;
    private final T originalValue;

    public ClothConfigWidgetConfigListEntry(ConfigControlBinding<T> binding, AbstractWidget widget) {
        super(binding.label(), false);
        this.binding = binding;
        this.widget = widget;
        originalValue = binding.get();
        saveCallback = binding::set;
    }

    @Override
    public T getValue() {
        return binding.get();
    }

    @Override
    public Optional<T> getDefaultValue() {
        return Optional.of(binding.defaultValue());
    }

    @Override
    public boolean isEdited() {
        return !Objects.equals(originalValue, binding.get());
    }

    @Override
    public int getItemHeight() {
        return widget.getHeight();
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return List.of(widget);
    }

    @Override
    public List<? extends NarratableEntry> narratables() {
        return List.of(widget);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphicsExtractor, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float delta) {
        super.extractRenderState(graphicsExtractor, index, y, x, entryWidth, entryHeight, mouseX, mouseY, hovered, delta);
        widget.setX(x + entryWidth - widget.getWidth());
        widget.setY(y);
        widget.extractRenderState(graphicsExtractor, mouseX, mouseY, delta);
    }
}
