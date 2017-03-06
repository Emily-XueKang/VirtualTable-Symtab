package cs652.j.semantics;

import cs652.j.parser.JBaseListener;
import cs652.j.parser.JParser;
import org.antlr.symtab.*;

import java.util.List;

public class DefineScopesAndSymbols extends JBaseListener {
	public Scope currentScope;
	public static final Type JINT_TYPE = new JPrimitiveType("int");
	public static final Type JFLOAT_TYPE = new JPrimitiveType("float");
	public static final Type JSTRING_TYPE = new JPrimitiveType("string");
	public static final Type JVOID_TYPE = new JPrimitiveType("void");

	public DefineScopesAndSymbols(GlobalScope globals) {
		currentScope = globals;
	}


	@Override
	public void enterFile(JParser.FileContext ctx) {
		currentScope.define((JPrimitiveType)JINT_TYPE);
		currentScope.define((JPrimitiveType)JFLOAT_TYPE);
		currentScope.define((JPrimitiveType)JSTRING_TYPE);
		currentScope.define((JPrimitiveType)JVOID_TYPE);

	}

	@Override
	public void enterMain(JParser.MainContext ctx) {
		Type type = ComputeTypes.JVOID_TYPE;
		JMethod main = new JMethod("main",ctx);
		main.setType(type);
		//System.out.println("currentscope-befoermain:"+currentScope);
		currentScope.define(main);
		currentScope = main;
		ctx.scope = (JMethod) currentScope;
		//System.out.println("currentscope-main:"+currentScope);
	}

	@Override
	public void exitMain(JParser.MainContext ctx) {
		currentScope = currentScope.getEnclosingScope();
	}

	@Override
	public void enterBlock(JParser.BlockContext ctx) {
		LocalScope ls = new LocalScope(currentScope);
		currentScope.nest(ls);
		currentScope = ls;
		ctx.scope = (LocalScope) currentScope;
//		String scopename = currentScope.getName();
//		System.out.println("currentscope-blk:"+ currentScope + scopename);
//		System.out.println("currentscope-ctx.scope:"+ ctx.scope + ctx.scope.getName());
	}

	@Override
	public void exitBlock(JParser.BlockContext ctx)
	{
		currentScope = currentScope.getEnclosingScope();
	}

	@Override
	public void enterLocalVariableDeclaration(JParser.LocalVariableDeclarationContext ctx) {
		Symbol type = currentScope.resolve(ctx.jType().getText());
		String scopename = currentScope.getName();
		//System.out.println("currentscope-blcok:"+ currentScope + scopename);
		String id = ctx.ID().getText();
		//System.out.println("varid:"+id);
		JVar var = new JVar(id);
		currentScope.define(var);
		//System.out.println("currentscope-var:"+currentScope);
		var.setType((Type) type);
		List<JVar> vl = (List<JVar>) currentScope.getAllSymbols();
		//System.out.println("allsym:" + vl);
	}


	public void enterClassDeclaration (JParser.ClassDeclarationContext ctx){
		String className = ctx.name.getText();
		//System.out.println("classname:" + className);
		JClass cs = new JClass(className,ctx.getParent());
		currentScope.define(cs);
		currentScope = cs;
		ctx.scope = (JClass) currentScope;//ClassDeclarationContext ctx's scope field is JClass type
	}
	public void exitClassDeclaration(JParser.ClassDeclarationContext ctx)
	{
		currentScope = currentScope.getEnclosingScope();
	}



	@Override
	public void enterMethodDeclaration(JParser.MethodDeclarationContext ctx) {
		String id = ctx.ID().getText();
		System.out.println("mid:" + id);
		JMethod m= new JMethod(id,ctx);
		JArg thisarg = new JArg("this");
		if(ctx.jType()!=null){
			Symbol type = currentScope.resolve(ctx.jType().getText());
			m.setType((Type) type);
			System.out.println("type:"+type.toString());
		}
		else{
			m.setType(JVOID_TYPE);
		}
		thisarg.setType((Type) currentScope.resolve(currentScope.getName()));
		System.out.println("type:"+currentScope.getName());
		currentScope.define(m);
		currentScope = m;
		currentScope.define(thisarg);
		ctx.scope = (JMethod) currentScope;
	}

	@Override
	public void exitMethodDeclaration(JParser.MethodDeclarationContext ctx) {
		currentScope = currentScope.getEnclosingScope();
	}

	@Override
	public void enterFieldDeclaration(JParser.FieldDeclarationContext ctx) {
		Symbol type = currentScope.resolve(ctx.jType().getText());
		String id = ctx.ID().getText();
		System.out.println("fieldid:"+id);
		JField f = new JField(id);
		currentScope.define(f);
		f.setType((Type) type);
	}


	@Override
	public void enterFormalParameter(JParser.FormalParameterContext ctx) {
		Symbol type = currentScope.resolve(ctx.jType().getText());
		String id = ctx.ID().getText();
		JArg pv = new JArg(id);
		System.out.println("varid:" + id);
		pv.setType((Type)type);
		currentScope.define(pv);
	}



}




