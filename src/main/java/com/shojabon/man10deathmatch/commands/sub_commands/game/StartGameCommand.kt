package com.shojabon.man10deathmatch.commands.sub_commands.game

import com.shojabon.man10deathmatch.Man10DeathMatch
import com.shojabon.man10deathmatch.enums.DeathMatchState
import com.shojabon.mcutils.Utils.BaseUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class StartGameCommand(private val plugin: Man10DeathMatch) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        Man10DeathMatch.currentGame.setGameState(DeathMatchState.LOBBY)
        sender.sendMessage(Man10DeathMatch.prefix + "§a§l試合をキャンセルしました")
        return true
    }
}