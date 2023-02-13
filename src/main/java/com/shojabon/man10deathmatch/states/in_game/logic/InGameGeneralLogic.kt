package com.shojabon.man10deathmatch.states.in_game.logic

import com.shampaggon.crackshot.events.WeaponDamageEntityEvent
import com.shojabon.man10deathmatch.data_class.DeathMatchGame
import com.shojabon.man10deathmatch.data_class.DeathMatchPlayer
import com.shojabon.man10deathmatch.events.Man10DeathMatchPlayerKillEvent
import com.shojabon.man10deathmatch.states.in_game.InGameState
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.entity.PlayerDeathEvent
import java.time.Duration
import java.util.*

class InGameGeneralLogic(val state: InGameState) : Listener {
    private val game: DeathMatchGame = state.game!!

    init {

        state.timerTillNextState.addOnIntervalEvent { remainingTime ->
            if (!game.notifyRemainingTimeMap.contains(remainingTime)) return@addOnIntervalEvent
            val remainingMinutes = remainingTime / 60
            val remainingSeconds = remainingTime % 60
            var result = ""
            if (remainingMinutes != 0) result += remainingMinutes.toString() + "分"
            if (remainingSeconds != 0) result += remainingSeconds.toString() + "秒"
            if (result == "") result = "0秒"
            Bukkit.broadcast(Component.text("残り時間$result"))
        }
    }
    @EventHandler
    fun onVanillaDamage(e: EntityDamageByEntityEvent) {
        val damagedUUID: UUID = e.entity.uniqueId
        val damagerUUID: UUID = e.damager.uniqueId
        val damaged: DeathMatchPlayer = game.getPlayer(damagedUUID) ?: return
        val damager: DeathMatchPlayer = game.getPlayer(damagerUUID) ?: return
        damaged.lastDamagePlayer = damager
        damaged.lastDamageTime = System.currentTimeMillis() / 1000L
    }

    @EventHandler
    fun onGunDamage(e: WeaponDamageEntityEvent) {
        val damagedUUID: UUID = e.victim.uniqueId
        val damagerUUID: UUID = e.player.uniqueId
        val damaged: DeathMatchPlayer = game.getPlayer(damagedUUID) ?: return
        val damager: DeathMatchPlayer = game.getPlayer(damagerUUID) ?: return
        damaged.lastDamagePlayer = damager
        damaged.lastDamageTime = System.currentTimeMillis() / 1000L
    }

    @EventHandler
    fun respawn(e: PlayerDeathEvent){
        val player = game.getPlayer(e.entity.uniqueId) ?: return
        e.isCancelled = true
        e.player.foodLevel = 20
        e.player.health = 20.0
        game.movePlayerToRandomSpawn(e.player)
        player.giveRandomEquipment()
    }

    @EventHandler
    fun onKill(e: PlayerDeathEvent) {
        val killed: DeathMatchPlayer = game.getPlayer(e.player.uniqueId) ?: return
        if (System.currentTimeMillis() / 1000L - killed.lastDamageTime > 5) return
        Bukkit.getServer().pluginManager.callEvent(Man10DeathMatchPlayerKillEvent(game, killed, killed.lastDamagePlayer!!, e))
    }

    @EventHandler
    fun onHunger(e: FoodLevelChangeEvent){
        val player = game.getPlayer(e.entity.uniqueId) ?: return
        e.isCancelled = true
        player.getPlayer()?.foodLevel = 20
    }

    @EventHandler
    fun onKillHeal(e: Man10DeathMatchPlayerKillEvent){
        val p = e.killer.getPlayer() ?: return
        p.showTitle(Title.title(Component.text("§a" + e.killed.playerName + "をキル"), Component.text(""), Title.Times.of(Duration.ZERO, Duration.ofMillis(500), Duration.ZERO)))
        p.foodLevel = 20
        p.health = 20.0
    }

    @EventHandler
    fun onDeathMessage(e: Man10DeathMatchPlayerKillEvent){
        val p = e.killed.getPlayer() ?: return
        p.showTitle(Title.title(Component.text("§c" + e.killer.playerName + "にキルされた"), Component.text(""), Title.Times.of(Duration.ZERO, Duration.ofMillis(500), Duration.ZERO)))
        p.foodLevel = 20
        p.health = 20.0
    }

    @EventHandler
    fun onKillRunCommand(e: Man10DeathMatchPlayerKillEvent){
        val killer = e.killer.getPlayer() ?: return
        val killed = e.killed.getPlayer() ?: return

        game.currentMapConfig?.getStringList("commands.onDeath")?.forEach{
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().consoleSender,
                    it
                            .replace("{killer}", killer.name)
                            .replace("{killed}", killed.name)
            )
        }

        game.currentMapConfig?.getStringList("commands.onKill")?.forEach{
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().consoleSender,
                    it
                            .replace("{killer}", killer.name)
                            .replace("{killed}", killed.name)
            )
        }
    }

}