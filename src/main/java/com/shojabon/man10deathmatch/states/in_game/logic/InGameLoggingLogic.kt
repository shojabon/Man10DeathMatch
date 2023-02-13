package com.shojabon.man10deathmatch.states.in_game.logic

import com.shojabon.man10deathmatch.Man10DeathMatch
import com.shojabon.man10deathmatch.data_class.DeathMatchGame
import com.shojabon.man10deathmatch.events.Man10DeathMatchPlayerKillEvent
import com.shojabon.man10deathmatch.states.in_game.InGameState
import com.shojabon.mcutils.Utils.MySQL.MySQLAPI
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.*

class InGameLoggingLogic(private val state: InGameState) : Listener {
    var game: DeathMatchGame? = state.game

    @EventHandler
    fun onKill(e: Man10DeathMatchPlayerKillEvent) {
        val payload = HashMap<String, Any>()
        payload["killer_mcid"] = e.killer.playerName
        payload["killer_uuid"] = e.killer.playerUUID
        payload["killed_mcid"] = e.killed.playerName
        payload["killed_uuid"] = e.killed.playerUUID
        payload["game_id"] = game?.gameId.toString()
        payload["kill_streak"] = state.killSteakLogic.getKillStreak(e.killer.playerUUID)
        Man10DeathMatch.threadPool.submit { Man10DeathMatch.mysql.execute(MySQLAPI.buildInsertQuery(payload, "kill_log")) }
    }
}