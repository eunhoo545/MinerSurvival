package org.example.code.rpg.Event;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.code.rpg.Manager.GuiManager;
import org.example.code.rpg.Manager.MoneyManager;
import org.example.code.rpg.RPG;

import java.util.Collections;

public class InventoryClickListener implements Listener {
    private RPG plugin;
    private GuiManager guiManager;
    private MoneyManager moneyManager;

    public InventoryClickListener(RPG plugin, GuiManager guiManager, MoneyManager moneyManager) {
        this.plugin = plugin;
        this.guiManager = guiManager;
        this.moneyManager = moneyManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String inventoryTitle = event.getView().getTitle();
        if (!inventoryTitle.equals("메뉴") && !inventoryTitle.equals("전직 상점")) return;

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            return;
        }

        if (clickedItem.getType() == Material.ENCHANTED_BOOK && clickedItem.hasItemMeta()) {
            String displayName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
            if (displayName.equals("전직")) {
                guiManager.jobShop(player);
            } else if (displayName.equals("[전직] 광부 1차")) {
                double cost = 10000.0;
                if (moneyManager.getBalance(player) >= cost) {
                    moneyManager.subtractBalance(player, cost);
                    player.sendMessage(ChatColor.GREEN + "성공적으로 구매 완료했습니다!");

                    ItemStack customItem = createCustomItem("[전직] 광부 1차", ChatColor.GOLD + "광부 1차로 전직합니다.");
                    addCustomItemToPlayer(player, customItem);
                } else {
                    player.sendMessage(ChatColor.RED + "잔액이 부족합니다.");
                }
            }
        } else if (clickedItem.getType() == Material.STONE) {
            player.sendMessage("You clicked on a Stone!");
        }
    }

    private ItemStack createCustomItem(String name, String lore) {
        ItemStack customItem = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = customItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            meta.setLore(Collections.singletonList(ChatColor.translateAlternateColorCodes('&', lore)));
            customItem.setItemMeta(meta);
        }
        return customItem;
    }

    private void addCustomItemToPlayer(Player player, ItemStack customItem) {
        if (player.getInventory().firstEmpty() != -1) { // 인벤토리에 공간이 있을 경우
            player.getInventory().addItem(customItem);
        } else { // 인벤토리에 공간이 없을 경우
            player.getWorld().dropItem(player.getLocation(), customItem);
            player.sendMessage(ChatColor.RED + "인벤토리가 가득 차서 아이템이 바닥에 떨어졌습니다!");
        }
    }
}
