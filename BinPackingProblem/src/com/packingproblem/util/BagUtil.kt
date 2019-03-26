package com.packingproblem.util

import com.packingproblem.main.Bag
import com.packingproblem.main.Item

class BagUtil {
	companion object {
		fun createAleatoryBag(listItems: List<Item>) : Bag {
			val arrayBag = createArrayBag()
			val weightBag : Int = calcWeight(arrayBag, listItems)
			return Bag(arrayBag, weightBag)
		}
		
		private fun calcWeight(arrayBag: IntArray, listItems: List<Item>) : Int {
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
	}
}