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

  val command: SlashCommandData = Commands.slash("levels", "Get current leaderboard data")
    .addOptions(new OptionData(OptionType.STRING, "rank", "The rank to show")
      .addChoices(ranksAsChoices()))

	val circular = Iterator.continually(List(15218988, 12536331, 13617937, 1617927, 2311385, 9699539)).flatten

  def handleEvent(event: SlashCommandInteractionEvent): MessageEmbed = {
    logger.info("event command called")
    val requestedRank: Option[String] = event.getInteraction.getOptions.asScala.find(_.getName == "rank").map(_.getAsString)

    val eventData: List[EventData] = FileUtils.getEventData(None)

    val charData = eventDataToCharData(eventData).filter(_.gained > 0).sortWith(charDataSort)

    val groupedCharData = charData.groupBy { c =>
      Rank.vocToRank(c.vocation)
    }.map { case (rank, value) => (rank.name, value) }

    val embed = new EmbedBuilder()

		// attempt to cycle through embed colors
		val embedColor = circular.next()

    embed.setTitle(":popcorn: Leaderboards :popcorn:", "https://www.tibia.com/community/?subtopic=guilds&page=view&GuildName=Loyalty").setColor(embedColor)
    requestedRank match {
      case Some(rank) =>
        addRankFieldToEmbed(groupedCharData, embed, rank, None)
      case None =>
        ranks.map(_.name).foreach { rank =>
          addRankFieldToEmbed(groupedCharData, embed, rank, Some(5))
					embed.setThumbnail("https://cdn.discordapp.com/icons/912739993015947324/a_286e97a9dc9c01c6d5eb4b43726927af.webp")
        }
    }
    embed.build()
  }

  private def addRankFieldToEmbed(groupedCharData: Map[String, List[CharData]], embed: EmbedBuilder, rank: String, limit: Option[Int]): Unit = {
    val rankCharData = groupedCharData.getOrElse(rank, List.empty)
		var emoji = ":fire:"
		var rankText = "under 300"

    val rankMessages = limit match {
      case Some(l) => rankCharData.take(l).map(rankMessage)
      case None => rankCharData.map(rankMessage)
    }

		rank match {
			case "Top Dogs" =>
				rankText = "over 1000"
				emoji = ":dog:"
			case "Mains" =>
				rankText = "under 1000"
				emoji = ":100:"
			case "Farmers" =>
				rankText = "under 600"
				emoji = ":farmer:"
			case "Makers" =>
				rankText = "under 300"
				emoji = ":wheelchair:"
		}

		val fieldValue = rankMessages match {
			case Nil => List(s"Nobody $rankText has gained any levels yet.")
			case messages => messages
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
		var spacer = ":black_small_square:"

		c.vocation match {
			case "Sorcerer" =>
				spacer = ":fire:"
			case "Master Sorcerer" =>
				spacer = ":fire:"
			case "Druid" =>
				spacer = ":snowflake:"
			case "Elder Druid" =>
				spacer = ":snowflake:"
			case "Paladin" =>
				spacer = ":bow_and_arrow:"
			case "Royal Paladin" =>
				spacer = ":bow_and_arrow:"
			case "Knight" =>
				spacer = ":shield:"
			case "Elite Knight" =>
				spacer = ":shield:"
		}
    s"$spacer **${c.name}**: ${c.gained} $levels (${c.startLevel} to ${c.endLevel})"
  }

}
