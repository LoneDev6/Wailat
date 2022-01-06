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
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Objects;

public final class Wailat extends JavaPlugin implements Listener
{
    private static Wailat instance;

    public HashMap<String, PlayerData> playerDatas = new HashMap<>();

    static boolean hasItemsAdder = false;

    public static Wailat inst()
    {
        return instance;
    }

    @Override
    public void onEnable()
    {
        instance = this;

        for(Player p : Bukkit.getServer().getOnlinePlayers())
        {
            if(p.hasPermission("wailat.ui"))
                registerPlayer(p);
        }

        hasItemsAdder = Bukkit.getPluginManager().isPluginEnabled("ItemsAdder");

        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
            new PAPI_Wailat().register();


        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable()
    {
        for(Player p : Bukkit.getServer().getOnlinePlayers())
            unregisterBossbar(p);
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e)
    {
        if(e.getPlayer().hasPermission("wailat.ui"))
            registerPlayer(e.getPlayer());
    }

    @EventHandler
    private void onLeave(PlayerQuitEvent e)
    {
        unregisterBossbar(e.getPlayer());
    }

    private void registerPlayer(Player p)
    {
        BossBar bossBar = Bukkit.createBossBar("", BarColor.WHITE, BarStyle.SOLID, new BarFlag[0]);
        bossBar.addPlayer(p);
        final PlayerData playerData = new PlayerData();
        playerDatas.put(p.getName(), playerData);

        playerData.bossbar = bossBar;

        final long interval = 5L;
        playerData.refreshTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
        {
            long passedTicks = 0;

            @Override
            public void run()
            {
                passedTicks += interval;

                Block block = p.getTargetBlockExact(8);

                // Save CPU
                if(p.isSneaking() != playerData.wasSneaking && Objects.equals(playerData.prevBlock, block))
                    return;

                playerData.prevBlock = block;
                playerData.wasSneaking = p.isSneaking();

                if (block == null || block.getType() == Material.AIR)
                {
                    bossBar.setTitle("");
                    return;
                }

                if (!playerData.wasSneaking)
                {
                    bossBar.setTitle(ChatColor.GREEN + BlockUtil.getDisplayName(block));
                    passedTicks = 0;
                }
                else
                {
                    if(passedTicks >= 15L) // Optimizing the drops getting code
                    {
                        bossBar.setTitle(BlockUtil.getDropsText(block));
                        passedTicks = 0;
                    }
                }
            }
        }, interval, interval);
    }

    private void unregisterBossbar(Player p)
    {
        PlayerData playerData = playerDatas.get(p.getName());
        if(playerData == null)
            return;

        playerData.bossbar.removePlayer(p);
        playerData.cancelRefreshTask();
    }
}
