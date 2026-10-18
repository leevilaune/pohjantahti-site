package dev.onesnzeroes.pt.domain;

import java.util.Locale;
import java.util.regex.Pattern;

/** Helpers for Clash of Clans tags such as {@code #2GY2L0UQP}. */
public final class TagUtil {

    /** Characters the game uses in tags. */
    private static final Pattern VALID = Pattern.compile("^#[0289PYLQGRJCUV]{3,15}$");

    private TagUtil() {
    }

    /** Upper-cases the tag and makes sure it starts with '#'. Accepts "2GY2L0UQP", "#2gy2l0uqp" or "%232GY2L0UQP". */
    public static String normalize(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("Tag must not be empty");
        }
        String tag = raw.trim().toUpperCase(Locale.ROOT);
        if (tag.startsWith("%23")) {
            tag = tag.substring(3);
        }
        return tag.startsWith("#") ? tag : "#" + tag;
    }

    /** Normalizes and validates; throws {@link IllegalArgumentException} for tags the game could not have issued. */
    public static String requireValid(String raw) {
        String tag = normalize(raw);
        if (!VALID.matcher(tag).matches()) {
            throw new IllegalArgumentException("Invalid clan tag: " + raw);
        }
        return tag;
    }
}
