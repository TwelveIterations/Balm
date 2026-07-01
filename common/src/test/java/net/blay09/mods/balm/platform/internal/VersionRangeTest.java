package net.blay09.mods.balm.platform.internal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VersionRangeTest {

    @Test
    void matchesInclusiveLowerAndExclusiveUpperBound() {
        final var range = VersionRange.parse("[1.0,2.0)");

        assertTrue(range.contains("1.0"));
        assertTrue(range.contains("1.5"));
        assertFalse(range.contains("2.0"));
    }

    @Test
    void matchesOpenEndedRanges() {
        assertTrue(VersionRange.parse("[1.0,)").contains("2.0"));
        assertFalse(VersionRange.parse("[1.0,)").contains("0.9"));
        assertTrue(VersionRange.parse("(,2.0]").contains("2.0"));
        assertFalse(VersionRange.parse("(,2.0]").contains("2.1"));
    }

    @Test
    void matchesExactVersionRange() {
        final var range = VersionRange.parse("[1.2.3]");

        assertTrue(range.contains("1.2.3"));
        assertFalse(range.contains("1.2.4"));
    }

    @Test
    void ignoresBuildMetadataWhenComparingVersions() {
        assertTrue(VersionRange.parse("[26.0.5,26.1)").contains("26.0.5+fabric"));
    }

    @Test
    void rejectsUnsupportedRangeForms() {
        assertThrows(IllegalArgumentException.class, () -> VersionRange.parse(">=1.0"));
        assertThrows(IllegalArgumentException.class, () -> VersionRange.parse("1.0"));
        assertThrows(IllegalArgumentException.class, () -> VersionRange.parse("(,1.0],[1.2,)"));
    }
}
