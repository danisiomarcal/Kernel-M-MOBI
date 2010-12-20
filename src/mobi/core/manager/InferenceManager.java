package mobi.core.manager;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import mobi.core.common.Relation;
import mobi.core.concept.Instance;
import mobi.core.relation.InstanceRelation;

public class InferenceManager implements Serializable {

	private static final long serialVersionUID = -2422147137827965229L;

	Map<Integer, Integer> relationPossibilitiesMap = new HashMap<Integer, Integer>();

	public Collection<Integer> infereRelation(Relation relation) {
		this.mountRelationPossibilitiesMap();
		this.findRelationPossibilities(relation);
		return this.relationPossibilitiesMap.values();
	}

	public InferenceManager() {
	}

	private void mountRelationPossibilitiesMap() {
		this.relationPossibilitiesMap.put(Relation.BIDIRECIONAL_COMPOSITION,
				Relation.BIDIRECIONAL_COMPOSITION);
		this.relationPossibilitiesMap.put(Relation.EQUIVALENCE,
				Relation.EQUIVALENCE);
		this.relationPossibilitiesMap.put(Relation.INHERITANCE,
				Relation.INHERITANCE);
		this.relationPossibilitiesMap.put(Relation.UNIDIRECIONAL_COMPOSITION,
				Relation.UNIDIRECIONAL_COMPOSITION);
		this.relationPossibilitiesMap.put(Relation.SYMMETRIC_COMPOSITION,
				Relation.SYMMETRIC_COMPOSITION);
	}

	private void findRelationPossibilities(Relation relation) {
		if (!this.InheritanceCanBe(relation))
			this.relationPossibilitiesMap.remove(Relation.INHERITANCE);
		
		if (!this.EquivalenceCanBe(relation))
			this.relationPossibilitiesMap.remove(Relation.EQUIVALENCE);
		
		if (!this.CompositionCanBe(relation))
		{
			this.relationPossibilitiesMap.remove(Relation.BIDIRECIONAL_COMPOSITION);
			this.relationPossibilitiesMap.remove(Relation.UNIDIRECIONAL_COMPOSITION);
			this.relationPossibilitiesMap.remove(Relation.SYMMETRIC_COMPOSITION);
		}
	}

	private boolean InheritanceCanBe(Relation relation)
	{
		if (relation.getClassA().getUri().equals(relation.getClassB().getUri()))
			return false;
		
		for(InstanceRelation instanceRelation: relation.getInstanceRelationMapA().values())
		{
			int numberRelations = instanceRelation.getAllInstances().size();
			if (numberRelations > 1)
				return false;
			else if (numberRelations == 1)
			{
				for(Instance instance: instanceRelation.getAllInstances().values())
				{
					if (!instance.getUri().equals(instanceRelation.getInstance().getUri()))
						return false;
				}
			}
		}
		
		return true;
	}
	
	private boolean EquivalenceCanBe(Relation relation)
	{
		if (relation.getClassA().getUri().equals(relation.getClassB().getUri()))
			return false;
		
		for(InstanceRelation instanceRelation: relation.getInstanceRelationMapA().values())
		{
			int numberRelations = instanceRelation.getAllInstances().size();
			if (numberRelations != 1)
				return false;
			else
			{
				for(Instance instance: instanceRelation.getAllInstances().values())
				{
					if (!instance.getUri().equals(instanceRelation.getInstance().getUri()))
						return false;
				}
			}
		}
		
		return true;
	}
	
	private boolean CompositionCanBe(Relation relation)
	{
		for(InstanceRelation instanceRelation: relation.getInstanceRelationMapA().values())
		{
			int numberRelations = instanceRelation.getAllInstances().size();
			if (numberRelations >= 0)
			{
				for(Instance instance: instanceRelation.getAllInstances().values())
				{
					if (instance.getUri().equals(instanceRelation.getInstance().getUri()))
						return false;
				}
			}else
				return false;
		}
		
		return true;
	}
}

//	private boolean Inheritance(
//			Collection<InstanceRelation> instanceRelationCollection) {
//		for (InstanceRelation instanceRelation : instanceRelationCollection) {
//			if (instanceRelation.getAllInstances().size() == 1) {
//				for (Instance instance : instanceRelation.getAllInstances()
//						.values()) {
//					if (!instanceRelation.getInstance().getUri().equals(
//							instance.getUri())) {
//						return false;
//					}
//				}
//			} else if (instanceRelation.getAllInstances().size() != 0)
//				return false;
//		}
//		return true;
//	}
//
//	private boolean Equivalence(Relation relation) {
//		if (relation.getInstanceRelationMapA().size() == relation
//				.getInstanceRelationMapB().size()) {
//			for (InstanceRelation instanceRelation : relation
//					.getInstanceRelationMapA().values()) {
//				if (instanceRelation.getAllInstances().size() != 1)
//					return false;
//				else {
//					String secondInstance = ((Instance) instanceRelation
//							.getAllInstances().entrySet().iterator().next().getValue())
//							.getUri();
//					if (!instanceRelation.getInstance().getUri().equals(
//							secondInstance))
//						return false;
//				}
//			}
//			return true;
//		}
//		return false;
//	}

//	private boolean CompositionSymmetric(Relation relation) {
//		for (InstanceRelation instanceRelation : relation
//				.getInstanceRelationMapA().values()) {
//			if (instanceRelation.getAllInstances().size() > 0) {
//				for (Instance instance : instanceRelation.getAllInstances()
//						.values()) {
//					if (instanceRelation.getInstance().getUri().equals(
//							instance.getUri()))
//						return false;
//				}
//			} else
//				return false;
//		}
//		return true;
//	}

