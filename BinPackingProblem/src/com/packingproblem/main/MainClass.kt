package com.packingproblem.main

import com.packingproblem.util.BagUtil

fun main(args: Array<String>){
	val listItems: List<Item> = initializeItems()
	val probabilityCrossover: Double = 0.80
	val probabilityMutation: Double = 0.05
	val populationSize: Int = 10
	val reproductionSize: Int = populationSize
	
	BagUtil.listItems = listItems
	
	var initialPopulation: MutableSet<Bag> = initializePopulation(populationSize, listItems)
	
	println("------------------------------------------------------")
	//calculateFitnessPenalize(initialPopulation)
	calculateFitnessRepair(initialPopulation)
	
	var numberOfReproduction = 0
	while(numberOfReproduction < 500){
		
		//selecionar cromossomos roleta
		
		//reprodução
		
		//repara/penaliza filhos e calcula fitness
		
		//seleciona populacao - repete de acordo com criterio de parada
		
		numberOfReproduction++;
	}
}

fun calculateFitnessPenalize(population: MutableSet<Bag>) {
	for(bag in population){
		var fitness : Double = bag.totalWeight.div(120.0)
		if(bag.factible) {
			fitness = fitness * 10
		}
		bag.fitness = fitness
		
		println(bag)
	}
}

fun calculateFitnessRepair(population: MutableSet<Bag>) {
	for(bag in population){
		if(bag.factible) {
			BagUtil.repairBag(bag)
		}
		bag.fitness = bag.totalWeight.div(120.0) * 10
		
		println(bag)
	}
}

fun initializePopulation(populationSize: Int, listItems: List<Item>): MutableSet<Bag> {
	
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