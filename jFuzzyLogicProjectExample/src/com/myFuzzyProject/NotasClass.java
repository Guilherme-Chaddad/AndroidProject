package com.myFuzzyProject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;

public class NotasClass {
	public static void main(String[] args) throws Exception {
		String filename = "Notas.fcl";
		FIS fis = FIS.load(filename, true);

		if (fis == null) {
			System.err.println("Can't load file: '" + filename + "'");
			System.exit(1);
		}

		File file = new File("dados_autent_bancaria.txt"); 
		  
		BufferedReader br = new BufferedReader(new FileReader(file)); 

		int right = 0;
		int wrong = 0;
		int linha = 1;
		String st; 
		while ((st = br.readLine()) != null) {
			System.out.println(st); 
//			if(linha != 5) {
//				linha++;
//				continue;
//			}
			
			String[] arrayProperties = st.split(",");
			
			double variance = Double.parseDouble(arrayProperties[0]);
			double skewness = Double.parseDouble(arrayProperties[1]);
			double curtosis = Double.parseDouble(arrayProperties[2]);
			double entropy = Double.parseDouble(arrayProperties[3]);
			int expectedValue = Integer.parseInt(arrayProperties[4]);
			
			// Get default function block
			FunctionBlock fb = fis.getFunctionBlock(null);

			// Set inputs
			fb.setVariable("Variance", variance);
			fb.setVariable("Skewness", skewness);
			fb.setVariable("Curtosis", curtosis);
			fb.setVariable("Entropy", entropy);

			// Evaluate
			fb.evaluate();

			// Show output variable's chart
			fb.getVariable("nota").defuzzify();

			// Print ruleSet
			double autentica = fb.getVariable("nota").getMembership("autentica");
			double falsa = fb.getVariable("nota").getMembership("falsa");
			boolean notaAutentica = falsa < autentica;
			boolean shouldBeAuthentic = expectedValue == 0;
			boolean rightAnswer = shouldBeAuthentic == notaAutentica;
			
			if(rightAnswer)
				right++;
			else
				wrong++;
			
//			System.out.println("Nivel Autentica: " + autentica);
//			System.out.println("Nivel Falsa: " + falsa);
//			System.out.println("Nota Autentica: " + notaAutentica);
//			System.out.println("Valor Nota Defuzzificada: " + fb.getVariable("nota").getValue());
//			
			System.out.println("Acertou o resultado: "+ rightAnswer);
			System.out.println("-----------------------------------------------------------");
			linha++;
		} 
		
		System.out.println("Numero acertos: "+right+" - Percentual de acerto: "+ (right/1372.0 * 100));
		System.out.println("Numero erros: "+wrong+" - Percentual de erro: "+ (wrong/1372.0 * 100));
	}
}
