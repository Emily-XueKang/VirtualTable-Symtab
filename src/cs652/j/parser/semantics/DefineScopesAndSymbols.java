package cs652.j.parser.semantics;

import cs652.j.parser.JBaseListener;
import cs652.j.parser.JParser;
import org.antlr.symtab.GlobalScope;
import org.antlr.symtab.Scope;
import org.antlr.symtab.*;

public class DefineScopesAndSymbols extends JBaseListener {
	public Scope currentScope;
	public static final Type JINT_TYPE = new JPrimitiveType("int");
	public static final Type JFLOAT_TYPE = new JPrimitiveType("float");

	public DefineScopesAndSymbols(GlobalScope globals) {
		currentScope = globals;
	}

//	public void enterFile(JParser.FileContext ctx){
//
//	}

	public void enterClassDeclaration (JParser.ClassDeclarationContext ctx){
		String className = ctx.getText();
		JClass cs = new JClass(className,ctx.getParent());
		currentScope.define(cs);
		currentScope = cs;
		//ctx.scope = currentScope;//ClassDeclarationContext ctx's scope field is JClass type
	}

	public void ClassDeclaration(JParser.ClassDeclarationContext ctx) {
		currentScope = currentScope.getEnclosingScope();
	}

}




