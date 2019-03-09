package org.black_ixx.bossshop.addon.itemshops;

import java.util.ArrayList;
import java.util.List;

import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.misc.Misc;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class ItemInfo {


    private List<String> menuitem;
    private String[] messages;


    public ItemInfo(List<String> menuitem, String message) {
        this(menuitem, new String[]{message});
    }

    public ItemInfo(List<String> menuitem, String[] messages) {
        this.menuitem = menuitem;
        this.messages = messages;
    }


    public ItemInfo(ConfigurationSection section) {
        if (section != null) {
            this.menuitem = section.getStringList("MenuItem");
            this.messages = new String[]{section.getString("Message")};
        } else {
            this.menuitem = new ArrayList<String>();
            this.messages = new String[]{"message not found"};
        }
    }

    public ItemInfo(ConfigurationSection section, String[] message_paths) {
        this.menuitem = section.getStringList("MenuItem");
        this.messages = new String[message_paths.length];
        for (int i = 0; i < message_paths.length; i++) {
            messages[i] = section.getString(message_paths[i]);
        }
    }


    public ItemStack getMenuItem(List<String> itemdata, ItemStack itemstack, int amount) {
        List<String> new_list = new ArrayList<String>();
        if (itemdata != null) {
            for (String entry : itemdata) {
                new_list.add(transformEntry(entry, itemstack, amount));
            }
        }
        if (menuitem != null) {
            for (String entry : menuitem) {
                new_list.add(transformEntry(entry, itemstack, amount));
            }
        }
        return ClassManager.manager.getItemStackCreator().createItemStack(new_list, false);
    }

    @SuppressWarnings("deprecation")
    public String transformEntry(String entry, ItemStack itemstack, int amount) {
        entry = entry.replace("%amount%", String.valueOf(amount));
        if (itemstack != null) {
            entry = entry.replace("%type%", ClassManager.manager.getItemStackTranslator().readMaterial(itemstack));
            entry = entry.replace("%durability%", String.valueOf(itemstack.getDurability()));
        }
        return entry;
    }

    public String getMessage() {
        return getMessage(0);
    }

    public String getMessage(int id) {
        return messages[id];
    }

}
