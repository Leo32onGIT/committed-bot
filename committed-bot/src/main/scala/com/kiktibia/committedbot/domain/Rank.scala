package com.kiktibia.committedbot.domain

case class Rank(id: Int, name: String, minLevel: Option[Int], maxLevel: Option[Int])

case object Rank {

  val ranks: List[Rank] = List(
    Rank(1, "Top Dogs", Some(1000), None),
		Rank(2, "Mains", Some(600), Some(999)),
		Rank(3, "Farmers", Some(300), Some(599)),
		Rank(4, "Makers", None, Some(299)),
  )

  def levelToRank(level: Int): Rank = {
    if (level < 300) ranks(3)
    else if (level < 600) ranks(2)
    else if (level < 1000) ranks(1)
    else  ranks.head
  }
}
