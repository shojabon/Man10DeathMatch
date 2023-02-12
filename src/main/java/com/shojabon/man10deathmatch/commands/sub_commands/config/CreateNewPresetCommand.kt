package com.shojabon.man10deathmatch.commands.sub_commands.config

import com.shojabon.man10deathmatch.Man10DeathMatch
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import java.io.*

class CreateNewPresetCommand(private val plugin: Man10DeathMatch) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val file = File(plugin.dataFolder.toString() + File.separator + "maps" + File.separator + args[2] + ".yml")
        if (file.exists()) {
            sender.sendMessage(Man10DeathMatch.prefix + "§c§lプリセットが存在します")
            return false
        }
        try {
            val parent: File = file.getParentFile()
            if (parent != null && !parent.exists() && !parent.mkdirs()) {
                sender.sendMessage(Man10DeathMatch.prefix + "§c§l games フォルダーの作成に失敗しました")
                return false
            }
            file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            val input: InputStream? = plugin.getResource("map.yml")
            val output: OutputStream = FileOutputStream(file)
            val DEFAULT_BUFFER_SIZE = 1024 * 4
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            var size: Int
            while (-1 != input?.read(buffer).also { size = it!! }) {
                output.write(buffer, 0, size)
            }
            input?.close()
            output.close()
        } catch (e: Exception) {
            sender.sendMessage(Man10DeathMatch.prefix + "§c§l内部エラーが発生しました")
            return false
        }
        return true
    }
}