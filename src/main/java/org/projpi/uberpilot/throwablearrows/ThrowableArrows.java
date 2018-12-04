package org.projpi.uberpilot.throwablearrows;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

/**
 * A plugin created to allow people to throw arrows.
 * @author UberPilot
 */
public class ThrowableArrows extends JavaPlugin
{
    private double modifier;
    private FileConfiguration config = this.getConfig();
    private ThrowableArrows pl;

    @Override
    public void onLoad()
    {
    }

    @Override
    public void onEnable()
    {
        pl = this;
        config.addDefault("modifier", 0.4);
        if(!getDataFolder().exists())
        {
            getDataFolder().mkdirs();
            config.options().copyDefaults(true);
            saveConfig();
        }
        modifier = config.getDouble("modifier");
        try
        {
            config.save(new File(getDataFolder(), "config.yml"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        getServer().getPluginManager().registerEvents(new Listener()
        {
            @EventHandler
            void onItemDropEvent(PlayerDropItemEvent e)
            {
                if(e.getItemDrop().getItemStack().getType() == Material.ARROW)
                {
                    Player p = e.getPlayer();
                    if(!p.isSneaking())
                    {
                        if(p.hasPermission("throwableArrows.use.stack"))
                        {
                            Bukkit.getScheduler().runTaskAsynchronously(pl, () ->
                            {
                                int i = e.getItemDrop().getItemStack().getAmount();
                                e.getItemDrop().remove();
                                for (; i > 0; i--)
                                {
                                    spawnArrow(Bukkit.getPlayer(p.getUniqueId()));
                                    try
                                    {
                                        Thread.sleep(25);
                                    }
                                    catch (InterruptedException e1)
                                    {
                                        e1.printStackTrace();
                                    }
                                }
                            });
                        }
                        else if (p.hasPermission("throwableArrows.use"))
                        {
                            e.getItemDrop().getItemStack().setAmount(e.getItemDrop().getItemStack().getAmount() - 1);
                            spawnArrow(p);
                        }
                    }
                }
            }
        }, pl);
    }

    private void spawnArrow(Player p)
    {
        Bukkit.getScheduler().runTask(pl, () -> p.launchProjectile(Arrow.class));
    }
}
