package org.black_ixx.bossshop.addon.itemshops;

import java.util.HashMap;
import java.util.Map;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.core.rewards.BSRewardType;
import org.black_ixx.bossshop.inbuiltaddons.advancedshops.ActionSet;
import org.black_ixx.bossshop.inbuiltaddons.advancedshops.BSBuyAdvanced;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class ItemShopsCreatorSimple {


	private ItemInfo simple;


	public ItemShopsCreatorSimple(FileConfiguration c){
		ConfigurationSection s_simple = c.getConfigurationSection("ShopItemLookSimple");
		simple = new ItemInfo(s_simple, new String[]{"MessageBuy", "MessageSell", "MessageSellAll"});
	}



	public BSBuy createBuyItem(BSShop shop, ISItem item, BossShop plugin, BSRewardType rewardtype, BSPriceType pricetype, double reward_multiplier, double price_multiplier, boolean worth_is_for_one_unit){
		BSRewardType buy_rewardtype = BSRewardType.Item;
		Object buy_reward = item.getItemList(item.getItemStack().getAmount());
		BSPriceType buy_pricetype = pricetype;
		Object buy_price = item.getWorth(true, price_multiplier, 1, rewardtype, false, worth_is_for_one_unit);

		Map<ClickType, ActionSet> actions = new HashMap<ClickType, ActionSet>();

		BSRewardType sell_rewardtype = rewardtype;
		Object sell_reward =  item.getWorth(false, reward_multiplier, 1, rewardtype, false, worth_is_for_one_unit);
		BSPriceType sell_pricetype = BSPriceType.Item;
		Object sell_price = item.getItemList(item.getItemStack().getAmount());
		actions.put(ClickType.RIGHT, new ActionSet(sell_rewardtype, sell_pricetype, sell_reward, sell_price, simple.getMessage(1), null, null, null));


		BSRewardType sellall_rewardtype = rewardtype;
		Object sellall_reward = item.getWorth(false, reward_multiplier, 1, rewardtype, true, worth_is_for_one_unit);
		BSPriceType sellall_pricetype = BSPriceType.ItemAll;
		ItemStack sellall_item = item.getItemStack().clone();
		sellall_item.setAmount(1);
		Object sellall_price = sellall_item;
		actions.put(ClickType.MIDDLE, new ActionSet(sellall_rewardtype, sellall_pricetype, sellall_reward, sellall_price, simple.getMessage(2), null, null, null));


		BSBuy buy = new BSBuyAdvanced(buy_rewardtype, buy_pricetype, buy_reward, buy_price, simple.getMessage(0), -1, null, item.getPath(), null, null, null, actions);

		buy.setItem(simple.getMenuItem(item.getItemData(), item.getItemStack(), item.getItemStack().getAmount()), false);

		return buy;
	}

}
