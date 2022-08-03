package com.kiktibia.committedbot

import akka.actor.ActorSystem
import com.kiktibia.committedbot.command.{EventCommand, RankupsCommand, EventEndCommand}
import com.typesafe.scalalogging.StrictLogging
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Guild
import scala.jdk.CollectionConverters._

object BotApp extends App with StrictLogging {

  implicit private val actorSystem: ActorSystem = ActorSystem()

  logger.info("Starting up")

  private val jda = JDABuilder.createDefault(Config.token)
    .addEventListeners(new BotListener())
    .build()

  jda.awaitReady()
  logger.info("JDA ready")

  private val guild: Guild = jda.getGuildById(Config.guildId)

//  guild.retrieveCommands().map { commands =>
//    commands.forEach(c => guild.deleteCommandById(c.getId).queue())
//  }.queue()
  private val commands = List(EventCommand.command, RankupsCommand.command)
	private val adminCommands = EventEndCommand.command
  guild.updateCommands().addCommands(commands.asJava).complete()
	guild.updateCommands().addCommands(adminCommands).complete()
}
