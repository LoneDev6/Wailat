package dev.lone.wailat;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.boss.BossBar;

public class PlayerData
{
    public BossBar bossbar;
    public boolean wasSneaking;
    public Block prevBlock;

    public Integer refreshTaskId;

    public void cancelRefreshTask()
    {
        Bukkit.getScheduler().cancelTask(refreshTaskId);
    }
}
