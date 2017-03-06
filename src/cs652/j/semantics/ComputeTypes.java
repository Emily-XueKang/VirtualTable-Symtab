package cs652.j.semantics;

import cs652.j.parser.JBaseListener;
import cs652.j.parser.JParser;
import org.antlr.symtab.*;

public class ComputeTypes extends JBaseListener {
	protected StringBuilder buf = new StringBuilder();
	//GlobalScope global;
	Scope currentScope;

	public static final Type JINT_TYPE = new JPrimitiveType("int");
	public static final Type JFLOAT_TYPE = new JPrimitiveType("float");
	public static final Type JSTRING_TYPE = new JPrimitiveType("string");
	public static final Type JVOID_TYPE = new JPrimitiveType("void");

	public ComputeTypes(GlobalScope globals) {
		this.currentScope = globals;
	}

	// ...

	// S U P P O R T
//	@Override
//	public void enterIntRef(JParser.JTypeContext ctx) {
//		ctx.type = JINT_TYPE;
//	}
//
//	@Override
//	public void enterFloatRef(JParser.FloatRefContext ctx) {
//		ctx.type = JFLOAT_TYPE;
//	}


    @Override
    public void enterFile(JParser.FileContext ctx) {
        currentScope = ctx.scope;
    }

//    @Override
//    public void exitFieldRef(JParser.FieldRefContext ctx) {
//
//
//    }


    @Override
    public void enterFieldRef(JParser.FieldRefContext ctx) {
        TypedSymbol var = (TypedSymbol)currentScope.resolve(ctx.ID().getText());
        ctx.type = var.getType();
    }

    @Override
    public void exitThisRef(JParser.ThisRefContext ctx) {
        //resolve this.x in the current class
        TypedSymbol var = (TypedSymbol)currentScope.resolve(ctx.getText());
        ctx.type = var.getType();
    }

    public String getRefOutput() {
		return buf.toString();
	}
}

