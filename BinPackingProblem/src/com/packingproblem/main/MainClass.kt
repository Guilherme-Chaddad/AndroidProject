package com.packingproblem.main

import com.packingproblem.entity.Item
import com.packingproblem.util.BagUtil
import kotlin.streams.toList

var sumFitness = 0.0
val probabilityCrossover: Double = 80.0
val probabilityMutation: Double = 5.0
val populationSize: Int = 10
val initialBagSize: Int = 0
val reproductionSize: Int = populationSize
val executionByGeneration = true
val executionUsingPenalization = true

fun main(args: Array<String>){
	val listItems: List<Item> = initializeItems()
	
	BagUtil.listItems = listItems
	
	var population: MutableList<Bag> = initializePopulation()

	if (executionUsingPenalization) calculateFitnessPenalize(population) else calculateFitnessRepair(population)

	var bestBag : Pair<Bag, Int>? = null
	var stopCondition = 0
	var execution = 0
	val limitGeneration = if(executionByGeneration) 500 else 500* populationSize

	while(stopCondition < limitGeneration){
		execution++
		println("-----------------------PARENTS SELECTED-------------------------------")
		val parents : MutableList<Bag> = selectParents(population)
		parents.forEach(::println)

		println("-----------------------REPRODUCTION-------------------------------")
		val children : MutableList<Bag> = doReproduction(parents)
		children.forEach(::println)

		println("-----------------------FITNESS CHILDREN-------------------------------")
		//repair/penalize children and calculate fitness
		if (executionUsingPenalization) calculateFitnessPenalize(children) else calculateFitnessRepair(children)

		println("-----------------------BEST INDIVIDUALS SELECTED-------------------------------")
		//select best individuals
		population = selectBestIndividuals(population, children)
		population.forEach(::println)

		if (executionByGeneration) stopCondition++ else stopCondition+= reproductionSize

		if(bestBag == null || bestBag.first.fitness < population.get(0).fitness){
			bestBag = Pair(population.get(0), execution)
		}
	}
	println("-----------------------BEST INDIVIDUAL FOUND-------------------------------")
	println("Execution: ${bestBag!!.second} - Bag: ${bestBag!!.first}")
}

fun selectBestIndividuals(parents: MutableList<Bag>, children: MutableList<Bag>) : MutableList<Bag> {

	var allIndividuals: MutableList<Bag> = mutableListOf()
	allIndividuals.addAll(parents)
	allIndividuals.addAll(children)

	allIndividuals = allIndividuals.sortedWith(compareByDescending({it.fitness})).toMutableList()
	/*allIndividuals = allIndividuals
							.stream()
							.sorted(Comparator { bag1, bag2 -> bag2.fitness.compareTo(bag1.fitness)})
							.toList()
							.toMutableList()*/

	/*println("-----------------------ORDERED ELEMENTS-------------------------------")
	allIndividuals.forEach(::println)*/

	return allIndividuals.take(populationSize).toMutableList()
}

fun doReproduction(parents: MutableList<Bag>) : MutableList<Bag> {
	val children : MutableList<Bag> = mutableListOf()

	//crossover
	for (i in 0 until parents.size step 2) {

		val parent1 = parents[i]
		val parent2 = parents[i+1]

		var (arrayBagChild1: IntArray, arrayBagChild2: IntArray) = doCrossover(parent1, parent2)

		doMutation(arrayBagChild1)
		doMutation(arrayBagChild2)

		children.add(BagUtil.createBag(arrayBagChild1))
		children.add(BagUtil.createBag(arrayBagChild2))
	}

	return children
}

