package org.black_ixx.bossshop.addon.itemshops;


import java.util.List;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.BSShops;
import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.core.rewards.BSRewardType;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.configuration.file.FileConfiguration;


public class ItemShopsCreator {


    private double price_multiplier, reward_multiplier;
    private boolean advanced_style, allow_sell, allow_buy, allow_buyall, allow_sellall, worth_is_for_one_unit;
    private String currency;
    private BSPriceType pricetype;
    private BSRewardType rewardtype;


    private ItemShopsCreatorSimple creator_simple;
    private ItemShopsCreatorAdvanced creator_advanced;


    public ItemShopsCreator(FileConfiguration c) {
        price_multiplier = c.getDouble("PriceMultiplier");
        reward_multiplier = c.getDouble("RewardMultiplier");
        currency = c.getString("CurrencyType");
        advanced_style = c.getBoolean("UseAdvancedStyle");
        allow_sell = c.getBoolean("AllowSell");
        allow_buy = c.getBoolean("AllowBuy");
        allow_sellall = c.getBoolean("AllowSellAll");
        allow_buyall = c.getBoolean("AllowBuyAll");
        worth_is_for_one_unit = c.getBoolean("WorthIsForOneUnit");
        creator_simple = new ItemShopsCreatorSimple(c);
        creator_advanced = new ItemShopsCreatorAdvanced(c);
    }


    public boolean isAllowSell() {
        return allow_sell;
    }

    public boolean isAllowBuy() {
        return allow_buy;
    }

    public boolean isAllowBuyAll() {
        return allow_buyall;
    }

    public boolean isAllowSellAll() {
        return allow_sellall;
    }

    public void loadItemShop(BSShops shophandler, BSShop shop, List<ISItem> items, BossShop plugin) {
        if (pricetype == null) {
            pricetype = BSPriceType.detectType(currency);
            pricetype.enableType();
            rewardtype = BSRewardType.detectType(currency);
            rewardtype.enableType();
        }

        for (ISItem item : items) {
            BSBuy buy = createBuyItem(shophandler, shop, item, plugin);
            shop.addShopItem(buy, buy.getItem(), ClassManager.manager);
        }

    }


    private BSBuy createBuyItem(BSShops shophandler, BSShop shop, ISItem item, BossShop plugin) {
        if (advanced_style) {
            return creator_advanced.createBuyItem(shophandler, shop, item, plugin, rewardtype, pricetype, reward_multiplier, price_multiplier, worth_is_for_one_unit);
        } else {
            return creator_simple.createBuyItem(shop, item, plugin, rewardtype, pricetype, reward_multiplier, price_multiplier, worth_is_for_one_unit);
        }
    }


}
