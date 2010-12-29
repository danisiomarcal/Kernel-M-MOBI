package mobi.test.mobi;

import java.io.IOException;

import mobi.core.Mobi;
import mobi.core.concept.Class;
import mobi.core.concept.Instance;
import mobi.core.relation.CompositionRelation;
import mobi.core.relation.InstanceRelation;
import mobi.extension.export.owl.Mobi2OWL;

public class TesteMOBIImportacaoAmericaDoSul {
	public static void main(String[] args) throws IOException,
	ClassNotFoundException {
		TesteMOBIImportacaoAmericaDoSul.LeDominioAmericaDoSul();
	}
	
	public static void LeDominioAmericaDoSul() {
		
		try
		{
			Mobi2OWL mobiOWL = new Mobi2OWL();
			mobiOWL.importForMobiOfOWL("C:\\BaseOntologia\\ronaldo@reconcavotecnologia_org_br_comgresso.owl");
			Mobi mobi = mobiOWL.getMobi();
			
			for(Class mobiClass : mobi.getAllClasses().values())
			{
				System.out.println("==================Reading Class: " + mobiClass.getUri().toUpperCase() + "==================");
				System.out.println(">>>Instances Belonging to Class");
				for(Instance instance : mobi.getClassInstances(mobiClass.getUri()))
				{
					System.out.println(instance.getUri());
				}
				System.out.println("==================End of Class: " + mobiClass.getUri().toUpperCase() + "==================");
			}
			
			for(CompositionRelation composition : mobi.getAllCompositionRelations().values())
			{
				System.out.println("==================Reading Relation==================");
				System.out.println("NAME A: " + composition.getNameA());
				System.out.println("NAME B: " + composition.getNameB());
				System.out.println("DOMAIN: " + composition.getClassA().getUri());
				System.out.println("RANGE: " + composition.getClassB().getUri());
				
				System.out.println("Reading Relations between instances");
				for(InstanceRelation instanceRelation : composition.getInstanceRelationMapA().values())
				{
					System.out.println("Instance " + instanceRelation.getInstance().getUri().toUpperCase() + " has values: ");
					for(Instance instance: instanceRelation.getAllInstances().values())
						System.out.println(instance.getUri().toUpperCase());
				}
				System.out.println("==================End of Relation==================");
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
