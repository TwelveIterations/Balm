package net.blay09.mods.balm.api.config;

@Deprecated
public abstract class OldAbstractBalmConfig {

    /*private final Map<Class<?>, BalmConfigHolder> activeConfigs = new HashMap<>();
    private final Map<Class<?>, BalmConfigHolder> defaultConfigs = new HashMap<>();
    private final Map<Class<?>, Function<?, ?>> syncMessageFactories = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BalmConfigHolder> T getActive(Class<T> clazz) {
        return (T) activeConfigs.get(clazz);
    }

    @SuppressWarnings("unchecked")
    public <T extends BalmConfigHolder> SyncConfigMessage<T> getConfigSyncMessage(Class<T> clazz) {
        Function<BalmConfigHolder, SyncConfigMessage<BalmConfigHolder>> factory = getConfigSyncMessageFactory(clazz);
        return factory != null ? (SyncConfigMessage<T>) factory.apply(getBackingConfig(clazz)) : null;
    }

    @SuppressWarnings("unchecked")
    public <T extends BalmConfigHolder> Function<BalmConfigHolder, SyncConfigMessage<BalmConfigHolder>> getConfigSyncMessageFactory(Class<T> clazz) {
        return (Function<BalmConfigHolder, SyncConfigMessage<BalmConfigHolder>>) syncMessageFactories.get(clazz);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BalmConfigHolder> void handleSync(Player player, SyncConfigMessage<T> message) {
        T data = message.getData();
        setActiveConfig((Class<T>) data.getClass(), data);
    }

    @Override
    public <T extends BalmConfigHolder> void registerConfig(Class<T> clazz, Function<T, SyncConfigMessage<T>> syncMessageFactory) {
        Balm.getConfig().initializeBackingConfig(clazz);
        defaultConfigs.put(clazz, createConfigDataInstance(clazz));
        if (syncMessageFactory != null) {
            registerSyncMessageFactory(clazz, syncMessageFactory);
        }
    }

    private <T> void registerSyncMessageFactory(Class<T> clazz, Function<T, SyncConfigMessage<T>> syncMessageFactory) {
        syncMessageFactories.put(clazz, syncMessageFactory);
    }

    @Override
    public <T extends BalmConfigHolder> void updateConfig(Class<T> clazz, Consumer<T> consumer) {
        T backingConfig = getBackingConfig(clazz);
        consumer.accept(backingConfig);
        Balm.getConfig().saveBackingConfig(clazz);

        // If active config does not match backing config, apply changes to the active config as well
        // This assumes that the client-side does not use updateConfig to change server-side configs, as that would result in a desync
        T activeConfig = getActive(clazz);
        if (activeConfig != backingConfig) {
            consumer.accept(getActive(clazz));
        }
    }

    @Override
    public <T extends BalmConfigHolder> void resetToBackingConfig(Class<T> clazz) {
        setActiveConfig(clazz, getBackingConfig(clazz));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void resetToBackingConfigs() {
        for (Class<?> clazz : activeConfigs.keySet()) {
            resetToBackingConfig((Class<? extends BalmConfigHolder>) clazz);
        }
    }

    @NotNull
    protected <T> T createConfigDataInstance(Class<T> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new IllegalArgumentException("Config class or sub-class missing a public no-arg constructor.", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Failed to create config data class", e);
        }
    }

    @Override
    public File getConfigFile(String configName) {
        return new File(getConfigDir(), configName + "-common.toml");
    }

    @Override
    public <T extends BalmConfigHolder> Table<String, String, BalmConfigProperty<?>> getConfigProperties(Class<T> clazz) {
        var backingConfig = Balm.getConfig().getBackingConfig(clazz);
        var defaultConfig = defaultConfigs.get(clazz);
        Table<String, String, BalmConfigProperty<?>> properties = HashBasedTable.create();
        for (Field rootField : ConfigReflection.getAllFields(clazz)) {
            var category = "";
            Class<?> fieldType = rootField.getType();
            if (isPropertyType(fieldType)) {
                var property = rootField.getName();
                properties.put(category, property, createConfigProperty(backingConfig, null, rootField, defaultConfig));
            } else {
                category = rootField.getName();
                for (Field propertyField : ConfigReflection.getAllFields(fieldType)) {
                    var property = propertyField.getName();
                    properties.put(category, property, createConfigProperty(backingConfig, rootField, propertyField, defaultConfig));
                }
            }
        }
        return properties;
    }

    private static BalmConfigProperty<?> createConfigProperty(BalmConfigHolder configData, Field categoryField, Field propertyField, BalmConfigHolder defaultConfig) {
        return new BalmConfigPropertyImpl<String>(configData, categoryField, propertyField, defaultConfig);
    }

    private static boolean isPropertyType(Class<?> type) {
        return type.isPrimitive()
                || type == String.class
                || type == Integer.class
                || type == Boolean.class
                || type == Float.class
                || type == Double.class
                || type == List.class
                || type == Set.class
                || type == ResourceLocation.class
                || Enum.class.isAssignableFrom(type);
    }*/
}