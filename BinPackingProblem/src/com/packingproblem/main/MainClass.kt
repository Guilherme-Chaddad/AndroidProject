package com.packingproblem.main

import com.packingproblem.entity.Item
import com.packingproblem.util.BagUtil

var sumFitness = 0.0
val probabilityCrossover: Double = 80.0
val probabilityMutation: Double = 5.0
val populationSize: Int = 10
val initialBagSize: Int = 15
val reproductionSize: Int = populationSize

fun main(args: Array<String>){
	val listItems: List<Item> = initializeItems()
	
	BagUtil.listItems = listItems
	
	var population: MutableList<Bag> = initializePopulation()

	calculateFitnessPenalize(population)
	//calculateFitnessRepair(population)

	var numberOfReproduction = 0
	//while(numberOfReproduction < 500){
		val parents : MutableList<Bag> = selectParents(population)
		parents.forEach(::println)

		println("-----------------------REPRODUCTION-------------------------------")
		//reprodução
		val children : MutableList<Bag> = doReproduction(parents)
		children.forEach(::println)
		//repara/penaliza filhos e calcula fitness
		
		//seleciona populacao - repete de acordo com criterio de parada
		
		numberOfReproduction++;
	//}
}

fun doReproduction(parents: MutableList<Bag>) : MutableList<Bag> {
	val children : MutableList<Bag> = mutableListOf()

	//crossover
	for (i in 0 until parents.size step 2) {

		val parent1 = parents[i]
		val parent2 = parents[i+1]

		var arrayBagChild1 : IntArray = IntArray(42)
		var arrayBagChild2 : IntArray = IntArray(42)

		val aleatoryCrossover = (1..100).random()

		if(aleatoryCrossover < probabilityCrossover) {


			val pointOfCrossover = (0..41).random()

			for(i in 0..pointOfCrossover) {
				arrayBagChild1[i] = parent1.items[i]
				arrayBagChild2[i] = parent2.items[i]
			}

			for(j in pointOfCrossover..41){
				arrayBagChild1[i] = parent2.items[i]
				arrayBagChild2[i] = parent1.items[i]
			}
		} else {
			arrayBagChild1 = parent1.items
			arrayBagChild2 = parent2.items
		}
		doMutation(arrayBagChild1)
		doMutation(arrayBagChild2)

		children.add(BagUtil.createBag(arrayBagChild1))
		children.add(BagUtil.createBag(arrayBagChild2))
	}

	return children
}

fun doMutation(arrayBag : IntArray) {

}

fun selectParents(population: MutableList<Bag>) : MutableList<Bag> {
	val parents : MutableList<Bag> = mutableListOf()
	
	println(sumFitness)

	//var totalPercOfFitness = 0.0
	population.forEach {
		val percFitnessTotal = it.fitness.div(sumFitness) * 100
		it.percentOfFitness = Math.round(percFitnessTotal * 100) / 100.0 //Round to 2 decimals
		//totalPercOfFitness = totalPercOfFitness.plus(bag.percentOfFitness)
	}
	println("------------------------------------------------------")
	population.forEach(::println)
	println("-----------------------PARENTS SELECTED-------------------------------")
	for(i in 1..reproductionSize) {
		val aleatoryPerc = (1..100).random().toDouble()
		var sumPercFitness = 0.0
		for( bag in population) {
			sumPercFitness = sumPercFitness.plus(bag.percentOfFitness)
			if(sumPercFitness >= aleatoryPerc) {
				parents.add(bag)
				break
			}
		}
	}
	return parents
}

fun calculateFitnessPenalize(population: MutableList<Bag>) {
	sumFitness = 0.0
	for(bag in population){
		var fitness = bag.totalValue.toDouble()
		if(!bag.factible) {
			//penalize more bags with higher weights
			var overweight = bag.totalWeight - 120.0
			if(overweight == 1.0)
				overweight = 1.5
			fitness = bag.totalValue.div(overweight)
		}
		bag.fitness = Math.round(fitness*100) / 100.0
		sumFitness = sumFitness.plus(bag.fitness)
		println(bag)
	}
}

fun calculateFitnessRepair(population: MutableList<Bag>) {
	sumFitness = 0.0
	for(bag in population){
		if(!bag.factible) {
			BagUtil.repairBag(bag)
		}
		bag.fitness = bag.totalValue.toDouble()
		sumFitness = sumFitness.plus(bag.fitness)
		println(bag)
	}
}

fun initializePopulation(): MutableList<Bag> {
	
	val bagsPopulation : MutableList<Bag> = mutableListOf()
	
	while(bagsPopulation.size < populationSize){
		val bag = BagUtil.createAleatoryBag(initialBagSize)
		bagsPopulation.add(bag)
		println(bag)
	}
	println("-----------------------------------------------------")
	return bagsPopulation
}

fun initializeItems(): List<Item> {
	return listOf(Item(3,1), Item(8,3), Item(12,1), Item(2,8), Item(8,9), Item(4,3), Item(4,2), Item(5,8),
	Item(1,5), Item(1,1), Item(8,1), Item(6,6), Item(4,3), Item(3,2), Item(3,5), Item(5,2), Item(7,3),
	Item(3,8), Item(5,9), Item(7,3), Item(4,2), Item(3,4), Item(7,5), Item(2,4), Item(3,3), Item(5,1),
	Item(4,3), Item(3,2), Item(7,14), Item(19,32), Item(20,20), Item(21,19), Item(11,15), Item(24,37),
	Item(13,18), Item(17,13), Item(18,19), Item(6,10), Item(15,15), Item(25,40), Item(12,17), Item(19,39))
}