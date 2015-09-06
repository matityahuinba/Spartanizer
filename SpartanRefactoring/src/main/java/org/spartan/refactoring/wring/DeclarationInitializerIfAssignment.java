package org.spartan.refactoring.wring;

import static org.spartan.refactoring.utils.Funcs.*;
import static org.spartan.refactoring.wring.Wrings.size;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;
import org.spartan.refactoring.utils.Extract;
import org.spartan.refactoring.utils.Subject;
import org.spartan.refactoring.wring.LocalInliner.LocalInlineWithValue;

/**
 * A {@link Wring} to convert <code>int a = 2;
 * if (b)
 *   a = 3;</code> into <code>int a = b ? 3 : 2;</code>
 *
 * @author Yossi Gil
 * @since 2015-08-07
 */
public final class DeclarationInitializerIfAssignment extends Wring.VariableDeclarationFragementAndStatement {
  @Override ASTRewrite go(final ASTRewrite r, final VariableDeclarationFragment f, final SimpleName n, final Expression initializer, final Statement nextStatement,
      final TextEditGroup g) {
    if (initializer == null)
      return null;
    final IfStatement s = asIfStatement(nextStatement);
    if (s == null || !Wrings.emptyElse(s))
      return null;
    s.setElseStatement(null);
    final Expression condition = s.getExpression();
    if (condition == null)
      return null;
    final Assignment a = Extract.assignment(then(s));
    if (a == null || !same(left(a), n) || a.getOperator() != Assignment.Operator.ASSIGN || doesUseForbiddenSiblings(f, condition, right(a)))
      return null;
    final LocalInlineWithValue i = new LocalInliner(n, r, g).byValue(initializer);
    if (!i.canInlineInto(condition, right(a)))
      return null;
    final ConditionalExpression newInitializer = Subject.pair(right(a), initializer).toCondition(condition);
    final int spending = i.replacedSize(newInitializer);
    final int savings = size(nextStatement, initializer);
    if (spending > savings)
      return null;
    r.replace(initializer, newInitializer, g);
    i.inlineInto(then(newInitializer), newInitializer.getExpression());
    r.remove(nextStatement, g);
    return r;
  }
  @Override public String description(final VariableDeclarationFragment f) {
    return "Consolidate initialization of " + f.getName() + " with the subsequent conditional assignment to it";
  }
}