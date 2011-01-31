package mobi.test.mobi;

import java.io.IOException;

import mobi.core.Mobi;
import mobi.core.common.Relation;
import mobi.core.concept.Class;
import mobi.core.concept.Instance;

public class TesteMOBIPessoa {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		TesteMOBIPessoa.carregaDominioPessoa();
	}

	public static Mobi carregaDominioPessoa() {
		Mobi mobi = new Mobi("Pessoa");

		Instance iDanisio = new Instance("Danisio");
		Instance iThiago    = new Instance("Thiago");
		
		Class cPessoa  = new Class("cPessoa");
		Class cPessoaJuridica = new Class("cPessoaJ");
		Class cPessoFisica = new Class("cPessoF");
		
		try {
			mobi.addConcept(iDanisio);
			mobi.addConcept(iThiago);
			
			mobi.addConcept(cPessoa);
			mobi.addConcept(cPessoaJuridica);
			mobi.addConcept(cPessoFisica);
			
			mobi.isOneOf(iDanisio, cPessoa);
			mobi.isOneOf(iDanisio, cPessoaJuridica);
			mobi.isOneOf(iThiago, cPessoa);
			mobi.isOneOf(iThiago, cPessoFisica);
			
			Relation r = mobi.createInheritanceRelation("inheritance");

			r.setClassA(cPessoa);
			r.setClassB(cPessoaJuridica);
			r.addInstanceRelation(iDanisio, iDanisio);
			r.processCardinality();
			mobi.addConcept(r);
			
			r = mobi.createInheritanceRelation("inheritance");

			r.setClassA(cPessoa);
			r.setClassB(cPessoFisica);
			r.addInstanceRelation(iThiago, iThiago);
			r.processCardinality();
			mobi.addConcept(r);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mobi;

	}
}