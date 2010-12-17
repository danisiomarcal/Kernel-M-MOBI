package mobi.test.mobi;

import java.io.IOException;

import mobi.core.Mobi;
import mobi.core.common.Relation;
import mobi.core.concept.Class;
import mobi.core.concept.Instance;

public class TesteMOBIProfessor {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		TesteMOBIProfessor.carregaDominioProfessor();
	}

	public static Mobi carregaDominioProfessor() {
		Mobi mobi        = new Mobi("Professor");

		Class cProfessor = new Class("cProfessor");
		Class cDisciplina  = new Class("cDisciplina");
		Class cTurma     = new Class("cTurma");
		Class cEscola    = new Class("cEscola");

		Instance iDanisio = new Instance("iDanisio");
		Instance iMatematica  = new Instance("iMatematica");
		Instance iPortugues  = new Instance("iPortugues");
		Instance iQuimica  = new Instance("iQuimica");
		Instance iTurma1  = new Instance("iTurma1");
		Instance iSartre  = new Instance("iSartre");
		
		try {
			mobi.addConcept(cProfessor);
			mobi.addConcept(cDisciplina);
			mobi.addConcept(cTurma);
			mobi.addConcept(cEscola);
			
			mobi.addConcept(iDanisio);
			mobi.addConcept(iMatematica);
			mobi.addConcept(iPortugues);
			mobi.addConcept(iQuimica);
			mobi.addConcept(iTurma1);
			mobi.addConcept(iSartre);
			
			mobi.isOneOf(iDanisio, cProfessor);
			mobi.isOneOf(iMatematica, cDisciplina);
			mobi.isOneOf(iQuimica, cDisciplina);
			mobi.isOneOf(iPortugues, cDisciplina);
			mobi.isOneOf(iTurma1, cTurma);
			mobi.isOneOf(iSartre, cEscola);
			
			Relation rProfessorDisciplina = mobi.createUnidirecionalCompositionRelationship("tem");
			rProfessorDisciplina.setClassA(cProfessor);
			rProfessorDisciplina.setClassB(cDisciplina);
			rProfessorDisciplina.addInstanceRelation(iDanisio, iMatematica);
			rProfessorDisciplina.addInstanceRelation(iDanisio, iPortugues);
			rProfessorDisciplina.addInstanceRelation(iDanisio, iQuimica);
			rProfessorDisciplina.processCardinality();
			mobi.addConcept(rProfessorDisciplina);
			
			Relation rProfessorTurma = mobi.createUnidirecionalCompositionRelationship("tem");
			rProfessorTurma.setClassA(cProfessor);
			rProfessorTurma.setClassB(cTurma);
			rProfessorTurma.addInstanceRelation(iDanisio, iTurma1);
			rProfessorTurma.processCardinality();
			mobi.addConcept(rProfessorTurma);
			
			Relation rEscolaProfessor = mobi.createUnidirecionalCompositionRelationship("tem");
			rEscolaProfessor.setClassA(cEscola);
			rEscolaProfessor.setClassB(cProfessor);
			rEscolaProfessor.addInstanceRelation(iSartre, iDanisio);
			rEscolaProfessor.processCardinality();
			mobi.addConcept(rEscolaProfessor);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return mobi;

	}
}