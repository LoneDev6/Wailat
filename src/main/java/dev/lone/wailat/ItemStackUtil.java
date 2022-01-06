package dev.lone.wailat;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public class ItemStackUtil
{
    public static String getDisplayName(ItemStack item)
    {
        String name = "";
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName())
            name = ChatColor.GRAY + item.getItemMeta().getDisplayName();
        else
            name = ChatColor.GRAY + WordUtils.capitalizeFully(item.getType().toString()).replace("_", " ");

        return name;
    }
}
