package com.kiktibia.committedbot.domain

case class Rank(id: Int, name: String, minLevel: Option[Int], maxLevel: Option[Int])

case object Rank {

  val ranks: List[Rank] = List(
    Rank(1, "New", None, None),
    Rank(2, "Loyal", None, None),
    Rank(3, "Leader", None, None),
  )

  def levelToRank(level: Int): Rank = {
    if (level < 300) ranks.head
    else if (level < 1000) ranks(1)
    else  ranks(2)
  }
}
