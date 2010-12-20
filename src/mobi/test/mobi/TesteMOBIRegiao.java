package mobi.test.mobi;

import java.io.IOException;

import mobi.core.Mobi;
import mobi.core.common.Relation;
import mobi.core.concept.Class;
import mobi.core.concept.Instance;


public class TesteMOBIRegiao {

	public static void main(String[] args) throws IOException, ClassNotFoundException { 
		TesteMOBIRegiao.carregaDominioRegiao();
	}    
	public static Mobi carregaDominioRegiao() {	

		Mobi mobi = new Mobi("Regiao");
		
        Instance iSul       = new Instance("Sul");
        Instance iNorte     = new Instance("Norte");
        Instance iNordeste  = new Instance("Nordeste");
        Instance iBrasil    = new Instance("Brasil");
        Instance iArgentina = new Instance("Argentina");
        
        Class cPais   = new Class("Pais");
        Class cRegiao = new Class("Regiao");
        Class cEstado = new Class("Estado");
        
		try {

			mobi.addConcept(iBrasil);
			mobi.addConcept(iArgentina);

			mobi.addConcept(iNorte);
			mobi.addConcept(iNordeste);
			mobi.addConcept(iSul);

			mobi.addConcept(cPais);
			mobi.addConcept(cRegiao);
			mobi.addConcept(cEstado);
			
			mobi.isOneOf("Brasil", "Pais");
			mobi.isOneOf("Argentina", "Pais");

			mobi.isOneOf("Norte", "Regiao");
			mobi.isOneOf("Nordeste", "Regiao");
			mobi.isOneOf("Sul", "Regiao");

			Relation rPaisRegiao = mobi.createBidirecionalCompositionRelationship("tem", "pertence");

			rPaisRegiao.setClassA(cPais);
			rPaisRegiao.setClassB(cRegiao);

			rPaisRegiao.addInstanceRelation(iArgentina, iNordeste);
			rPaisRegiao.addInstanceRelation(iBrasil, iNorte);
			rPaisRegiao.addInstanceRelation(iBrasil, iSul);
			rPaisRegiao.addInstanceRelation(iBrasil, iNordeste);
			rPaisRegiao.processCardinality();
			mobi.addConcept(rPaisRegiao);
			
			Instance iBahia = new Instance("Bahia");
			mobi.addConcept(iBahia);
			mobi.isOneOf("Bahia", "Estado");

			Instance iMinas = new Instance("Minas_Gerais");
			mobi.addConcept(iMinas);
			mobi.isOneOf("Minas_Gerais", "Estado");

			Instance iPernambuco = new Instance("Pernambuco");
			mobi.addConcept(iPernambuco);
			mobi.isOneOf("Pernambuco", "Estado");
			
			Instance iRiodeJaneiro = new Instance("Rio_de_Janeiro");
			mobi.addConcept(iRiodeJaneiro);
			mobi.isOneOf("Rio_de_Janeiro", "Estado");

			Instance iSaoPaulo = new Instance("Sao_Paulo");
			mobi.addConcept(iSaoPaulo);
			mobi.isOneOf("Sao_Paulo", "Estado");

			Relation rRegiaoEstado = mobi.createBidirecionalCompositionRelationship("tem", "pertence");

			rRegiaoEstado.setClassA(cRegiao);
			rRegiaoEstado.setClassB(cEstado);
			rRegiaoEstado.addInstanceRelation(iNordeste, iBahia);
			rRegiaoEstado.addInstanceRelation(iNordeste, iPernambuco);
			rRegiaoEstado.processCardinality();
			mobi.addConcept(rRegiaoEstado);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mobi;

	}
}