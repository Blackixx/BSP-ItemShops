package org.black_ixx.bossshop.addon.itemshops;

import java.util.ArrayList;
import java.util.List;

import org.black_ixx.bossshop.core.rewards.BSRewardType;
import org.black_ixx.bossshop.core.rewards.BSRewardTypeNumber;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.inventory.ItemStack;

public class ISItem {

    private String path;
    private double worth, fix_sell, fix_buy;
    private ItemStack itemstack;
    private boolean allow_sell;
    private boolean allow_buy;
    private boolean allow_sellall;
    private boolean allow_buyall;
    private String permission;

    private List<String> itemdata;

    public ISItem(String path, double worth, double fix_buy, double fix_sell, List<String> itemdata, boolean allow_sell,
                  boolean allow_buy, boolean allow_sellall, boolean allow_buyall) {
        this(path,worth,fix_buy,fix_sell,itemdata,allow_sell,allow_buy,allow_sellall,allow_buyall,"");
    }

    public ISItem(String path, double worth, double fix_buy, double fix_sell, List<String> itemdata, boolean allow_sell,
                  boolean allow_buy, boolean allow_sellall, boolean allow_buyall, String permission) {
        this.path = path;
        this.worth = worth;
        this.fix_buy = fix_buy;
        this.fix_sell = fix_sell;
        this.itemdata = itemdata;
        this.allow_sell = allow_sell;
        this.allow_buy = allow_buy;
        this.allow_sellall = allow_sellall;
        this.allow_buyall = allow_buyall;
        this.permission = permission;
        this.itemstack = ClassManager.manager.getItemStackCreator().createItemStack(itemdata, false);
    }


    public String getPath() {
        return path;
    }

    /*
     * Note: There are two kinds of amounts: 1. Server owners can give items in the config an amount 2. In advanced shops items get a certain amount
     * Advanced shops need to set "receive worth of one unit" to true and include an own amount, while simple shops set amount to 1 because they get the predefined amount.
     */
    public Number getWorth(boolean type_buy, double factor, int amount, BSRewardType type, boolean receive_worth_of_one_unit, boolean worth_is_for_one_unit) {
        double d = worth * factor;

        if (type_buy && fix_buy > 0) { // Got fix values? Replace by them
            d = fix_buy;
        } else if (!type_buy && fix_sell > 0) {
            d = fix_sell;
        }

        d *= amount;

        if (receive_worth_of_one_unit & !worth_is_for_one_unit) {
            d /= itemstack.getAmount(); // Can happen in advanced shop
        } else if (!receive_worth_of_one_unit && worth_is_for_one_unit) {
            d *= itemstack.getAmount(); // Can happen in simple shop
        }

        if (type instanceof BSRewardTypeNumber) {
            BSRewardTypeNumber n = (BSRewardTypeNumber) type;
            if (n.isIntegerValue()) {
                return (int) d;
            }
        }
        return d;
    }

    @Deprecated
    public double getWorth() {
        return worth;
    }


    public List<String> getItemData() {
        return itemdata;
    }

    public ItemStack getItemStack() {
        return itemstack;
    }

    public boolean allowSell() {
        return allow_sell;
    }

    public boolean allowBuy() {
        return allow_buy;
    }

    public boolean allowSellAll() {
        return allow_sellall && allow_sell;
    }

    public boolean allowBuyAll() {
        return allow_buyall && allow_buy;
    }

    public String getPermission() {
        return permission;
    }

    public List<ItemStack> getItemList(int amount) {
        ArrayList<ItemStack> list = new ArrayList<ItemStack>();
        ItemStack item = itemstack.clone();
        item.setAmount(amount);
        list.add(item);
        return list;
    }

}
