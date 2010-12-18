package mobi.test.mobi;

import java.io.IOException;
import java.util.Collection;

import mobi.core.Mobi;
import mobi.core.common.Relation;
import mobi.core.concept.Class;
import mobi.core.concept.Instance;
import mobi.core.relation.EquivalenceRelation;
import mobi.core.relation.GenericRelation;
import mobi.core.relation.InheritanceRelation;

public class TesteMOBIGenericoHeranca {

	public static void main(String[] args) throws IOException,
	ClassNotFoundException {
		TesteMOBIGenericoHeranca.carregaDominioGenerico();
	}
	
	public static Mobi carregaDominioGenerico() {
		Mobi mobi = new Mobi("DominioGenerico");
		
		try
		{
			Class cPessoa 	 = new Class("cPessoa");
			Class cProfessor 	 = new Class("cProfessor");
			
			Instance iMaria = new Instance("iMaria");
			Instance iJoao = new Instance("iJoao");
			
			mobi.addConcept(cPessoa);
			mobi.addConcept(cProfessor);
			
			mobi.addConcept(iMaria);
			mobi.addConcept(iJoao);
			
			mobi.isOneOf(iMaria, cPessoa);
			mobi.isOneOf(iMaria, cProfessor);
			mobi.isOneOf(iJoao, cPessoa);
			
			GenericRelation genericRelation = (GenericRelation)mobi.createGenericRelation("generic1");
			genericRelation.setClassA(cPessoa);
			genericRelation.setClassB(cProfessor);
			genericRelation.addInstanceRelation(iMaria, iMaria);
			genericRelation.addInstanceRelation(iJoao, null);
			genericRelation.processCardinality();
			mobi.addConcept(genericRelation);
			
			Collection<Integer> possibilities = mobi.infereRelation(genericRelation);
			
			if (possibilities.contains(Relation.EQUIVALENCE))
			{
				EquivalenceRelation equivalenceRelation = (EquivalenceRelation)mobi.convertToEquivalenceRelation(genericRelation,"equals");
				mobi.addConcept(equivalenceRelation);
			}else //Choice
			{
				InheritanceRelation inheritanceRelation = (InheritanceRelation)mobi.convertToInheritanceRelation(genericRelation,"inheritance");
				mobi.addConcept(inheritanceRelation);
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return mobi;
	}
}