//private void findRelationPossibilities() {
//
// if (this.premise1) {
// this.relationPossibilitiesMap
// .remove(Relation.BIDIRECIONAL_COMPOSITION);
// this.relationPossibilitiesMap
// .remove(Relation.BIDIRECIONAL_COMPOSITION_HAS_BELONGS_TO);
// this.relationPossibilitiesMap
// .remove(Relation.UNIDIRECIONAL_COMPOSITION);
// this.relationPossibilitiesMap
// .remove(Relation.SYMMETRIC_COMPOSITION);
// }
//
// if (!(this.premise1 && this.premise2)) {
// this.relationPossibilitiesMap.remove(Relation.EQUIVALENCE);
// this.relationPossibilitiesMap.remove(Relation.INHERITANCE);
// }
//
// if (this.premise3) {
// this.relationPossibilitiesMap.remove(Relation.EQUIVALENCE);
// }
//
// if ((this.premise1 && this.premise2 && this.premise3)) {
// this.relationPossibilitiesMap
// .remove(Relation.BIDIRECIONAL_COMPOSITION);
// this.relationPossibilitiesMap
// .remove(Relation.BIDIRECIONAL_COMPOSITION_HAS_BELONGS_TO);
// this.relationPossibilitiesMap.remove(Relation.EQUIVALENCE);
// this.relationPossibilitiesMap
// .remove(Relation.SYMMETRIC_COMPOSITION);
// this.relationPossibilitiesMap
// .remove(Relation.UNIDIRECIONAL_COMPOSITION);
// }
//
// }

// /**
// * Verify if the instances of a relation are always equals in the both
// sides
// * @return true if the premise condition was founded
// */
// private boolean validatePremise1(Collection<InstanceRelation>
// instanceRelationCollection){
// for (InstanceRelation instanceRelation : instanceRelationCollection) {
// if (instanceRelation.getAllInstances().size() != 1) return false;
// for (Instance instance : instanceRelation.getAllInstances().values()) {
// if (!instanceRelation.getInstance().getUri().equals(instance.getUri())){
// return false;
// }
// }
// }
// return true;
// }

// private boolean validatePremise2(Relation relation){
// if (relation.getClassA().getUri().equals(relation.getClassB().getUri()))
// return false;
//		
// return true;
// }
//	
// private boolean validatePremise3(Collection<InstanceRelation>
// instanceRelationCollection){
// for (InstanceRelation instanceRelation : instanceRelationCollection) {
// if (instanceRelation.getAllInstances().size() == 0) return true;
// }
// return false;
// }

// private boolean hasCompositionRelation() {
// if ((this.relationPossibilitiesMap
// .containsKey(Relation.BIDIRECIONAL_COMPOSITION))
// || (this.relationPossibilitiesMap
// .containsKey(Relation.BIDIRECIONAL_COMPOSITION_HAS_BELONGS_TO))
// || (this.relationPossibilitiesMap
// .containsKey(Relation.UNIDIRECIONAL_COMPOSITION))
// || (this.relationPossibilitiesMap
// .containsKey(Relation.SYMMETRIC_COMPOSITION))) {
// return true;
// }
// return false;
// }

// private void findCompositionRelationPossibilities() {
//
// if (this.compositionPremise1 && this.compositionPremise2
// && !this.compositionPremise3) {
// this.relationPossibilitiesMap
// .remove(Relation.BIDIRECIONAL_COMPOSITION);
// this.relationPossibilitiesMap
// .remove(Relation.BIDIRECIONAL_COMPOSITION_HAS_BELONGS_TO);
// this.relationPossibilitiesMap
// .remove(Relation.SYMMETRIC_COMPOSITION);
// }
//
// if (!this.compositionPremise1 && this.compositionPremise2
// && !this.compositionPremise3) {
// this.relationPossibilitiesMap
// .remove(Relation.BIDIRECIONAL_COMPOSITION);
// this.relationPossibilitiesMap
// .remove(Relation.BIDIRECIONAL_COMPOSITION_HAS_BELONGS_TO);
// this.relationPossibilitiesMap
// .remove(Relation.SYMMETRIC_COMPOSITION);
// }
//
// if (this.compositionPremise2 && this.compositionPremise3) {
// this.relationPossibilitiesMap
// .remove(Relation.UNIDIRECIONAL_COMPOSITION);
// this.relationPossibilitiesMap
// .remove(Relation.SYMMETRIC_COMPOSITION);
// }
//
// if (this.compositionPremise2 && this.compositionPremise3
// && !this.compositionPremise4) {
// this.relationPossibilitiesMap
// .remove(Relation.BIDIRECIONAL_COMPOSITION);
// this.relationPossibilitiesMap
// .remove(Relation.BIDIRECIONAL_COMPOSITION_HAS_BELONGS_TO);
// this.relationPossibilitiesMap
// .remove(Relation.UNIDIRECIONAL_COMPOSITION);
// }
//
// }

// private boolean validateCompositionPremise1(Collection<InstanceRelation>
// instanceRelationCollection){
//		
// for (InstanceRelation instanceRelation : instanceRelationCollection) {
// if (instanceRelation.getAllInstances().size() > 1) return false;
// }
//		
// return true;
// }
//	
// private boolean validateCompositionPremise2(Relation relation){
// CompositionRelation compositionRelation = (CompositionRelation)relation;
// if(!compositionRelation.getNameA().equals(""))
// return true;
// return false;
// }
//	
// private boolean validateCompositionPremise3(Relation relation){
// CompositionRelation compositionRelation = (CompositionRelation)relation;
// if(!compositionRelation.getNameB().equals(""))
// return true;
// return false;
// }
//	
// private boolean validateCompositionPremise4(Relation relation){
// CompositionRelation compositionRelation = (CompositionRelation)relation;
// if(compositionRelation.getNameA().equals(compositionRelation.getNameB()))
// return true;
// return false;
// }
