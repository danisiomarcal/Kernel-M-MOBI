package mobi.test.mobi;

import java.io.IOException;

import mobi.core.Mobi;
import mobi.core.common.Relation;
import mobi.core.concept.Class;
import mobi.core.concept.Instance;

public class TesteMOBIEleicao {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		TesteMOBIEleicao.carregaDominioEleicao();
	}

	public static Mobi carregaDominioEleicao() {
		Mobi mobi = new Mobi("Eleicao");

		Class cCandidato = new Class("Candidato");
		Class cPrefeito  = new Class("Prefeito");
<<<<<<< HEAD
		Class cVice      = new Class("Vice-Prefeito");
		Class cVereador  = new Class("Vereador");
		
		Instance iPrefeito = new Instance("Marcus");
		Instance iVicePrefeito  = new Instance("Vinicius");
		Instance iVereador   = new Instance("Oliveira");
		Instance iCandidato2 = new Instance("Marcus");
		Instance iCandidato3 = new Instance("iCandidato_3");
		
		try {
=======
		Class cVice      = new Class("Vice");
		Class cVereador  = new Class("Vereador");
		Class cPartido   = new Class("Partido");
		Class cPessoa 	 = new Class("Pessoa");
		Class eq1 = new Class("Eq1");
		Class eq2 = new Class("Eq2");
		
		Instance iPrefeitoPinheiro = new Instance("Pinheiro", cPessoa);
		Instance iViceLidice       = new Instance("Lidice", null);
		Instance iVereadorA        = new Instance("VereadorA");
		Instance iPrefeitoTeste    = new Instance("PrefeitoTeste");
		Instance ieq1 = new Instance("iEq1");
		
		try {
			mobi.addConcept(eq1);
			mobi.addConcept(eq2);
			mobi.addConcept(ieq1);
			
			mobi.addConcept(cPessoa);
			mobi.addConcept(iPrefeitoTeste);
			mobi.addConcept(cPartido);
			mobi.linkInstances("PT;PSDB;PDT;PMDB;PSB;Teste", "Partido");

			mobi.addConcept(iPrefeitoPinheiro);

			mobi.addConcept(iViceLidice);
			mobi.addConcept(iVereadorA);

>>>>>>> 0c23cd9e08c58948b226771063086415f12dd17b
			mobi.addConcept(cCandidato);
			mobi.addConcept(cPrefeito);
			mobi.addConcept(cVice);
			mobi.addConcept(cVereador);
<<<<<<< HEAD

			mobi.addConcept(iPrefeito);
			mobi.addConcept(iVicePrefeito);
			mobi.addConcept(iVereador);
			mobi.addConcept(iCandidato2);
			mobi.addConcept(iCandidato3);

			mobi.isOneOf(iPrefeito,  cPrefeito);
			mobi.isOneOf(iVicePrefeito,  cVice);
			mobi.isOneOf(iVereador,  cVereador);
			mobi.isOneOf(iCandidato2,  cCandidato);
			mobi.isOneOf(iCandidato3,  cCandidato);

			Relation rCandaidatoVice = mobi.createInheritanceRelation("CandidatoVice");
			rCandaidatoVice.setClassA(cCandidato);
			rCandaidatoVice.setClassB(cVice);
			rCandaidatoVice.addInstanceRelation(iCandidato2, iVicePrefeito);
			rCandaidatoVice.processCardinality();
			mobi.addConcept(rCandaidatoVice);

			Relation rCandaidatoPrefeito = mobi.createInheritanceRelation("CandidatoPrefeito");
			rCandaidatoPrefeito.setClassA(cCandidato);
			rCandaidatoPrefeito.setClassB(cPrefeito);
			rCandaidatoPrefeito.addInstanceRelation(iCandidato2, iPrefeito);
			rCandaidatoPrefeito.processCardinality();
			mobi.addConcept(rCandaidatoPrefeito);
			
			Relation rCandaidatoVereador = mobi.createInheritanceRelation("CandidatoVereador");
			rCandaidatoVereador.setClassA(cCandidato);
			rCandaidatoVereador.setClassB(cVereador);
			rCandaidatoVereador.addInstanceRelation(iCandidato2, iVereador);
			rCandaidatoVereador.processCardinality();
			mobi.addConcept(rCandaidatoVereador);
			
=======
			mobi.addConcept(cPartido);

			mobi.isOneOf("Pinheiro",      "Candidato");
			mobi.isOneOf("Pinheiro",      "Prefeito");
			mobi.isOneOf("Lidice",        "Candidato");
			mobi.isOneOf("Lidice",        "Vice");
			mobi.isOneOf("VereadorA",     "Vereador");
			mobi.isOneOf("VereadorA",     "Vice");
			mobi.isOneOf("PrefeitoTeste", "Prefeito");
			mobi.isOneOf("iEq1", "Eq1");
			mobi.isOneOf("iEq1", "Eq2");
			
//			System.out.println(mobi.getAllInstances());
//			System.out.println(mobi.getContext());
//			System.out.println(mobi.getAllClasses());

			//Relation rCandaidatoPrefeito = mobi.createInheritanceRelation("CandidatoPrefeito");

			//rCandaidatoPrefeito.setClassA(cCandidato);
			//rCandaidatoPrefeito.setClassB(cPrefeito);
//			rCandaidatoPrefeito.setContext(dEleicao);

			//mobi.addConcept(rCandaidatoPrefeito);
			//rCandaidatoPrefeito.addInstanceRelation(iPrefeitoPinheiro, iPrefeitoPinheiro);

			//rCandaidatoPrefeito.addInstanceRelation(iViceLidice, null);
			//rCandaidatoPrefeito.processCardinality();

			//System.out.println(rCandaidatoPrefeito);
			//System.out.println(rCandaidatoPrefeito.getClassA());
			//System.out.println(rCandaidatoPrefeito.getClassB());
			//System.out.println(rCandaidatoPrefeito.getCardinalityA());
			//System.out.println(rCandaidatoPrefeito.getCardinalityB());
			//System.out.println(rCandaidatoPrefeito.getInstanceRelationMapA().values());

			Relation rCandaidatoVice = mobi.createInheritanceRelation("CandidatoVice");

			rCandaidatoVice.setClassA(cCandidato);
			rCandaidatoVice.setClassB(cVice);
//			rCandaidatoVice.setContext(dEleicao);

			rCandaidatoVice.addInstanceRelation(iViceLidice, iViceLidice);

			rCandaidatoVice.addInstanceRelation(iVereadorA, iVereadorA);

			rCandaidatoVice.addInstanceRelation(iPrefeitoPinheiro, null);
			rCandaidatoVice.processCardinality();
			mobi.addConcept(rCandaidatoVice);

			Relation rCEquivalence = mobi.createEquivalenceRelation("Equivale");

			rCEquivalence.setClassA(eq1);
			rCEquivalence.setClassB(eq2);
//			rCandaidatoVice.setContext(dEleicao);

			rCEquivalence.addInstanceRelation(ieq1, ieq1);
			rCEquivalence.processCardinality();
			mobi.addConcept(rCEquivalence);

			
			Relation rCandaidatoVereador = mobi.createInheritanceRelation("CandidatoVereador");
			 
			rCandaidatoVereador.setClassA(cCandidato);
			rCandaidatoVereador.setClassB(cVereador);
//			rCandaidatoVereador.setContext(dEleicao);


			rCandaidatoVereador.addInstanceRelation(iVereadorA, iVereadorA);

			rCandaidatoVereador.processCardinality();
			mobi.addConcept(rCandaidatoVereador);
			
			Relation rCandaidatoVereador2 = mobi.createInheritanceRelation("CandidatoVereador2");
			rCandaidatoVereador2.setClassA(cPrefeito);
			rCandaidatoVereador2.setClassB(cVereador);
			
			rCandaidatoVereador.addInstanceRelation(iPrefeitoPinheiro, iPrefeitoPinheiro);
			rCandaidatoVereador2.processCardinality();
			mobi.addConcept(rCandaidatoVereador2);
			
			Relation rCandidatoPartido = mobi.createBidirecionalCompositionRelationship("vota", "votado_por");

			rCandidatoPartido.setClassA(cCandidato);
			rCandidatoPartido.setClassB(cPartido);
//			rCandidatoPartido.setContext(dEleicao);

			rCandidatoPartido.addListToInstanceRelation("Pinheiro:PT;Lidice:PSDB;VereadorA:PT;PrefeitoTeste:PSDB");

			rCandidatoPartido.processCardinality();
			
			mobi.addConcept(rCandidatoPartido);

			Relation rPrefeitoVice = mobi.createBidirecionalCompositionRelationship("elege", "eleito_por");

			rPrefeitoVice.setClassA(cCandidato);
			rPrefeitoVice.setClassB(cVice);

			//rPrefeitoVice.addListToInstanceRelation("Pinheiro:Lidice");
			rPrefeitoVice.addInstanceRelation(iPrefeitoPinheiro, iViceLidice);
			rPrefeitoVice.processCardinality();
			
			mobi.addConcept(rPrefeitoVice);
			
			System.out.println("Fim");
>>>>>>> 0c23cd9e08c58948b226771063086415f12dd17b
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return mobi;

	}
}