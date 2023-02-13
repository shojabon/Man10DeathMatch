package com.shojabon.man10deathmatch.data_class

import com.shojabon.man10deathmatch.Man10DeathMatch
import org.bukkit.Bukkit
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import java.util.*

class DeathMatchPlayer(private val p: Player) {
    var playerUUID: UUID = p.uniqueId
    var playerName: String = p.name

    var lastDamagePlayer: DeathMatchPlayer? = null
    var lastDamageTime: Long = 0

    fun executeEquipment(){
        if(Man10DeathMatch.currentGame == null) return
        val config = Man10DeathMatch.currentGame.currentMapConfig ?: return
        val equipmentCommands = config.getStringList("equipments")
        if(equipmentCommands.size == 0) return
        equipmentCommands.shuffle()
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().consoleSender, equipmentCommands[0])
    }

    fun getPlayer(): Player?{
        return Bukkit.getPlayer(playerUUID)
    }

    fun executeKillStreak(streak: Int) {
        val killStreaks: ConfigurationSection = Man10DeathMatch.currentGame.currentMapConfig?.getConfigurationSection("commands.killStreak")
                ?: return
        for (commandForKillStreak in killStreaks.getKeys(false)) {
            if (!commandForKillStreak.equals(streak.toString(), ignoreCase = true)) continue
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().consoleSender, killStreaks.getString(commandForKillStreak)
                    ?.replace("{player}", playerName) ?: "")
        }
    }

    fun giveRandomEquipment(){
        val equipmentCommands: MutableList<String> = Man10DeathMatch.currentGame.currentMapConfig?.getStringList("equipments") ?: return
        equipmentCommands.shuffle()
        if(equipmentCommands.size == 0) return
        val commandToExecute = equipmentCommands[0] ?: return
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().consoleSender, commandToExecute.replace("{player}", playerName))
    }

}