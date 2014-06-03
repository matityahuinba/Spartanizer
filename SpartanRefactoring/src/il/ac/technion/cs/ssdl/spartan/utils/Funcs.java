package il.ac.technion.cs.ssdl.spartan.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Assignment.Operator;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

/**
 * 
 * Useful Functions
 * 
 */
public enum Funcs {
	;
	/**
	 * @param os
	 *          an unknown number of objects
	 * @return true if one of the objects is a null or false otherwise
	 */
	public static boolean hasNull(final Object... os) {
		for (final Object o : os)
			if (o == null)
				return true;
		return false;
	}
	/**
	 * @param t
	 *          the AST who is to own the new variable declaration fragment
	 * @param r
	 *          the ASTRewrite for the given AST
	 * @param varName
	 *          the variable name for the new fragment
	 * @param initalizer
	 *          the initial value for the new fragment (for the variable)
	 * @return the new variable declaration fragment or null if one of the given
	 *         parameters was null
	 */
	public static VariableDeclarationFragment makeVarDeclFrag(final AST t, final ASTRewrite r, final SimpleName varName,
			final Expression initalizer) {
		if (hasNull(t,r,varName,initalizer))
			return null;
		final VariableDeclarationFragment $ = t.newVariableDeclarationFragment();
		$.setInitializer(null == initalizer.getParent() ? initalizer : (Expression) r.createCopyTarget(initalizer));
		$.setName(null == varName.getParent() ? varName : (SimpleName) r.createCopyTarget(varName));
		return $;
	}
	/**
	 * @param t
	 *          the AST who is to own the new parenthesized conditional expression
	 * @param r
	 *          ASTRewrite for the given AST
	 * @param cond
	 *          the condition
	 * @param thenExp
	 *          the then statement to set in the conditional
	 * @param elseExp
	 *          the else statement to set in the conditional
	 * @return a parenthesized conditional expression
	 */
	public static ParenthesizedExpression makeParenthesizedConditionalExp(final AST t, final ASTRewrite r, final Expression cond,
			final Expression thenExp, final Expression elseExp) {
		if (hasNull(t,r,cond,thenExp,elseExp))
			return null;
		final ConditionalExpression $ = t.newConditionalExpression();
		$.setExpression(cond.getParent() == null ? cond : (Expression) r.createCopyTarget(cond));
		$.setThenExpression(thenExp.getParent() == null ? thenExp : (Expression) r.createCopyTarget(thenExp));
		$.setElseExpression(elseExp.getParent() == null ? elseExp : (Expression) r.createCopyTarget(elseExp));
		return makeParenthesizedExpression(t, r, $);
	}
	/**
	 * @param t
	 *          the AST who is to own the new If Statement
	 * @param r
	 *          ASTRewrite for the given AST
	 * @param cond
	 *          the condition
	 * @param thenStmnt
	 *          the then statement to set in the If Statement
	 * @param elseStmnt
	 *          the else statement to set in the If Statement
	 * @return a new if Statement
	 */
	public static IfStatement makeIfStmnt(final AST t, final ASTRewrite r, final Expression cond,
			final Statement thenStmnt, final Statement elseStmnt) {
		if (hasNull(t,r,cond,thenStmnt,elseStmnt))
			return null;
		final IfStatement $ = t.newIfStatement();
		$.setExpression(cond.getParent() == null ? cond : (Expression) r.createCopyTarget(cond));
		$.setThenStatement(thenStmnt.getParent() == null ? thenStmnt : (Statement) r.createCopyTarget(thenStmnt));
		$.setElseStatement(elseStmnt.getParent() == null ? elseStmnt : (Statement) r.createCopyTarget(elseStmnt));
		return $;
	}
	/**
	 * @param t
	 *          the AST who is to own the new return statement
	 * @param r
	 *          ASTRewrite for the given AST
	 * @param e
	 *          the expression to return in the return statement
	 * @return the new return statement
	 */
	public static ReturnStatement makeReturnStatement(final AST t, final ASTRewrite r, final Expression e) {
		if (hasNull(t,r))
			return null;
		final ReturnStatement $ = t.newReturnStatement();
		$.setExpression(e == null || e.getParent() == null ? e : (Expression) r.createCopyTarget(e));
		return $;
	}
	/**
	 * @param t
	 *          the AST who is to own the new return statement
	 * @param r
	 *          ASTRewrite for the given AST
	 * @param o
	 *          the operator for the new infix expression
	 * @param left
	 *          the left expression
	 * @param right
	 *          the right expression
	 * @return the new infix expression
	 */
	public static InfixExpression makeInfixExpression(final AST t, final ASTRewrite r, final InfixExpression.Operator o,
			final Expression left, final Expression right) {
		if (hasNull(t,r,o,right,left))
			return null;
		final InfixExpression $ = t.newInfixExpression();
		$.setOperator(o);
		$.setRightOperand(right.getParent() == null ? right : (Expression) r.createCopyTarget(right));
		$.setLeftOperand(left.getParent() == null ? left : (Expression) r.createCopyTarget(left));
		return $;
	}
	/**
	 * @param t
	 *          the AST who is to own the new return statement
	 * @param r
	 *          ASTRewrite for the given AST
	 * @param o
	 *          the assignment operator
	 * @param right
	 *          right side of the assignment, usually an expression
	 * @param left
	 *          left side of the assignment, usually a variable name
	 * @return the new assignment
	 */
	public static Assignment makeAssigment(final AST t, final ASTRewrite r, final Operator o, final Expression right,
			final Expression left) {
		if (hasNull(t,r,o,right,left))
			return null;
		final Assignment $ = t.newAssignment();
		$.setOperator(o);
		$.setRightHandSide(right.getParent() == null ? right : (Expression) r.createCopyTarget(right));
		$.setLeftHandSide(left.getParent() == null ? left : (Expression) r.createCopyTarget(left));
		return $;
	}
	/**
	 * @param t
	 *          the AST who is to own the new return statement
	 * @param r
	 *          ASTRewrite for the given AST
	 * @param operand
	 *          the operand for the new prefix Expression
	 * @param o
	 *          the operator for the new prefix Expression
	 * @return the new prefix expression or null if one of the given parameters
	 *         was null
	 */
	public static PrefixExpression makePrefixExpression(final AST t, final ASTRewrite r, final Expression operand,
			final PrefixExpression.Operator o) {
		if (hasNull(t,operand,o))
			return null;
		final PrefixExpression $ = t.newPrefixExpression();
		$.setOperator(o);
		$.setOperand(operand.getParent() == null ? operand : (Expression) r.createCopyTarget(operand));
		return $;
	}
	/**
	 * @param t
	 *          the AST who is to own the new return statement
	 * @param r
	 *          ASTRewrite for the given AST
	 * @param exp
	 *          the expression to put in parenthesis
	 * @return the given expression with parenthesis
	 */
	public static ParenthesizedExpression makeParenthesizedExpression(final AST t, final ASTRewrite r, final Expression exp) {
		if (hasNull(t,r,exp))
			return null;
		final ParenthesizedExpression $ = t.newParenthesizedExpression();
		$.setExpression(exp.getParent() == null ? exp : (Expression) r.createCopyTarget(exp));
		return $;
	}
	/**
	 * @param node
	 *          a node to extract an expression from
	 * @return null if the statement is not an expression or return statement or
	 *         the expression if they are
	 */
	public static Expression getExpression(final ASTNode node) {
		if (node == null)
			return null;
		switch (node.getNodeType()) {
		case ASTNode.EXPRESSION_STATEMENT:
			return ((ExpressionStatement) node).getExpression();
		case ASTNode.RETURN_STATEMENT:
			return ((ReturnStatement) node).getExpression();
		default:
			return null;
		}
	}
	/**
	 * @param n
	 *          a statement or a block to extract the expression statement from
	 * @return the expression statement if n is a block or an expression statement
	 *         or null if it not an expression statement or if the block contains
	 *         more than one statement
	 */
	public static ExpressionStatement getExpressionStatement(final Statement n) {
		if (n == null)
			return null;
		final ASTNode $ = n.getNodeType() != ASTNode.BLOCK ? n : getStmntFromBlock(n);
		return !($ != null && $.getNodeType() == ASTNode.EXPRESSION_STATEMENT) ? null : (ExpressionStatement) $;
	}
	/**
	 * @param n
	 *          a statement or block to extract the assignment from
	 * @return null if the block contains more than one statement or if the
	 *         statement is not an assignment or the assignment if it exists
	 */
	public static Assignment getAssignment(final Statement n) {
		final ExpressionStatement $ = getExpressionStatement(n);
		return $ == null || $.getExpression().getNodeType() != ASTNode.ASSIGNMENT ? null : (Assignment) $.getExpression();
	}
	/**
	 * @param n
	 *          the statement or block to extract the method invocation from
	 * @return the method invocation if it exists or null if it doesn't or if the
	 *         block contains more than one statement
	 */
	public static MethodInvocation getMethodInvocation(final Statement n) {
		final ExpressionStatement $ = getExpressionStatement(n);
		return $ == null || $.getExpression().getNodeType() != ASTNode.METHOD_INVOCATION ? null
				: (MethodInvocation) $.getExpression();
	}
	/**
	 * @param s
	 *          the statement or block to check if it is an assignment
	 * @return true if it is an assignment or false if it is not or if the block
	 *         Contains more than one statement
	 */
	public static boolean checkIsAssignment(final Statement s) {
		if (s == null || s.getNodeType() == ASTNode.BLOCK && getStmntFromBlock(s) == null)
			return false;
		final ExpressionStatement $ = getExpressionStatement(getStmntFromBlock(s));
		return $ != null && $.getExpression().getNodeType() == ASTNode.ASSIGNMENT;
	}
	/**
	 * @param exp
	 *          the expression to check if it is an assignment
	 * @return true if it is an assignment or false otherwise
	 */
	public static boolean checkIsAssignment(final Expression exp) {
		return exp != null && exp.getNodeType() == ASTNode.ASSIGNMENT;
	}
	/**
	 * @param b
	 *          the block to check
	 * @return true if a return statement exists in the block or false otherwise
	 */
	public static boolean checkReturnStmnt(final Block b) {
		if (b == null)
			return false;
		for (int i = 0; i < b.statements().size(); i++)
			if (((ASTNode) b.statements().get(i)).getNodeType() == ASTNode.RETURN_STATEMENT)
				return true;
		return false;
	}
	/**
	 * @param b
	 *          the block to get the statement from
	 * @return if b is a block with just 1 statement it returns that statement, if
	 *         b is statement it returns b and if b is null it returns a null
	 */
	public static Statement getStmntFromBlock(final Statement b) {
		return b != null && b.getNodeType() == ASTNode.BLOCK ? getStmntFromBlock((Block)b) : b;
	}
	private static Statement getStmntFromBlock(final Block b) {
		return b.statements().size() != 1 ? null : (Statement) b.statements().get(0);
	}
	/**
	 * @param s
	 *          the statement or block to check
	 * @return true if s contains a return statement or false otherwise
	 */
	public static boolean checkIfReturnStmntExist(final Statement s) {
		if (s==null)
			return false;
		switch(s.getNodeType()){
		case ASTNode.RETURN_STATEMENT: return true;
		case ASTNode.BLOCK: {
			for (final Object node : ((Block)s).statements())
				if (((ASTNode)node).getNodeType() == ASTNode.RETURN_STATEMENT)
					return true;
			break;
		}
		default: break;
		}
		return false;
	}
	/**
	 * @param node
	 *          the node to get the number of statements in
	 * @return 0 is s is null, 1 if s is a statement or the number of statement in
	 *         the block is s is a block
	 */
	public static int getNumOfStmnts(final ASTNode node) {
		if (node == null)
			return 0;
		switch(node.getNodeType()){
		case ASTNode.BLOCK: return statements(node).size();
		default: return 1;
		}
	}
	/**
	 * @param s
	 *          The node from which to return statement.
	 * @return null if it is not possible to extract the return statement.
	 */
	public static ReturnStatement getReturnStatement(final ASTNode s) {
		if (s == null)
			return null;
		switch(s.getNodeType()){
		case ASTNode.BLOCK: return getReturnStatement((Block) s);
		case ASTNode.RETURN_STATEMENT: return (ReturnStatement) s;
		default: return null;
		}
	}
	private static ReturnStatement getReturnStatement(final Block b) {
		return b.statements().size() != 1 ? null : getReturnStatement((Statement) b.statements().get(0));
	}
	/**
	 * @param s
	 *          the variable/s declaration from which to extract the proper
	 *          fragment
	 * @param name
	 *          the name by which to look for the fragment
	 * @return the fragment if such with the given name exists or null otherwise
	 *         (or if s or name are null)
	 */
	public static VariableDeclarationFragment getVarDeclFrag(final Statement s, final Expression name) {
		return hasNull(s,name)
				|| s.getNodeType() != ASTNode.VARIABLE_DECLARATION_STATEMENT
				|| name.getNodeType() != ASTNode.SIMPLE_NAME ?
						null : getVarDeclFrag(((VariableDeclarationStatement) s).fragments(), (SimpleName) name);
	}
	private static VariableDeclarationFragment getVarDeclFrag(final List<VariableDeclarationFragment> frags, final SimpleName name) {
		for (final VariableDeclarationFragment o : frags)
			if (name.toString().equals(o.getName().toString()))
				return o;
		return null;
	}
	/**
	 * String wise comparison of all the given SimpleNames
	 * 
	 * @param cmpTo
	 *          a string to compare all names to
	 * @param names
	 *          SimplesNames to compare by their string value to cmpTo
	 * @return true if all names are the same (string wise) or false otherwise
	 */
	public static boolean cmpSimpleNames(final Expression cmpTo, final Expression... names) {
		if (hasNull(cmpTo,names) || cmpTo.getNodeType() != ASTNode.SIMPLE_NAME)
			return false;
		for (final Expression name : names)
			if (name == null || name.getNodeType() != ASTNode.SIMPLE_NAME
			|| !((SimpleName) name).getIdentifier().equals(((SimpleName) cmpTo).getIdentifier()))
				return false;
		return true;
	}
	/**
	 * @param cmpTo
	 *          the assignment operator to compare all to
	 * @param op
	 *          A unknown number of assignments operators
	 * @return true if all the operator are the same or false otherwise
	 */
	public static boolean cmpAsgnOps(final Assignment.Operator cmpTo, final Assignment.Operator... op) {
		if (hasNull(cmpTo,op))
			return false;
		for (final Assignment.Operator o : op)
			if (o == null || cmpTo != o)
				return false;
		return true;
	}
	/**
	 * the function checks if all the given assignments has the same left hand
	 * side(variable) and operator
	 * 
	 * @param cmpTo
	 *          The assignment to compare all others to
	 * @param asgns
	 *          The assignments to compare
	 * @return true if all assignments has the same left hand side and operator as
	 *         the first one or false otherwise
	 */
	public static boolean cmpAsgns(final Assignment cmpTo, final Assignment... asgns) {
		if (hasNull(cmpTo,asgns))
			return false;
		for (final Assignment asgn : asgns)
			if (asgn == null || !cmpAsgnOps(cmpTo.getOperator(), asgn.getOperator())
			|| !cmpSimpleNames(cmpTo.getLeftHandSide(), asgn.getLeftHandSide()))
				return false;
		return true;
	}
	/**
	 * the function receives a condition and the then boolean value and returns
	 * the proper condition (its negation if thenValue is false)
	 * 
	 * @param t
	 *          the AST who is to own the new return statement
	 * @param r
	 *          ASTRewrite for the given AST
	 * @param cond
	 *          the condition to try to negate
	 * @param thenValue
	 *          the then value
	 * @return the original condition if thenValue was true or its negation if it
	 *         was false (or null if any of the given parameter were null)
	 */
	public static Expression tryToNegateCond(final AST t, final ASTRewrite r, final Expression cond, final boolean thenValue) {
		if (hasNull(t,cond))
			return null;
		return thenValue ? cond : makePrefixExpression(t, r, makeParenthesizedExpression(t, r, cond), PrefixExpression.Operator.NOT);
	}

