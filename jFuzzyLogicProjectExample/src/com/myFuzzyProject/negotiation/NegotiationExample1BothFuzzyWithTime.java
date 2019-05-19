package com.myFuzzyProject.negotiation;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;

public class NegotiationExample1BothFuzzyWithTime {

	static Double initialPrice = 584.0;
	static Double utility = 0.4476;
	static Integer round = 1;
	static Integer time = 20;
	static Double vendorUrgency = 0.5;
	public static void main(String[] args) throws Exception {
				
		Double offerVendor = initialPrice;
		Double offerBuyer = 300.0;
		Double nextOfferVendor = 0.0;
		Double nextOfferBuyer = 0.0;
		boolean buyerTurn = true;
		
		Double incrementPercent = calculateFuzzyBuyer();
		Double reductionPercent = calculateFuzzyVendor();
		
		System.out.println(String.format("-------------------ROUND %d-------------------------", round));
		System.out.println("OFFER VENDOR");
		nextOfferBuyer = offerBuyer + (offerBuyer * (incrementPercent / 100));
		boolean thereIsAgreement = makeOffer(offerVendor, nextOfferBuyer, false);
		round++;
		time--;
		while(!thereIsAgreement && time > 0) {
			System.out.println(String.format("-------------------ROUND %d-------------------------", round));

			incrementPercent = calculateFuzzyBuyer();
			reductionPercent = calculateFuzzyVendor();
			if(buyerTurn) {
				System.out.println("OFFER BUYER");
				offerBuyer = nextOfferBuyer;
				nextOfferVendor = offerVendor - (offerVendor * (reductionPercent / 100));
				thereIsAgreement = makeOffer(offerBuyer, nextOfferVendor, true);
			} else {
				System.out.println("OFFER VENDOR");
				offerVendor = nextOfferVendor;
				nextOfferBuyer = offerBuyer + (offerBuyer * (incrementPercent / 100));
				thereIsAgreement = makeOffer(offerVendor, nextOfferBuyer, false);
			}
			buyerTurn = !buyerTurn;
			round++;
			time--;
		}
	}

	private static double calculateFuzzyBuyer() {
		String filename = "NegotiationBuyerExample1Time.fcl";
		FIS fis = FIS.load(filename, true);

		if (fis == null) {
			System.err.println("Can't load file: '" + filename + "'");
			System.exit(1);
		}
		// Get default function block
		FunctionBlock fb = fis.getFunctionBlock(null);

		// Set inputs
		fb.setVariable("price", initialPrice);
		fb.setVariable("utility", utility);
		fb.setVariable("time", time);

		// Evaluate
		fb.evaluate();

		// Show output variable's chart
		fb.getVariable("increment").defuzzify();

		// Print ruleSet
		//System.out.println(fb);
		double incrementPercent = fb.getVariable("increment").getValue();
		System.out.println("Incremento: " + incrementPercent);
		return incrementPercent;
	}


	private static Double calculateFuzzyVendor() {
		String filename = "NegotiationVendorExample1Time.fcl";
		FIS fis = FIS.load(filename, true);

		if (fis == null) {
			System.err.println("Can't load file: '" + filename + "'");
			System.exit(1);
		}
		// Get default function block
		FunctionBlock fb = fis.getFunctionBlock(null);

		// Set inputs
		fb.setVariable("price", initialPrice);
		fb.setVariable("urgency", vendorUrgency);
		fb.setVariable("time", time);

		// Evaluate
		fb.evaluate();

		// Show output variable's chart
		fb.getVariable("reduction").defuzzify();

		// Print ruleSet
		double reductionPercent = fb.getVariable("reduction").getValue();
		System.out.println("Reduction: " + reductionPercent);
		return reductionPercent;
	}
	
	private static boolean makeOffer(Double offer, Double valueToAcceptOffer, boolean isBuyer) {
		System.out.println(String.format("Offer: %.2f - Next Offer: %.2f", offer, valueToAcceptOffer));
		return isBuyer ? offer >= valueToAcceptOffer : offer <= valueToAcceptOffer;
	}
}
