package dev.loveeev.discordzimoxy2.command;

import okhttp3.*;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;

public class AdminBanCommand implements CommandExecutor {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final OkHttpClient client = new OkHttpClient();
    private final String webhookUrl;

    public AdminBanCommand(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ban")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length >= 2) {
                    String username = args[0];
                    String reason = String.join(" ", args).substring(args[0].length() + 1);
                    String adminName = player.getName();
                    if(args[0] != player.getName()) {
                        Player target = Bukkit.getPlayer(username);
                        if (target != null) {
                            Bukkit.getServer().getBanList(BanList.Type.NAME).addBan(target.getName(), reason, null, sender.getName());
                            target.kickPlayer("Вы были забанены администратором. Причина: " + reason);
                        }

                        // Создаем JSON-объект для вебхука
                        JSONObject json = new JSONObject();
                        json.put("content", "Игрок забанен:");

                        JSONArray embeds = new JSONArray();

                        JSONObject embed = new JSONObject();
                        embed.put("title", "Игрок забанен:");
                        embed.put("description", "Ник: " + username + "\nПричина: " + reason + "\nНик админа: " + adminName);

                        embeds.add(embed);
                        json.put("embeds", embeds);

                        sendDiscordWebhook(json);

                        return true;
                    }else {
                        player.sendMessage("Вы не можете заблокировать сами себя.");
                    }
                } else {
                    player.sendMessage("Неверное использование команды! Используйте: /ban ник причина");
                    return false;
                }
            } else {
                sender.sendMessage("Эту команду могут использовать только игроки!");
                return false;
            }
        }
        return false;
    }

    private void sendDiscordWebhook(JSONObject json) {
        RequestBody body = RequestBody.create(JSON, json.toJSONString());
        Request request = new Request.Builder()
                .url(webhookUrl)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println("Ошибка отправки сообщения в Discord: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
