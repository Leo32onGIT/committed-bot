package com.kiktibia.committedbot

import com.typesafe.config.ConfigFactory

object Config {
  private val root = ConfigFactory.load().getConfig("committed-bot")

  val token: String = root.getString("token")
  val guildId: String = root.getString("guild-id")
  val dataDir: String = root.getString("data-dir")
}
