package gg.scala.basics.plugin.shutdown

import gg.scala.lemon.Lemon
import gg.scala.lemon.util.task.DiminutionRunnable
import me.lucko.helper.utils.Players
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.Tasks
import net.evilblock.cubed.util.time.TimeUtil
import org.bukkit.Bukkit

class ShutdownRunnable(seconds: Int) : DiminutionRunnable(seconds)
{
    init
    {
        runTaskTimerAsynchronously(Lemon.instance, 0L, 20L)
    }

    override fun onRun()
    {
        Players.all().forEach {
            it.sendMessage("")
            it.sendMessage("${CC.RED}The server is rebooting in ${CC.B_RED}${TimeUtil.formatIntoDetailedString(seconds)}${CC.RED}!")
            it.sendMessage("")
        }
    }

    override fun onEnd()
    {
        Tasks.sync {
            Bukkit.getPluginManager().callEvent(ServerShutdownEvent())

            Tasks.delayed(5L) {
                Bukkit.getOnlinePlayers().forEach {
                    it.kickPlayer("${CC.RED}The server is rebooting!")
                }

                Bukkit.getServer().dispatchCommand(
                    Bukkit.getConsoleSender(),
                    "stop"
                )
            }
        }
    }

    override fun cancel()
    {
        super.cancel()

        if (seconds > 1)
        {
            broadcast("${CC.RED}The scheduled server shutdown has been cancelled!")
        }
    }

    override fun getSeconds(): List<Int>
    {
        return listOf(
            18000,
            14400,
            10800,
            7200,
            3600,
            2700,
            1800,
            900,
            600,
            300,
            240,
            180,
            120,
            60,
            50,
            40,
            30,
            15,
            10,
            5,
            4,
            3,
            2,
            1
        )
    }
}
