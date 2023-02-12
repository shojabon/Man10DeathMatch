package com.shojabon.man10deathmatch.data_class

import com.shojabon.man10deathmatch.Man10DeathMatch
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class DeathMatchPlayer(private val p: Player) {
    var playerUUID: UUID = p.uniqueId
    var playerName: String = p.name

    fun executeEquipment(){
        if(Man10DeathMatch.currentGame == null) return
        val config = Man10DeathMatch.currentGame?.currentMapConfig ?: return
        val equipmentCommands = config.getStringList("equipments")
        if(equipmentCommands.size == 0) return
        equipmentCommands.shuffle()
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().consoleSender, equipmentCommands[0])
    }

}