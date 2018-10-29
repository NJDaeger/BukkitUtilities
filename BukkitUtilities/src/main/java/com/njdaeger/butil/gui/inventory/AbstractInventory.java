package com.njdaeger.butil.gui.inventory;

import com.njdaeger.butil.gui.IButton;
import com.njdaeger.butil.gui.IGui;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public abstract class AbstractInventory<T extends IGui<T>> implements IGui<T> {

    protected final Function<Player, String> title;
    protected final Plugin plugin;
    protected final int size;

    private final Map<UUID, Inventory> inventories;
    private final IButton<T, ?>[] slots;

    @SuppressWarnings("unchecked")
    public AbstractInventory(Plugin plugin, int size, Function<Player, String> title) {
        Validate.isTrue(size % 9 == 0, "Size must be a multiple of 9.");

        this.title = title;
        this.plugin = plugin;
        this.size = size;
        this.inventories = new HashMap<>();
        this.slots = new IButton[size];

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public AbstractInventory(Plugin plugin, int size, String title) {
        this(plugin, size, t -> title);
    }

    @Override
    public <S extends IButton<T, S>> T setItem(int index, S button) {
        button.setParentGui((T) this);
        this.slots[index] = button;
        return (T)this;
    }

    @Override
    public <S extends IButton<T, S>> T addItem(S button) {
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] == null) {
                button.setParentGui((T)this);
                this.slots[i] = button;
                break;
            }
        }
        return (T)this;
    }

    @Override
    public T removeItem(int slot) {
        this.slots[slot] = null;
        return (T)this;
    }

    @Override
    public T removeItems(int startIndex, int countToRemove) {
        for (int i = startIndex; i < startIndex + countToRemove; i++) {
            if (i >= size) break;
            this.slots[i].setParentGui(null);
            this.slots[i] = null;
        }
        return (T)this;
    }

    @Override
    public boolean isSlotOpen(int slot) {
        return slot > -1 && slot < size && slots[slot] != null;
    }

    @Override
    public int getSlotOf(IButton<?, ?> button) {
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] == button) return i;
        }
        return -1;
    }

    @Override
    public IButton<T, ?> getButton(int slot) {
        return isSlotOpen(slot) ? slots[slot] : null;
    }

    @Override
    public <S extends IButton<T, S>> IButton<T, S> getButton(Class<S> slotType, int slot) {
        return isSlotOpen(slot) ? (IButton<T, S>)slots[slot] : null;
    }

    @Override
    public void update(Player player) {
        Inventory inventory = inventories.computeIfAbsent(player.getUniqueId(), uuid -> getNewInventory(player));
        buildInventory(inventory);
        player.updateInventory();
    }

    @Override
    public void open(Player player) {
        Inventory inventory = inventories.computeIfAbsent(player.getUniqueId(), uuid -> getNewInventory(player));
        player.openInventory(inventory);
    }

    @Override
    public void closeAll() {
        Bukkit.getOnlinePlayers().stream().filter(p -> inventories.containsKey(p.getUniqueId())).forEach(HumanEntity::closeInventory);
        inventories.clear();
    }

    @Override
    public void destroy() {
        InventoryCloseEvent.getHandlerList().unregister(this);
        InventoryClickEvent.getHandlerList().unregister(this);
        InventoryDragEvent.getHandlerList().unregister(this);

        closeAll();
    }

    private Inventory getNewInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, size, title.apply(player));
        buildInventory(inventory);
        return inventory;
    }

    private void buildInventory(Inventory inventory) {
        inventory.clear();
        for (IButton<T, ?> slot : slots) {
            if (slot != null) {
                inventory.setItem(slot.getSlot(), slot.getStack());
            }
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (inventories.containsValue(event.getInventory()) && event.getWhoClicked() instanceof Player) {
            event.setResult(Event.Result.DENY);
            event.setCancelled(true);
            ((Player) event.getWhoClicked()).updateInventory();
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player) || event.getClickedInventory() == null) return;

        Player player = (Player) event.getWhoClicked();
        Inventory inventory = inventories.get(player.getUniqueId());
        if (inventory == null) return;

        if (inventory.equals(event.getClickedInventory())) {
            event.setCancelled(true);
            if (isSlotOpen(event.getSlot())) {
                slots[event.getSlot()].onClick(event);
            }
        } else if (inventories.containsValue(event.getView().getTopInventory())) {
            event.setResult(Event.Result.DENY);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player) || inventories.values().stream().noneMatch(inv -> event.getInventory().equals(inv))) return;
        inventories.remove(event.getPlayer().getUniqueId());
    }

}
