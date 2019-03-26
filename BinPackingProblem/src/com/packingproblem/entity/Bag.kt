package com.packingproblem.main

class Bag (val items: IntArray, var fitness: Double, val totalWeight: Int, val factible : Boolean) {
	
	constructor(items: IntArray, totalWeight: Int) : this(items, 0.0, totalWeight, totalWeight <= 120)
	
	override fun equals(other: Any?) : Boolean {
		if (this === other) return true
			
	    other as Bag
	
	    if (!this.items.contentEquals(other.items)) return false
	
	    return true
	}
	
	override fun toString() : String{
		return "Factible: $factible - Weight: $totalWeight - Fitness: $fitness - Items: "+items.joinToString()
	}
}