package mobi.test.mobi;

import java.io.IOException;

import mobi.core.Mobi;
import mobi.core.common.Relation;
import mobi.core.concept.Class;
import mobi.core.concept.Instance;

public class TesteMOBIEleicao {

	public static void main(String[] args) throws IOException, ClassNotFoundException, Exception {
		TesteMOBIEleicao.carregaDominioEleicao();
	}

	public static Mobi carregaDominioEleicao() throws Exception {
		Mobi mobi = new Mobi("Eleicao");

		Class cCandidato = new Class("Candidato");
		Class cPrefeito  = new Class("Prefeito");
		Class cVice      = new Class("Vice-Prefeito");
		Class cVereador  = new Class("Vereador");
		
		Instance iPrefeito = new Instance("Marcus");
		Instance iVicePrefeito  = new Instance("Vinicius");
		Instance iVereador   = new Instance("Oliveira");
		Instance iCandidato2 = new Instance("Marcus");
		Instance iCandidato3 = new Instance("iCandidato_3");
		
		try {
			mobi.addConcept(cCandidato);
			mobi.addConcept(cPrefeito);
			mobi.addConcept(cVice);
			mobi.addConcept(cVereador);

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

//			Relation rCandaidatoVice = mobi.createInheritanceRelation("CandidatoVice");
//			rCandaidatoVice.setClassA(cCandidato);
//			rCandaidatoVice.setClassB(cVice);
//			rCandaidatoVice.addInstanceRelation(iCandidato2, iVicePrefeito);
//			rCandaidatoVice.processCardinality();
//			mobi.addConcept(rCandaidatoVice);

			Relation rCandaidatoPrefeito = mobi.createInheritanceRelation("CandidatoPrefeito");
			rCandaidatoPrefeito.setClassA(cCandidato);
			rCandaidatoPrefeito.setClassB(cPrefeito);
			rCandaidatoPrefeito.addInstanceRelation(iCandidato2, iPrefeito);
			rCandaidatoPrefeito.processCardinality();
			mobi.addConcept(rCandaidatoPrefeito);
//			
//			Relation rCandaidatoVereador = mobi.createInheritanceRelation("CandidatoVereador");
//			rCandaidatoVereador.setClassA(cCandidato);
//			rCandaidatoVereador.setClassB(cVereador);
//			rCandaidatoVereador.addInstanceRelation(iCandidato2, iVereador);
//			rCandaidatoVereador.processCardinality();
//			mobi.addConcept(rCandaidatoVereador);
			
		} catch (Exception e) {
			throw e;
		}
		return mobi;

	}
}