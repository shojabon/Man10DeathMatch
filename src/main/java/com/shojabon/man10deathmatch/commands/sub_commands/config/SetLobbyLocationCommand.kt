package com.shojabon.man10deathmatch.commands.sub_commands.config

import com.shojabon.man10deathmatch.Man10DeathMatch
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SetLobbyLocationCommand(private val plugin: Man10DeathMatch) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        plugin.config.set("lobbyLocation", (sender as Player).location)
        plugin.saveConfig()
        sender.sendMessage(Man10DeathMatch.prefix +  "ロケーションを保存しました")
        return true
    }
}