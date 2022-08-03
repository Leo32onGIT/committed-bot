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

  val command: SlashCommandData = Commands.slash("finishevent", "declare winners of event and start a new one")
	  .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_SERVER))

  def handleEvent(): MessageEmbed = {
    logger.info("finishevent command called")

    val eventData: List[EventData] = FileUtils.getEventData(None)
    val charData = eventDataToCharData(eventData).filter(_.gained > 0).sortWith(charDataSort)

    val groupedCharData = charData.groupBy { c =>
      Rank.levelToRank(c.startLevel)
    }.map { case (rank, value) => (rank.name, value) }

    val embed = new EmbedBuilder()
    embed.setTitle(":trophy: Event Winners :trophy:", "https://www.tibia.com/community/?subtopic=guilds&page=view&GuildName=Committed").setColor(16753451)
		embed.setThumbnail("https://cdn.discordapp.com/icons/839339600102948914/10fe4ed209cea3c1e99c791261e3d930.webp")
		var emoji = ":fire:"

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
      EmbedHelper.addMultiFields(embed, s"$emoji $rank $emoji", prizeMessages, false)
    }

    embed.build()

  }

}
