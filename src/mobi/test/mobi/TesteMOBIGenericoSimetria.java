package mobi.test.mobi;

import java.io.IOException;
import java.util.Collection;

import mobi.core.Mobi;
import mobi.core.common.Relation;
import mobi.core.concept.Class;
import mobi.core.concept.Instance;
import mobi.core.relation.GenericRelation;
import mobi.core.relation.SymmetricRelation;

public class TesteMOBIGenericoSimetria {
	public static void main(String[] args) throws IOException,
	ClassNotFoundException {
		TesteMOBIGenericoSimetria.carregaDominioGenerico();
	}
	
	public static Mobi carregaDominioGenerico() {
		Mobi mobi = new Mobi("DominioGenerico");
		
		try
		{
			Class cEstado = new Class("cEstado");
			
			Instance iBahia = new Instance("iBahia");
			Instance iMinasGerais = new Instance("iMinasGerais");
			Instance iSaoPaulo = new Instance("iSaoPaulo");
			Instance iRioJaneiro = new Instance("iRioJaneiro");
			
			mobi.addConcept(cEstado);
			
			mobi.addConcept(iBahia);
			mobi.addConcept(iMinasGerais);
			mobi.addConcept(iSaoPaulo);
			mobi.addConcept(iRioJaneiro);
			
			mobi.isOneOf(iBahia, cEstado);
			mobi.isOneOf(iMinasGerais, cEstado);
			mobi.isOneOf(iSaoPaulo, cEstado);
			mobi.isOneOf(iRioJaneiro, cEstado);
		
			GenericRelation genericRelation = (GenericRelation)mobi.createGenericRelation("generic1");
			genericRelation.setClassA(cEstado);
			genericRelation.setClassB(cEstado);
			genericRelation.addInstanceRelation(iSaoPaulo, iRioJaneiro);
			genericRelation.addInstanceRelation(iMinasGerais, iBahia);
			genericRelation.addInstanceRelation(iMinasGerais, iRioJaneiro);
			genericRelation.addInstanceRelation(iMinasGerais, iSaoPaulo);
			
			genericRelation.processCardinality();
			mobi.addConcept(genericRelation);
			
			Collection<Integer> possibilities = mobi.infereRelation(genericRelation);
			
			if(possibilities.contains(Relation.SYMMETRIC_COMPOSITION)) //Preferred choice
			{
				SymmetricRelation symmetric = (SymmetricRelation)mobi.convertToSymmetricRelation(genericRelation, "fazFronteira");
				mobi.addConcept(symmetric);
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return mobi;
	}
}
