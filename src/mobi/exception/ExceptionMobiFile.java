package mobi.exception;

import java.io.Serializable;

public class ExceptionMobiFile extends Exception implements
Serializable{

	private static final long serialVersionUID = 5624804955055154479L;
	
	public ExceptionMobiFile(String exception) {
		super(exception);
	}
}
