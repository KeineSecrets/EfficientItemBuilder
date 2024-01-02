package dev.mzcy.itembuilder.impl;



import com.google.common.primitives.Ints;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for detecting server version for legacy support :(
 */
public final class VersionHelper {

    private static final String NMS_VERSION = getNmsVersion();

    // Unbreakable change
    private static final int V1_11 = 1110;
    // Material and components on items change
    private static final int V1_13 = 1130;
    // PDC and customModelData
    private static final int V1_14 = 1140;
    // Paper adventure changes
    private static final int V1_16_5 = 1165;
    // SkullMeta#setOwningPlayer was added
    private static final int V1_12_1 = 1121;
    // PlayerProfile API
    private static final int V1_20_1 = 1201;

    private static final int CURRENT_VERSION = getCurrentVersion();

    private static final boolean IS_PAPER = checkPaper();

    /**
     * Checks if the version supports Components or not
     * Spigot always false
     */
    public static final boolean IS_COMPONENT_LEGACY = false;

    /**
     * Checks if the version is lower than 1.13 due to the item changes
     */
    public static final boolean IS_ITEM_LEGACY = false;

    /**
     * Checks if the version supports the {@link org.bukkit.inventory.meta.ItemMeta#setUnbreakable(boolean)} method
     */
    public static final boolean IS_UNBREAKABLE_LEGACY = false;

    /**
     * Checks if the version supports {@link org.bukkit.persistence.PersistentDataContainer}
     */
    public static final boolean IS_PDC_VERSION = true;

    /**
     * Checks if the version doesn't have {@link org.bukkit.inventory.meta.SkullMeta#setOwningPlayer(OfflinePlayer)} and
     * {@link org.bukkit.inventory.meta.SkullMeta#setOwner(String)} should be used instead
     */
    public static final boolean IS_SKULL_OWNER_LEGACY = false;

    /**
     * Checks if the version has {@link org.bukkit.inventory.meta.ItemMeta#setCustomModelData(Integer)}
     */
    public static final boolean IS_CUSTOM_MODEL_DATA = true;

    /**
     * Checks if the version has {@link org.bukkit.profile.PlayerProfile}
     */
    public static final boolean IS_PLAYER_PROFILE_API = true;

    /**
     * Check if the server has access to the Paper API
     * Taken from <a href="https://github.com/PaperMC/PaperLib">PaperLib</a>
     *
     * @return True if on Paper server (or forks), false anything else
     */
    private static boolean checkPaper() {
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    /**
     * Gets the current server version
     *
     * @return A protocol like number representing the version, for example 1.16.5 - 1165
     */
    private static int getCurrentVersion() {
        // No need to cache since will only run once
        final Matcher matcher = Pattern.compile("(?<version>\\d+\\.\\d+)(?<patch>\\.\\d+)?").matcher(Bukkit.getBukkitVersion());

        final StringBuilder stringBuilder = new StringBuilder();
        if (matcher.find()) {
            stringBuilder.append(matcher.group("version").replace(".", ""));
            final String patch = matcher.group("patch");
            if (patch == null) stringBuilder.append("0");
            else stringBuilder.append(patch.replace(".", ""));
        }

        //noinspection UnstableApiUsage
        final Integer version = Ints.tryParse(stringBuilder.toString());

        // Should never fail
        if (version == null) try {
            throw new Exception("Could not retrieve server version!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return version;
    }

    private static String getNmsVersion() {
        final String version = Bukkit.getServer().getClass().getPackage().getName();
        return version.substring(version.lastIndexOf('.') + 1);
    }

    public static Class<?> craftClass(@NotNull final String name) throws ClassNotFoundException {
        return Class.forName("org.bukkit.craftbukkit." + NMS_VERSION + "." + name);
    }

}
