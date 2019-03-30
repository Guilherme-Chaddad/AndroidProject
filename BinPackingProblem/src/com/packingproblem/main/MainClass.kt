package com.packingproblem.main

import com.packingproblem.entity.Item
import com.packingproblem.util.BagUtil

var sumFitness = 0.0
val probabilityCrossover: Double = 80.0
val probabilityMutation: Double = 5.0
val populationSize: Int = 10
val initialBagSize: Int = 0
val reproductionSize: Int = populationSize
val executionByGeneration = true
val executionUsingPenalization = false
val printInfo = false
val printMapInfo = true

fun main(args: Array<String>){
	val listItems: List<Item> = initializeItems()
	
	BagUtil.listItems = listItems
	
	var population: MutableList<Bag> = initializePopulation()

	if (executionUsingPenalization) calculateFitnessPenalize(population) else calculateFitnessRepair(population)

	var bestBag : Pair<Bag, Int>? = null
	var stopCondition = 0
	var generation = 0
	val limitGeneration = if(executionByGeneration) 500 else 500* populationSize
	val mapFitness : MutableMap<Int, Pair<Double,Double>> = mutableMapOf()
	includeFitnessInfoGeneration(generation, population, mapFitness)

	while(stopCondition < limitGeneration){
		generation++
		if(printInfo) println("-----------------------GENERATION $generation-------------------------------")
		if(printInfo) println("-----------------------PARENTS SELECTED-------------------------------")
		val parents : MutableList<Bag> = selectParents(population)
		if(printInfo) parents.forEach(::println)

		if(printInfo) println("-----------------------REPRODUCTION-------------------------------")
		val children : MutableList<Bag> = doReproduction(parents)
		if(printInfo) children.forEach(::println)

		if(printInfo) println("-----------------------FITNESS CHILDREN-------------------------------")
		//repair/penalize children and calculate fitness
		if (executionUsingPenalization) calculateFitnessPenalize(children) else calculateFitnessRepair(children)

		if(printInfo) println("-----------------------BEST INDIVIDUALS SELECTED-------------------------------")
		//select best individuals
		population = selectBestIndividuals(population, children)
		if(printInfo) population.forEach(::println)

		if (executionByGeneration) stopCondition++ else stopCondition+= reproductionSize

		includeFitnessInfoGeneration(generation, population, mapFitness)
		if(bestBag == null || bestBag.first.fitness < population.get(0).fitness){
			bestBag = Pair(population.get(0), generation)
		}
	}
	println("-----------------------BEST INDIVIDUAL FOUND-------------------------------")
	println("Generation: ${bestBag!!.second} - Bag: ${bestBag!!.first}")
	println(bestBag.second)
	println(bestBag.first.fitness)
	println(bestBag.first.totalItems)
	println(bestBag.first.totalWeight)
	println(bestBag.first.items.joinToString())
	if(printMapInfo) println("----------------------- MAP FITNESS BY GENERATION -------------------------------")
	if(printMapInfo) mapFitness.forEach { k, v -> println("$k,${v.first}")}
}

fun includeFitnessInfoGeneration(generation: Int, population: MutableList<Bag>, mapFitness : MutableMap<Int, Pair<Double, Double>>) {
	val bestFitness = population.sortedWith(compareByDescending {it.fitness} ).get(0).fitness
	val averageFitness = population.sumByDouble { bag -> bag.fitness } / population.size

	mapFitness.put(generation, Pair(bestFitness, averageFitness))
}

fun selectBestIndividuals(parents: MutableList<Bag>, children: MutableList<Bag>) : MutableList<Bag> {

	var allIndividuals: MutableList<Bag> = mutableListOf()
	allIndividuals.addAll(parents)
	allIndividuals.addAll(children)

	allIndividuals = allIndividuals.sortedWith(compareByDescending {it.fitness} ).toMutableList()
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

		arrayBagChild1 = doMutation(arrayBagChild1)
		arrayBagChild2 = doMutation(arrayBagChild2)

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

		for (crossoverIndex in pointOfCrossover+1..41) {
			arrayBagChild1[crossoverIndex] = parent2.items[crossoverIndex]
			arrayBagChild2[crossoverIndex] = parent1.items[crossoverIndex]
		}
	} else {
		arrayBagChild1 = parent1.items
		arrayBagChild2 = parent2.items
	}
	return Pair(arrayBagChild1, arrayBagChild2)
}

fun doMutation(arrayBag : IntArray) : IntArray{
	var arrayBagMutated = IntArray(arrayBag.size)
	for(i in 0..41){
		val aleatoryMutation = (1..100).random()
		if(aleatoryMutation < probabilityMutation){
			if(arrayBag[i] == 0)
				arrayBagMutated[i] = 1
			else
				arrayBagMutated[i] = 0
		} else {
			arrayBagMutated[i] = arrayBag[i]
		}
	}
	return arrayBagMutated
}

fun selectParents(population: MutableList<Bag>) : MutableList<Bag> {
	val parents : MutableList<Bag> = mutableListOf()

	sumFitness = 0.0
	population.forEach { sumFitness = sumFitness.plus(it.fitness) }
	if(printInfo) println("SUM FITNESS $sumFitness")

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

	if(printInfo) println("-----------------------POPULATION AFTER PENALIZATION-------------------------------")
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
		if(printInfo) println(bag)
	}
}

fun calculateFitnessRepair(population: MutableList<Bag>) {
	if(printInfo) println("-----------------------POPULATION AFTER REPAIR-------------------------------")
	for(bag in population){
		if(!bag.factible) {
			BagUtil.repairBag(bag)
		}
		bag.fitness = bag.totalValue.toDouble()
		if(printInfo) println(bag)
	}
}

fun initializePopulation(): MutableList<Bag> {
	
	val bagsPopulation : MutableList<Bag> = mutableListOf()

	if(printInfo) println("-----------------------INITIAL POPULATION-------------------------------")
	while(bagsPopulation.size < populationSize){
		val bag = BagUtil.createAleatoryBag(initialBagSize)
		bagsPopulation.add(bag)
		if(printInfo) println(bag)
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