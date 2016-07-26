package il.org.spartan.refactoring.wring;

import il.org.spartan.refactoring.spartanizations.*;
import il.org.spartan.refactoring.utils.*;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;

/** An adapter that converts the @{link Wring} protocol into that of
 * {@link Spartanization}
 * @author Yossi Gil
 * @since 2015/07/25 */
public class AsSpartanization extends Spartanization {
  /** Instantiates this class
   * @param inner The wring we wish to convert
   * @param name The title of the refactoring */
  @SuppressWarnings("unchecked") public AsSpartanization(final Wring<? extends ASTNode> inner, final String name) {
    super(name);
    this.inner = (Wring<ASTNode>) inner;
  }
  @Override protected ASTVisitor collect(final List<Rewrite> $, final CompilationUnit u) {
    // Source.set(u);
    return new ASTVisitor() {
      @Override public boolean visit(final Block it) {
        return process(it);
      }
      @Override public boolean visit(final ConditionalExpression e) {
        return process(e);
      }
      @Override public boolean visit(final IfStatement it) {
        return process(it);
      }
      @Override public boolean visit(final InfixExpression it) {
        return process(it);
      }
      @Override public boolean visit(final PrefixExpression it) {
        return process(it);
      }
      @Override public boolean visit(final VariableDeclarationFragment it) {
        return process(it);
      }
      <N extends ASTNode> boolean process(final N n) {
        if (!inner.initialize(u).createScalpel(null, null).scopeIncludes(n) || inner.nonEligible(n))
          return true;
        $.add(inner.make(n));
        return true;
      }
    };
  }
  @Override protected final void fillRewrite(final ASTRewrite r, final CompilationUnit u, final IMarker m) {
    // Source.set(u);
    u.accept(new ASTVisitor() {
      @Override public boolean visit(final Block e) {
        return go(e);
      }
      @Override public boolean visit(final ConditionalExpression e) {
        return go(e);
      }
      @Override public boolean visit(final IfStatement s) {
        return go(s);
      }
      @Override public boolean visit(final InfixExpression e) {
        return go(e);
      }
      @Override public boolean visit(final PrefixExpression e) {
        return go(e);
      }
      @Override public boolean visit(final VariableDeclarationFragment f) {
        return go(f);
      }
      private <N extends ASTNode> boolean go(final N n) {
        if (inRange(m, n))
          inner.initialize(u).createScalpel(r, null).make(n).go(r, null);
        return true;
      }
    });
  }

  final Wring<ASTNode> inner;
}