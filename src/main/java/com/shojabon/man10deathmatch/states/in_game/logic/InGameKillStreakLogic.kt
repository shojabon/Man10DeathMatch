package com.shojabon.man10deathmatch.states.in_game.logic

import com.shojabon.man10deathmatch.data_class.DeathMatchGame
import com.shojabon.man10deathmatch.events.Man10DeathMatchPlayerKillEvent
import com.shojabon.man10deathmatch.states.in_game.InGameState
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.*

class InGameKillStreakLogic(private val state: InGameState) : Listener {
    var game: DeathMatchGame? = state.game
    private var killStreakMap: HashMap<UUID, Int> = HashMap()

    fun getKillStreak(uuid: UUID?): Int {
        return if (!killStreakMap.containsKey(uuid)) {
            1
        } else killStreakMap[uuid]!!
    }
    @EventHandler
    fun onKill(e: Man10DeathMatchPlayerKillEvent) {
        if (!killStreakMap.containsKey(e.killer.playerUUID)) {
            killStreakMap[e.killer.playerUUID] = 0
        }
        killStreakMap.remove(e.killed.playerUUID)
        var current = killStreakMap[e.killer.playerUUID]!!
        current += 1
        e.killer.executeKillStreak(current)
        killStreakMap[e.killer.playerUUID] = current
    }
}