package com.shojabon.man10deathmatch.commands.sub_commands.game

import com.shojabon.man10deathmatch.Man10DeathMatch
import com.shojabon.mcutils.Utils.BaseUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class SetCurrentStateTimeCommand(private val plugin: Man10DeathMatch) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!BaseUtils.isInt(args[2])) {
            sender.sendMessage(Man10DeathMatch.prefix + "§c§l時間は数字でなくてはなりません")
            return true
        }
        if(Man10DeathMatch.currentGame.gameState == null){
            sender.sendMessage(Man10DeathMatch.prefix + "§c§l現在時間が操作できる状態ではありません")
            return true
        }
        Man10DeathMatch.currentGame.gameState?.timerTillNextState?.setRemainingTime(args[2].toInt())
        sender.sendMessage(Man10DeathMatch.prefix + "§a§l時間を設定しました")
        return true
    }
}