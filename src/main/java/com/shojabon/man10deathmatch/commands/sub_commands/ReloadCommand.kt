package com.shojabon.man10deathmatch.commands.sub_commands

import com.shojabon.man10deathmatch.Man10DeathMatch
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class ReloadCommand(val plugin: Man10DeathMatch) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        Man10DeathMatch.endGame()
        plugin.reloadConfig()
        Man10DeathMatch.config = plugin.config
        sender.sendMessage(Man10DeathMatch.prefix + "§a§lコンフィグをリロードしました")
        return true
    }
}