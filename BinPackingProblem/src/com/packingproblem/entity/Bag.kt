package com.packingproblem.main

class Bag (val items: IntArray, var fitness: Double, var totalWeight: Int, var totalValue: Int,
		   var factible : Boolean, var percentOfFitness: Long, var totalItems : Int) {
	
	constructor(items: IntArray, totalWeight: Int, totalValue: Int, totalItems: Int) :
			this(items, 0.0, totalWeight, totalValue, totalWeight <= 120, 0, totalItems)
	
	override fun equals(other: Any?) : Boolean {
		if (this === other) return true
			
	    other as Bag
	
	    if (!this.items.contentEquals(other.items)) return false
	
	    return true
	}
	
	override fun toString() : String{
		return "Factible: $factible - Weight: $totalWeight - Value: $totalValue - Items: $totalItems - Fitness: $fitness - Percent of Fitness: $percentOfFitness Items: "+items.joinToString()
	}
}