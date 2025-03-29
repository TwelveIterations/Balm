package net.blay09.mods.balm.api.util;

import net.minecraft.core.NonNullList;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @deprecated Not in use by Balm.
 */
@Deprecated(forRemoval = true, since = "1.22")
public class ListUtils {
    @Deprecated
    public static <T> NonNullList<T> nonNullListOf(@Nullable List<T> list, T defaultValue) {
        if (list == null) {
            return null;
        }

        final var result = NonNullList.withSize(list.size(), defaultValue);
        for (int i = 0; i < list.size(); i++) {
            T value = list.get(i);
            result.set(i, value != null ? value : defaultValue);
        }
        return result;
    }
}
