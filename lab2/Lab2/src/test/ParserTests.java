package test;

import static org.junit.Assert.fail;

import java.io.StringReader;

import lexer.Lexer;

import org.junit.Test;

import parser.Parser;

public class ParserTests {
	private void runtest(String src) {
		runtest(src, true);
	}

	private void runtest(String src, boolean succeed) {
		Parser parser = new Parser();
		try {
			parser.parse(new Lexer(new StringReader(src)));
			if(!succeed) {
				fail("Test was supposed to fail, but succeeded");
			}
		} catch (beaver.Parser.Exception e) {
			if(succeed) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		} catch (Throwable e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testEmptyModule() {
		runtest("module Test { }");
	}
	@Test
	public void testModuleWithImports() {
		runtest("module Test { import lib1; }");
		runtest("module Test { import lib1; import lib2; }");
		runtest("module Test { import lib1; import lib2; import lib3; }");
	}
	
	@Test
	public void testModuleWithInvalidImports() {
		runtest("module Test { import lib1 }", false);
		runtest("module Test { import lib1 import lib2; }", false);
		runtest("module Test { import lib1; import lib2; import lib3 }", false);
	}
	
	@Test
	public void testModuleWithModuleDeclarations_TypeDeclaration() {
		runtest("module Test { public type t = \"asd\"; }");
		runtest("module Test { type t = \"asd\"; }");
	}
	
	@Test
	public void testModuleWithModuleDeclarations_FieldDeclaration() {
		runtest("module Test { public void t; }");
		runtest("module Test { void t; }");
		runtest("module Test { public int t; }");
		runtest("module Test { int t; }");
		runtest("module Test { public boolean t; }");
		runtest("module Test { boolean t; }");
		
		runtest("module Test { public Class1 t; }");
		runtest("module Test { Class1 t; }");
		
		runtest("module Test { public Class1[] t; }");
		runtest("module Test { Class1[] t; }");
		runtest("module Test { public Class1[][] t; }");
		runtest("module Test { Class1[][] t; }");
		runtest("module Test { public int[] t; }");
		runtest("module Test { int[] t; }");
		runtest("module Test { public int[][] t; }");
		runtest("module Test { int[][] t; }");
		runtest("module Test { int[][][][][] t; }");
	}
	
	@Test
	public void testModuleWithModuleDeclarations_FunctionDeclaration_EmptyStatement() {
		runtest("module Test { public int[][] testArray(int[][] t, boolean x) {} }");
		runtest("module Test { public int[][] testArray(Class1[][] t) {} }");
		runtest("module Test { public int[][] testArray() {} }");
		runtest("module Test { public int[][] testArray(int[][] t,) {} }", false);
	}
	
	@Test
	public void testModuleWithModuleDeclarations_FunctionDeclaration_LocalVariable() {
		runtest("module Test{ public int[][] testArray() { void t; } }");
		runtest("module Test{ public int[][] testArray() { void t; int x; } }");
		runtest("module Test{ public int[][] testArray() { while (x) {} } }");
		runtest("module Test{ public int[][] testArray() { void t int x; } }", false);
	}
	
	@Test
	public void testModuleWithModuleDeclarations_FunctionDeclaration_WhileStatement() {
		runtest("module Test { public Class1[][] test() { while (true) {} } }");
		runtest("module Test { public Class1[][] test() { while (x) { int x; string s; } } }");
		runtest("module Test { public Class1[][] test() { while (x) { int[][] x; } } }");
		runtest("module Test { public Class1[][] test() { while (x = 10) { int[][] x; } } }");
		runtest("module Test { public Class1[][] test() { while (x = 10) { break; return x; } } }");
		runtest("module Test { public Class1[][] test() { while (x = 10) { 100 == 129 * 120 - 120; } } }");
		runtest("module Test { public Class1[][] test() { while (x = 10) { while (x + y == 100) { test(100 + 120 * -120); } } } }");
		runtest("module Test { public Class1[][] test() { while (x = 10) { 100 == 100; 200 == 200; } } }");
		runtest("module Test { public Class1[][] test() { while (x = 10) { if (x == 100) { int j; j = 1000 + (-100); } else { { string k; k = \"123\"; } } } } }");
	}
	
	@Test
	public void testModuleWithModuleDeclarations_Function_Declaration_IfStatement() {
		runtest("module Test { public Class1 test() { if (t) f = 100; } }");
		runtest("module Test { public Class1 test() { if (t) f = 100; else while (true) { return 123; } } }");
		runtest("module Test { public Class1 test() { if (t) { f = 100; ttt = 1000; } else { while (true) { if (100 == 100) return true; else return false; } } } }");
		runtest("module Test { public Class1 test() { if (t) { int f; f = 100; } else while (true) { string k; } } }");
		runtest("module Test { public Class1 test() { else while (true) { return 123; } } }", false);
	}
	
	@Test
	public void testModuleWithModuleDeclarations_FunctionDeclaration_BreakStatement() {
		runtest("module Test{ public int[][] testArray() { break; } }");
		runtest("module Test{ public int[][] testArray() { break } }", false);
	}
	
	@Test
	public void testModuleWithModuleDeclarations_FunctionDeclaration_ReturnStatement() {
		runtest("module Test{ public int[][] testArray() { return; } }");
		runtest("module Test{ public int[][] testArray() { return -100 + 100 - 240 == -80 * 125 * 80; } }");
		runtest("module Test{ public int[][] testArray() { return -100; } }");
		runtest("module Test{ public int[][] testArray() { return -100 + 100 + 240 <= \"Test 1\"; } }");
		runtest("module Test{ public int[][] testArray() { return false != true; } }");
		runtest("module Test{ public int[][] testArray() { return test() > (100 * 200 / 300 % 100); } }");
		runtest("module Test{ public int[][] testArray() { return test(100, \"test\", true, false) > (100 * 200 / 300 % 100); } }");
		runtest("module Test{ public int[][] testArray() { return [1, 2, 3, true, false, test(199, \"testasd\")] >= id1; } }");
		runtest("module Test{ public int[][] testArray() { return arr[getIndex(100)] >= id1; } }");
		runtest("module Test{ public int[][] testArray() { return a = 100 * -100; } }");
		runtest("module Test{ public int[][] testArray() { return a[(100 * 100)] = 100 * -100; } }");
		
		runtest("module Test{ public int[][] testArray() { return f(a,); } }", false);
		runtest("module Test{ public int[][] testArray() { return [] } }", false);
		runtest("module Test{ public int[][] testArray() { return [a, ] } }", false);
	}
	
	@Test
	public void testModuleWithModuleDeclarations_FunctionDeclaration_ExpressionStatement() {
		runtest("module Test{ public int[][] testArray() { -100 + 100 - 240 == -80 * 125 * 80; } }");
		runtest("module Test{ public int[][] testArray() { -100; } }");
		runtest("module Test{ public int[][] testArray() { -100 + 100 + 240 <= \"Test 1\"; } }");
		runtest("module Test{ public int[][] testArray() { false != true; } }");
		runtest("module Test{ public int[][] testArray() { test() > (100 * 200 / 300 % 100); } }");
		runtest("module Test{ public int[][] testArray() { test(100, \"test\", true, false) > (100 * 200 / 300 % 100); } }");
		runtest("module Test{ public int[][] testArray() { [1, 2, 3, true, false, test(199, \"testasd\")] >= id1; } }");
		runtest("module Test{ public int[][] testArray() { arr[getIndex(100)] >= id1; } }");
		runtest("module Test{ public int[][] testArray() { a = 100 * -100; } }");
		runtest("module Test{ public int[][] testArray() { arr[100] = 100 * -100; } }");
		runtest("module Test{ public int[][] testArray() { brea; } }");
		
		runtest("module Test{ public int[][] testArray() { return f(a,); } }", false);
		runtest("module Test{ public int[][] testArray() { return [] } }", false);
		runtest("module Test{ public int[][] testArray() { return [a, ] } }", false);
	}
	
	@Test
	public void testFunctionCall(){
		//test precondition
		runtest("module Test{ public int function_name() {  } }");
		
		//test function call 
		runtest("module Test{ public int function_name() { abc(); } }");
		runtest("module Test{ public int function_name() { abc(a=1); } }");
		runtest("module Test{ public int function_name() { abc(a=1,b=2); } }");		
		runtest("module Test{ public int function_name() { abc(a=1); } }");
		runtest("module Test{ public int function_name() { abc(a,b); } }");
		runtest("module Test{ public int function_name() { abc(1,2); } }");
	}
	
	@Test
	public void testArrayExpression(){
		//test precondition
		runtest("module Test{ public int function_name() {  } }");
		
		//test array expression 
		runtest("module Test{ public int function_name() { []; } }",false);
		runtest("module Test{ public int function_name() { [a=1]; } }");
		runtest("module Test{ public int function_name() { [a=1,b=2]; } }");
		runtest("module Test{ public int function_name() { [a]; } }");
		runtest("module Test{ public int function_name() { [a,b]; } }");
		runtest("module Test{ public int function_name() { [1]; } }");
		runtest("module Test{ public int function_name() { [1,2]; } }");
	}
	
	@Test
	public void testBooleanLiteral(){
		//test precondition
		runtest("module Test{ public int function_name() {  } }");
		
		//start test
		runtest("module Test{ public int function_name() { true; } }");
		runtest("module Test{ public int function_name() { false; } }");
	}
	
	@Test
	public void testParenthesisExpression(){
		//test precondition
		runtest("module Test{ public int function_name() {  } }");
		
		//start test
		runtest("module Test{ public int function_name() { (); } }",false);
		runtest("module Test{ public int function_name() { (1+1); } }");
		runtest("module Test{ public int function_name() { (true+true); } }");
		runtest("module Test{ public int function_name() { (a[1]+b[c=2]); } }");
	}
	
	@Test
	public void testFactor(){
		//test precondition
		runtest("module Test{ public int function_name() {  } }");
		
		runtest("module Test{ public int function_name() { -123; } }");
		runtest("module Test{ public int function_name() { -\"abc\"; } }");
		runtest("module Test{ public int function_name() { -true; } }");
		runtest("module Test{ public int function_name() { -a[1]; } }");
		runtest("module Test{ public int function_name() { -abc(a=1); } }");
		runtest("module Test{ public int function_name() { a=-3; } }");	
	}
	
	@Test
	public void testTerm(){
		//test precondition
		runtest("module Test{ public int function_name() {  } }");
		
		runtest("module Test{ public int function_name() { -123*-\"abc\"; } }");
		runtest("module Test{ public int function_name() { -true*-a[1]; } }");
		runtest("module Test{ public int function_name() { -abc(a=1)*-3; } }");
		runtest("module Test{ public int function_name() { -abc(a=1)*def(); } }");
	}
	
	@Test
	public void testArithmeticExpression(){
		//test precondition
		runtest("module Test{ public int function_name() {  } }");
		
		runtest("module Test{ public int function_name() { -123*-\"abc\"--true*-a[1]; } }");
		runtest("module Test{ public int function_name() { -abc(a=1)*-3+-abc(a=1)*def(); } }");
	}
	
	@Test
	public void testRHSAssignment(){
		//test precondition
		runtest("module Test{ public int function_name() {  } }");
		
		runtest("module Test{ public int function_name() { a()==true; } }");
		runtest("module Test{ public int function_name() { a()!=1; } }");
		runtest("module Test{ public int function_name() { a()<\"abc\"; } }");
		runtest("module Test{ public int function_name() { a()>a(); } }");
		runtest("module Test{ public int function_name() { a()<=a(); } }");
		runtest("module Test{ public int function_name() { a()>=true; } }");
		runtest("module Test{ public int function_name() { -123*-\"abc\"--true*-a[1]==-abc(a=1)*-3+-abc(a=1)*def(); } }");
	}
	
	@Test
	public void testAssignment(){
		//test precondition
		runtest("module Test{ public int function_name() {  } }");
		
		runtest("module Test{ public int function_name() { a=1; } }");
		runtest("module Test{ public int function_name() { a=b=c=3; } }");
		runtest("module Test{ public int function_name() { a=a()==true==false==123==-3; } }",false);
	}

	//yong
	@Test
	public void testLocalVariableDeclaration() {
		runtest("module Test { int a; int[] b; String c;}");
	}
	
	@Test
	public void testBlockStatement() {
		runtest("module Test { int a; int b; boolean c; public void testFunction() { if(a-b<1) {} while(c == true) {} while(c == true) {break;} if(c == true) {return;}} }");
	}
	
	@Test
	public void testIfStatement() {
		runtest("module Test { int a; int b; boolean c; public void testFunction() { if(a-b<1) {} } }");
	}
	
	
	@Test
	public void testWhileStatement() {
		runtest("module Test { int a; int b; boolean c; public void testFunction() { while(c == true) {} } }");
	}
	
	@Test
	public void testBreakStatement() {
		runtest("module Test { int a; int b; boolean c; public void testFunction() { while(c == true) {break;} } }");
	}
	
	@Test
	public void testReturnStatement() {
		runtest("module Test { int a; int b; boolean c; public int testFunction() { if(c == true) {return;} } }");
	}
	
	@Test
	public void testExpressionStatement() {
		runtest("module Test { int a; int b; boolean c; public int testFunction() { a == b; } }");
	}
	

}
