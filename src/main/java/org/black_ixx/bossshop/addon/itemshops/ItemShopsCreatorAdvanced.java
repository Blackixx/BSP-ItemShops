package org.black_ixx.bossshop.addon.itemshops;


import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.BSShops;
import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.core.rewards.BSRewardType;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

public class ItemShopsCreatorAdvanced {


	private String look_adv_subshop_displayname;

	private ItemInfo preview, buy, sell, sellall, buyall, back, close;


	public ItemShopsCreatorAdvanced(FileConfiguration c){
		ConfigurationSection s_adv_subshop = c.getConfigurationSection("ShopItemLookAdvanced.SubShop");
		look_adv_subshop_displayname = s_adv_subshop.getString("Displayname");

		preview = new ItemInfo(c.getConfigurationSection("ShopItemLookAdvanced.Preview"));
		buy = new ItemInfo(c.getConfigurationSection("ShopItemLookAdvanced.Buy"));
		sell = new ItemInfo(c.getConfigurationSection("ShopItemLookAdvanced.Sell"));
		sellall = new ItemInfo(c.getConfigurationSection("ShopItemLookAdvanced.SellAll"));
		buyall = new ItemInfo(c.getConfigurationSection("ShopItemLookAdvanced.BuyAll"));
		back = new ItemInfo(c.getConfigurationSection("ShopItemLookAdvanced.Back"));
		close = new ItemInfo(c.getConfigurationSection("ShopItemLookAdvanced.Close"));
	}




	public BSBuy createBuyItem(BSShops shophandler, BSShop shop, ISItem item, BossShop plugin, BSRewardType rewardtype, BSPriceType pricetype, double reward_multiplier, double price_multiplier, boolean worth_is_for_one_unit){
		String shop_name = "itemshop_advanced_"+item.getPath().toLowerCase();
		BSBuy buy = new BSBuy(BSRewardType.Shop, BSPriceType.Nothing, shop_name, null, preview.getMessage(), -1, null, item.getPath());

		buy.setItem(preview.getMenuItem(item.getItemData(), item.getItemStack(), 1), false);

		shophandler.addShop(createSubShop(shophandler, shop, buy, item, shop_name, plugin, rewardtype, pricetype, reward_multiplier, price_multiplier, worth_is_for_one_unit));
		return buy;
	}


	public BSShop createSubShop(BSShops shophandler, BSShop shop, BSBuy buy, ISItem item, String shop_name, BossShop plugin, BSRewardType rewardtype, BSPriceType pricetype, double reward_multiplier, double price_multiplier, boolean worth_is_for_one_unit){
		BSShop individual_shop = new BSShop(shophandler.createId(), shop_name, null, true, plugin, ClassManager.manager.getStringManager().transform(preview.transformEntry(look_adv_subshop_displayname.replace("%parentshopname%", shop.getValidDisplayName(null, null)), item.getItemStack(), item.getItemStack().getAmount()), buy, shop, null, null), 0, null) {

			@Override
			public void reloadShop() {
			}			
		};


		int levels[] = null;

		switch(item.getItemStack().getMaxStackSize()){
		case 1:
			levels = new int[]{1};
			break;
		case 8:
			levels = new int[]{1, 8};
			break;
		case 16:
			levels = new int[]{1, 4, 16};
			break;
		case 32:
			levels = new int[]{1, 8, 32};
			break;
		case 64:
			levels = new int[]{1, 8, 64};
			break;
		}


		if(levels != null){

			for(int level = 0; level < levels.length; level++){
				int amount = levels[level];
				int first_slot = level*9 + 3;

				addBuyItemPreview(individual_shop, item, first_slot+1, amount);

				if(item.allowBuy()){
					addBuyItemBuy(individual_shop, pricetype, rewardtype, item, price_multiplier, first_slot, amount, worth_is_for_one_unit);
				}else if(item.allowSell()){
					addBuyItemSell(individual_shop, rewardtype, item, reward_multiplier, first_slot, amount, worth_is_for_one_unit);
				}

				if(item.allowSell()){
					addBuyItemSell(individual_shop, rewardtype, item, reward_multiplier, first_slot+2, amount, worth_is_for_one_unit);
				} else if (item.allowBuy()){
					addBuyItemBuy(individual_shop, pricetype, rewardtype, item, price_multiplier, first_slot+2, amount, worth_is_for_one_unit);
				}
			}

		}


		if(item.allowBuyAll() && item.allowSellAll()){
			addBuyItemBuyAll(individual_shop, pricetype, rewardtype, item, price_multiplier, (levels.length)*9 + 3, worth_is_for_one_unit);
			addBuyItemSellAll(individual_shop, rewardtype, item, reward_multiplier, (levels.length)*9 + 5, worth_is_for_one_unit);
		}else{

			if(item.allowSellAll()){
				addBuyItemSellAll(individual_shop, rewardtype, item, reward_multiplier, (levels.length)*9 + 4, worth_is_for_one_unit);
			}else if(item.allowBuyAll()){
				addBuyItemBuyAll(individual_shop, pricetype, rewardtype, item, price_multiplier, (levels.length)*9 + 4, worth_is_for_one_unit);
			}

		}

		addBuyItemBack(individual_shop, shop, (levels.length+1)*9);
		addBuyItemClose(individual_shop, (levels.length+2)*9 - 1);

		individual_shop.finishedAddingItems();

		return individual_shop;
	}

