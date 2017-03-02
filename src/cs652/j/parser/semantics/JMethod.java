package cs652.j.parser.semantics;

import org.antlr.symtab.MethodSymbol;
import org.antlr.v4.runtime.ParserRuleContext;

public class JMethod extends MethodSymbol {

	protected String implicitparameter = "this";
	public JMethod(String name, ParserRuleContext tree) {
		super(name);
		setDefNode(tree);
	}


}
