package mobi.extension.export.owl;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mobi.core.Mobi;
import mobi.core.common.Relation;
import mobi.core.concept.Class;
import mobi.core.concept.Instance;
import mobi.core.relation.CompositionRelation;
import mobi.core.relation.EquivalenceRelation;
import mobi.core.relation.InheritanceRelation;
import mobi.core.relation.InstanceRelation;
import mobi.core.relation.SymmetricRelation;
import mobi.exception.ExceptionInverseRelation;
import mobi.exception.ExceptionMobiFile;
import mobi.exception.ExceptionSymmetricRelation;
import mobi.exception.ExceptionURI;
import mobi.test.mobi.TesteMOBIEleicao;
import mobi.test.mobi.TesteMOBIProfessorAluno;
import mobi.test.mobi.TesteMOBIRegiao;

import com.hp.hpl.jena.ontology.AllValuesFromRestriction;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.ontology.SomeValuesFromRestriction;
import com.hp.hpl.jena.ontology.SymmetricProperty;
import com.hp.hpl.jena.ontology.UnionClass;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.RSIterator;
import com.hp.hpl.jena.rdf.model.ReifiedStatement;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class Mobi2OWL {

	private static int RESTRICTION_ALLVALUES = 1;
	private String ontologyPrefix = null;
	private String validatorOWLGeneratedByMMobi = "mobidomain";

	private Mobi mobi;

	private JenaOntologyExporter provider = new JenaOntologyExporter();

	private OntModel jena;

	public Mobi2OWL(String ontologyPrefix, Mobi mobi) {
		this.mobi = mobi;
		this.ontologyPrefix = ontologyPrefix + mobi.getContext().getUri() + "#";
	}

	public void setOntologyPrefix(String ontologyPrefix) {
		this.ontologyPrefix = ontologyPrefix;
	}

	public void setExportPath(String exportPath) {
		this.provider.setExportPath(exportPath);
	}

	public void setMobi(Mobi mobi) {
		this.mobi = mobi;
	}

	private OntClass createJenaClass(Class mobiClass) {
		OntClass jenaClasse = this.jena.createClass(this.ontologyPrefix
				+ mobiClass.getUri());
		if (mobiClass.getComment() != null) {
			jenaClasse.setComment(mobiClass.getComment(), null);
		}
		return jenaClasse;
	}

	public void exportClasses() {
		for (Class mobiClass : this.mobi.getAllClasses().values()) {
			this.createJenaClass(mobiClass);
		}
	}

	// public void
	// exportInstancesWithMoreThanOneClass(mobi.core.concept.Instance i) {
	//
	// for (Class mobiClass : this.mobi.getInstanceClasses(i)) {
	// if (!this.mobi.isSuperClass(mobiClass)) {
	// OntClass classeA = this.createJenaClass(mobiClass);
	// this.jena.createIndividual(this.ontologyPrefix + i.getUri(),
	// classeA);
	// }
	// }
	//
	// // Old Code to create Multiple Inheritance
	// Map<String, Class> classMap = new HashMap<String, Class>();
	// for (Class classe1 : this.mobi.getInstanceClasses(i)) {
	// for (Class classe2 : this.mobi.getInstanceClasses(i)) {
	// if ((!classe1.getUri().equals(classe2.getUri()))
	// && (!this.mobi.isSubClassOf(classe1, classe2))
	// && (!this.mobi.isSuperClassOf(classe1, classe2))) {
	// if (this.jena.getOntClass(this.ontologyPrefix
	// + classe2.getUri() + classe1.getUri()) == null) {
	// classMap.put(classe1.getUri(), classe1);
	// }
	// }
	// }
	// }
	//
	// if (classMap.values().size() > 0) {
	// String classFinalName = "";
	// RDFNode[] myNode = new RDFNode[classMap.values().size()];
	// int count = 0;
	// for (Class mobiClass : classMap.values()) {
	// OntClass classeA = this.createJenaClass(mobiClass);
	// myNode[count++] = classeA;
	// classFinalName += mobiClass.getUri();
	// }
	// OntClass interClasse = this.jena.createIntersectionClass(
	// this.ontologyPrefix + classFinalName, this.jena
	// .createList(myNode));
	// this.jena.createIndividual(this.ontologyPrefix + i.getUri(),
	// interClasse);
	// }
	// }

	public void exportInstances() {
		for (Instance instance : this.mobi.getAllInstances().values()) {
			for (Class mobiClass : this.mobi.getInstanceClasses(instance)) {
				OntClass classeA = this.createJenaClass(mobiClass);
				Class baseClass = instance.getBaseClass();

				Individual instanceJena = this.jena
						.getIndividual(this.ontologyPrefix + instance.getUri());

				if (baseClass != null) {
					OntClass baseClass2 = this.createJenaClass(baseClass);

					if (!baseClass.getUri().equals(mobiClass.getUri()))
						baseClass2.addSubClass(classeA);

					if (instanceJena == null) {
						instanceJena = this.jena.createIndividual(
								this.ontologyPrefix + instance.getUri(),
								baseClass2);
						instanceJena.addOntClass(classeA);
					} else {
						instanceJena.addOntClass(baseClass2);
						instanceJena.addOntClass(classeA);
					}
				} else {
					if (instanceJena == null)
						instanceJena = this.jena.createIndividual(
								this.ontologyPrefix + instance.getUri(),
								classeA);
					else
						instanceJena.addOntClass(classeA);
				}
			}
		}
	}

	// public void exportInstances() {
	//
	// for (Instance instance : this.mobi.getAllInstances().values()) {
	//
	// if (this.mobi.getInstanceClasses(instance).size() > 1) {
	// this.exportInstancesWithMoreThanOneClass(instance);
	// } else {
	// Class mobiClass = this.mobi.getInstanceClasses(instance)
	// .iterator().next();
	// if (mobiClass != null) {
	// this.jena.createIndividual(this.ontologyPrefix
	// + instance.getUri(), this
	// .createJenaClass(mobiClass));
	// }
	// }
	// }
	// }

	public void exportInheritanceRelations() {

		for (InheritanceRelation ir : this.mobi.getAllInheritanceRelations()
				.values()) {
			OntClass classeA = this.createJenaClass(ir.getClassA());
			OntClass classeB = this.createJenaClass(ir.getClassB());
			classeA.addSubClass(classeB);
		}
	}

	public void exportEquivalenceRelations() {

		for (EquivalenceRelation ir : this.mobi.getAllEquivalenceRelations()
				.values()) {
			OntClass classeA = this.createJenaClass(ir.getClassA());
			OntClass classeB = this.createJenaClass(ir.getClassB());
			classeA.addEquivalentClass(classeB);
			classeB.addEquivalentClass(classeA);
		}
	}

	public void exportCompositionRelations() throws ExceptionInverseRelation,
			ExceptionSymmetricRelation {

		HashMap<String, HashMap<String, HashMap<String, Integer>>> relationsPropertys = new HashMap<String, HashMap<String, HashMap<String, Integer>>>();
		HashMap<String, HashMap<String, HashMap<String, Class>>> listRestrictions = new HashMap<String, HashMap<String, HashMap<String, Class>>>();

		for (CompositionRelation cr : this.mobi.getAllCompositionRelations()
				.values()) {

			for (SymmetricRelation sr : this.mobi.getAllSymmetricRelations()
					.values()) {
				if (this.mobi.getPropertyName(sr.getName(), sr.getClassA(),
						sr.getClassB()).equals(
						this.mobi.getPropertyName(cr.getNameA(),
								cr.getClassA(), cr.getClassB())))
					throw new ExceptionSymmetricRelation(
							"Unable to generate the OWL file, as it has a symmetrical relationship with the name: "
									+ this.mobi.getPropertyName(cr.getNameA(),
											cr.getClassA(), cr.getClassB())
									+ " for this relationship composition.");

				if (cr.getNameB() != null
						&& this.mobi.getPropertyName(cr.getNameB(),
								cr.getClassB(), cr.getClassA()).equals(
								this.mobi.getPropertyName(sr.getName(), sr
										.getClassA(), sr.getClassB())))
					throw new ExceptionSymmetricRelation(
							"Unable to generate the OWL file, as it has a symmetrical relationship with the name:"
									+ this.mobi.getPropertyName(cr.getNameB(),
											cr.getClassB(), cr.getClassA())
									+ " for this relationship composition.");
			}

			OntClass classA = this.createJenaClass(cr.getClassA());
			OntClass classB = this.createJenaClass(cr.getClassB());

			ObjectProperty objectPropertyA = null, objectPropertyB = null;
			String nameJenaObjectPropertyA = null, nameJenaObjectPropertyB = null;

			if (cr.getNameA() != null) {
				nameJenaObjectPropertyA = this.mobi.getPropertyName(cr
						.getNameA(), cr.getClassA(), cr.getClassB());

				objectPropertyA = this.jena.createObjectProperty(
						this.ontologyPrefix + nameJenaObjectPropertyA, false);

				objectPropertyA.setDomain(this.getJenaDomainsObjectProperty(
						nameJenaObjectPropertyA, true));
				objectPropertyA.setRange(this.getJenaRangesObjectProperty(
						nameJenaObjectPropertyA, true));

				if (!relationsPropertys.containsKey(nameJenaObjectPropertyA))
					relationsPropertys.put(nameJenaObjectPropertyA,
							new HashMap<String, HashMap<String, Integer>>());

				if (!((HashMap<String, HashMap<String, Integer>>) relationsPropertys
						.get(nameJenaObjectPropertyA)).containsKey(cr
						.getClassA().getUri()))
					((HashMap<String, HashMap<String, Integer>>) relationsPropertys
							.get(nameJenaObjectPropertyA)).put(cr.getClassA()
							.getUri(), new HashMap<String, Integer>());

				if (this.getMobiDomainsObjectProperty(nameJenaObjectPropertyA,
						true).size() > 1) {
					if (!listRestrictions.containsKey(nameJenaObjectPropertyA))
						listRestrictions.put(nameJenaObjectPropertyA,
								new HashMap<String, HashMap<String, Class>>());

					if (!listRestrictions.get(nameJenaObjectPropertyA)
							.containsKey(cr.getClassA().getUri()))
						listRestrictions.get(nameJenaObjectPropertyA).put(
								cr.getClassA().getUri(),
								new HashMap<String, Class>());

					if (!listRestrictions.get(nameJenaObjectPropertyA).get(
							cr.getClassA().getUri()).containsKey(
							cr.getClassB().getUri()))
						listRestrictions.get(nameJenaObjectPropertyA).get(
								cr.getClassA().getUri()).put(
								cr.getClassB().getUri(), cr.getClassB());
				}
			}

			if (cr.getNameB() != null) {
				nameJenaObjectPropertyB = this.mobi.getPropertyName(cr
						.getNameB(), cr.getClassB(), cr.getClassA());

				String nameInversePropertyA = this.mobi
						.getInversePropertyName(nameJenaObjectPropertyB);
				String nameInversePropertyB = this.mobi
						.getInversePropertyName(nameJenaObjectPropertyA);

				if (!nameInversePropertyA.equals(nameJenaObjectPropertyA))
					throw new ExceptionInverseRelation(
							"Unable to generate the OWL file, as the inverse of the"
									+ nameJenaObjectPropertyB
									+ " is the property"
									+ nameInversePropertyA
									+ ". Thus, the property"
									+ nameJenaObjectPropertyA
									+ " can not be attributed to the reverse of the latter.");

				if (!nameInversePropertyB.equals(nameJenaObjectPropertyB))
					throw new ExceptionInverseRelation(
							"Unable to generate the OWL file, as the inverse of the"
									+ nameJenaObjectPropertyA
									+ " is the property "
									+ nameInversePropertyB
									+ ". Thus, the property"
									+ nameJenaObjectPropertyB
									+ " can not be attributed to the reverse of the latter.");

				if (nameInversePropertyA.equals(nameInversePropertyB))
					throw new ExceptionInverseRelation(
							"Unable to generate the OWL file as a composition of two-way relationship with the names of the inverse and direct the same to be characterized as a symmetrical relationship.");

				objectPropertyB = this.jena.createObjectProperty(
						this.ontologyPrefix + nameJenaObjectPropertyB, false);

				objectPropertyB.setDomain(this.getJenaDomainsObjectProperty(
						nameJenaObjectPropertyB, true));
				objectPropertyB.setRange(this.getJenaRangesObjectProperty(
						nameJenaObjectPropertyB, true));

				if (!relationsPropertys.containsKey(nameJenaObjectPropertyB))
					relationsPropertys.put(nameJenaObjectPropertyB,
							new HashMap<String, HashMap<String, Integer>>());

				if (!((HashMap<String, HashMap<String, Integer>>) relationsPropertys
						.get(nameJenaObjectPropertyB)).containsKey(cr
						.getClassB().getUri()))
					((HashMap<String, HashMap<String, Integer>>) relationsPropertys
							.get(nameJenaObjectPropertyB)).put(cr.getClassB()
							.getUri(), new HashMap<String, Integer>());

				if (this.getMobiDomainsObjectProperty(nameJenaObjectPropertyB,
						true).size() > 1) {
					if (!listRestrictions.containsKey(nameJenaObjectPropertyB))
						listRestrictions.put(nameJenaObjectPropertyB,
								new HashMap<String, HashMap<String, Class>>());

					if (!listRestrictions.get(nameJenaObjectPropertyB)
							.containsKey(cr.getClassB().getUri()))
						listRestrictions.get(nameJenaObjectPropertyB).put(
								cr.getClassB().getUri(),
								new HashMap<String, Class>());

					if (!listRestrictions.get(nameJenaObjectPropertyB).get(
							cr.getClassB().getUri()).containsKey(
							cr.getClassA().getUri()))
						listRestrictions.get(nameJenaObjectPropertyB).get(
								cr.getClassB().getUri()).put(
								cr.getClassA().getUri(), cr.getClassA());
				}
			}

			if ((cr.getNameA() != null) && (cr.getNameB() != null)) {
				objectPropertyA.setInverseOf(objectPropertyB);
				objectPropertyB.setInverseOf(objectPropertyA);
			}

			for (InstanceRelation instanceRelation : cr
					.getInstanceRelationMapA().values()) {

				Individual individualA = this.jena.createIndividual(
						this.ontologyPrefix
								+ instanceRelation.getInstance().getUri(),
						classA);

				for (Instance instance : instanceRelation.getAllInstances()
						.values()) {
					Individual individualB = jena.createIndividual(
							this.ontologyPrefix + instance.getUri(), classB);

					if (cr.getNameA() != null) {
						individualA.addProperty(objectPropertyA, individualB);

						if (!((HashMap<String, HashMap<String, Integer>>) relationsPropertys
								.get(nameJenaObjectPropertyA)).get(
								cr.getClassA().getUri()).containsKey(
								instanceRelation.getInstance().getUri()))
							((HashMap<String, HashMap<String, Integer>>) relationsPropertys
									.get(nameJenaObjectPropertyA)).get(
									cr.getClassA().getUri()).put(
									instanceRelation.getInstance().getUri(), 1);
						else {
							Integer count = ((HashMap<String, HashMap<String, Integer>>) relationsPropertys
									.get(nameJenaObjectPropertyA)).get(
									cr.getClassA().getUri()).get(
									instanceRelation.getInstance().getUri());
							((HashMap<String, HashMap<String, Integer>>) relationsPropertys
									.get(nameJenaObjectPropertyA)).get(
									cr.getClassA().getUri()).put(
									instanceRelation.getInstance().getUri(),
									count + 1);
						}

					}
					if (cr.getNameB() != null) {
						individualB.addProperty(objectPropertyB, individualA);

						if (!((HashMap<String, HashMap<String, Integer>>) relationsPropertys
								.get(nameJenaObjectPropertyB)).get(
								cr.getClassB().getUri()).containsKey(
								instance.getUri()))
							((HashMap<String, HashMap<String, Integer>>) relationsPropertys
									.get(nameJenaObjectPropertyB)).get(
									cr.getClassB().getUri()).put(
									instance.getUri(), 1);
						else {
							Integer count = ((HashMap<String, HashMap<String, Integer>>) relationsPropertys
									.get(nameJenaObjectPropertyB)).get(
									cr.getClassB().getUri()).get(
									instance.getUri());
							((HashMap<String, HashMap<String, Integer>>) relationsPropertys
									.get(nameJenaObjectPropertyB)).get(
									cr.getClassB().getUri()).put(
									instance.getUri(), count + 1);
						}
					}
				}
			}
		}

		Set<String> keysR = listRestrictions.keySet();

		for (String keyR : keysR) {
			HashMap<String, HashMap<String, Class>> l2 = listRestrictions
					.get(keyR);
			Set<String> keys2 = l2.keySet();
			for (String key2 : keys2) {
				this.createJenaRestriction(this.jena
						.createObjectProperty(this.ontologyPrefix + keyR),
						this.jena.createClass(this.ontologyPrefix + key2),
						this.createUnionJenaClassFromMobiClass(l2.get(key2)),
						RESTRICTION_ALLVALUES);
			}
		}

		this.convertToFunctionalOrInverseFunctionalProperty(relationsPropertys,
				true);
	}

	private OntClass getJenaDomainsObjectProperty(
			String nameJenaObjectProperty, boolean isCompositionRelation) {
		return this.createUnionJenaClassFromMobiClass(this
				.getMobiDomainsObjectProperty(nameJenaObjectProperty,
						isCompositionRelation));
	}

	private HashMap<String, Class> getMobiDomainsObjectProperty(
			String nameJenaObjectProperty, boolean isCompositionRelation) {
		HashMap<String, Class> hashDomains = new HashMap<String, Class>();

		if (isCompositionRelation) {
			for (CompositionRelation c : this.mobi.getAllCompositionRelations()
					.values()) {
				if (this.mobi.getPropertyName(c.getNameA(), c.getClassA(),
						c.getClassB()).equals(nameJenaObjectProperty)
						&& !hashDomains.containsKey(c.getClassA().getUri()))
					hashDomains.put(c.getClassA().getUri(), c.getClassA());

				if (c.getNameB() != null
						&& this.mobi.getPropertyName(c.getNameB(),
								c.getClassB(), c.getClassA()).equals(
								nameJenaObjectProperty)
						&& !hashDomains.containsKey(c.getClassB().getUri()))
					hashDomains.put(c.getClassB().getUri(), c.getClassB());
			}
		} else {
			for (SymmetricRelation s : this.mobi.getAllSymmetricRelations()
					.values()) {
				if (this.mobi.getPropertyName(s.getName(), s.getClassA(),
						s.getClassB()).equals(nameJenaObjectProperty)
						&& !hashDomains.containsKey(s.getClassA().getUri()))
					hashDomains.put(s.getClassA().getUri(), s.getClassA());
			}
		}

		return hashDomains;
	}

	private OntClass getJenaRangesObjectProperty(String nameJenaObjectProperty,
			boolean isCompositionRelation) {
		return this.createUnionJenaClassFromMobiClass(this
				.getMobiRangesObjectProperty(nameJenaObjectProperty,
						isCompositionRelation));
	}

	private HashMap<String, Class> getMobiRangesObjectProperty(
			String nameJenaObjectProperty, boolean isCompositionRelation) {
		HashMap<String, Class> hashRanges = new HashMap<String, Class>();

		if (isCompositionRelation) {
			for (CompositionRelation c : this.mobi.getAllCompositionRelations()
					.values()) {
				if (this.mobi.getPropertyName(c.getNameA(), c.getClassA(),
						c.getClassB()).equals(nameJenaObjectProperty)
						&& !hashRanges.containsKey(c.getClassB().getUri()))
					hashRanges.put(c.getClassB().getUri(), c.getClassB());

				if (c.getNameB() != null
						&& this.mobi.getPropertyName(c.getNameB(),
								c.getClassB(), c.getClassA()).equals(
								nameJenaObjectProperty)
						&& !hashRanges.containsKey(c.getClassA().getUri()))
					hashRanges.put(c.getClassA().getUri(), c.getClassA());
			}
		} else {
			for (SymmetricRelation s : this.mobi.getAllSymmetricRelations()
					.values()) {
				if (this.mobi.getPropertyName(s.getName(), s.getClassA(),
						s.getClassB()).equals(nameJenaObjectProperty)
						&& !hashRanges.containsKey(s.getClassB().getUri()))
					hashRanges.put(s.getClassB().getUri(), s.getClassB());
			}
		}

		return hashRanges;
	}

	private OntClass createUnionJenaClassFromMobiClass(
			HashMap<String, Class> hash) {
		RDFNode[] nodes = new RDFNode[hash.size()];
		Iterator<Map.Entry<String, Class>> iterator = hash.entrySet()
				.iterator();

		int pos = 0;

		while (iterator.hasNext()) {
			nodes[pos] = this.createJenaClass(iterator.next().getValue());
			pos++;
		}

		return this.jena.createUnionClass(null, this.jena.createList(nodes));
	}

	private com.hp.hpl.jena.ontology.Restriction createJenaRestriction(
			ObjectProperty property, OntClass domain, OntClass range, int type) {
		if (type == Mobi2OWL.RESTRICTION_ALLVALUES) {
			AllValuesFromRestriction rAllVallues = this.jena
					.createAllValuesFromRestriction(null, property, range);
			rAllVallues.addSubClass(domain);
			return (com.hp.hpl.jena.ontology.Restriction) rAllVallues;
		} else {
			SomeValuesFromRestriction rSomeVallues = this.jena
					.createSomeValuesFromRestriction(null, property, range);
			rSomeVallues.addSubClass(domain);
			return (com.hp.hpl.jena.ontology.Restriction) rSomeVallues;
		}
	}

	public void exportSymmetricRelations() throws ExceptionSymmetricRelation {

		HashMap<String, HashMap<String, HashMap<String, Integer>>> relationsPropertys = new HashMap<String, HashMap<String, HashMap<String, Integer>>>();
		HashMap<String, HashMap<String, HashMap<String, Class>>> listRestrictions = new HashMap<String, HashMap<String, HashMap<String, Class>>>();

		for (SymmetricRelation symmetricRelation : this.mobi
				.getAllSymmetricRelations().values()) {

			String nameJenaObjectProperty = this.mobi.getPropertyName(
					symmetricRelation.getName(), symmetricRelation.getClassA(),
					symmetricRelation.getClassB());

			for (CompositionRelation cr : this.mobi
					.getAllCompositionRelations().values()) {
				if ((this.mobi.getPropertyName(cr.getNameA(), cr.getClassA(),
						cr.getClassB()).equals(nameJenaObjectProperty))
						|| (cr.getNameB() != null && this.mobi.getPropertyName(
								cr.getNameB(), cr.getClassB(), cr.getClassA())
								.equals(nameJenaObjectProperty)))
					throw new ExceptionSymmetricRelation(
							"Could not create OWL file. There is another composition relation with the name: "
									+ nameJenaObjectProperty
									+ " for this symmetric relation.");
			}

			OntClass classeA = this.createJenaClass(symmetricRelation
					.getClassA());
			OntClass classeB = this.createJenaClass(symmetricRelation
					.getClassB());

			if (!relationsPropertys.containsKey(nameJenaObjectProperty))
				relationsPropertys.put(nameJenaObjectProperty,
						new HashMap<String, HashMap<String, Integer>>());

			if (!((HashMap<String, HashMap<String, Integer>>) relationsPropertys
					.get(nameJenaObjectProperty)).containsKey(symmetricRelation
					.getClassA().getUri()))
				((HashMap<String, HashMap<String, Integer>>) relationsPropertys
						.get(nameJenaObjectProperty)).put(symmetricRelation
						.getClassA().getUri(), new HashMap<String, Integer>());

			if (!((HashMap<String, HashMap<String, Integer>>) relationsPropertys
					.get(nameJenaObjectProperty)).containsKey(symmetricRelation
					.getClassB().getUri()))
				((HashMap<String, HashMap<String, Integer>>) relationsPropertys
						.get(nameJenaObjectProperty)).put(symmetricRelation
						.getClassB().getUri(), new HashMap<String, Integer>());

			// if (this.getMobiDomainsObjectProperty(nameJenaObjectProperty,
			// false).size() > 1)
			// {
			if (!listRestrictions.containsKey(nameJenaObjectProperty))
				listRestrictions.put(nameJenaObjectProperty,
						new HashMap<String, HashMap<String, Class>>());

			if (!listRestrictions.get(nameJenaObjectProperty).containsKey(
					symmetricRelation.getClassA().getUri()))
				listRestrictions.get(nameJenaObjectProperty).put(
						symmetricRelation.getClassA().getUri(),
						new HashMap<String, Class>());

			if (!listRestrictions.get(nameJenaObjectProperty).get(
					symmetricRelation.getClassA().getUri()).containsKey(
					symmetricRelation.getClassB().getUri()))
				listRestrictions.get(nameJenaObjectProperty).get(
						symmetricRelation.getClassA().getUri()).put(
						symmetricRelation.getClassB().getUri(),
						symmetricRelation.getClassB());

			if (!listRestrictions.get(nameJenaObjectProperty).containsKey(
					symmetricRelation.getClassB().getUri()))
				listRestrictions.get(nameJenaObjectProperty).put(
						symmetricRelation.getClassB().getUri(),
						new HashMap<String, Class>());

			if (!listRestrictions.get(nameJenaObjectProperty).get(
					symmetricRelation.getClassB().getUri()).containsKey(
					symmetricRelation.getClassA().getUri()))
				listRestrictions.get(nameJenaObjectProperty).get(
						symmetricRelation.getClassB().getUri()).put(
						symmetricRelation.getClassA().getUri(),
						symmetricRelation.getClassA());
			// }

			ObjectProperty objectProperty = this.jena
					.createObjectProperty(this.ontologyPrefix
							+ nameJenaObjectProperty);
			SymmetricProperty p = objectProperty.convertToSymmetricProperty();

			p.setInverseOf(p);

			// AllValuesFromRestriction avf2 = this.createJenaRestriction(p,
			// classeA, classeB,
			// Restriction.ALL_VALUES).asAllValuesFromRestriction();
			// AllValuesFromRestriction avf3 = this.createJenaRestriction(p,
			// classeB, classeA,
			// Restriction.ALL_VALUES).asAllValuesFromRestriction();
			//			
			p.setDomain(this.getJenaDomainsObjectProperty(
					nameJenaObjectProperty, false));
			p.setRange(this.getJenaRangesObjectProperty(nameJenaObjectProperty,
					false));

			for (InstanceRelation instanceRelation : symmetricRelation
					.getInstanceRelationMapA().values()) {

				Individual individual = this.jena.createIndividual(
						this.ontologyPrefix
								+ instanceRelation.getInstance().getUri(),
						classeA);

				for (Instance instance : instanceRelation.getAllInstances()
						.values()) {
					Individual individualv1 = this.jena.createIndividual(
							this.ontologyPrefix + instance.getUri(), classeB);
					individualv1.addProperty(objectProperty, individual);

					individual.addProperty(objectProperty, individualv1);

					if (!((HashMap<String, HashMap<String, Integer>>) relationsPropertys
							.get(nameJenaObjectProperty)).get(
							symmetricRelation.getClassA().getUri())
							.containsKey(
									instanceRelation.getInstance().getUri()))
						((HashMap<String, HashMap<String, Integer>>) relationsPropertys
								.get(nameJenaObjectProperty)).get(
								symmetricRelation.getClassA().getUri()).put(
								instanceRelation.getInstance().getUri(), 1);
					else {
						Integer count = ((HashMap<String, HashMap<String, Integer>>) relationsPropertys
								.get(nameJenaObjectProperty)).get(
								symmetricRelation.getClassA().getUri()).get(
								instanceRelation.getInstance().getUri());
						((HashMap<String, HashMap<String, Integer>>) relationsPropertys
								.get(nameJenaObjectProperty)).get(
								symmetricRelation.getClassA().getUri()).put(
								instanceRelation.getInstance().getUri(),
								count + 1);
					}
				}
			}

			for (InstanceRelation instanceRelation : symmetricRelation
					.getInstanceRelationMapB().values()) {

				Individual individual = this.jena.createIndividual(
						this.ontologyPrefix
								+ instanceRelation.getInstance().getUri(),
						classeB);

				for (Instance instance : instanceRelation.getAllInstances()
						.values()) {
					Individual individualv1 = this.jena.createIndividual(
							this.ontologyPrefix + instance.getUri(), classeA);
					individual.addProperty(objectProperty, individualv1);

					individualv1.addProperty(objectProperty, individual);

					// if (!((HashMap<String, HashMap<String, Integer>>)
					// relationsPropertys
					// .get(nameJenaObjectProperty)).get(
					// symmetricRelation.getClassB().getUri())
					// .containsKey(
					// instanceRelation.getInstance().getUri()))
					// ((HashMap<String, HashMap<String, Integer>>)
					// relationsPropertys
					// .get(nameJenaObjectProperty)).get(
					// symmetricRelation.getClassB().getUri()).put(
					// instanceRelation.getInstance().getUri(), 1);
					// else {
					// Integer count = ((HashMap<String, HashMap<String,
					// Integer>>) relationsPropertys
					// .get(nameJenaObjectProperty)).get(
					// symmetricRelation.getClassB().getUri()).get(
					// instanceRelation.getInstance().getUri());
					// ((HashMap<String, HashMap<String, Integer>>)
					// relationsPropertys
					// .get(nameJenaObjectProperty)).get(
					// symmetricRelation.getClassB().getUri()).put(
					// instanceRelation.getInstance().getUri(),
					// count + 1);
					// }
				}
			}
		}

		Set<String> keysR = listRestrictions.keySet();

		for (String keyR : keysR) {
			HashMap<String, HashMap<String, Class>> l2 = listRestrictions
					.get(keyR);
			Set<String> keys2 = l2.keySet();
			for (String key2 : keys2) {
				this.createJenaRestriction(this.jena
						.createObjectProperty(this.ontologyPrefix + keyR),
						this.jena.createClass(this.ontologyPrefix + key2),
						this.createUnionJenaClassFromMobiClass(l2.get(key2)),
						Mobi2OWL.RESTRICTION_ALLVALUES);
			}
		}

		this.convertToFunctionalOrInverseFunctionalProperty(relationsPropertys,
				false);
	}

	private void convertToFunctionalOrInverseFunctionalProperty(
			HashMap<String, HashMap<String, HashMap<String, Integer>>> objectProperts,
			boolean isCompositionRelations) {
		Set<String> keysPropertys = objectProperts.keySet();
		for (String keyProperty : keysPropertys) {
			ObjectProperty objectProperty = this.jena.createObjectProperty(
					this.ontologyPrefix + keyProperty, false);

			boolean sideAFunctional = this.convertObjectPropertyToFunctional(
					keyProperty, objectProperts);

			if (sideAFunctional)
				objectProperty.convertToFunctionalProperty();

			if (isCompositionRelations) {
				String nameInverseProperty = this.mobi
						.getInversePropertyName(keyProperty);

				if (nameInverseProperty != null
						&& this.convertObjectPropertyToFunctional(
								nameInverseProperty, objectProperts))
					objectProperty.convertToInverseFunctionalProperty();
			} else {
				if (sideAFunctional)
					objectProperty.convertToInverseFunctionalProperty();
			}
		}
	}

	private boolean convertObjectPropertyToFunctional(
			String nameProperty,
			HashMap<String, HashMap<String, HashMap<String, Integer>>> listSearch) {
		HashMap<String, HashMap<String, Integer>> h = listSearch
				.get(nameProperty);
		Set<String> keysClass = h.keySet();

		for (String keyClass : keysClass) {
			HashMap<String, Integer> h2 = h.get(keyClass);
			Set<String> keysIndividuals = h2.keySet();
			for (String keyIndividual : keysIndividuals) {
				if (h2.get(keyIndividual) > 1)
					return false;
			}
		}

		return true;
	}

	public Mobi importForMobiOfOWL(String inputFileOWL)
			throws ExceptionMobiFile, ExceptionURI {
		OntModel ontology = ModelFactory.createOntologyModel();
		InputStream in = FileManager.get().open(inputFileOWL);

		if (in == null)
			throw new IllegalArgumentException("OWL file not found.");

		try {
			ontology.read(in, null);
		} catch (Exception ex) {
			throw new ExceptionMobiFile("Problem reading the OWL file.");
		}

		String domainname = null;
		if (ontology.getNsPrefixMap().containsKey(
				this.validatorOWLGeneratedByMMobi)) {
			domainname = ontology
					.getNsPrefixURI(this.validatorOWLGeneratedByMMobi);
		} else {
			throw new ExceptionMobiFile("OWL file not generated by m-mobi.");
		}

		Mobi mobi = new Mobi(domainname.substring(
				domainname.lastIndexOf("/") + 1, domainname.lastIndexOf("#")));
		ExtendedIterator<OntClass> classes = ontology.listClasses();

		while (classes.hasNext()) {
			OntClass jenaClass = classes.next();

			if (jenaClass.getURI() != null) {
				Class mobiClass = new Class(jenaClass.getLocalName());
				mobi.addConcept(mobiClass);
				ExtendedIterator<? extends OntResource> individuals = jenaClass
						.listInstances();

				while (individuals.hasNext()) {
					Individual jenaIndividual = individuals.next()
							.asIndividual();
					Instance mobiInstance = new Instance(jenaIndividual
							.getLocalName());
					mobi.addConcept(mobiInstance);
					mobi.isOneOf(mobiInstance, mobiClass);
				}

				mobi = this.AddInheritanceOrEquivalenceMobiClass(mobi,
						Relation.INHERITANCE, jenaClass);

				mobi = this.AddInheritanceOrEquivalenceMobiClass(mobi,
						Relation.EQUIVALENCE, jenaClass);
			}
		}

		ExtendedIterator<ObjectProperty> properties = ontology
				.listObjectProperties();
		while (properties.hasNext()) {
			ObjectProperty property = properties.next();

			OntClass domain = property.listDomain().next().asClass()
					.asUnionClass().listOperands().toList().get(0);
			OntClass range = property.listRange().next().asClass()
					.asUnionClass().listOperands().toList().get(0);

			Relation r = null;

			if (property.isSymmetricProperty())
				r = mobi.createSymmetricRelation(this.getNameObjectProperty(
						property.getLocalName(),
						domain.getLocalName().length(), range.getLocalName()
								.length()));
			else if (property.getInverse() == null)
				r = mobi.createUnidirecionalCompositionRelationship(this
						.getNameObjectProperty(property.getLocalName(), domain
								.getLocalName().length(), range.getLocalName()
								.length()));
			else if (property.getInverse() != null)
				r = mobi.createBidirecionalCompositionRelationship(this
						.getNameObjectProperty(property.getLocalName(), domain
								.getLocalName().length(), range.getLocalName()
								.length()), this.getNameObjectProperty(property
						.getInverse().getLocalName(), range.getLocalName()
						.length(), domain.getLocalName().length()));

			r.setClassA(mobi.getClass(domain.getLocalName()));
			r.setClassB(mobi.getClass(range.getLocalName()));

			ExtendedIterator<? extends OntResource> individuals = domain
					.listInstances();

			while (individuals.hasNext()) {
				Individual individualDomain = individuals.next().asIndividual();

				NodeIterator propertyValues = ontology.getIndividual(
						individualDomain.getURI()).listPropertyValues(property);

				while (propertyValues.hasNext()) {
					RDFNode node = propertyValues.next();
					Individual individualValue = node.as(Individual.class);
					r.addInstanceRelation(mobi.getInstance(individualDomain
							.getLocalName()), mobi.getInstance(individualValue
							.getLocalName()));
				}
			}

			r.processCardinality();
			mobi.addConcept(r);
		}

		return mobi;
	}

	private String getNameObjectProperty(String nameMobiObjectProperty,
			int lengthClassA, int lengthClassB) {
		int quantity = nameMobiObjectProperty.length()
				- (lengthClassA + lengthClassB + 2);
		return nameMobiObjectProperty.substring(lengthClassA + 1, lengthClassA
				+ 1 + quantity);
	}

	private Mobi AddInheritanceOrEquivalenceMobiClass(Mobi mobi,
			int typeRelation, OntClass jenaClass) {

		Class mobiClass = mobi.getClass(jenaClass.getLocalName());
		List<OntClass> subClasses = null;

		if (typeRelation == Relation.INHERITANCE)
			subClasses = this.OntClassInheritanceChain(jenaClass
					.listSubClasses().toList());
		else if (typeRelation == Relation.EQUIVALENCE)
			subClasses = jenaClass.listEquivalentClasses().toList();

		for (OntClass jenaSubClass : subClasses) {
			if (jenaSubClass != null
					&& !jenaSubClass.getLocalName().equals(mobiClass.getUri())) {

				Class mobiSubClass = new Class(jenaSubClass.getLocalName());
				mobi.addConcept(mobiSubClass);

				Relation relation = null;
				if (typeRelation == Relation.INHERITANCE)
					relation = mobi.createInheritanceRelation("isSuper");
				else if (typeRelation == Relation.EQUIVALENCE)
					relation = mobi.createEquivalenceRelation("equals");

				relation.setClassA(mobiClass);
				relation.setClassB(mobiSubClass);

				ExtendedIterator<? extends OntResource> individualsClassA = jenaClass
						.listInstances();
				ExtendedIterator<? extends OntResource> individualsClassB = jenaSubClass
						.listInstances();

				while (individualsClassA.hasNext()) {
					Individual individualA = individualsClassA.next()
							.asIndividual();
					Instance instanceA = mobi.getInstance(individualA
							.getLocalName());

					while (individualsClassB.hasNext()) {
						Individual individualB = individualsClassB.next()
								.asIndividual();
						Instance instanceB = mobi.getInstance(individualB
								.getLocalName());

						if (instanceB != null
								&& instanceA.getUri()
										.equals(instanceB.getUri()))
							relation.addInstanceRelation(instanceA, instanceB);
					}
				}

				relation.processCardinality();
				mobi.addConcept(relation);
			}
		}

		return mobi;
	}

	private List<OntClass> OntClassInheritanceChain(List<OntClass> _subClasses) {

		List<OntClass> removeSubClasses = new ArrayList<OntClass>();

		for (OntClass jenaClass : _subClasses) {
			List<OntClass> subClassCopy = jenaClass.listSubClasses().toList();
			for (OntClass subClass : subClassCopy) {
				if (_subClasses.contains(subClass)) {
					if (!removeSubClasses.contains(subClass))
						removeSubClasses.add(subClass);
				}
			}
		}

		for (OntClass subClass : removeSubClasses)
			_subClasses.remove(_subClasses.lastIndexOf(subClass));

		return _subClasses;
	}

	public void exportMobiToOWL(String ontoFileName) {

		try {
			this.jena = this.provider.getOntology(ontoFileName);
			this.jena.setNsPrefix(this.validatorOWLGeneratedByMMobi,
					this.ontologyPrefix);

			this.jena.begin();
			// Basic Concepts
			this.exportClasses();
			this.exportInstances();

			// Relations
			this.exportInheritanceRelations();
			this.exportCompositionRelations();
			this.exportSymmetricRelations();
			this.exportEquivalenceRelations();

			this.jena.commit();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {

		// Mobi2OWL mobi2OWL = new Mobi2OWL("http://www.mobi.org/",
		// TesteMOBIProfessorAluno.carregaDominioProfessorAluno());
		// Mobi2OWL mobi2OWL = new Mobi2OWL("http://www.mobi.org/",
		// TesteMOBIPessoa.carregaDominioPessoa());
		Mobi mobi = TesteMOBIRegiao.carregaDominioRegiao();
		Mobi2OWL mobi2OWL = new Mobi2OWL("http://www.mobi.org/", mobi);
		// Mobi2OWL mobi2OWL = new Mobi2OWL("http://www.mobi.org/",
		// TesteMobiCampeonatoBasquete.CarregaDominio());
		// Mobi2OWL mobi2OWL = new Mobi2OWL("http://www.mobi.org/",
		// TesteMOBIRegiao.carregaDominioRegiao());
		// Mobi2OWL mobi2OWL = new Mobi2OWL("http://www.mobi.org/",
		// TesteMOBIAmericaDoSul.carregaDominioAmericaDoSul());

		mobi2OWL.setExportPath("C:\\BaseOntologia");
		mobi2OWL.exportMobiToOWL("Teste.owl");

		Mobi importForMobiOfOWL = mobi2OWL
				.importForMobiOfOWL("C:\\BaseOntologia\\Teste.owl");

		Mobi2OWL mobi3OWL = new Mobi2OWL("http://www.mobi.org/",
				importForMobiOfOWL);
		mobi3OWL.setExportPath("C:\\BaseOntologia");
		mobi3OWL.exportMobiToOWL("Teste2.owl");

		System.out.println("Fim da Exportacao");
	}

}
