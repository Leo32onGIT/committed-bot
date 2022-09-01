package com.kiktibia.committedbot

import com.kiktibia.committedbot.command.{EventCommand, FinishEventCommand}
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.io.{BufferedWriter, File, FileWriter}

class BotListener extends ListenerAdapter {

  override def onSlashCommandInteraction(event: SlashCommandInteractionEvent): Unit = {
    event.getName match {
      case "levels" =>
        handleEvent(event)
			case "winners" =>
	        handleFinishEvent(event)
      case _ =>
    }
  }

  private def handleEvent(event: SlashCommandInteractionEvent): Unit = {
		event.deferReply().queue()
    val embed = EventCommand.handleEvent(event)
    event.sendMessage(embed).queue()
  }

	private def handleFinishEvent(event: SlashCommandInteractionEvent): Unit = {
    val embed = FinishEventCommand.handleEvent()
    event.replyEmbeds(embed).queue()

    val eventWriter = new BufferedWriter(new FileWriter("/home/data/committed-bot/event/0.dat"))
    eventWriter.close()
  }
}
