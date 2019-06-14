package org.projpi.throwablearrows;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

/**
 * A plugin created to allow people to throw arrows.
 *
 * @author UberPilot
 */
public class ThrowableArrows extends JavaPlugin
{
    private double modifier;
    private FileConfiguration config = this.getConfig();
    private ThrowableArrows pl;
    private boolean allowDrop;
    private boolean allowClick;
    private boolean normalizeDamage;
    private long cooldown;
    private int damage;
    private HashMap<UUID, Long> lastUse = new HashMap<>();

    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void onEnable()
    {
        pl = this;
        config.addDefault("modifier", 0.4);
        config.addDefault("allow-drop", true);
        config.addDefault("allow-click", false);
        config.addDefault("normalize-damage", false);
        config.addDefault("cooldown", 100L);
        config.addDefault("damage", 6);
        if (!getDataFolder().exists())
        {
            getDataFolder().mkdirs();
            config.options().copyDefaults(true);
            saveConfig();
        }
        modifier = config.getDouble("modifier");
        allowDrop = config.getBoolean("allow-drop");
        allowClick = config.getBoolean("allow-click");
        normalizeDamage = config.getBoolean("normalize-damage");
        cooldown = config.getLong("cooldown");
        damage = config.getInt("damage");
        saveConfig();
        try
        {
            config.save(new File(getDataFolder(), "config.yml"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if(allowClick)
        {
            getServer().getPluginManager().registerEvents(new ClickListener(this), this);
        }

        if(allowDrop)
        {
            getServer().getPluginManager().registerEvents(new DropListener(this), this);
        }

        if(normalizeDamage)
        {
            getServer().getPluginManager().registerEvents(new DamageListener(this), this);
        }
    }

    void spawnArrow(Player p)
    {
        Bukkit.getScheduler().runTask(pl, () ->
        {
            Arrow a = p.launchProjectile(Arrow.class);
            a.setCustomName("ThrownArrow");
            a.setCustomNameVisible(false);
            a.setVelocity(a.getVelocity().multiply(modifier));
        });
    }

    public int getDamage()
    {
        return damage;
    }

    boolean canUse(Player p)
    {
        if(lastUse.containsKey(p.getUniqueId()))
        {
            return (System.currentTimeMillis() > lastUse.get(p.getUniqueId()) + cooldown);
        }
        else
        {
            return true;
        }
    }

    void use(Player p)
    {
        lastUse.put(p.getUniqueId(), System.currentTimeMillis());
    }
}
