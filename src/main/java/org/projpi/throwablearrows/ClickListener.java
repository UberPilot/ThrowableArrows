package org.projpi.throwablearrows;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ClickListener implements Listener
{
    private final ThrowableArrows instance;

    public ClickListener(ThrowableArrows instance)
    {
        this.instance = instance;
    }

    @EventHandler
    private void onRightClick(PlayerInteractEvent e)
    {
        if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK))
        {
            return;
        }
        Player p = e.getPlayer();
        if (!p.hasPermission("throwableArrows.use") && !p.hasPermission("throwableArrows.use.click"))
        {
            return;
        }
        if(!instance.canUse(p))
        {
            return;
        }

        if (p.getInventory().getItemInMainHand().getType() == Material.ARROW)
        {
            p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
            instance.spawnArrow(Bukkit.getPlayer(p.getUniqueId()));
            instance.use(p);
            return;
        }

        if (p.getInventory().getItemInOffHand().getType() == Material.ARROW)
        {
            p.getInventory().getItemInOffHand().setAmount(p.getInventory().getItemInOffHand().getAmount() - 1);
            instance.spawnArrow(Bukkit.getPlayer(p.getUniqueId()));
            instance.use(p);
        }
    }

}
