package com.shojabon.man10deathmatch.data_class

import com.shojabon.man10deathmatch.Man10DeathMatch
import com.shojabon.man10deathmatch.enums.DeathMatchState
import com.shojabon.man10deathmatch.states.LobbyState
import com.shojabon.man10deathmatch.states.in_game.InGameState
import com.shojabon.mcutils.Utils.SConfigFile
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import java.io.File
import java.util.*

class DeathMatchGame(val plugin: Man10DeathMatch) {

    public var gameStateType = DeathMatchState.NONE
    public var gameState: DeathMatchGameStateData? = null
    private var gameCounter = 0

    public var currentMapConfig: FileConfiguration? = null
    var notifyRemainingTimeMap: MutableList<Int> = mutableListOf()

    //マップ選択
    fun selectMap(): FileConfiguration?{
        val maps = SConfigFile.getAllFileNameInPath(plugin.dataFolder.toString() + File.separator + "maps")
        if(maps.size == 0) return null
        val selectedMap = maps[gameCounter%maps.size] ?: return null
        gameCounter += 1
        return SConfigFile.getConfigFile(selectedMap.path)
    }

    fun loadMap(){
        notifyRemainingTimeMap.clear()
        // 残り時間通知リスト
        notifyRemainingTimeMap = currentMapConfig?.getIntegerList("remainingTimeNotification")!!

    }

    //ステート管理
    fun setGameState(state: DeathMatchState) {
        Bukkit.getScheduler().runTask(plugin, Runnable {

            //stop current state
            gameState?.beforeEnd()

            //start next state
            gameStateType = state
            val data: DeathMatchGameStateData = getStateData(gameStateType) ?: return@Runnable
            gameState = data
            gameState!!.beforeStart()
        })
    }


    fun getStateData(state: DeathMatchState?): DeathMatchGameStateData? {
        return when (state) {
            DeathMatchState.LOBBY -> {
                LobbyState()
            }

            DeathMatchState.IN_GAME -> InGameState()
            DeathMatchState.END -> TODO()
            DeathMatchState.NONE -> null
            null -> null

        }
    }

    fun getPlayer(uuid: UUID): DeathMatchPlayer?{
        return Man10DeathMatch.registeredPlayers[uuid]
    }

    fun executeCommand(commandName: String){
        val config = currentMapConfig ?: return
        config.getStringList("commands.$commandName").forEach{
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().consoleSender, it)
        }
    }

    fun canPlayMap(config: FileConfiguration): Boolean{
        if(config.getList("spawnLocations", ArrayList<Location>())!!.size == 0) return false
        return true
    }

    fun getAllSpawnPoints(): List<Location>{
        return Man10DeathMatch.currentGame.currentMapConfig?.getList("spawnLocations", ArrayList<Location>()) as List<Location>
    }

    fun movePlayerToRandomSpawn(p: Player){
        val spawnLocations = getAllSpawnPoints()
        p.teleport(spawnLocations.shuffled()[0])
    }


}