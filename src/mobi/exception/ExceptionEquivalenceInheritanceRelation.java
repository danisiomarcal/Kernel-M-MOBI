package mobi.exception;

public class ExceptionEquivalenceInheritanceRelation extends Exception
implements java.io.Serializable
{
	private static final long serialVersionUID = 1219514491917320869L;

	public ExceptionEquivalenceInheritanceRelation(String msg) {
		super(msg);
	}
}
