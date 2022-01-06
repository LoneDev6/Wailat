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
                return tmp.getItemMeta().getDisplayName();
            }
            else if(ItemsAdder.isCustomCrop(block))
            {
                return ItemsAdder.getCustomSeedNameFromCrop(block);
            }
        }
        return ChatColor.GRAY + WordUtils.capitalizeFully(block.getType().toString()).replace("_", " ");
    }
}
