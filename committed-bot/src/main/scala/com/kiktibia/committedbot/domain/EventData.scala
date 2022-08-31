package com.kiktibia.committedbot.domain

import java.time.{LocalDateTime, ZoneOffset, ZonedDateTime}
import java.time.format.DateTimeFormatter
import com.typesafe.scalalogging.StrictLogging

case class EventData(date: ZonedDateTime, name: String, level: Int, vocation: String)

case object EventData {

  def fromString(s: String): EventData = {
    val split = s.split(",")
    val ldt = LocalDateTime.parse(split.head, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    val zdt = ldt.atZone(ZoneOffset.UTC)
		val voc = split(3).toString()
		logger.info(zdt)
		logger.info(split(1))
		logger.info(split(2))
		logger.info(voc)
    EventData(zdt, split(1), split(2).toInt, voc)
  }

}
