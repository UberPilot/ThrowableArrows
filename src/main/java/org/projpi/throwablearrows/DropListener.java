package org.projpi.throwablearrows;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropListener implements Listener
{
    private final ThrowableArrows instance;

    public DropListener(ThrowableArrows instance)
    {
        this.instance = instance;
    }

    @EventHandler
    private void onItemDropEvent(PlayerDropItemEvent e)
    {
        if (e.getItemDrop().getItemStack().getType() != Material.ARROW)
        {
            return;
        }

        Player p = e.getPlayer();
        if (!p.hasPermission("throwableArrows.use.drop"))
        {
            return;
        }
        if (p.isSneaking())
        {
            return;
        }
        if(!instance.canUse(p))
        {
            return;
        }

        if (p.hasPermission("throwableArrows.use.stack"))
        {
            Bukkit.getScheduler().runTaskAsynchronously(instance, () ->
            {
                int i = e.getItemDrop().getItemStack().getAmount();
                e.getItemDrop().remove();
                for (; i > 0; i--)
                {
                    instance.spawnArrow(Bukkit.getPlayer(p.getUniqueId()));
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
        else
        {
            e.getItemDrop().getItemStack().setAmount(e.getItemDrop().getItemStack().getAmount() - 1);
            instance.spawnArrow(p);
        }
    }
}
