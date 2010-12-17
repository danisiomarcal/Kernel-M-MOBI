package mobi.test.mobi;

import java.io.IOException;
import java.util.Collection;

import mobi.core.Mobi;
import mobi.core.common.Relation;
import mobi.core.concept.Class;
import mobi.core.concept.Instance;
import mobi.core.manager.InferenceManager;
import mobi.core.relation.EquivalenceRelation;
import mobi.core.relation.GenericRelation;

public class TesteMOBIGenericoHerancaEquivalencia {
	
	public static void main(String[] args) throws IOException,
	ClassNotFoundException {
		TesteMOBIGenericoHerancaEquivalencia.carregaDominioGenerico();
	}

	public static Mobi carregaDominioGenerico() {
		Mobi mobi = new Mobi("DominioGenerico");
		
		try
		{
			Class CDoscente 	 = new Class("cDoscente");
			Class cProfessor 	 = new Class("cProfessor");
			
			Instance iThiago = new Instance("iThiago");
			Instance iDanisio = new Instance("iDanisio");
			
			mobi.addConcept(CDoscente);
			mobi.addConcept(cProfessor);
			
			mobi.addConcept(iThiago);
			mobi.addConcept(iDanisio);
			
			mobi.isOneOf(iThiago, CDoscente);
			mobi.isOneOf(iThiago, cProfessor);
			mobi.isOneOf(iDanisio, cProfessor);
			mobi.isOneOf(iDanisio, CDoscente);
			
			GenericRelation genericRelation = (GenericRelation)mobi.createGenericRelation("generic1");
			genericRelation.setClassA(cProfessor);
			genericRelation.setClassB(CDoscente);
			genericRelation.addInstanceRelation(iThiago, iThiago);
			genericRelation.addInstanceRelation(iDanisio, iDanisio);
			genericRelation.processCardinality();
			mobi.addConcept(genericRelation);
			
			InferenceManager inference = new InferenceManager();
			Collection<Integer> possibilities = inference.infereRelation(genericRelation);
			
			if (possibilities.contains(Relation.EQUIVALENCE)) //Preferred choice
			{
				EquivalenceRelation equivalenceRelation = (EquivalenceRelation)mobi.convertToEquivalenceRelation(genericRelation,"equals");
				mobi.addConcept(equivalenceRelation);
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return mobi;
	}
}
