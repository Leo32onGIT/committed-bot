package com.kiktibia.committedbot.domain

case class Rank(id: Int, name: String, minLevel: Option[Int], maxLevel: Option[Int])

case object Rank {

  val ranks: List[Rank] = List(
    Rank(1, "Knights", None, None),
		Rank(2, "Druids", None, None),
		Rank(3, "Paladins", None, None),
		Rank(4, "Sorcerers", None, None),
		Rank(5, "All", None, None),
		Rank(6, "Makers", None, None)
  )

	def vocToRank(vocation: String, level: Int): Rank = {
		if (level < 500) ranks(5)
    else if (vocation.contains("Knight")) ranks.head
    else if (vocation.contains("Druid")) ranks(1)
    else if (vocation.contains("Paladin")) ranks(2)
		else if (vocation.contains("Sorcerer")) ranks(3)
    else  ranks(4)
  }

	def vocToAll(vocation: String, level: Int): Rank = {
		if (level < 500) ranks(5)
		else  ranks(4)
	}
}
