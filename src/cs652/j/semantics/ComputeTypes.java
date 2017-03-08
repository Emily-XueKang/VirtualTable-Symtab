package cs652.j.semantics;

import cs652.j.parser.JBaseListener;
import cs652.j.parser.JParser;
import org.antlr.symtab.*;
import org.antlr.v4.runtime.tree.TerminalNode;

public class ComputeTypes extends JBaseListener {
	protected StringBuilder buf = new StringBuilder();
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


    @Override
    public void enterFile(JParser.FileContext ctx) {
	    currentScope = ctx.scope;
    }

    @Override
    public void enterMain(JParser.MainContext ctx) {
        currentScope = ctx.scope;
    }

    @Override
    public void enterBlock(JParser.BlockContext ctx) {
        currentScope = ctx.scope;
    }

    @Override
    public void enterClassDeclaration(JParser.ClassDeclarationContext ctx) {
        currentScope = ctx.scope;
    }

    @Override
    public void enterMethodDeclaration(JParser.MethodDeclarationContext ctx) {
        currentScope = ctx.scope;
    }

//    @Override
//    public void exitFieldRef(JParser.FieldRefContext ctx) {
//        String id = ctx.ID().getText();
//        TypedSymbol var;
//        JClass cs = (JClass) ctx.expression().type;
//	    var = (TypedSymbol) cs.resolveMember(id);
//	    if(var != null){
//        ctx.type = var.getType();
//        }
//        buf.append(ctx.getText()+ " is " +ctx.type + "\n");
//    }
//


    @Override
    public void exitFieldRef(JParser.FieldRefContext ctx) {
        String id = ctx.ID().getText();
        String text = ctx.getText();
        System.out.println("id is: "+ id);
        System.out.println("text is: "+ text);
        TypedSymbol var;
        JClass cs = (JClass) ctx.expression().type;
        System.out.println("class: "+ cs);
        if(cs != null){
            var = (TypedSymbol) cs.resolveMember(id);
            if(var != null)
            ctx.type = var.getType();
        }
//	    var = (TypedSymbol) cs.resolveMember(id);
//	    if(var != null){
//        ctx.type = var.getType();
//        }
        buf.append(ctx.getText()+ " is " +ctx.type + "\n");
    }

    @Override
    public void enterCtorCall(JParser.CtorCallContext ctx) {
        Symbol type = currentScope.resolve(ctx.ID().getText());
        //TypedSymbol var = (TypedSymbol) currentScope.resolve(ctx.ID().getText());
        ctx.type = (Type) type;
        buf.append(ctx.getText()+ " is " +ctx.type + "\n");
    }

    @Override
    public void enterLiteralRef(JParser.LiteralRefContext ctx) {
	    if(ctx.FLOAT()!=null){
	        ctx.type = JFLOAT_TYPE;
        }
        else if(ctx.INT()!=null){
	        ctx.type = JINT_TYPE;
        }
        buf.append(ctx.getText()+ " is " +ctx.type + "\n");
    }

    @Override
    public void exitIdRef(JParser.IdRefContext ctx) {
	    System.out.println("idscope:" + currentScope);
        TypedSymbol var = (TypedSymbol)currentScope.resolve(ctx.ID().getText());
        System.out.println("var is :" + var);
        if(var != null){
            ctx.type = var.getType();
        }
        buf.append(ctx.getText()+ " is " +ctx.type + "\n");
    }

    @Override
    public void exitThisRef(JParser.ThisRefContext ctx) {
        System.out.println("this in scope:" + currentScope.getEnclosingScope().getEnclosingScope());
	    JClass c = (JClass) currentScope.getEnclosingScope().getEnclosingScope().resolve(currentScope.getEnclosingScope().getEnclosingScope().getName());
        ctx.type = c;
        buf.append(ctx.getText() + " is " +ctx.type + "\n" );
    }

    @Override
    public void enterNullRef(JParser.NullRefContext ctx) {
        ctx.type = JVOID_TYPE;
    }

    @Override
    public void exitMethodCall(JParser.MethodCallContext ctx) {
        TypedSymbol mvar = (TypedSymbol)currentScope.resolve(ctx.ID().getText());
        if(mvar !=null){
            ctx.type = mvar.getType();
        }
        buf.append(ctx.getText() + " is " +ctx.type + "\n");
    }

    @Override
    public void exitQMethodCall(JParser.QMethodCallContext ctx) {
        String id = ctx.ID().getText();
        TypedSymbol mvar;
        JClass cs = (JClass) ctx.expression().type;
        if(cs != null){
            mvar = (TypedSymbol) cs.resolveMember(id);
            if(mvar != null){
                ctx.type = mvar.getType();
            }
        }
        buf.append(ctx.getText()+ " is " +ctx.type + "\n");
    }


    public String getRefOutput() {
		return buf.toString();
	}
}

