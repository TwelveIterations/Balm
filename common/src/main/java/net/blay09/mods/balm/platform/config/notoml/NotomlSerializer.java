package net.blay09.mods.balm.platform.config.notoml;

import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

public class NotomlSerializer {

    public static void serialize(Writer writer, Notoml notoml) throws IOException {
        writer.write(serializeToString(notoml));
    }

    private static String serializeToString(Notoml notoml) {
        StringBuilder sb = new StringBuilder();

        var data = notoml.getProperties();
        var comments = notoml.getComments();
        var sortedCategoryKeys = data.rowKeySet().stream().sorted().toList();
        for (String category : sortedCategoryKeys) {
            String categoryComment = comments.get(category, "");
            if (categoryComment != null && !categoryComment.isEmpty()) {
                sb.append("\n").append("# ").append(categoryComment).append("\n");
            }

            if (!category.isEmpty()) {
                sb.append("[").append(category).append("]").append("\n");
            }

            Map<String, Object> categoryProperties = data.row(category);
            var sortedPropertyKeys = categoryProperties.keySet().stream().sorted().toList();
            for (String property : sortedPropertyKeys) {
                String propertyComment = comments.get(category, property);
                if (propertyComment != null && !propertyComment.isEmpty()) {
                    sb.append("\n").append("# ").append(propertyComment).append("\n");
                }

                sb.append(property).append(" = ");

                Object value = categoryProperties.get(property);
                switch (value) {
                    case String stringValue -> {
                        if (stringValue.contains("\n")) {
                            sb.append("\"\"\"\n").append(value).append("\n\"\"\"");
                        } else {
                            sb.append("\"").append(stringValue.replace("\"", "\\\"")).append("\"");
                        }
                    }
                    case Identifier identifierValue -> sb.append("\"").append(identifierValue).append("\"");
                    case Collection<?> listValue -> serializeList(listValue, sb);
                    case StringRepresentable stringRepresentable ->
                            sb.append("\"").append(stringRepresentable.getSerializedName()).append("\"");
                    case Enum<?> enumValue -> sb.append("\"").append(enumValue.name()).append("\"");
                    case null, default -> sb.append(value);
                }

                sb.append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private static void serializeList(Collection<?> list, StringBuilder sb) {
        sb.append("[ ");
        boolean newLines = list.size() > 3;
        var first = true;
        for (final var value : list) {
            if (!first) {
                sb.append(", ");
            }
            first = false;

            if (newLines) {
                sb.append("\n");
            }
            if (newLines) {
                sb.append("    ");
            }
            switch (value) {
                case String stringValue -> sb.append("\"").append(stringValue.replace("\"", "\\\"")).append("\"");
                case Identifier identifierValue -> sb.append("\"").append(identifierValue).append("\"");
                case StringRepresentable stringRepresentable ->
                        sb.append("\"").append(stringRepresentable.getSerializedName()).append("\"");
                case Enum<?> enumValue -> sb.append("\"").append(enumValue.name()).append("\"");
                case null, default -> sb.append(value);
            }
        }
        if (newLines) {
            sb.append("\n]");
        } else {
            sb.append(" ]");
        }
    }

}
