package com.packingproblem.util

import com.packingproblem.main.Bag
import com.packingproblem.entity.Item

class BagUtil {
	companion object {
		var listItems : List<Item> = listOf()
		private var weightTotal = 0
		private var valueTotal = 0
		private var totalItems = 0
		fun createAleatoryBag() : Bag {
			val arrayBag = createArrayBag()
			calcWeightValue(arrayBag)
			return Bag(arrayBag, weightTotal, valueTotal, totalItems)
		}
		
		private fun calcWeightValue(arrayBag: IntArray) {
			weightTotal = 0
			valueTotal = 0
			totalItems = 0
			for(i in 0..41){
				if(arrayBag[i] == 1) {
					weightTotal = weightTotal.plus(listItems.get(i).weight)
					valueTotal = valueTotal.plus(listItems.get(i).value)
					totalItems++
				}
			}
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

				calcWeightValue(bag.items)
				bag.totalWeight = weightTotal
				bag.totalValue = valueTotal
				bag.totalItems = totalItems
				bag.factible = weightTotal <= 120
			}
		}
	}
}