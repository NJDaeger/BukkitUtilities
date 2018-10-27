package com.njdaeger.butil;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ItemBuilder {

    private ItemStack stack;

    private ItemBuilder(Material material) {
        this.stack = new ItemStack(material);

    }

    public static ItemBuilder of(Material material) {
        return new ItemBuilder(material);
    }

    public ItemBuilder amount(int amount) {
        stack.setAmount(amount);
        return this;
    }

    public ItemBuilder displayName(String displayname) {
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(displayname);
        stack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder localizedName(String localizedName) {
        ItemMeta meta = stack.getItemMeta();
        meta.setLocalizedName(localizedName);
        stack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(String... lore) {
        return lore(Stream.of(lore).map(line -> ChatColor.WHITE + line).collect(Collectors.toList()));
    }

    public ItemBuilder lore(List<String> lore) {
        ItemMeta meta = stack.getItemMeta();
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder itemFlags(ItemFlag... flags) {
        ItemMeta meta = stack.getItemMeta();
        meta.addItemFlags(flags);
        stack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        stack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder unbreakable() {
        ItemMeta meta = stack.getItemMeta();
        meta.setUnbreakable(true);
        stack.setItemMeta(meta);
        return this;
    }

    public ItemStack build() {
        return stack;
    }

}
