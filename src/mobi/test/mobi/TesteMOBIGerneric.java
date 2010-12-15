package mobi.test.mobi;

import java.io.IOException;

import mobi.core.Mobi;
import mobi.core.common.Relation;
import mobi.core.concept.Class;
import mobi.core.concept.Instance;
import mobi.core.manager.InferenceManager;
import mobi.core.relation.GenericRelation;

public class TesteMOBIGerneric {
	
	public static void main(String[] args) throws IOException,
	ClassNotFoundException {
		TesteMOBIGerneric.carregaDominioGeneric();
	}

	public static Mobi carregaDominioGeneric() {
		Mobi mobi = new Mobi("DominioGenerico");
		
		try
		{
			Class cPessoa = new Class("cPessoa");
			Class cAluno = new Class("cAluno");
			Class cProfessor = new Class("cProfessor");
			
			Instance iDanisio    = new Instance("iDanisio");
			Instance iDanisio2     = new Instance("iDanisio");
			Instance iProfessor     = new Instance("iProfessor");
			
			mobi.addConcept(cPessoa);
			mobi.addConcept(cAluno);
			mobi.addConcept(cProfessor);
			
			mobi.addConcept(iDanisio);
			mobi.addConcept(iDanisio2);
			mobi.addConcept(iProfessor);
			
			mobi.isOneOf(iDanisio, cPessoa);
			mobi.isOneOf(iDanisio2, cAluno);
			mobi.isOneOf(iProfessor, cProfessor);
			
			GenericRelation genericRelation = (GenericRelation)mobi.createGenericRelation("generic1");
			genericRelation.setClassA(cPessoa);
			genericRelation.setClassB(cAluno);
			genericRelation.addInstanceRelation(iDanisio, iDanisio2);
			mobi.addConcept(genericRelation);
			
			InferenceManager inferencia = new InferenceManager();
			
			System.out.println("Possibilidades");
			for(int i : inferencia.infereRelation(genericRelation))
			{
				System.out.println(i);
			}
			
			GenericRelation genericRelation2 = (GenericRelation)mobi.createGenericRelation("generic2");
			genericRelation2.setClassA(cProfessor);
			genericRelation2.setClassB(cAluno);
			genericRelation2.addInstanceRelation(iProfessor, iDanisio2);
			mobi.addConcept(genericRelation2);
			
			System.out.println("Possibilidades");
			for(int i : inferencia.infereRelation(genericRelation2))
			{
				System.out.println(i);
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return mobi;
	}
}
