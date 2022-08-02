package com.kiktibia.committedbot

import com.typesafe.config.ConfigFactory

object Config {
  private val root = ConfigFactory.load().getConfig("committed-bot")

  val token: String = root.getString("token")
  val guildId: String = root.getString("guild-id")
  val dataDir: String = root.getString("data-dir")
	val circular = Iterator.continually(List(16711680, 16744192, 16776960, 65280, 255, 9699539)).flatten
}
