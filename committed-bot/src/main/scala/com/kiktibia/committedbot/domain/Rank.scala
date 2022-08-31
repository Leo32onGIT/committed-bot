package com.kiktibia.committedbot.domain

case class Rank(id: Int, name: String, minLevel: Option[Int], maxLevel: Option[Int])

case object Rank {

  val ranks: List[Rank] = List(
    Rank(1, "Knights", None, None),
		Rank(2, "Druids", None, None),
		Rank(3, "Paladins", None, None),
		Rank(4, "Sorcerers", None, None),
  )

  def levelToRank(level: Int): Rank = {
    if (level < 300) ranks(3)
    else if (level < 600) ranks(2)
    else if (level < 1000) ranks(1)
    else  ranks.head
  }

	def vocToRank(vocation: String): Rank = {
    if (vocation.contains("Knight")) ranks.head
    else if (vocation.contains("Druid")) ranks(1)
    else if (vocation.contains("Paladin")) ranks(2)
		else if (vocation.contains("Sorcerer")) ranks(3)
    else  ranks.head
  }
}
