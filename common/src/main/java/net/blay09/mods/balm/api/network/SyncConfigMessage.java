package net.blay09.mods.balm.api.network;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.BalmConfigData;
import net.blay09.mods.balm.api.config.ExpectedType;
import net.blay09.mods.balm.api.config.Synced;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Deprecated(forRemoval = true, since = "1.21.5")
public class SyncConfigMessage<TData> implements CustomPacketPayload {

    private final Type<? extends CustomPacketPayload> type;
    private final TData data;

    public SyncConfigMessage(Type<? extends CustomPacketPayload> type, TData data) {
        this.type = type;
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
                value = readValue(buf, data, field, type);
                field.set(data, value);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private static <TData> Object readValue(FriendlyByteBuf buf, TData data, Field field, Class<?> type) throws IllegalAccessException {
        Object value;
        if (String.class.isAssignableFrom(type)) {
            value = buf.readUtf();
        } else if (ResourceLocation.class.isAssignableFrom(type)) {
            value = buf.readResourceLocation();
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
        } else if (List.class.isAssignableFrom(type)) {
            final var count = buf.readVarInt();
            final var list = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                list.add(readValue(buf, data, field, field.getAnnotation(ExpectedType.class).value()));
            }
            value = list;
        } else if (Set.class.isAssignableFrom(type)) {
            final var count = buf.readVarInt();
            final var set = new HashSet<>();
            for (int i = 0; i < count; i++) {
                set.add(readValue(buf, data, field, field.getAnnotation(ExpectedType.class).value()));
            }
            value = set;
        } else {
            value = field.get(data);
            readSyncedFields(buf, value, field.getAnnotation(Synced.class) != null);
        }
        return value;
    }

    public static <TData, TMessage extends SyncConfigMessage<TData>> BiConsumer<FriendlyByteBuf, TMessage> createEncoder(Class<TData> clazz) {
        return (buf, message) -> {
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
                writeValue(buf, data, field, type, value);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private static <TData> void writeValue(FriendlyByteBuf buf, TData data, Field field, Class<?> type, Object value) throws IllegalAccessException {
        if (String.class.isAssignableFrom(type)) {
            buf.writeUtf((String) value);
        } else if (ResourceLocation.class.isAssignableFrom(type)) {
            buf.writeResourceLocation((ResourceLocation) value);
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
        } else if (List.class.isAssignableFrom(type)) {
            final var list = (List<?>) value;
            buf.writeVarInt(list.size());
            for (Object element : list) {
                writeValue(buf, data, field, field.getAnnotation(ExpectedType.class).value(), element);
            }
        } else if (Set.class.isAssignableFrom(type)) {
            final var set = (Set<?>) value;
            buf.writeVarInt(set.size());
            for (Object element : set) {
                writeValue(buf, data, field, field.getAnnotation(ExpectedType.class).value(), element);
            }
        } else {
            writeSyncedFields(buf, field.get(data), field.getAnnotation(Synced.class) != null);
        }
    }

    public static <TMessage extends SyncConfigMessage<TData>, TData extends BalmConfigData> void register(CustomPacketPayload.Type<TMessage> type,
                                                                                                          Class<TMessage> messageClass,
                                                                                                          Function<TData, TMessage> messageFactory,
                                                                                                          Class<TData> dataClass,
                                                                                                          Supplier<TData> dataFactory) {
        Supplier<TData> copyFactory = SyncConfigMessage.createDeepCopyFactory(() -> Balm.getConfig().getBackingConfig(dataClass), dataFactory);
        Balm.getNetworking().registerClientboundPacket(type, messageClass, StreamCodec.of((RegistryFriendlyByteBuf buf, TMessage message) -> {
            TData data = message.getData();
            writeSyncedFields(buf, data, false);
        }, buf -> {
            TData data = copyFactory.get();
            readSyncedFields(buf, data, false);
            return messageFactory.apply(data);
        }), Balm.getConfig()::handleSync);
    }

    public TData getData() {
        return data;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return type;
    }
}
