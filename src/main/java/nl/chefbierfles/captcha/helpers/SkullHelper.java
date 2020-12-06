package nl.chefbierfles.captcha.helpers;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public final class SkullHelper {

    private static Method metaSetProfileMethod;
    private static Field metaProfileField;

    /**
     * Modifies a skull to use the skin based on the given base64 string.
     *
     * @param base64 The base64 string containing the texture.
     * @return The skull with a custom texture.
     */
    public final static ItemStack itemWithBase64(String base64) {

        ItemStack item = getPlayerSkull();

        notNull(item, "item");
        notNull(base64, "base64");

        if (!(item.getItemMeta() instanceof SkullMeta)) {
            return null;
        }
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(Bukkit.getPlayer("3dfd5c70-cbf3-43e2-9da3-8d45b1660d46"));
        mutateItemMeta(meta, base64);
        item.setItemMeta(meta);

        return item;
    }

    private final static ItemStack getPlayerSkull() {
        return new ItemStack(Material.SKULL_ITEM, 1, (short)3);
    }

    private final static void mutateItemMeta(SkullMeta meta, String b64) {
        try {
            if (metaSetProfileMethod == null) {
                metaSetProfileMethod = meta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
                metaSetProfileMethod.setAccessible(true);
            }
            metaSetProfileMethod.invoke(meta, makeProfile(b64));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            // if in an older API where there is no setProfile method,
            // we set the profile field directly.
            try {
                if (metaProfileField == null) {
                    metaProfileField = meta.getClass().getDeclaredField("profile");
                    metaProfileField.setAccessible(true);
                }
                metaProfileField.set(meta, makeProfile(b64));

            } catch (NoSuchFieldException | IllegalAccessException ex2) {
                ex2.printStackTrace();
            }
        }
    }

    private final static GameProfile makeProfile(String b64) {
        // random uuid based on the b64 string
        UUID id = new UUID(
                b64.substring(b64.length() - 20).hashCode(),
                b64.substring(b64.length() - 10).hashCode()
        );
        GameProfile profile = new GameProfile(id, "lalalala cashewnoot");
        profile.getProperties().put("textures", new Property("textures", b64));
        return profile;
    }

    private final static void notNull(Object o, String name) {
        if (o == null) {
            throw new NullPointerException(name + " should not be null!");
        }
    }

    public static boolean isSameSkull(ItemStack itemStack1, ItemStack itemStack2) {
        return itemStack1.isSimilar(itemStack2);
    }

}
