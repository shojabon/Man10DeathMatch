package com.shojabon.man10deathmatch.data_class

import com.shojabon.mcutils.Utils.SScoreboard
import com.shojabon.mcutils.Utils.STimer
import org.bukkit.Bukkit
import org.bukkit.boss.BossBar
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin

open class DeathMatchGameStateData : Listener {
    var plugin: Plugin = Bukkit.getPluginManager().getPlugin("Man10DeathMatch")!!

    //timer
    var timerTillNextState: STimer = STimer()
    open fun defineTimer() {}

    //boss bar
    var bar: BossBar? = null
    open fun defineBossBar() {}

    //score board
    var scoreboard: SScoreboard? = null
    open fun defineScoreboard() {}
    var logics = ArrayList<Listener>()
    open fun defineLogics() {}
    fun registerLogic(listener: Listener) {
        logics.add(listener)
    }

    //inner required start stop cancel functions
    fun beforeStart() {
        defineTimer()
        defineBossBar()
        defineScoreboard()
        defineLogics()
        Bukkit.getServer().pluginManager.registerEvents(this, plugin)
        for (listener in logics) {
            Bukkit.getServer().pluginManager.registerEvents(listener, plugin)
        }

        //register boss bar
        if (bar != null) {
            for (p in Bukkit.getServer().onlinePlayers) {
                bar!!.addPlayer(p)
            }
        }

        //register scoreboard
        if (scoreboard != null) {
            for (p in Bukkit.getServer().onlinePlayers) {
                scoreboard!!.addPlayer(p)
            }
        }
        start()
    }

    fun beforeEnd() {
        HandlerList.unregisterAll(this as Listener)
        timerTillNextState.stop()
        end()

        //remove bar
        if (bar != null) {
            bar!!.removeAll()
            bar!!.isVisible = false
            bar = null
        }

        //scoreboard
        if (scoreboard != null) {
            scoreboard!!.remove()
            scoreboard = null
        }
        for (listener in logics) {
            HandlerList.unregisterAll(listener)
        }
    }

    fun beforeCancel() {
        HandlerList.unregisterAll(this as Listener)
        timerTillNextState.stop()
        cancel()

        //remove bar
        if (bar != null) {
            bar!!.removeAll()
            bar!!.isVisible = false
            bar = null
        }

        //scoreboard
        if (scoreboard != null) {
            scoreboard!!.remove()
            scoreboard = null
        }
    }

    // interface start stop cancel functions
    open fun start() {}
    open fun end() {}
    open fun cancel() {}

    //bar events
    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        if (bar == null) return
        bar!!.addPlayer(e.getPlayer())
        scoreboard?.addPlayer(e.getPlayer())
    }

    @EventHandler
    fun onPlayerLeave(e: PlayerQuitEvent) {
        if (bar == null) return
        bar!!.addPlayer(e.getPlayer())
    }
}