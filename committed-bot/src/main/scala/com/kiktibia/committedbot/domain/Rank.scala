package com.kiktibia.committedbot.domain

case class Rank(id: Int, name: String, minLevel: Option[Int], maxLevel: Option[Int])

case object Rank {

  val ranks: List[Rank] = List(
    Rank(1, "Makers", None, Some(199)),
    Rank(2, "Farmers", Some(200), Some(399)),
    Rank(3, "Mains", Some(400), Some(699)),
    Rank(4, "Ballers", Some(700), Some(999)),
    Rank(5, "Top Dogs", Some(1000), None),
  )

  def levelToRank(level: Int): Rank = {
    if (level < 200) ranks.head
    else if (level < 400) ranks(1)
    else if (level < 700) ranks(2)
    else if (level < 1000) ranks(3)
    else  ranks(4)
  }
}
