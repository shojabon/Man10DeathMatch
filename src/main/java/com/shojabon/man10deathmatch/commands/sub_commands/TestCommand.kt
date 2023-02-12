package com.shojabon.man10deathmatch.commands.sub_commands

import com.shojabon.man10deathmatch.Man10DeathMatch
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class TestCommand(var plugin: Man10DeathMatch) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        return true
    }
}