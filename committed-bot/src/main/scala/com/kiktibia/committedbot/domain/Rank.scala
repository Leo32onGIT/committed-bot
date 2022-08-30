package com.kiktibia.committedbot.domain

case class Rank(id: Int, name: String, minLevel: Option[Int], maxLevel: Option[Int])

case object Rank {

  val ranks: List[Rank] = List(
    Rank(1, "Makers", None, Some(299)),
    Rank(2, "Farmers", Some(300), Some(599)),
    Rank(3, "Mains", Some(600), Some(999)),
    Rank(4, "Top Dogs", Some(1000), None),
  )

  def levelToRank(level: Int): Rank = {
    if (level < 300) ranks.head
    else if (level < 600) ranks(1)
    else if (level < 1000) ranks(2)
    else  ranks(3)
  }
}
