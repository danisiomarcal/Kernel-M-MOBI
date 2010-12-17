package mobi.test.mobi;

import java.io.IOException;

import mobi.core.Mobi;
import mobi.core.concept.Class;
import mobi.core.concept.Instance;
import mobi.core.relation.InheritanceRelation;
import mobi.core.relation.InstanceRelation;
import mobi.extension.export.owl.Mobi2OWL;

public class TesteMOBIImportacaoPessoa {
	public static void main(String[] args) throws IOException,
	ClassNotFoundException {
		TesteMOBIImportacaoPessoa.LeDominioPessoa();
	}
	
public static void LeDominioPessoa() {
		
		try
		{
			Mobi2OWL mobiOWL = new Mobi2OWL();
			mobiOWL.importForMobiOfOWL("C:\\BaseOntologia\\Pessoa.owl");
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
				System.out.println("==================Reading Relation==================");
				System.out.println("SUPER CLASS: " + inheritance.getClassA().getUri());
				System.out.println("SUB CLASS: " + inheritance.getClassB().getUri());
				
				System.out.println("Reading Relations between instances");
				for(InstanceRelation instanceRelation : inheritance.getInstanceRelationMapA().values())
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
