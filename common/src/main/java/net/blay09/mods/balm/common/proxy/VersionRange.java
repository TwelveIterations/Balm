package net.blay09.mods.balm.common.proxy;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Locale;

public class VersionRange {

    private final @Nullable String lower;
    private final boolean lowerInclusive;
    private final @Nullable String upper;
    private final boolean upperInclusive;

    private VersionRange(@Nullable String lower, boolean lowerInclusive, @Nullable String upper, boolean upperInclusive) {
        this.lower = lower;
        this.lowerInclusive = lowerInclusive;
        this.upper = upper;
        this.upperInclusive = upperInclusive;
    }

    public boolean contains(String version) {
        if (lower != null) {
            final var lowerComparison = compareVersions(version, lower);
            if (lowerComparison < 0 || lowerComparison == 0 && !lowerInclusive) {
                return false;
            }
        }

        if (upper != null) {
            final var upperComparison = compareVersions(version, upper);
            return upperComparison <= 0 && (upperComparison != 0 || upperInclusive);
        }

        return true;
    }

    public static VersionRange parse(String versionRange) {
        if (versionRange.isEmpty()) {
            throw new IllegalArgumentException("Invalid version range '" + versionRange + "'");
        }

        final var lowerInclusive = switch (versionRange.charAt(0)) {
            case '[' -> true;
            case '(' -> false;
            default -> throw new IllegalArgumentException("Invalid version range '" + versionRange + "'");
        };

        final var closingSquareIndex = versionRange.lastIndexOf(']');
        final var closingParenIndex = versionRange.lastIndexOf(')');
        final var endIndex = Math.max(closingSquareIndex, closingParenIndex);
        if (endIndex == -1 || endIndex != versionRange.length() - 1) {
            throw new IllegalArgumentException("Invalid version range '" + versionRange + "'");
        }

        final var endChar = versionRange.charAt(endIndex);
        final var range = versionRange.substring(1, endIndex);
        final var separatorIndex = range.indexOf(',');
        if (separatorIndex != range.lastIndexOf(',')) {
            throw new IllegalArgumentException("Invalid version range '" + versionRange + "'");
        }

        if (separatorIndex == -1) {
            final var version = range.trim();
            if (version.isEmpty()) {
                throw new IllegalArgumentException("Invalid version range '" + versionRange + "'");
            }
            return new VersionRange(version, lowerInclusive, version, endChar == ']');
        }

        final var lower = range.substring(0, separatorIndex).trim();
        final var upper = range.substring(separatorIndex + 1).trim();
        return new VersionRange(lower.isEmpty() ? null : lower, lowerInclusive, upper.isEmpty() ? null : upper, endChar == ']');
    }

    private static int compareVersions(String left, String right) {
        final var leftParts = tokenize(stripBuildMetadata(left));
        final var rightParts = tokenize(stripBuildMetadata(right));
        final var maxLength = Math.max(leftParts.size(), rightParts.size());
        for (var i = 0; i < maxLength; i++) {
            final var leftPart = i < leftParts.size() ? leftParts.get(i) : VersionPart.ZERO;
            final var rightPart = i < rightParts.size() ? rightParts.get(i) : VersionPart.ZERO;
            final var comparison = leftPart.compareTo(rightPart);
            if (comparison != 0) {
                return comparison;
            }
        }

        return 0;
    }

    private static String stripBuildMetadata(String version) {
        final var buildMetadataIndex = version.indexOf('+');
        return buildMetadataIndex != -1 ? version.substring(0, buildMetadataIndex) : version;
    }

    private static ArrayList<VersionPart> tokenize(String version) {
        final var parts = new ArrayList<VersionPart>();
        final var token = new StringBuilder();
        Boolean digitToken = null;
        for (var i = 0; i < version.length(); i++) {
            final var character = version.charAt(i);
            final var isDigit = Character.isDigit(character);
            if (!Character.isLetterOrDigit(character)) {
                flushToken(parts, token, digitToken);
                digitToken = null;
                continue;
            }

            if (digitToken != null && digitToken != isDigit) {
                flushToken(parts, token, digitToken);
            }

            token.append(character);
            digitToken = isDigit;
        }

        flushToken(parts, token, digitToken);
        return parts;
    }

    private static void flushToken(ArrayList<VersionPart> parts, StringBuilder token, @Nullable Boolean digitToken) {
        if (token.isEmpty() || digitToken == null) {
            return;
        }

        parts.add(digitToken ? VersionPart.numeric(token.toString()) : VersionPart.qualifier(token.toString()));
        token.setLength(0);
    }

    private record VersionPart(long number, String qualifier, boolean numeric) implements Comparable<VersionPart> {
        private static final VersionPart ZERO = numeric("0");

        private static VersionPart numeric(String value) {
            try {
                return new VersionPart(Long.parseLong(value), "", true);
            } catch (NumberFormatException e) {
                return new VersionPart(Long.MAX_VALUE, value, true);
            }
        }

        private static VersionPart qualifier(String value) {
            return new VersionPart(0, value.toLowerCase(Locale.ROOT), false);
        }

        @Override
        public int compareTo(VersionPart other) {
            if (numeric != other.numeric) {
                return numeric ? 1 : -1;
            }

            if (numeric) {
                return Long.compare(number, other.number);
            }

            return qualifier.compareTo(other.qualifier);
        }
    }
}
