package net.blay09.mods.balm.platform.config.schema.builder;

import net.blay09.mods.balm.platform.config.schema.internal.ConfigSchemaImpl;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;

import java.util.List;
import java.util.Set;

public class ConfigPropertyBuilder {
    protected final ConfigSchemaImpl schema;
    protected final String category;
    protected final String name;
    protected String comment = "";
    protected boolean synced;

    public ConfigPropertyBuilder(ConfigSchemaImpl schema, String name) {
        this.schema = schema;
        this.category = "";
        this.name = name;
    }

    public ConfigPropertyBuilder(ConfigSchemaImpl schema, String category, String name) {
        this.schema = schema;
        this.category = category;
        this.name = name;
    }

    public ConfigPropertyBuilder comment(String comment) {
        this.comment = comment;
        return this;
    }

    public ConfigPropertyBuilder synced() {
        this.synced = true;
        return this;
    }

    public BooleanConfigProperty boolOf(boolean defaultValue) {
        return schema.addAndReturn(new BooleanConfigProperty(this, defaultValue));
    }

    public IntConfigProperty intOf(int defaultValue) {
        return schema.addAndReturn(new IntConfigProperty(this, defaultValue));
    }

    public IntConfigProperty intOf(int defaultValue, int minValue, int maxValue) {
        return schema.addAndReturn(new IntConfigProperty(this, defaultValue, minValue, maxValue));
    }

    public LongConfigProperty longOf(long defaultValue) {
        return schema.addAndReturn(new LongConfigProperty(this, defaultValue));
    }

    public LongConfigProperty longOf(long defaultValue, long minValue, long maxValue) {
        return schema.addAndReturn(new LongConfigProperty(this, defaultValue, minValue, maxValue));
    }

    public FloatConfigProperty floatOf(float defaultValue) {
        return schema.addAndReturn(new FloatConfigProperty(this, defaultValue));
    }

    public FloatConfigProperty floatOf(float defaultValue, float minValue, float maxValue) {
        return schema.addAndReturn(new FloatConfigProperty(this, defaultValue, minValue, maxValue));
    }

    public DoubleConfigProperty doubleOf(double defaultValue) {
        return schema.addAndReturn(new DoubleConfigProperty(this, defaultValue));
    }

    public DoubleConfigProperty doubleOf(double defaultValue, double minValue, double maxValue) {
        return schema.addAndReturn(new DoubleConfigProperty(this, defaultValue, minValue, maxValue));
    }

    public StringConfigProperty stringOf(String defaultValue) {
        return schema.addAndReturn(new StringConfigProperty(this, defaultValue));
    }

    public <T extends Enum<T> & StringRepresentable> EnumConfigProperty<T> enumOf(T defaultValue) {
        return schema.addAndReturn(new EnumConfigProperty<>(this, defaultValue));
    }

    public <T> ListConfigProperty<T> listOf(Class<T> nestedType, List<T> defaultValue) {
        return schema.addAndReturn(new ListConfigProperty<>(this, nestedType, defaultValue));
    }

    public <T> SetConfigProperty<T> setOf(Class<T> nestedType, Set<T> defaultValue) {
        return schema.addAndReturn(new SetConfigProperty<>(this, nestedType, defaultValue));
    }

    public IdentifierConfigProperty IdentifierOf(Identifier defaultValue) {
        return schema.addAndReturn(new IdentifierConfigProperty(this, defaultValue));
    }
}
