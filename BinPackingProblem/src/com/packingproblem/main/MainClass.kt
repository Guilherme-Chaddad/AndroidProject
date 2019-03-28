package com.packingproblem.main

import com.packingproblem.entity.Item
import com.packingproblem.util.BagUtil

var sumFitness = 0.0

fun main(args: Array<String>){
	val listItems: List<Item> = initializeItems()
	val probabilityCrossover: Double = 0.80
	val probabilityMutation: Double = 0.05
	val populationSize: Int = 10
	val reproductionSize: Int = populationSize
	
	BagUtil.listItems = listItems
	
	var population: MutableSet<Bag> = initializePopulation(populationSize)
	
	//println("------------------------------------------------------")
	//calculateFitnessPenalize(population)
	calculateFitnessRepair(population)
	
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
	population.forEach { bag -> bag.percentOfFitness = Math.round(bag.fitness.div(sumFitness) * 100) }
	
	println("--------------------------------------------")
	
	population.forEach {bag -> println(bag)}
	return parents
}

fun calculateFitnessPenalize(population: MutableSet<Bag>) {
	sumFitness = 0.0
	for(bag in population){
		var percentOfWeight : Double = bag.totalWeight.div(120.0)
		var fitness = percentOfWeight
		if(bag.factible) {
			fitness = percentOfWeight * bag.totalValue
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
		//var fitness = bag.totalWeight + bag.totalValue
		var fitness = bag.totalWeight.div(120.0) * bag.totalValue
		bag.fitness = (Math.round(fitness*100.0) / 100.0) + bag.totalItems
		sumFitness = sumFitness.plus(bag.fitness)
		println(bag)
	}
}

fun initializePopulation(populationSize: Int): MutableSet<Bag> {
	
	val bagsPopulation : MutableSet<Bag> = mutableSetOf()
	
	while(bagsPopulation.size <= populationSize){
		val bag = BagUtil.createAleatoryBag()
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