package dev.lone.wailat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class Wailat extends JavaPlugin implements Listener
{
    HashMap<String, BossBar> bossBars = new HashMap<>();
    HashMap<String, Integer> tasks = new HashMap<>();

    static boolean hasItemsAdder = false;

    @Override
    public void onEnable()
    {
        for(Player p : Bukkit.getServer().getOnlinePlayers())
            if(p.hasPermission("wailat.ui"))
                registerBossbar(p);

        Bukkit.getPluginManager().registerEvents(this, this);

        hasItemsAdder = (Bukkit.getPluginManager().getPlugin("ItemsAdder") != null);
    }

    @Override
    public void onDisable()
    {
        for(Player p : Bukkit.getServer().getOnlinePlayers())
            unregisterBossbar(p);
    }

    @EventHandler
    void onJoin(PlayerJoinEvent e)
    {
        if(e.getPlayer().hasPermission("wailat.ui"))
            registerBossbar(e.getPlayer());
    }

    @EventHandler
    void onLeave(PlayerQuitEvent e)
    {
        unregisterBossbar(e.getPlayer());
    }

    private void unregisterBossbar(Player p)
    {
        BossBar boss = bossBars.get(p.getName());
        if(boss == null)
            return;
        boss.removePlayer(p);
        bossBars.remove(p.getName());
        Bukkit.getScheduler().cancelTask(tasks.get(p.getName()));
    }

    void registerBossbar(Player p)
    {
        BossBar bossBar = Bukkit.createBossBar("", BarColor.WHITE, BarStyle.SOLID, new BarFlag[0]);
        bossBar.addPlayer(p);
        bossBars.put(p.getName(), bossBar);

        int i = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            Block block = p.getTargetBlockExact(8);
            if (block == null || block.getType() == Material.AIR)
            {
                bossBar.setTitle("");
                return;
            }

            if(!p.isSneaking())
            {
                bossBar.setTitle(ChatColor.GREEN + BlockUtil.getDisplayName(block));
            }
            else
            {
                StringBuilder title = new StringBuilder("Drops: ");

                for(ItemStack drop : block.getDrops())
                    title.append(ItemStackUtil.getDisplayName(drop)).append(drop.getAmount() > 1 ? (" x" + drop.getAmount()) : "").append(", ");
                title = new StringBuilder(title.substring(0, title.length() - 2));

                if(title.toString().equals("Drops"))
                    title = new StringBuilder("Drops nothing");

                bossBar.setTitle(title.toString());
            }
        }, 20L, 5L);

        tasks.put(p.getName(), i);
    }
}
