package com.kiktibia.committedbot.domain

case class Rank(id: Int, name: String, minLevel: Option[Int], maxLevel: Option[Int])

case object Rank {

  val ranks: List[Rank] = List(
    Rank(1, "Night Walker", None, Some(199)),
    Rank(2, "Night Raider", Some(200), Some(399)),
    Rank(3, "Faceless", Some(400), Some(599)),
    Rank(4, "Ironborne", Some(600), Some(799)),
    Rank(5, "Valyrian", Some(800), Some(999)),
    Rank(6, "Stormborn", Some(1000), None),
  )

  def levelToRank(level: Int): Rank = {
    if (level < 200) ranks.head
    else if (level < 400) ranks(1)
    else if (level < 600) ranks(2)
    else if (level < 800) ranks(3)
    else if (level < 1000) ranks(4)
    else  ranks(5)
  }
}
