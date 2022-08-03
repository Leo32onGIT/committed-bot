package com.kiktibia.committedbot

import com.kiktibia.committedbot.command.{EventCommand, RankupsCommand, EventEndCommand}
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.io.{BufferedWriter, File, FileWriter}

class BotListener extends ListenerAdapter {

  override def onSlashCommandInteraction(event: SlashCommandInteractionEvent): Unit = {
    event.getName match {
      case "event" =>
        handleEvent(event)
      case "rankups" =>
        handleRankups(event)
			case "eventend" =>
	        handleEventEnd(event)
      case _ =>
    }
  }

  private def handleEvent(event: SlashCommandInteractionEvent): Unit = {
    val embed = EventCommand.handleEvent(event)
    event.replyEmbeds(embed).queue()
  }

  private def handleRankups(event: SlashCommandInteractionEvent): Unit = {
    val embed = RankupsCommand.handleEvent()
    event.replyEmbeds(embed).queue()
  }

	private def handleEventEnd(event: SlashCommandInteractionEvent): Unit = {
    val embed = EventEndCommand.handleEvent()
    event.replyEmbeds(embed).queue()

    //val eventWriter = new BufferedWriter(new FileWriter("/home/data/committed-bot/event/0.dat"))
    //eventWriter.close()
  }
}
