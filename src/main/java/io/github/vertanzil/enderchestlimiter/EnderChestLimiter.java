package io.github.vertanzil.enderchestlimiter;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.WorldCoord;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

import static org.bukkit.Bukkit.getLogger;

public final class EnderChestLimiter  extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getLogger().log(Level.INFO, "E.C.L has been enabled!");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "E.C.L has been disabled!");
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        getLogger().log(Level.INFO, event.getBlockPlaced().getType().toString());
        Player player = event.getPlayer();

        Town town = TownyAPI.getInstance().getTown(player.getLocation());
        Resident resident = TownyAPI.getInstance().getResident(player);

        if (event.getBlockPlaced().getType() == Material.ENDER_CHEST) {

            if (town != null && resident.hasTown() && resident.getTownOrNull().equals(town)) {
                if (town.getTownBlock(WorldCoord.parseWorldCoord(event.getPlayer().getLocation())).getTypeName().toLowerCase().contains("bank")) {
                    event.setCancelled(false);
                }
            }
            //NO TOWN
            if (town != null && town.hasResident(player.getName())) {
                if (player.hasPermission("ecl.bypass") || player.isOp()) {
                    event.setCancelled(false);
                } else {
                    event.setCancelled(true);
                    player.sendMessage("[E.C.L]" + " " + "You cannot place ender chests outside of a bank plot.");
                }
            }
        }
    }
}