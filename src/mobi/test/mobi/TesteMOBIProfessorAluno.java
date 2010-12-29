package mobi.test.mobi;

import java.io.IOException;

import mobi.core.Mobi;
import mobi.core.common.Relation;
import mobi.core.concept.Class;
import mobi.core.concept.Instance;

public class TesteMOBIProfessorAluno {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		TesteMOBIProfessorAluno.carregaDominioProfessorAluno();
	}

	public static Mobi carregaDominioProfessorAluno() {
		Mobi mobi = new Mobi("ProfessorAluno");

		Instance Eduardo = new Instance("Eduardo");
		Instance Danisio    = new Instance("Danisio");
		
		Class Pessoa  = new Class("Pessoa");
		Class Professor = new Class("Professor");
		Class Aluno = new Class("Aluno");
		Class cDocente = new Class("Docente");
		
		try {
			mobi.addConcept(Eduardo);
			mobi.addConcept(Danisio);
			
			mobi.addConcept(Pessoa);
			mobi.addConcept(Professor);
			mobi.addConcept(Aluno);
			mobi.addConcept(cDocente);
			
			mobi.isOneOf(Eduardo, Pessoa);
			mobi.isOneOf(Eduardo, Professor);
			mobi.isOneOf(Eduardo, Aluno);
			mobi.isOneOf(Eduardo, cDocente);
			
			mobi.isOneOf(Danisio, Pessoa);
			mobi.isOneOf(Danisio, Aluno);
			
			Relation r = mobi.createInheritanceRelation("inheritance");

			r.setClassA(Pessoa);
			r.setClassB(Professor);
			r.addInstanceRelation(Eduardo, Eduardo);
			
			
			r.processCardinality();
			mobi.addConcept(r);
			
			Relation r2 = mobi.createInheritanceRelation("inheritance");

			r2.setClassA(Pessoa);
			r2.setClassB(Aluno);
			r2.addInstanceRelation(Eduardo, Eduardo);
			r2.addInstanceRelation(Danisio, Danisio);
			
			r2.processCardinality();
			mobi.addConcept(r2);

			Relation r3 = mobi.createEquivalenceRelation("equals");
			r3.setClassA(Professor);
			r3.setClassB(cDocente);
			r3.addInstanceRelation(Eduardo, Eduardo);
			r3.processCardinality();
			mobi.addConcept(r3);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mobi;

	}
}
