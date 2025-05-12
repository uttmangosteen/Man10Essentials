package io.github.uttmangosteen.man10Essentials.Commands;

import io.github.uttmangosteen.man10Essentials.Global;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.bukkit.Bukkit;

public class Ohatsukit_C implements CommandExecutor {
    private final JavaPlugin plugin;

    public Ohatsukit_C(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    // ItemStack[] Base64 変換関数
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
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!sender.hasPermission("red.man10.ohatsukit")) return true;
        switch (args.length) {
            case 1:
                switch (args[0]) {
                    case "on":
                        Global.enabled_give_ohatsukit = true;
                        plugin.getConfig().set("ohatsukit.mode", true);
                        plugin.saveConfig();
                        sender.sendMessage(Global.prefix + "§a初期装備を配布します§r");
                        return true;
                    case "off":
                        Global.enabled_give_ohatsukit = false;
                        plugin.getConfig().set("ohatsukit.mode", false);
                        plugin.saveConfig();
                        sender.sendMessage(Global.prefix + "§c初期装備は配られません§r");
                        return true;
                    case "register":
                        if (!(sender instanceof Player player)) {
                            sender.sendMessage(Global.prefix + "§cこのコマンドはプレイヤーのみ実行できます§r");
                            return true;
                        }
                        PlayerInventory inv = player.getInventory();
                        String inventoryBase64 = itemStackArrayToBase64(inv.getContents());
                        plugin.getConfig().set("ohatsukit.inv", inventoryBase64);
                        plugin.saveConfig();
                        sender.sendMessage(Global.prefix + "§a現在の装備と所持品を登録しました§r");
                        return true;
                    default:
                        return false;
                }
            case 2:
                if (!args[0].equalsIgnoreCase("give")) return false;
                String savedInv = plugin.getConfig().getString("ohatsukit.inv");
                if (savedInv == null) {
                    sender.sendMessage(Global.prefix + "§ckitが登録されていません§r");
                    return true;
                }
                Player targetPlayer = Bukkit.getPlayer(args[1]);
                if (targetPlayer == null) {
                    sender.sendMessage(Global.prefix + "§cプレイヤーが見つかりません§r");
                    return true;
                }
                ItemStack[] inv = itemStackArrayFromBase64(savedInv);
                ItemStack[] targetInv = targetPlayer.getInventory().getContents();
                if (inv != null) {

                    //同じ場所が空いていたらできるだけ同じ場所へ
                    for (int i = 0; i < inv.length; i++) {
                        if (inv[i] != null && targetInv[i] == null) {
                            targetInv[i] = inv[i];
                            inv[i] = null;
                        }
                    }
                    targetPlayer.getInventory().setContents(targetInv);
                    // 残りのアイテムを空いているスロットに追加
                    for (ItemStack invItem : inv) {
                        if (invItem != null) {
                            if (targetPlayer.getInventory().firstEmpty() != -1) {
                                targetPlayer.getInventory().addItem(invItem);
                                continue;
                            }
                            targetPlayer.sendMessage(Global.prefix + "§c" + targetPlayer.getName() + "のインベントリに空きがなかったため一部のアイテムは消えました§r");
                            break;
                        }
                    }
                }

                sender.sendMessage(Global.prefix + "§a" + targetPlayer.getName() + "に初期装備を付与しました§r");
                return true;
        }
        return false;
    }
}
