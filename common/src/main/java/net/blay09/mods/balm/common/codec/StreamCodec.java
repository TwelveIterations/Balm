package net.blay09.mods.balm.common.codec;

import java.util.function.BiConsumer;
import java.util.function.Function;

public interface StreamCodec<TBuffer, TData> {
    static <TBuffer, TData> StreamCodec<TBuffer, TData> of(BiConsumer<TBuffer, TData> encoder, Function<TBuffer, TData> decoder) {
        return new StreamCodec<>() {
            @Override
            public void encode(TBuffer buf, TData value) {
                encoder.accept(buf, value);
            }

            @Override
            public TData decode(TBuffer buf) {
                return decoder.apply(buf);
            }
        };
    }

    void encode(TBuffer buf, TData value);

    TData decode(TBuffer buf);

    default <TMappedData> StreamCodec<TBuffer, TMappedData> map(final Function<? super TData, ? extends TMappedData> mapper, final Function<? super TMappedData, ? extends TData> reverseMapper) {
        return new StreamCodec<>() {
            public TMappedData decode(TBuffer buffer) {
                return mapper.apply(StreamCodec.this.decode(buffer));
            }

            public void encode(TBuffer buffer, TMappedData data) {
                StreamCodec.this.encode(buffer, reverseMapper.apply(data));
            }
        };
    }
}
