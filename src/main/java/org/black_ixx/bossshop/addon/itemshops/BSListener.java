package org.black_ixx.bossshop.addon.itemshops;

import java.util.ArrayList;
import java.util.List;

import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.events.BSLoadShopItemsEvent;
import org.black_ixx.bossshop.managers.config.BSConfigShop;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BSListener implements Listener {

    private ItemShops shops;

    public BSListener(ItemShops shops) {
        this.shops = shops;
    }

    @EventHandler
    public void loadShop(BSLoadShopItemsEvent event) {
        BSShop shop = event.getShop();
        if (shop instanceof BSConfigShop) {
            BSConfigShop c = (BSConfigShop) shop;
            ConfigurationSection s = c.getConfig();
            if (s.contains("itemshop")) {
                s = s.getConfigurationSection("itemshop");

                List<ISItem> items = new ArrayList<ISItem>();

                for (String key : s.getKeys(false)) {
                    ISItem item = loadItem(s.getConfigurationSection(key), shops.getCreator().isAllowSell(),
                            shops.getCreator().isAllowBuy(), shops.getCreator().isAllowSellAll(), shops.getCreator().isAllowBuyAll());
                    if (item != null) {
                        items.add(item);
                    }
                }

                if (!items.isEmpty()) {
                    shops.loadItemShop(event.getShopHandler(), shop, items);
                }

            }

        }
    }


    private ISItem loadItem(ConfigurationSection section, boolean allow_sell, boolean allow_buy, boolean allow_sellall, boolean allow_buyall) {
        if (section != null) {
            double worth = section.getDouble("Worth");
            double fix_buy = section.getDouble("PriceBuy", -1);
            double fix_sell = section.getDouble("RewardSell", -1);
            List<String> itemdata = section.getStringList("Item");

            if (section.contains("AllowSell")) {
                allow_sell = section.getBoolean("AllowSell");
            }
            if (section.contains("AllowBuy")) {
                allow_buy = section.getBoolean("AllowBuy");
            }
            if (section.contains("AllowSellAll")) {
                allow_sellall = section.getBoolean("AllowSellAll");
            }
            if (section.contains("AllowBuyAll")) {
                allow_buyall = section.getBoolean("AllowBuyAll");
            }

            return new ISItem(section.getName(), worth, fix_buy, fix_sell, itemdata, allow_sell, allow_buy, allow_sellall, allow_buyall);
        }
        return null;
    }

}
