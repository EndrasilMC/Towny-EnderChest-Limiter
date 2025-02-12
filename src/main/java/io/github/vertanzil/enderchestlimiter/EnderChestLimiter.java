package io.github.vertanzil.enderchestlimiter;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.*;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;
import org.bukkit.Material;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Level;



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
        Player player = event.getPlayer();

        Town town = TownyAPI.getInstance().getTown(player.getLocation());
        Resident resident = TownyAPI.getInstance().getResident(player);


        if (event.getBlockPlaced().getType() == Material.ENDER_CHEST) {
            //CHECK IF THE PLAYER IS IN THEIR OWN TOWN
            if (town != null && resident.hasTown() && resident.getTownOrNull().equals(town)) {

                for (TownBlock tb : town.getTownBlocks()) {
                    if (tb.getType() == TownBlockType.BANK) {
                        event.setCancelled(false);
                    } else {
                        event.setCancelled(true);
                        player.sendMessage("[E.C.L]" + " " + "You cannot place ender chests outside of a bank plot.");
                    }
                }
            }

            //CHECK IF PLAYER IS IN ANOTHER PERSONS TOWN.
            if (town != null && town.hasResident(player.getName())) {
                boolean bBuild = PlayerCacheUtil.getCachePermission(player, event.getBlock().getLocation(), event.getBlock().getType(), TownyPermission.ActionType.BUILD);

                if (bBuild) {

                    if (player.hasPermission("ecl.bypass") || player.isOp()) {
                        event.setCancelled(false);
                    } else {
                        for (TownBlock tb : town.getTownBlocks()) {
                            if (tb.getType() == TownBlockType.BANK) {
                                event.setCancelled(false);
                            } else {
                                event.setCancelled(true);
                                player.sendMessage("[E.C.L]" + " " + "You cannot place ender chests outside of a bank plot.");
                            }
                        }
                    }
                }
            } else {
                if (player.hasPermission("ecl.bypass") || player.isOp()) {
                    event.setCancelled(false);
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }

}