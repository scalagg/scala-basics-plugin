package gg.scala.basics.plugin.commands.admin

import gg.scala.commons.acf.ConditionFailedException
import gg.scala.commons.acf.annotation.*
import gg.scala.commons.acf.annotation.Optional
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.lemon.handler.PlayerHandler
import gg.scala.lemon.player.LemonPlayer
import gg.scala.lemon.player.wrapper.AsyncLemonPlayer
import gg.scala.lemon.sessions.Session
import gg.scala.lemon.sessions.SessionService
import gg.scala.lemon.util.QuickAccess
import net.evilblock.cubed.util.CC
import org.apache.commons.lang.time.DurationFormatUtils
import org.bukkit.entity.Player
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.TemporalAdjusters
import java.util.*
import java.util.concurrent.CompletableFuture

@AutoRegister
object PlaytimeCommand : ScalaCommand()
{
    @Syntax("[player]")
    @CommandCompletion("@players")
    @CommandAlias("playtime|pt|howlonghaveiplayed")
    @CommandPermission("scbasics.command.playtime")
    fun onPlayTime(
        player: Player, @Optional target: AsyncLemonPlayer?
    ): CompletableFuture<Void>
    {
        if (target != null && !player.hasPermission("scbasics.command.playtime.other"))
        {
            throw ConditionFailedException("You do not have permission to view playtime of other players!")
        }

        if (target != null)
        {
            player.sendMessage("${CC.GREEN}Fetching playtime...")

            return target.validatePlayers(player, false) {
                handlePlaytimeComputation(player, it)
            }
        } else
        {
            return CompletableFuture
                .runAsync {
                    handlePlaytimeComputation(
                        player,
                        PlayerHandler.findPlayer(player)
                            .orElse(null)!!
                    )
                }
        }
    }

    private fun startOfDay(): ZonedDateTime
    {
        return ZonedDateTime
            .now(ZoneOffset.UTC)
            .toLocalDate()
            .atStartOfDay(ZoneOffset.UTC)
    }

    private fun endOfDay(): ZonedDateTime
    {
        return ZonedDateTime
            .now(ZoneOffset.UTC)
            .toLocalDate()
            .atTime(LocalTime.MAX)
            .atZone(ZoneOffset.UTC)
    }

    private fun handlePlaytimeComputation(
        player: Player, target: LemonPlayer
    )
    {
        val sessions = SessionService.loadSessions(target.uniqueId)
        val zoneId = TimeZone.getDefault().toZoneId()
        val localDate = LocalDate.now(zoneId)

        val weekStart = localDate.with(
            TemporalAdjusters
                .previousOrSame(DayOfWeek.MONDAY)
        )
        val weekEnd = localDate.with(
            TemporalAdjusters
                .nextOrSame(DayOfWeek.SUNDAY)
        )

        val completePlaytime = sessions.values.sumOf(Session::length)
        val completePlaytimeSessions = sessions.values.size

        val end = endOfDay()
        val start = startOfDay()

        val currentWeek = sessions.values.filter {
            val current = Instant
                .ofEpochMilli(it.timestamp)
                .atZone(zoneId)
                .toLocalDate()

            return@filter current.isAfter(weekStart)
                && current.isBefore(weekEnd)
        }

        val currentWeekPlaytime = currentWeek.sumOf(Session::length)
        val currentWeekPlaytimeSessions = currentWeek.size

        val currentDay = sessions.values.filter {
            val current = it.timestamp / 1000L

            return@filter start.toEpochSecond() < current
                && end.toEpochSecond() > current
        }

        val currentDayPlaytime = currentDay.sumOf(Session::length)
        val currentDayPlaytimeSessions = currentDay.size

        val coloredName = QuickAccess.fetchColoredName(target.uniqueId)

        player.sendMessage("${CC.B_SEC}Playtime of $coloredName${CC.B_SEC}:")
        player.sendMessage(
            " ${CC.SEC}Today ${CC.GRAY}(${currentDayPlaytimeSessions} sessions)${CC.SEC}: ${CC.WHITE}${
                DurationFormatUtils.formatDurationWords(
                    currentDayPlaytime, true, true
                )
            }"
        )

        player.sendMessage(
            " ${CC.SEC}This week ${CC.GRAY}(${currentWeekPlaytimeSessions} sessions)${CC.SEC}: ${CC.WHITE}${
                DurationFormatUtils.formatDurationWords(
                    currentWeekPlaytime, true, true
                )
            }"
        )

        player.sendMessage(
            " ${CC.SEC}All-time total ${CC.GRAY}(${completePlaytimeSessions} sessions)${CC.SEC}: ${CC.WHITE}${
                DurationFormatUtils.formatDurationWords(
                    completePlaytime, true, true
                )
            }"
        )
    }
}
