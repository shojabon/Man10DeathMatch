package com.shojabon.man10deathmatch.states

import com.shojabon.man10deathmatch.Man10DeathMatch
import com.shojabon.man10deathmatch.data_class.DeathMatchGame
import com.shojabon.man10deathmatch.data_class.DeathMatchGameStateData
import com.shojabon.man10deathmatch.enums.DeathMatchState
import com.shojabon.mcutils.Utils.SScoreboard
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import java.awt.TextComponent
import java.util.*

class LobbyState : DeathMatchGameStateData() {
    var game: DeathMatchGame? = Man10DeathMatch.currentGame

    override fun start() {
        timerTillNextState.start()
        Bukkit.getServer().onlinePlayers.forEach {
            it.teleport(Man10DeathMatch.config.getLocation("lobbyLocation")!!)
        }
    }

    override fun end() {}
    override fun defineTimer() {
        timerTillNextState.setRemainingTime(10)
        timerTillNextState.addOnEndEvent {
            game?.setGameState(DeathMatchState.IN_GAME)
        }
    }

    override fun defineBossBar() {
        val barTitle = "§c§l次のマップまで §a§l残り§e§l{time}§a§l秒"
        this.bar = Bukkit.createBossBar("", BarColor.WHITE, BarStyle.SOLID)
        timerTillNextState.linkBossBar(bar, true)
        timerTillNextState.addOnIntervalEvent { e -> bar?.setTitle(barTitle.replace("{registered}", Man10DeathMatch.registeredPlayers.size.toString()).replace("{time}", java.lang.String.valueOf(e))) }
    }

    override fun defineScoreboard() {
        scoreboard = SScoreboard("TEST")
        scoreboard!!.setTitle("§4§lMan10DeathMatch")
        scoreboard!!.setText(0, "§c§lマップ選択中")
        timerTillNextState.addOnIntervalEvent { e ->
            scoreboard!!.setText(3, "§a§l残り§e§l$e§a§l秒")
            scoreboard!!.setText(2, "§a§l現在登録者§e§l" + (Man10DeathMatch.registeredPlayers.size) + "§a§l人")
            scoreboard!!.renderText()
        }
    }
}