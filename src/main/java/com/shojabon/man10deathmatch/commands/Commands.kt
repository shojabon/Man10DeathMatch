package com.shojabon.man10deathmatch.commands

import com.shojabon.man10deathmatch.Man10DeathMatch
import com.shojabon.man10deathmatch.commands.sub_commands.ReloadCommand
import com.shojabon.man10deathmatch.commands.sub_commands.TestCommand
import com.shojabon.man10deathmatch.commands.sub_commands.config.AddSpawnLocationCommand
import com.shojabon.man10deathmatch.commands.sub_commands.config.CreateNewPresetCommand
import com.shojabon.man10deathmatch.commands.sub_commands.config.SetLobbyLocationCommand
import com.shojabon.man10deathmatch.commands.sub_commands.game.CancelCurrentGameCommand
import com.shojabon.man10deathmatch.commands.sub_commands.game.SetCurrentStateTimeCommand
import com.shojabon.man10deathmatch.commands.sub_commands.game.StartGameCommand
import com.shojabon.mcutils.Utils.SCommandRouter.*

class Commands(var plugin: Man10DeathMatch) : SCommandRouter() {

    init {
        registerCommands()
        registerEvents()
    }

    fun registerEvents() {
        setNoPermissionEvent { e: SCommandData -> e.sender.sendMessage(Man10DeathMatch.prefix + "§c§lあなたは権限がありません") }
        setOnNoCommandFoundEvent { e: SCommandData -> e.sender.sendMessage(Man10DeathMatch.prefix + "§c§lコマンドが存在しません") }
    }

    fun registerCommands() {
        //コンフィグ系コマンド
        addCommand(
                SCommandObject()
                        .addArgument(SCommandArgument().addAllowedString("config"))
                        .addArgument(SCommandArgument().addAllowedString("create"))
                        .addArgument(SCommandArgument().addAlias("ゲーム名"))
                        .addRequiredPermission("man10deathmatch.op.config.create")
                        .addExplanation("新たにプリセットを作成する")
                        .setExecutor(CreateNewPresetCommand(plugin))
        )
        addCommand(
                SCommandObject()
                        .addArgument(SCommandArgument().addAllowedString("config"))
                        .addArgument(SCommandArgument().addAllowedString("set"))
                        .addArgument(SCommandArgument().addAllowedString("lobbyLocation"))
                        .addRequiredPermission("man10deathmatch.op.config.lobbyLocation")
                        .addExplanation("ロビーの位置を設定する")
                        .setExecutor(SetLobbyLocationCommand(plugin))
        )

        addCommand(
                SCommandObject()
                        .addArgument(SCommandArgument().addAllowedString("config"))
                        .addArgument(SCommandArgument().addAllowedString("add"))
                        .addArgument(SCommandArgument().addAlias("マップ名"))
                        .addArgument(SCommandArgument().addAllowedString("spawnLocation"))
                        .addRequiredPermission("man10deathmatch.op.config.spawnLocation")
                        .addExplanation("沸き位置を設定する")
                        .setExecutor(AddSpawnLocationCommand(plugin))
        )

        addCommand(
                SCommandObject()
                        .addArgument(SCommandArgument().addAllowedString("game"))
                        .addArgument(SCommandArgument().addAllowedString("setTime"))
                        .addArgument(SCommandArgument().addAllowedType(SCommandArgumentType.INT).addAlias("時間"))
                        .addRequiredPermission("man10deathmatch.op.game.setTime")
                        .addExplanation("現在のステートの時間を設定する")
                        .setExecutor(SetCurrentStateTimeCommand(plugin))
        )

        addCommand(
                SCommandObject()
                        .addArgument(SCommandArgument().addAllowedString("game"))
                        .addArgument(SCommandArgument().addAllowedString("cancel"))
                        .addRequiredPermission("man10deathmatch.op.game.cancel")
                        .addExplanation("現在の試合をキャンセルする")
                        .setExecutor(CancelCurrentGameCommand(plugin))
        )

        addCommand(
                SCommandObject()
                        .addArgument(SCommandArgument().addAllowedString("game"))
                        .addArgument(SCommandArgument().addAllowedString("start"))
                        .addRequiredPermission("man10deathmatch.op.game.start")
                        .addExplanation("試合を開始する")
                        .setExecutor(StartGameCommand(plugin))
        )

        addCommand(
                SCommandObject()
                        .addArgument(SCommandArgument().addAllowedString("test"))
                        .addRequiredPermission("man10deathmatch.op.test")
                        .addExplanation("テストコマンド")
                        .setExecutor(TestCommand(plugin))
        )
        addCommand(
                SCommandObject()
                        .addArgument(SCommandArgument().addAllowedString("reload"))
                        .addRequiredPermission("man10deathmatch.op.reload")
                        .addExplanation("リロードコマンド")
                        .setExecutor(ReloadCommand(plugin))
        )
    }
}