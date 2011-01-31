package mobi.test.mobi;

import java.io.IOException;

import mobi.core.Mobi;
import mobi.core.concept.Class;
import mobi.core.concept.Instance;
import mobi.core.relation.CompositionRelation;
import mobi.core.relation.EquivalenceRelation;
import mobi.core.relation.InheritanceRelation;
import mobi.core.relation.InstanceRelation;
import mobi.extension.export.owl.Mobi2OWL;

public class TesteMOBIImportacao {
	public static void main(String[] args) throws IOException,
	ClassNotFoundException {
	}
	
	public static void LeDominio(String path) {
		
		try
		{
			Mobi2OWL mobiOWL = new Mobi2OWL();
			mobiOWL.importForMobiOfOWL(path);
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
			
			for(InheritanceRelation inheritance : mobi.getAllInheritanceRelations().values())
			{
				System.out.println("==================Reading Relation Inheritance==================");
				System.out.println("SUPER CLASS: " + inheritance.getClassA().getUri());
				System.out.println("SUB CLASS: " + inheritance.getClassB().getUri());
				System.out.println("NAME: " + inheritance.getUri());
				
				System.out.println("Reading Relations between instances");
				for(InstanceRelation instanceRelation : inheritance.getInstanceRelationMapA().values())
				{
					System.out.println("Instance " + instanceRelation.getInstance().getUri().toUpperCase() + " has values: ");
					for(Instance instance: instanceRelation.getAllInstances().values())
						System.out.println(instance.getUri().toUpperCase());
				}
				System.out.println("==================End of Relation==================");
			}
			
			for(EquivalenceRelation equivalence : mobi.getAllEquivalenceRelations().values())
			{
				System.out.println("==================Reading Relation==================");
				System.out.println("CLASS A: " + equivalence.getClassA().getUri());
				System.out.println("CLASS B: " + equivalence.getClassB().getUri());
				System.out.println("NAME: " + equivalence.getUri());
				System.out.println("Reading Relations between instances");
				for(InstanceRelation instanceRelation : equivalence.getInstanceRelationMapA().values())
				{
					System.out.println("Instance " + instanceRelation.getInstance().getUri().toUpperCase() + " has values: ");
					for(Instance instance: instanceRelation.getAllInstances().values())
						System.out.println(instance.getUri().toUpperCase());
				}
				System.out.println("==================End of Relation==================");
			}
			
			for(CompositionRelation composition : mobi.getAllCompositionRelations().values())
			{
				System.out.println("==================Reading Relation Composition==================");
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
