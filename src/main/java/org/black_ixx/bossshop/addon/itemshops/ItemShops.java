package org.black_ixx.bossshop.addon.itemshops;

import java.util.List;

import org.black_ixx.bossshop.api.BossShopAddonConfigurable;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.BSShops;
import org.black_ixx.bossshop.managers.config.FileHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class ItemShops extends BossShopAddonConfigurable {


    private ItemShopsCreator creator;


    @Override
    public String getAddonName() {
        return "ItemShops";
    }

    @Override
    public String getRequiredBossShopVersion() {
        return "1.9.9";
    }

    @Override
    public void enableAddon() {
        load();
        getServer().getPluginManager().registerEvents(new BSListener(this), this);
    }

    public void load() {
        if (!getAddonConfig().getFile().exists()) {
            new FileHandler().copyFromJar(this, "config.yml");

            new FileHandler().exportShops(getBossShop()); //Because shops are only imported when there is no shop folder already
            new FileHandler().copyFromJar(this, getBossShop(), true, "ItemShopExample.yml", "ItemShopExample.yml");
        } else {
            new FileHandler().copyDefaultsFromJar(this, "config.yml");
        }

        reloadConfig();
        FileConfiguration c = getConfig();
        creator = new ItemShopsCreator(c);

    }

    @Override
    public void disableAddon() {
    }


    public ItemShopsCreator getCreator() {
        return creator;
    }


    @Override
    public void bossShopReloaded(CommandSender sender) {
        load();
    }

    @Override
    public void bossShopFinishedLoading() {
    }


    public void loadItemShop(BSShops shophandler, BSShop shop, List<ISItem> items) {
        creator.loadItemShop(shophandler, shop, items, getBossShop());
    }

    @Override
    public boolean saveConfigOnDisable() {
        return false;
    }

}
