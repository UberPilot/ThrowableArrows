package org.projpi.throwablearrows;

import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener
{
    ThrowableArrows instance;

    public DamageListener(ThrowableArrows instance)
    {
        this.instance = instance;
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageByEntityEvent event)
    {
        if (event.getCause() != EntityDamageEvent.DamageCause.PROJECTILE)
        {
            return;
        }
        if (!(event.getDamager() instanceof Arrow))
        {
            return;
        }
        final Arrow arrow = (Arrow) event.getDamager();
        if (!arrow.getCustomName().equals("ThrownArrow"))
        {
            return;
        }
        event.setDamage(instance.getDamage());
    }
}
