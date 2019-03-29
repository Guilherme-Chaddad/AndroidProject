package com.packingproblem.main

import com.packingproblem.entity.Item
import com.packingproblem.util.BagUtil

var sumFitness = 0.0

fun main(args: Array<String>){
	val listItems: List<Item> = initializeItems()
	val probabilityCrossover: Double = 0.80
	val probabilityMutation: Double = 0.05
	val populationSize: Int = 10
	val initialBagSize: Int = 15
	val reproductionSize: Int = populationSize
	
	BagUtil.listItems = listItems
	
	var population: MutableSet<Bag> = initializePopulation(populationSize, initialBagSize)
	
	//println("------------------------------------------------------")
	calculateFitnessPenalize(population)
	//calculateFitnessRepair(population)
	
	var numberOfReproduction = 0
	//while(numberOfReproduction < 500){
		
		val parents : MutableSet<Bag> = selectParents(population)
		
		//reprodução
		
		//repara/penaliza filhos e calcula fitness
		
		//seleciona populacao - repete de acordo com criterio de parada
		
		numberOfReproduction++;
	//}
}

fun selectParents(population: MutableSet<Bag>) : MutableSet<Bag> {
	val parents : MutableSet<Bag> = mutableSetOf()
	
	println(sumFitness)

	var totalPercOfFitness = 0.0
	population.forEach { bag ->
		val percFitnessTotal = bag.fitness.div(sumFitness) * 100
		bag.percentOfFitness = Math.round(percFitnessTotal * 100) / 100.0 //Round to 2 decimals
		totalPercOfFitness = totalPercOfFitness.plus(bag.percentOfFitness)
	}
	
	println("--------------------------------------------")
	
	population.forEach {bag -> println(bag)}
	println("Perc Total Fitness: $totalPercOfFitness")
	return parents
}

fun calculateFitnessPenalize(population: MutableSet<Bag>) {
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

fun calculateFitnessRepair(population: MutableSet<Bag>) {
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

fun initializePopulation(populationSize: Int, initialBagSize: Int): MutableSet<Bag> {
	
	val bagsPopulation : MutableSet<Bag> = mutableSetOf()
	
	while(bagsPopulation.size <= populationSize){
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