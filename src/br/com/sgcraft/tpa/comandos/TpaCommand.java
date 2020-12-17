package br.com.sgcraft.tpa.comandos;

import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.sgcraft.tpa.Main;

public class TpaCommand implements CommandExecutor {

	private Main plugin;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Player p = (Player) sender;
		plugin = Main.getPlugin();

		if (!(sender instanceof Player)) {
			System.out.println("Apenas jogadores podem executar esse comando!");
			return false;
		}
		if (cmd.getName().equalsIgnoreCase("tpa")) {
			if (args.length != 1) {
				p.sendMessage("�cUso incorreto! Utilize /tpa <jogador>.");
				return false;
			}

			Player target = Bukkit.getPlayerExact(args[0]);

			if (target == null) {
				p.sendMessage("�cO jogador " + args[0] + " n�o est� online!");
				return false;
			}

			if (p == target) {
				p.sendMessage("�cVoc� n�o pode enviar pedidos de teleporte para si mesmo!");
				return false;
			}

			if (plugin.teleporte_pendente.containsKey(p.getName())
					|| plugin.teleporte_expirar.containsKey(p.getName())) {
				p.sendMessage("�cVoc� j� pediu um pedido de teleporte, aguarde um pouco!");
				return false;
			}

			if (plugin.teleporte_pendente.containsValue(target.getName())
					|| plugin.teleporte_expirar.containsValue(target.getName())
					|| plugin.teleporte_pendente.containsKey(target.getName())
					|| plugin.teleporte_expirar.containsKey(target.getName())) {
				p.sendMessage("�cEsse jogador j� tem um pedido de teleporte, aguarde um pouco!");
				return false;
			}

			if (!plugin.msgCooldown(p)) {
				if (plugin.tptoggle.contains(p.getName())) {
					p.sendMessage("�cVoc� n�o pode enviar pedidos de teleporte! Use /tptoggle.");
					return false;
				}
				if (plugin.tptoggle.contains(target.getName()) && !p.hasPermission("sgcraft.staff")) {
					p.sendMessage(
							"�cVoc� n�o pode enviar pedidos de teleporte para esse jogador, pois ele est� com teleporte desativado!");
					return false;
				}
				p.sendMessage("�ePedido de teleporte enviado para o jogador �7" + target.getName() + "�e.");
				target.sendMessage("");
				target.sendMessage("�7" + p.getName() + " �eest� pedindo para ir at� voc�.");
				plugin.sendJSONMsg(target, "�ePara aceitar este pedido clique ", "�a�lAQUI", "�e.",
						"�aClique aqui para aceitar!", "/tpaceitar " + p.getName());
				plugin.sendJSONMsg(target, "�ePara negar este pedido clique ", "�4�lAQUI", "�e.",
						"�cClique aqui para negar!", "/tpnegar " + p.getName());
				target.sendMessage("");

				if (p.hasPermission("sgcraft.tpa.cooldown"))
					plugin.cooldown.put(p.getName(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(15));

				plugin.teleporte_pendente.put(p.getName(), target.getName());
				plugin.teleporte_expirar.put(p.getName(), target.getName());
				Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
					@Override
					public void run() {
						if (plugin.teleporte_expirar.containsKey(p.getName())) {
							p.sendMessage("�cO seu pedido de teleporte expirou.");
							target.sendMessage("�cO pedido de teleporte do jogador " + p.getName() + " expirou.");
							plugin.teleporte_expirar.remove(p.getName());
							if (plugin.teleporte_pendente.containsKey(p.getName()))
								plugin.teleporte_pendente.remove(p.getName());
						}
					}
				}, 20L * 30);
				return true;
			}

		}
		return false;
	}
}
