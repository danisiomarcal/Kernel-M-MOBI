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

		Class cPessoa = new Class("cPessoa");
		Class cMacaco  = new Class("cMacaco");
		
		Instance iThiago = new Instance("iThiago");
		Instance iMacaco = new Instance("iMacaco");
		Instance iMacaco2 = new Instance("iMacaco2");
		Instance iLeticia = new Instance("iLeticia");
		
		try {
			mobi.addConcept(cPessoa);
			mobi.addConcept(cMacaco);

			mobi.isOneOf(iThiago,  cPessoa);
			mobi.isOneOf(iLeticia,  cPessoa);
			mobi.isOneOf(iMacaco2,  cMacaco);
			mobi.isOneOf(iMacaco,  cMacaco);
			
			Relation rTemFilho = mobi.createUnidirecionalCompositionRelationship("TemFilho");
			rTemFilho.setClassA(cPessoa);
			rTemFilho.setClassB(cPessoa);
			rTemFilho.addInstanceRelation(iThiago, iLeticia);
			rTemFilho.processCardinality();
			mobi.addConcept(rTemFilho);
			
			Relation rTemFilho2 = mobi.createUnidirecionalCompositionRelationship("TemFilho");
			rTemFilho2.setClassA(cMacaco);
			rTemFilho2.setClassB(cMacaco);
			rTemFilho2.addInstanceRelation(iMacaco, iMacaco2);
			rTemFilho2.processCardinality();
			mobi.addConcept(rTemFilho2);
			
		} catch (Exception e) {
			throw e;
		}
		return mobi;

	}
}