	/**
	 * Counts the number of nodes in the tree of which node is root.
	 *
	 * @param n
	 *          The node.
	 * @return Number of abstract syntax tree nodes under the parameter.
	 */
	public static int countNodes(final ASTNode n) {
		final AtomicInteger $ = new AtomicInteger(0);
		n.accept(new ASTVisitor() {
			/**
			 * @see org.eclipse.jdt.core.dom.ASTVisitor#preVisit(org.eclipse.jdt.core.dom.ASTNode)
			 * @param _
			 *          ignored
			 */
			@Override public void preVisit(@SuppressWarnings("unused") final ASTNode _) {
				$.incrementAndGet();
			}
		});
		return $.get();
	}

	/**
	 * @param root
	 * 				the node whose children we return
	 * @return  A list containing all the nodes in the given root's sub tree
	 */
	public static List<ASTNode> getChildren(final ASTNode root) {
		if (root == null)
			return null;
		final List<ASTNode> $ = new ArrayList<ASTNode>();
		root.accept(new ASTVisitor() {
			@Override public void preVisit(final ASTNode node){
				$.add(node);
			}
		});
		$.remove(0);
		return $;
	}

	/**
	 * @param exps
	 * 				expressions to check
	 * @return true if one of the expressions is a conditional or parenthesized conditional expression
	 * 				or false otherwise
	 */
	public static boolean isOneExpCondExp(final Expression ...exps) {
		for (final Expression e : exps){
			if (e == null)
				continue;
			switch (e.getNodeType()) {
			case ASTNode.CONDITIONAL_EXPRESSION:
				return true;
			case ASTNode.PARENTHESIZED_EXPRESSION: {
				final Expression parenthsizedExp = ((ParenthesizedExpression) e).getExpression();
				if (parenthsizedExp.getNodeType() == ASTNode.CONDITIONAL_EXPRESSION)
					return true;
				break;
			}
			default: break;
			}
		}
		return false;
	}

	/**
	 * @param n
	 * 			the potential block who's statements list we return
	 * @return  the list of statements in n if it is a block or null otherwise
	 */
	public static List<ASTNode> statements(final ASTNode n) {
		return n.getNodeType() != ASTNode.BLOCK ? null : statements((Block) n);
	}
	private static List<ASTNode> statements(final Block b) {
		return b.statements();
	}

	/**
	 * Get the containing node by type.
	 * Say we want to find the first block that wraps our node:
	 * getContainerByNodeType(node, ASTNode.BLOCK);
	 * @param n
	 * 		Node to find its container
	 * @param ASTNodeType
	 * 		The type of the containing node we want to find
	 * @return
	 * 		The containing node
	 */
	public static ASTNode getContainerByNodeType(final ASTNode n, final int ASTNodeType) {
		ASTNode parentBlock = n.getParent();
		while (parentBlock.getNodeType() != ASTNodeType){
			if (parentBlock.getParent() == parentBlock.getRoot()) break;
			parentBlock = parentBlock.getParent();
		}
		return parentBlock;
	}
}
