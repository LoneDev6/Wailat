package dev.lone.wailat;

import dev.lone.itemsadder.api.ItemsAdder;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class BlockUtil
{
    public static String getDisplayName(Block block)
    {
        if(Wailat.hasItemsAdder)
        {
            if(ItemsAdder.isCustomBlock(block))
            {
                ItemStack tmp = ItemsAdder.getCustomBlock(block);
                if (tmp == null)
                    return "";
                return ChatColor.GRAY + tmp.getItemMeta().getDisplayName();
            }
            else if(ItemsAdder.isCustomCrop(block))
            {
                return ChatColor.GRAY + ItemsAdder.getCustomItem(ItemsAdder.getCustomSeedNameFromCrop(block)).getItemMeta().getDisplayName();
            }
        }

        return ChatColor.GRAY + WordUtils.capitalizeFully(block.getType().toString()).replace("_", " ");
    }

    public static String getDropsText(Block block)
    {
        StringBuilder title = new StringBuilder("Drops: ");
        if(Wailat.hasItemsAdder && ItemsAdder.isCustomBlock(block))
        {
            for (ItemStack drop : ItemsAdder.getCustomBlockLoot(block))
                title.append(ItemStackUtil.getDisplayName(drop)).append(drop.getAmount() > 1 ? (" x" + drop.getAmount()) : "").append(", ");
            title = new StringBuilder(title.substring(0, title.length() - 2));
        }
        else if(Wailat.hasItemsAdder && ItemsAdder.isCustomCrop(block))
        {
            title.append(ItemsAdder.getCustomItem(ItemsAdder.getCustomSeedNameFromCrop(block)).getItemMeta().getDisplayName());
        }
        else
        {
            for (ItemStack drop : block.getDrops())
                title.append(ItemStackUtil.getDisplayName(drop)).append(drop.getAmount() > 1 ? (" x" + drop.getAmount()) : "").append(", ");
            title = new StringBuilder(title.substring(0, title.length() - 2));
        }

        if(title.toString().equals("Drops"))
            title = new StringBuilder("Drops nothing");

        return title.toString();
    }
}
