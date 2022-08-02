package com.kiktibia.committedbot.command

import com.kiktibia.committedbot.domain.{CharData, EventData, Rank}
import com.kiktibia.committedbot.util.{EmbedHelper, FileUtils}
import com.typesafe.scalalogging.StrictLogging
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.Command.Choice
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.{Commands, OptionData, SlashCommandData}
import Rank.ranks

import scala.jdk.CollectionConverters._

object EventCommand extends StrictLogging with Command {

  val command: SlashCommandData = Commands.slash("event", "get info of event")
    .addOptions(new OptionData(OptionType.STRING, "rank", "The rank to show")
      .addChoices(ranksAsChoices()))

	val circular = Iterator.continually(List(15218988, 12536331, 13617937, 1617927, 2311385, 9699539)).flatten

  def handleEvent(event: SlashCommandInteractionEvent): MessageEmbed = {
    logger.info("event command called")
    val requestedRank: Option[String] = event.getInteraction.getOptions.asScala.find(_.getName == "rank").map(_.getAsString)

    val eventData: List[EventData] = FileUtils.getEventData(None)

    val charData = eventDataToCharData(eventData).filter(_.gained > 0).sortWith(charDataSort)

    val groupedCharData = charData.groupBy { c =>
      Rank.levelToRank(c.startLevel)
    }.map { case (rank, value) => (rank.name, value) }

    val embed = new EmbedBuilder()

		// attempt to cycle through embed colors
		val embedColor = circular.next()

    embed.setTitle("<:server_owner:906644897019338814> Leaderboards <:server_owner:906644897019338814>", "https://www.tibia.com/community/?subtopic=guilds&page=view&GuildName=Committed").setColor(embedColor)
    requestedRank match {
      case Some(rank) =>
        addRankFieldToEmbed(groupedCharData, embed, rank, None)
      case None =>
        ranks.map(_.name).foreach { rank =>
          addRankFieldToEmbed(groupedCharData, embed, rank, Some(5))
        }
    }
    embed.build()
  }

  private def addRankFieldToEmbed(groupedCharData: Map[String, List[CharData]], embed: EmbedBuilder, rank: String, limit: Option[Int]): Unit = {
    val rankCharData = groupedCharData.getOrElse(rank, List.empty)
		var emoji = ":fire:"

    val rankMessages = limit match {
      case Some(l) => rankCharData.take(l).map(rankMessage)
      case None => rankCharData.map(rankMessage)
    }

    val fieldValue = rankMessages match {
      case Nil => List("Nobody has gained any levels yet.")
      case messages => messages
    }
		rank match {
			case "Night Walker" =>
    		emoji = "<a:Rotworm_1x:1003487298526126150>"
			case "Night Raider" =>
				emoji = "<a:luckydragon:1003487247187841106>"
			case "Faceless" =>
				emoji = "<a:noxiousripptor:1003487284642983966>"
			case "Ironborne" =>
				emoji = "<a:magmacolossus:1003487259833663488>"
			case "Valyrian" =>
				emoji = "<a:Ferumbras_1x:1003487231194959982>"
			case "Stormborn" =>
				emoji = "<a:morshabaal:1003487272043302974>"
		}
		EmbedHelper.addMultiFields(embed, s"$emoji $rank $emoji", fieldValue, false)
	}

  private def ranksAsChoices() = {
    Rank.ranks.map { rank =>
      new Choice(rank.name, rank.name)
    }
  }.asJava

  private def rankMessage(c: CharData): String = {
    val levels = if (c.gained == 1) "level" else "levels"
    s"• **${c.name}**: ${c.gained} $levels (${c.startLevel} to ${c.endLevel})"
  }

}
