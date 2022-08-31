package com.kiktibia.committedbot.command

import com.kiktibia.committedbot.domain.{CharData, EventData}

trait Command {

  def eventDataToCharData(eventData: List[EventData]): List[CharData] = {
    val reversedEventData = eventData.reverse
    val charNames = eventData.map(_.name).toSet
    charNames.map { name =>
      val startLevel = eventData.filter(_.name == name).head.level
      val endLevel = reversedEventData.filter(_.name == name).head.level
			val vocation = eventData.filter(_.name == name).head.vocation
      CharData(name, startLevel, endLevel, endLevel - startLevel, vocation)
    }.toList
  }

  // sort char data by who gained the most levels, breaking ties by who is the highest level
  def charDataSort(c1: CharData, c2: CharData): Boolean = {
    if (c1.gained == c2.gained) c1.startLevel > c2.startLevel
    else c1.gained > c2.gained
  }

}
