package net.blay09.mods.balm.neoforge.config;

/*public class NeoForgeBalmConfigOld extends AbstractBalmConfig {

    private <T> void readConfigValues(String parentPath, T instance, ModConfig config) throws IllegalAccessException {
        List<Field> fields = ConfigReflection.getAllFields(instance.getClass());
        for (Field field : fields) {
            String path = parentPath + field.getName();
            final var spec = ((ModConfigSpec) config.getSpec()).getValues();
            boolean hasValue = spec.contains(path);
            Class<?> type = field.getType();
            try {
                if (hasValue && Integer.TYPE.isAssignableFrom(type)) {
                    final var value = (ModConfigSpec.IntValue) spec.get(path);
                    field.set(instance, value.get());
                } else if (hasValue && Long.TYPE.isAssignableFrom(type)) {
                    final var value = (ModConfigSpec.LongValue) spec.get(path);
                    field.set(instance, value.get());
                } else if (hasValue && Float.TYPE.isAssignableFrom(type)) {
                    Object value = spec.get(path);
                    if (value instanceof ModConfigSpec.DoubleValue doubleValue) {
                        field.set(instance, doubleValue.get().floatValue());
                    } else {
                        logger.error("Invalid config value for {}, expected {} but got {}", path, type.getName(), value.getClass());
                    }
                } else if (hasValue && Double.TYPE.isAssignableFrom(type)) {
                    Object value = spec.get(path);
                    if (value instanceof ModConfigSpec.DoubleValue doubleValue) {
                        field.set(instance, doubleValue.getAsDouble());
                    } else {
                        logger.error("Invalid config value for {}, expected {} but got {}", path, type.getName(), value.getClass());
                    }
                } else if (hasValue && ResourceLocation.class.isAssignableFrom(type)) {
                    final var value = (ModConfigSpec.ConfigValue<String>) spec.get(path);
                    field.set(instance, ResourceLocation.parse(value.get()));
                } else if (hasValue && (Collection.class.isAssignableFrom(type))) {
                    final var value = (ModConfigSpec.ConfigValue<?>) spec.getRaw(path);
                    final var raw = value.get();
                    if (raw instanceof List<?> list) {
                        ExpectedType expectedType = field.getAnnotation(ExpectedType.class);
                        Function<Object, Object> mapper = (it) -> it;
                        if (expectedType != null && ResourceLocation.class.isAssignableFrom(expectedType.value())) {
                            mapper = (it) -> ResourceLocation.parse((String) it);
                        } else if (expectedType != null && Enum.class.isAssignableFrom(expectedType.value())) {
                            mapper = (it) -> parseEnumValue(expectedType.value(), (String) it);
                        }
                        try {
                            if (List.class.isAssignableFrom(type)) {
                                field.set(instance, list.stream().map(mapper).collect(Collectors.toList()));
                            } else if (Set.class.isAssignableFrom(type)) {
                                field.set(instance, list.stream().map(mapper).collect(Collectors.toSet()));
                            }
                        } catch (IllegalArgumentException e) {
                            logger.error("Invalid config value for " + path + ", expected " + type.getName() + " but got " + raw.getClass());
                        }
                    } else {
                        logger.error("Null config value for " + path + ", falling back to default");
                    }
                } else if (hasValue && (type.isPrimitive() || String.class.isAssignableFrom(type))) {
                    final var value = spec.get(path);
                    if (value instanceof ModConfigSpec.ConfigValue<?> stringValue) {
                        try {
                            field.set(instance, stringValue.get());
                        } catch (IllegalArgumentException e) {
                            logger.error("Invalid config value for " + path + ", expected " + type.getName() + " but got " + value.getClass());
                        }
                    } else {
                        logger.error("Null config value for " + path + ", falling back to default");
                    }
                } else if (hasValue && type.isEnum()) {
                    final var value = (ModConfigSpec.EnumValue<?>) spec.get(path);
                    field.set(instance, value.get());
                } else {
                    readConfigValues(path + ".", field.get(instance), config);
                }
            } catch (Exception e) {
                logger.error("Unexpected error loading config value for " + path + ", falling back to default", e);
            }
        }
    }

    private static Object parseEnumValue(Class<?> type, String value) {
        for (Object enumConstant : type.getEnumConstants()) {
            if (enumConstant.toString().equalsIgnoreCase(value)) {
                return enumConstant;
            }
        }

        return null;
    }

    private <T extends BalmConfigData> void writeConfigValues(Class<?> clazz, T configData) {
        try {
            writeConfigValues("", clazz, configData);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private <T> void writeConfigValues(String parentPath, Class<?> clazz, T instance) throws IllegalAccessException {
        List<Field> fields = ConfigReflection.getAllFields(instance.getClass());
        for (Field field : fields) {
            String path = parentPath + field.getName();
            Class<?> type = field.getType();
            Object value = field.get(instance);
            if (type.isPrimitive() || Enum.class.isAssignableFrom(type) || String.class.isAssignableFrom(type)) {
                final var property = (ModConfigSpec.ConfigValue<Object>) configProperties.get(clazz, path);
                if (property != null) {
                    property.set(value);
                }
            } else if (ResourceLocation.class.isAssignableFrom(type)) {
                final var property = (ModConfigSpec.ConfigValue<Object>) configProperties.get(clazz, path);
                if (property != null) {
                    property.set(((ResourceLocation) value).toString());
                }
            } else if (Collection.class.isAssignableFrom(type)) {
                final var property = (ModConfigSpec.ConfigValue<Object>) configProperties.get(clazz, path);
                if (property != null) {
                    property.set(new ArrayList<>((Collection<?>) value));
                }
            } else {
                writeConfigValues(path + ".", field.getType(), field.get(instance));
            }
        }
    }

    @Override
    public <T extends BalmConfigData> T initializeBackingConfig(Class<T> clazz) {
        modContainer.getEventBus().addListener((ModConfigEvent.Reloading event) -> {
            configs.put(clazz, event.getConfig());
            T newConfigData = readConfigValues(clazz, event.getConfig());
            configData.put(clazz, newConfigData);

            // Only rewrite active configs with reload if we're the hosting server or there is no syncing
            boolean hasSyncMessage = getConfigSyncMessageFactory(clazz) != null;
            boolean isHostingServer = ServerLifecycleHooks.getCurrentServer() != null;
            boolean isIngame = Balm.getProxy().isIngame();
            if (!hasSyncMessage || isHostingServer || !isIngame) {
                setActiveConfig(clazz, newConfigData);
            }
        });
    }

    @Override
    public <T extends BalmConfigData> void saveBackingConfig(Class<T> clazz) {
        ModConfig modConfig = configs.get(clazz);
        if (modConfig != null) {
            writeConfigValues(clazz, configData.get(clazz));
            ((ModConfigSpec) modConfig.getSpec()).save();
        }
    }

}*/
