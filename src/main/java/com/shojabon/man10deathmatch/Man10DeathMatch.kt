package com.shojabon.man10deathmatch

import com.shojabon.man10deathmatch.commands.Commands
import com.shojabon.man10deathmatch.data_class.DeathMatchGame
import com.shojabon.man10deathmatch.data_class.DeathMatchPlayer
import com.shojabon.man10deathmatch.enums.DeathMatchState
import com.shojabon.mcutils.Utils.MySQL.MySQLAPI
import com.shojabon.mcutils.Utils.SConfigFile
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import java.util.concurrent.Executors

class Man10DeathMatch : JavaPlugin(), Listener {

    companion object{
        val threadPool = Executors.newCachedThreadPool()
        lateinit var currentGame: DeathMatchGame
        val registeredPlayers = HashMap<UUID, DeathMatchPlayer>()
        var prefix = "§4§l[Man10DeathMatch]§f"
        lateinit var config: FileConfiguration

        lateinit var mysql: MySQLAPI

        fun endGame(){
            currentGame.setGameState(DeathMatchState.NONE)
        }
    }
    override fun onEnable() {
        // Plugin startup logic
        saveDefaultConfig()
        currentGame = DeathMatchGame(this)
        server.pluginManager.registerEvents(this, this)
        Man10DeathMatch.config = config;
        currentGame.setGameState(DeathMatchState.LOBBY)
        val normalCommands = Commands(this)
        getCommand("mdm")!!.setExecutor(normalCommands)
        getCommand("mdm")!!.tabCompleter = normalCommands

        Bukkit.getServer().onlinePlayers.forEach{
            registeredPlayers[it.uniqueId] = DeathMatchPlayer(it)
        }

        mysql = MySQLAPI(this)
        mysql.execute(SConfigFile.getResourceAsText(this, "sql/kill_table.sql"))


    }

    override fun onDisable() {
        // Plugin shutdown logic
        endGame()
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent){
        registeredPlayers[e.player.uniqueId] = DeathMatchPlayer(e.player)
        if(currentGame.gameStateType == DeathMatchState.LOBBY){
            e.player.teleport(config.getLocation("lobbyLocation")!!)
        }
        if(currentGame.gameStateType == DeathMatchState.IN_GAME){
            currentGame.movePlayerToRandomSpawn(e.player)
        }
    }

    @EventHandler
    fun onLeave(e: PlayerQuitEvent){
        registeredPlayers.remove(e.player.uniqueId)
    }
}