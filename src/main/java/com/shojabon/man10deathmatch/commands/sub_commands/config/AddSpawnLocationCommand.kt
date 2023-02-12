package com.shojabon.man10deathmatch.commands.sub_commands.config

import com.shojabon.man10deathmatch.Man10DeathMatch
import com.shojabon.mcutils.Utils.SConfigFile
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File
import java.io.IOException

class AddSpawnLocationCommand(val plugin: Man10DeathMatch) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val filePath: String = plugin.dataFolder.toString() + File.separator + "maps" + File.separator + args[2] + ".yml"
        val config: YamlConfiguration? = SConfigFile.getConfigFile(filePath)
        if (config == null) {
            sender.sendMessage(Man10DeathMatch.prefix + "§c§lゲームが存在しません")
            return false
        }
        var currentInitialSpawnLocation = config.getList("spawnLocations", ArrayList<Location>()) as List<Location>
        currentInitialSpawnLocation = ArrayList(currentInitialSpawnLocation)
        currentInitialSpawnLocation.add((sender as Player).location)
        config.set("spawnLocations", currentInitialSpawnLocation)
        try {
            config.save(File(filePath))
        } catch (e: IOException) {
            sender.sendMessage(Man10DeathMatch.prefix + "§c§l保存時に問題が発生しました")
        }
        sender.sendMessage(Man10DeathMatch.prefix + "§a§lロケーションを保存しました")
        return true
    }
}