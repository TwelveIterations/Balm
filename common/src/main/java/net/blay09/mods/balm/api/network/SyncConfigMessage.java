package net.blay09.mods.balm.api.network;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.BalmConfigData;
import net.blay09.mods.balm.api.config.Synced;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Deprecated(since = "1.21.5")
public class SyncConfigMessage<TData> {

    private final TData data;

    public SyncConfigMessage(TData data) {
        this.data = data;
    }

    public static <TData> Supplier<TData> createDeepCopyFactory(Supplier<TData> from, Supplier<TData> factory) {
        return () -> {
            TData to = factory.get();
            ConfigReflection.deepCopy(from.get(), to);
            return to;
        };
    }

    public static <TData, TMessage extends SyncConfigMessage<TData>> Function<FriendlyByteBuf, TMessage> createDecoder(Class<?> clazz, Function<TData, TMessage> messageFactory, Supplier<TData> dataFactory) {
        return buf -> {
            TData data = dataFactory.get();
            readSyncedFields(buf, data, false);
            return messageFactory.apply(data);
        };
    }

    private static <TData> void readSyncedFields(FriendlyByteBuf buf, TData data, boolean forceSynced) {
        List<Field> syncedFields = !forceSynced ? ConfigReflection.getSyncedFields(data.getClass()) : Arrays.asList(data.getClass().getFields());
        syncedFields.sort(Comparator.comparing(Field::getName));
        for (Field field : syncedFields) {
            Class<?> type = field.getType();
            Object value;
            try {
                if (String.class.isAssignableFrom(type)) {
                    value = buf.readUtf();
                } else if (Enum.class.isAssignableFrom(type)) {
                    value = type.getEnumConstants()[buf.readByte()];
                } else if (int.class.isAssignableFrom(type)) {
                    value = buf.readInt();
                } else if (float.class.isAssignableFrom(type)) {
                    value = buf.readFloat();
                } else if (double.class.isAssignableFrom(type)) {
                    value = buf.readDouble();
                } else if (boolean.class.isAssignableFrom(type)) {
                    value = buf.readBoolean();
                } else if (long.class.isAssignableFrom(type)) {
                    value = buf.readLong();
                } else {
                    value = field.get(data);
                    readSyncedFields(buf, value, field.getAnnotation(Synced.class) != null);
                }
                field.set(data, value);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public static <TData, TMessage extends SyncConfigMessage<TData>> BiConsumer<TMessage, FriendlyByteBuf> createEncoder(Class<TData> clazz) {
        return (message, buf) -> {
            TData data = message.getData();
            writeSyncedFields(buf, data, false);
        };
    }

    private static <TData> void writeSyncedFields(FriendlyByteBuf buf, TData data, boolean forceSynced) {
        List<Field> syncedFields = !forceSynced ? ConfigReflection.getSyncedFields(data.getClass()) : Arrays.asList(data.getClass().getFields());
        syncedFields.sort(Comparator.comparing(Field::getName));
        for (Field field : syncedFields) {
            Class<?> type = field.getType();
            final Object value;
            try {
                value = field.get(data);
                if (String.class.isAssignableFrom(type)) {
                    buf.writeUtf((String) value);
                } else if (Enum.class.isAssignableFrom(type)) {
                    buf.writeByte(((Enum<?>) value).ordinal());
                } else if (int.class.isAssignableFrom(type)) {
                    buf.writeInt((int) value);
                } else if (float.class.isAssignableFrom(type)) {
                    buf.writeFloat((float) value);
                } else if (double.class.isAssignableFrom(type)) {
                    buf.writeDouble((double) value);
                } else if (boolean.class.isAssignableFrom(type)) {
                    buf.writeBoolean((boolean) value);
                } else if (long.class.isAssignableFrom(type)) {
                    buf.writeLong((long) value);
                } else {
                    writeSyncedFields(buf, field.get(data), field.getAnnotation(Synced.class) != null);
                }
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public static <TMessage extends SyncConfigMessage<TData>, TData extends BalmConfigData> void register(ResourceLocation resourceLocation,
                                                                                                          Class<TMessage> messageClass,
                                                                                                          Function<TData, TMessage> messageFactory,
                                                                                                          Class<TData> dataClass,
                                                                                                          Supplier<TData> dataFactory) {
        Supplier<TData> copyFactory = SyncConfigMessage.createDeepCopyFactory(() -> Balm.getConfig().getBackingConfig(dataClass), dataFactory);
        Balm.getNetworking().registerClientboundPacket(resourceLocation, messageClass, (TMessage message, FriendlyByteBuf buf) -> {
            TData data = message.getData();
            writeSyncedFields(buf, data, false);
        }, buf -> {
            TData data = copyFactory.get();
            readSyncedFields(buf, data, false);
            return messageFactory.apply(data);
        }, Balm.getConfig()::handleSync);
    }

    public TData getData() {
        return data;
    }

}
