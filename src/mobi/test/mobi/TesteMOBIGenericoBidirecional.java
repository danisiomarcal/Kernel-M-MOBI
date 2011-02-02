package mobi.test.mobi;

import java.io.IOException;
import java.util.Collection;

import mobi.core.Mobi;
import mobi.core.common.Relation;
import mobi.core.concept.Class;
import mobi.core.concept.Instance;
import mobi.core.relation.CompositionRelation;
import mobi.core.relation.GenericRelation;

public class TesteMOBIGenericoBidirecional {
	public static void main(String[] args) throws IOException,
	ClassNotFoundException {
		TesteMOBIGenericoBidirecional.carregaDominioGenerico();
	}
	
	public static Mobi carregaDominioGenerico() {
		Mobi mobi = new Mobi("DominioGenerico");
		
		try
		{
			Class cCandidato = new Class("cCandidato");
			Class cPartido 	 = new Class("cPartido");
			
			Instance iPT = new Instance("iPT");
			Instance iPSDB = new Instance("iPSDB");
			Instance iPMDB = new Instance("iPMDB");
			Instance iPSOL = new Instance("iPSOL");
			
			Instance iJoseSerra = new Instance("iJoseSerra");
			Instance iMarina = new Instance("iMarina");
			Instance iPlinio = new Instance("iPlinio");
			Instance iJoaoHenrique = new Instance("iJoaoHenrique");
			Instance iJoseMaria = new Instance("iJoseMaria");
			Instance iIsmael = new Instance("iIsmael");
			
			mobi.addConcept(cCandidato);
			mobi.addConcept(cPartido);
			
			mobi.addConcept(iPT);
			mobi.addConcept(iPSDB);
			mobi.addConcept(iPMDB);
			mobi.addConcept(iPSOL);
			
			mobi.isOneOf(iPT, cPartido);
			mobi.isOneOf(iPSDB, cPartido);
			mobi.isOneOf(iPMDB, cPartido);
			mobi.isOneOf(iPSOL, cPartido);
			
			mobi.addConcept(iJoseSerra);
			mobi.addConcept(iMarina);
			mobi.addConcept(iPlinio);
			mobi.addConcept(iJoaoHenrique);
			mobi.addConcept(iJoseMaria);
			mobi.addConcept(iIsmael);
			
			mobi.isOneOf(iJoseSerra, cCandidato);
			mobi.isOneOf(iMarina, cCandidato);
			mobi.isOneOf(iPlinio, cCandidato);
			mobi.isOneOf(iJoaoHenrique, cCandidato);
			mobi.isOneOf(iJoseMaria, cCandidato);
			mobi.isOneOf(iIsmael, cCandidato);
			
			GenericRelation genericRelation = (GenericRelation)mobi.createGenericRelation("generic1");
			genericRelation.setClassA(cPartido);
			genericRelation.setClassB(cCandidato);
			genericRelation.addInstanceRelation(iPSDB, iJoseSerra);
			genericRelation.addInstanceRelation(iPSOL, iPlinio);
			genericRelation.addInstanceRelation(iPMDB, iMarina);
			genericRelation.addInstanceRelation(iPMDB, null);
			genericRelation.addInstanceRelation(iPT, iJoseMaria);
			genericRelation.addInstanceRelation(iPSDB, iIsmael);
			genericRelation.addInstanceRelation(iPT, iPlinio);
			
			genericRelation.processCardinality();
			mobi.addConcept(genericRelation);
			
			Collection<Integer> possibilities = mobi.infereRelation(genericRelation);
			
			for(Integer i: possibilities)
				System.out.println(i.toString());
			
			if(possibilities.contains(Relation.BIDIRECIONAL_COMPOSITION)) //Preferred choice
			{
				CompositionRelation composition = 
					(CompositionRelation)mobi.convertToBidirecionalCompositionRelationship(genericRelation, "tem", "pertence");
				mobi.addConcept(composition);
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return mobi;
	}
}
