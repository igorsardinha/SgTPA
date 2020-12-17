package br.com.sgcraft.tpa.comandos;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.sgcraft.tpa.Main;

public class OtherTpaCommands implements CommandExecutor {

	private Main plugin;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player p = (Player) sender;
		plugin = Main.getPlugin();
		
		if(!(sender instanceof Player)) {
			System.out.println("Apenas jogadores podem executar esse comando!");
			return false;
		}
		
		if(cmd.getName().equalsIgnoreCase("tptoggle")) {
			if(!plugin.tptoggle.contains(p.getName())) {
				plugin.tptoggle.add(p.getName());
				p.sendMessage("�cVoc� �lDESATIVOU �co seu teleporte!");
				p.sendMessage("�eAgora, outros jogadores n�o conseguir�o fazer um pedido de teleporte para voc�.");
				return true;
			} else {
				plugin.tptoggle.remove(p.getName());
				p.sendMessage("�aVoc� �aATIVOU �ao seu teleporte!");
				p.sendMessage("�eAgora, outros jogadores conseguir�o fazer um pedido de teleporte para voc�.");
				return true;
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("tpcancelar")) {
			if(!plugin.teleportando.contains(p.getName()) && !plugin.teleporte_pendente.containsKey(p.getName()) && !plugin.teleporte_expirar.containsKey(p.getName())) {
				p.sendMessage("�cVoc� n�o tem pedidos de teleporte recentes.");
				return false;
			}
			if(plugin.teleportando.contains(p.getName())) 
				plugin.teleportando.remove(p.getName());
			if(plugin.teleporte_pendente.containsKey(p.getName()))
				plugin.teleporte_pendente.remove(p.getName());
			if(plugin.teleporte_expirar.containsKey(p.getName()))
				plugin.teleporte_expirar.remove(p.getName());
			p.sendMessage("�cVoc� cancelou o seu teleporte.");
			plugin.sendTitle(p, "�6Teleporte", "�fcancelado!");
			return true;
		}
		return false;
	}

}
