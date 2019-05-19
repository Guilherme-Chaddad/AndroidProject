package com.myFuzzyProject.negotiation;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;

public class NegotiationExample2BothFuzzy {

	static Double initialPrice = 4000.0;
	static Double utility = 0.92;
	static Double urgency = 0.0;
	
	static Integer round = 1;
	static Double vendorUrgency = 0.5;
	public static void main(String[] args) throws Exception {
				
		Double offerVendor = initialPrice;
		Double offerBuyer = 3000.0;
		Double nextOfferVendor = 0.0;
		Double nextOfferBuyer = 0.0;
		boolean buyerTurn = true;
		
		Double incrementPercent = initializeFuzzyBuyer();
		Double reductionPercent = initializeFuzzyVendor();
		
		System.out.println(String.format("-------------------ROUND %d-------------------------", round));
		System.out.println("OFFER VENDOR");
		nextOfferBuyer = offerBuyer + (offerBuyer * (incrementPercent / 100));
		boolean thereIsAgreement = makeOffer(offerVendor, nextOfferBuyer, false);
		
		while(!thereIsAgreement) {
			round++;
			System.out.println(String.format("-------------------ROUND %d-------------------------", round));
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
		}
	}

	private static double initializeFuzzyBuyer() {
		String filename = "NegotiationBuyerExample2.fcl";
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
		fb.setVariable("urgency", urgency);

		// Evaluate
		fb.evaluate();

		// Show output variable's chart
		fb.getVariable("increment").defuzzify();

		double incrementPercent = fb.getVariable("increment").getValue();
		System.out.println("Incremento: " + incrementPercent);
		return incrementPercent;
	}

	private static Double initializeFuzzyVendor() {
		String filename = "NegotiationVendorExample2.fcl";
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

		// Evaluate
		fb.evaluate();

		// Show output variable's chart
		fb.getVariable("reduction").defuzzify();

		// Print ruleSet
		//System.out.println(fb);
		double reductionPercent = fb.getVariable("reduction").getValue();
		System.out.println("Reduction: " + reductionPercent);
		return reductionPercent;
	}
	
	private static boolean makeOffer(Double offer, Double valueToAcceptOffer, boolean isBuyer) {
		System.out.println(String.format("Offer: %.2f - Next Offer: %.2f", offer, valueToAcceptOffer));
		return isBuyer ? offer >= valueToAcceptOffer : offer <= valueToAcceptOffer;
	}
}
