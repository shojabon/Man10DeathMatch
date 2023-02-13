package com.shojabon.man10deathmatch.states.in_game

import com.shojabon.man10deathmatch.Man10DeathMatch
import com.shojabon.man10deathmatch.data_class.DeathMatchGame
import com.shojabon.man10deathmatch.data_class.DeathMatchGameStateData
import com.shojabon.man10deathmatch.enums.DeathMatchState
import com.shojabon.man10deathmatch.states.in_game.logic.InGameGeneralLogic
import com.shojabon.man10deathmatch.states.in_game.logic.InGameKillStreakLogic
import com.shojabon.man10deathmatch.states.in_game.logic.InGameLoggingLogic
import com.shojabon.man10deathmatch.states.in_game.logic.InGameViewLogic
import com.shojabon.mcutils.Utils.SScoreboard
import org.bukkit.Bukkit
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent

class InGameState : DeathMatchGameStateData() {
    var game: DeathMatchGame? = Man10DeathMatch.currentGame

    override fun start() {
        timerTillNextState.start()
        moveAllPlayersToSpawnPoint()
    }

    public val generalLogic = InGameGeneralLogic(this)
    public val killSteakLogic = InGameKillStreakLogic(this)
    public val viewLogic = InGameViewLogic(this)
    public val loggingLogic = InGameLoggingLogic(this)

    override fun defineLogics() {
        registerLogic(generalLogic)
        registerLogic(killSteakLogic)
        registerLogic(viewLogic)
        registerLogic(loggingLogic)
    }

    override fun end() {}
    override fun defineTimer() {
        timerTillNextState.setRemainingTime(300)
        timerTillNextState.addOnEndEvent { game?.setGameState(DeathMatchState.LOBBY) }
    }

    override fun defineBossBar() {
        val barTitle = "§c§l試合中 §a§l残り§e§l{time}§a§l秒"
        this.bar = Bukkit.createBossBar("", BarColor.WHITE, BarStyle.SOLID)
        timerTillNextState.linkBossBar(bar, true)
        timerTillNextState.addOnIntervalEvent { e -> bar?.setTitle(barTitle.replace("{registered}", Man10DeathMatch.registeredPlayers.size.toString()).replace("{time}", java.lang.String.valueOf(e))) }
    }

    override fun defineScoreboard() {
        scoreboard = SScoreboard("TEST")
        scoreboard!!.setTitle("§4§lMan10DeathMatch")
        scoreboard!!.setText(0, "§c§l試合中")
        timerTillNextState.addOnIntervalEvent { e ->
            scoreboard!!.setText(3, "§a§l残り§e§l$e§a§l秒")
            scoreboard!!.setText(2, "§a§l現在登録者§e§l" + (Man10DeathMatch.registeredPlayers.size) + "§a§l人")
            scoreboard!!.renderText()
        }
    }

    fun moveAllPlayersToSpawnPoint(){
        val spawnLocations = game?.getAllSpawnPoints()
        Bukkit.getServer().onlinePlayers.forEachIndexed{ index, player ->
            player.teleport(spawnLocations!![index%spawnLocations.size])
            game?.getPlayer(player.uniqueId)?.giveRandomEquipment()
        }
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent){
        game?.movePlayerToRandomSpawn(e.player)
    }
}