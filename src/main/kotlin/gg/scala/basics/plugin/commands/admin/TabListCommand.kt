package gg.scala.basics.plugin.commands.admin

import gg.scala.basics.plugin.chatfilter.ChatFilterService
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.acf.CommandHelp
import gg.scala.commons.acf.annotation.*
import gg.scala.commons.annotations.commands.AssignPermission
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.issuer.ScalaPlayer
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.prompt.InputPrompt
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

/**
 * @author puugz
 * @since 27/08/2021 20:02
 */
@AutoRegister
@CommandAlias("tablist|tabconfig|tablistconfig|tabconf|tab")
@CommandPermission("scbasics.command.tablist")
object TabListCommand : ScalaCommand()
{
    @Default
    @HelpCommand
    fun onHelp(help: CommandHelp)
    {
        help.showHelp()
    }

    enum class TabListPart
    {
        HEADER, FOOTER
    }

    @AssignPermission
    @Subcommand("set")
    @Description("Set a tab list part value.")
    fun onSet(player: ScalaPlayer, part: TabListPart)
    {
        InputPrompt()
            .withText(
                "Enter the value in MiniMessage format:"
            )
            .acceptInput { _, input ->
                val component = MiniMessage
                    .miniMessage().deserialize(
                        input.replace("<nl>", "\n")
                    )

                val configuration = ChatFilterService.configModel()

                when (part)
                {
                    TabListPart.HEADER -> configuration.header = component
                    TabListPart.FOOTER -> configuration.footer = component
                }

                ChatFilterService.applyKvUpdate(configuration)

                player.sendMessage(
                    "${CC.GREEN}The tablist ${part.name} has been set to:",
                    LegacyComponentSerializer.legacySection().serialize(component)
                )
            }
            .start(player.bukkit())
    }

    @AssignPermission
    @Subcommand("reset")
    @Description("Reset a tab list part value.")
    fun onReset(player: ScalaPlayer, part: TabListPart)
    {
        val configuration = ChatFilterService.configModel()

        when (part)
        {
            TabListPart.HEADER -> configuration.header = Component.empty()
            TabListPart.FOOTER -> configuration.footer = Component.empty()
        }

        player.sendMessage(
            "${CC.GREEN}The tablist ${part.name} has been reset to default."
        )
    }
}
