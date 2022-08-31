package com.kiktibia.committedbot.command

import com.kiktibia.committedbot.domain.Rank.ranks
import com.kiktibia.committedbot.domain.{CharData, EventData, Rank}
import com.kiktibia.committedbot.util.{EmbedHelper, FileUtils}
import com.typesafe.scalalogging.StrictLogging
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.interactions.commands.build.{Commands, CommandData, SlashCommandData}
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.Permission

object FinishEventCommand extends StrictLogging with Command {

  val command: SlashCommandData = Commands.slash("winners", "declare winners of event and start a new one")
	  .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_SERVER))

  def handleEvent(): MessageEmbed = {
    logger.info("finishevent command called")

    val eventData: List[EventData] = FileUtils.getEventData(None)
    val charData = eventDataToCharData(eventData).filter(_.gained > 0).sortWith(charDataSort)

    val groupedCharData = charData.groupBy { c =>
      Rank.vocToRank(c.vocation, c.startLevel)
    }.map { case (rank, value) => (rank.name, value) }

    val embed = new EmbedBuilder()
    embed.setTitle(":trophy: Event Winners :trophy:", "https://www.tibia.com/community/?subtopic=guilds&page=view&GuildName=Loyalty").setColor(16753451)
		embed.setThumbnail("https://cdn.discordapp.com/icons/912739993015947324/a_286e97a9dc9c01c6d5eb4b43726927af.webp")
		var emoji = ":fire:"
		var rankText = ""

    ranks.map(_.name).foreach { rank =>
      val scores = groupedCharData.getOrElse(rank, List.empty)
      val top3 = scores.take(3)
      val winners = top3 ++ scores.drop(3).takeWhile(_.gained == top3.last.gained)
      val winnersWithIndex = winners.zipWithIndex
      val prizeMessages = if (winnersWithIndex.isEmpty) List("<:oof:851137785759137814>") else winnersWithIndex.map { case (winner, index) =>
				val levels = if (winner.gained == 1) "level" else "levels"
				val currentMedal = if (index == 0) ":first_place:" else if (index == 1) ":second_place:" else if (index == 2) ":third_place:" else ":medal:"
        s"$currentMedal **${winner.name}**: ${winner.gained} $levels (${winner.startLevel} to ${winner.endLevel})"
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

      EmbedHelper.addMultiFields(embed, s"$emoji $rank $emoji $rankText", prizeMessages, false)
    }

    embed.build()

  }

}