fun doCrossover(parent1: Bag,parent2: Bag): Pair<IntArray, IntArray> {
	var arrayBagChild1: IntArray = IntArray(42)
	var arrayBagChild2: IntArray = IntArray(42)

	val aleatoryCrossover = (1..100).random()

	if (aleatoryCrossover < probabilityCrossover) {

		val pointOfCrossover = (0..41).random()

		for (crossoverIndex in 0..pointOfCrossover) {
			arrayBagChild1[crossoverIndex] = parent1.items[crossoverIndex]
			arrayBagChild2[crossoverIndex] = parent2.items[crossoverIndex]
		}

		for (crossoverIndex in pointOfCrossover..41) {
			arrayBagChild1[crossoverIndex] = parent2.items[crossoverIndex]
			arrayBagChild2[crossoverIndex] = parent1.items[crossoverIndex]
		}
	} else {
		arrayBagChild1 = parent1.items
		arrayBagChild2 = parent2.items
	}
	return Pair(arrayBagChild1, arrayBagChild2)
}

fun doMutation(arrayBag : IntArray) {
	for(i in 0..41){
		val aleatoryMutation = (1..100).random()
		if(aleatoryMutation < probabilityMutation){
			if(arrayBag[i] == 0)
				arrayBag[i] = 1
			else
				arrayBag[i] = 0
		}
	}
}

fun selectParents(population: MutableList<Bag>) : MutableList<Bag> {
	val parents : MutableList<Bag> = mutableListOf()

	sumFitness = 0.0
	population.forEach { sumFitness = sumFitness.plus(it.fitness) }
	println("SUM FITNESS $sumFitness")

	//var totalPercOfFitness = 0.0
	population.forEach {
		val percFitnessTotal = it.fitness.div(sumFitness) * 100
		it.percentOfFitness = Math.round(percFitnessTotal * 100000) / 100000.0 //Round to 5 decimals
		//totalPercOfFitness = totalPercOfFitness.plus(bag.percentOfFitness)
	}

	for(i in 1..reproductionSize) {
		val aleatoryPerc = (1..100).random().toDouble()
		var sumPercFitness = 0.0
		var j = 0
		for( bag in population) {
			sumPercFitness = sumPercFitness.plus(bag.percentOfFitness)
			if(sumPercFitness >= aleatoryPerc || j == population.lastIndex) {
				parents.add(bag)
				break
			}
			j++
		}
	}
	return parents
}

fun calculateFitnessPenalize(population: MutableList<Bag>) {

	println("-----------------------POPULATION AFTER PENALIZATION-------------------------------")
	for(bag in population){
		var fitness = bag.totalValue.toDouble()
		if(!bag.factible) {
			//penalize more bags with higher weights
			var overweight = bag.totalWeight - 120.0
			if(overweight == 1.0)
				overweight = 1.5
			fitness = bag.totalValue.div(overweight)
		}
		bag.fitness = Math.round(fitness*10000) / 10000.0
		println(bag)
	}
}

fun calculateFitnessRepair(population: MutableList<Bag>) {
	println("-----------------------POPULATION AFTER REPAIR-------------------------------")
	for(bag in population){
		if(!bag.factible) {
			BagUtil.repairBag(bag)
		}
		bag.fitness = bag.totalValue.toDouble()
		println(bag)
	}
}

fun initializePopulation(): MutableList<Bag> {
	
	val bagsPopulation : MutableList<Bag> = mutableListOf()

	println("-----------------------INITIAL POPULATION-------------------------------")
	while(bagsPopulation.size < populationSize){
		val bag = BagUtil.createAleatoryBag(initialBagSize)
		bagsPopulation.add(bag)
		println(bag)
	}
	return bagsPopulation
}

fun initializeItems(): List<Item> {
	return listOf(Item(3,1), Item(8,3), Item(12,1), Item(2,8), Item(8,9), Item(4,3), Item(4,2), Item(5,8),
	Item(1,5), Item(1,1), Item(8,1), Item(6,6), Item(4,3), Item(3,2), Item(3,5), Item(5,2), Item(7,3),
	Item(3,8), Item(5,9), Item(7,3), Item(4,2), Item(3,4), Item(7,5), Item(2,4), Item(3,3), Item(5,1),
	Item(4,3), Item(3,2), Item(7,14), Item(19,32), Item(20,20), Item(21,19), Item(11,15), Item(24,37),
	Item(13,18), Item(17,13), Item(18,19), Item(6,10), Item(15,15), Item(25,40), Item(12,17), Item(19,39))
}