	private BSBuy addBuyItemBuy(BSShop individual_shop, BSPriceType pricetype, BSRewardType rewardtype, ISItem item, double price_multiplier, int slot, int amount, boolean worth_is_for_one_unit){
		BSBuy shopitem = new BSBuy(BSRewardType.Item, pricetype, item.getItemList(amount), item.getWorth(true, price_multiplier, amount, rewardtype, true, worth_is_for_one_unit) , buy.getMessage(), slot, null, item.getPath()+"-buy-"+amount);
		shopitem.setShop(individual_shop);
		individual_shop.addShopItem(shopitem, buy.getMenuItem(null, null, amount), ClassManager.manager);
		return shopitem;
	}

	private BSBuy addBuyItemBuyAll(BSShop individual_shop, BSPriceType pricetype, BSRewardType rewardtype, ISItem item, double price_multiplier, int slot, boolean worth_is_for_one_unit){
		ItemStack i = item.getItemStack().clone();
		i.setAmount(1);
		BSBuy shopitem = new BSBuy(BSRewardType.ItemAll, pricetype, i, item.getWorth(true, price_multiplier, 1, rewardtype, true, worth_is_for_one_unit), buyall.getMessage(), slot, null, item.getPath()+"-buyall");
		shopitem.setShop(individual_shop);
		individual_shop.addShopItem(shopitem, buyall.getMenuItem(null, null, 1), ClassManager.manager);
		return shopitem;
	}

	private BSBuy addBuyItemSell(BSShop individual_shop, BSRewardType rewardtype, ISItem item, double reward_multiplier, int slot, int amount, boolean worth_is_for_one_unit){
		BSBuy shopitem = new BSBuy(rewardtype, BSPriceType.Item, item.getWorth(false, reward_multiplier, amount, rewardtype, true, worth_is_for_one_unit) , item.getItemList(amount), sell.getMessage(), slot, null, item.getPath()+"-sell-"+amount);
		shopitem.setShop(individual_shop);
		individual_shop.addShopItem(shopitem, sell.getMenuItem(null, null, amount), ClassManager.manager);
		return shopitem;
	}

	private BSBuy addBuyItemSellAll(BSShop individual_shop, BSRewardType rewardtype, ISItem item, double reward_multiplier, int slot, boolean worth_is_for_one_unit){
		ItemStack i = item.getItemStack().clone();
		i.setAmount(1);
		BSBuy shopitem = new BSBuy(rewardtype, BSPriceType.ItemAll, item.getWorth(false, reward_multiplier, 1, rewardtype, true, worth_is_for_one_unit) , i, sellall.getMessage(), slot, null, item.getPath()+"-sellall");
		shopitem.setShop(individual_shop);
		individual_shop.addShopItem(shopitem, sellall.getMenuItem(null, null, 1), ClassManager.manager);
		return shopitem;
	}

	private BSBuy addBuyItemPreview(BSShop individual_shop, ISItem item, int slot, int amount){
		BSBuy shopitem = new BSBuy(BSRewardType.Nothing, BSPriceType.Nothing, null, null, preview.getMessage(), slot, null, item.getPath()+"-preview-"+amount);
		shopitem.setShop(individual_shop);
		individual_shop.addShopItem(shopitem, preview.getMenuItem(item.getItemData(), item.getItemStack(), amount), ClassManager.manager);
		return shopitem;
	}

	private BSBuy addBuyItemClose(BSShop individual_shop, int slot){
		BSBuy shopitem = new BSBuy(BSRewardType.Close, BSPriceType.Nothing, null, null, close.getMessage(), slot, null, "close");
		shopitem.setShop(individual_shop);
		individual_shop.addShopItem(shopitem, close.getMenuItem(null, null, 1), ClassManager.manager);
		return shopitem;
	}

	private BSBuy addBuyItemBack(BSShop individual_shop, BSShop main_shop, int slot){
		BSBuy shopitem = new BSBuy(BSRewardType.Shop, BSPriceType.Nothing, main_shop.getShopName().toLowerCase(), null, back.getMessage(), slot, null, "back");
		shopitem.setShop(individual_shop);
		individual_shop.addShopItem(shopitem, back.getMenuItem(null, null, 1), ClassManager.manager);
		return shopitem;
	}

}
