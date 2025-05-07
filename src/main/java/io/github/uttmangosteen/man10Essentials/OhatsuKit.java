package io.github.uttmangosteen.man10Essentials;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.bukkit.util.io.BukkitObjectInputStream;

public class OhatsuKit implements CommandExecutor {
    private final JavaPlugin plugin;
    public OhatsuKit(JavaPlugin plugin) {this.plugin = plugin;}

    // ItemStack[] をBase64で保存・復元するユーティリティ
    private String itemStackArrayToBase64(ItemStack[] items) {
        try {
            java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(items.length);
            for (ItemStack item : items) dataOutput.writeObject(item);
            dataOutput.close();
            return java.util.Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private ItemStack[] itemStackArrayFromBase64(String data) {
        try {
            byte[] bytes = java.util.Base64.getDecoder().decode(data);
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(new java.io.ByteArrayInputStream(bytes));
            int length = dataInput.readInt();
            ItemStack[] items = new ItemStack[length];
            for (int i = 0; i < length; i++) items[i] = (ItemStack) dataInput.readObject();
            dataInput.close();
            return items;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (!sender.hasPermission("red.man10.ohatsukit")) return true;
        if (args.length == 0) return false;
        if (args[0].equalsIgnoreCase("register")) {
            if (!(sender instanceof Player)) return true;
            Player player = (Player) sender;

            String invBase64 = itemStackArrayToBase64(player.getInventory().getContents());
            String armorBase64 = itemStackArrayToBase64(player.getInventory().getArmorContents());
            plugin.getConfig().set("ohatsukit.inventory", invBase64);
            plugin.getConfig().set("ohatsukit.armor", armorBase64);
            plugin.saveConfig();
            player.sendMessage("§a現在のインベントリと装備を登録しました。");
            return true;
        }
        if (args[0].equalsIgnoreCase("set")) {
            Player target = Bukkit.getPlayerExact(args[1]);
            if (target == null) {
                sender.sendMessage("§c指定したプレイヤーが見つかりません");
                return true;
            }
            String invBase64 = plugin.getConfig().getString("ohatsukit.inventory");
            String armorBase64 = plugin.getConfig().getString("ohatsukit.armor");
            if (invBase64 == null || armorBase64 == null) {
                sender.sendMessage("§cインベントリが登録されていません");
                return true;
            }
            ItemStack[] kitInv = itemStackArrayFromBase64(invBase64);
            ItemStack[] kitArmor = itemStackArrayFromBase64(armorBase64);
            if (kitInv == null || kitArmor == null) {
                sender.sendMessage("§cインベントリの読み込みに失敗しました");
                return true;
            }
            target.getInventory().setContents(kitInv);
            target.getInventory().setArmorContents(kitArmor);
            sender.sendMessage("§a" + target.getName() + "のインベントリを上書きしました。");
            target.sendMessage("§e§l国王より最初の装備を下賜された！");
            return true;
        }
        return true;
    }
}


