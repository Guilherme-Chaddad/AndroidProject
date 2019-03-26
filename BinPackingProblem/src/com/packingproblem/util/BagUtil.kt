package com.packingproblem.util

import com.packingproblem.main.Bag
import com.packingproblem.main.Item

class BagUtil {
	companion object {
		var listItems : List<Item> = listOf()
		
		fun createAleatoryBag() : Bag {
			val arrayBag = createArrayBag()
			val weightBag : Int = calcWeight(arrayBag)
			return Bag(arrayBag, weightBag)
		}
		
		private fun calcWeight(arrayBag: IntArray) : Int {
			var weightTotal = 0
			for(i in 0..41){
				if(arrayBag[i] == 1)
					weightTotal = weightTotal.plus(listItems.get(i).weight) 
			}
			return weightTotal
		}
		
		private fun createArrayBag(): IntArray {
			val arrayBag = IntArray(42)
			for(i in 0..41){
				arrayBag[i] = (0..1).random();
			}
			return arrayBag
		}
		
		fun repairBag(bag : Bag) {
			while(!bag.factible) {
				val itemPosition = (0..41).random()
				
				if(bag.items[itemPosition] == 1)
					bag.items[itemPosition] = 0
				
				val weight = calcWeight(bag.items)
				bag.totalWeight = weight
				bag.factible = weight <= 120
			}
		}
	}
}