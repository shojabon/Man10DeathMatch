package com.shojabon.man10deathmatch.states.in_game.logic

import com.shojabon.man10deathmatch.data_class.DeathMatchGame
import com.shojabon.man10deathmatch.events.Man10DeathMatchPlayerKillEvent
import com.shojabon.man10deathmatch.states.in_game.InGameState
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.*
import kotlin.collections.HashMap

class InGameViewLogic(private val state: InGameState) : Listener {
    var game: DeathMatchGame? = state.game

//    @EventHandler
//    fun onKill(e: Man10DeathMatchPlayerKillEvent) {
//        Bukkit.broadcast(Component.text("§e§l" + e.killer.playerName + "§a§lは§e§l" + e.killed.playerName + "§a§lを殺した"))
//    }
}