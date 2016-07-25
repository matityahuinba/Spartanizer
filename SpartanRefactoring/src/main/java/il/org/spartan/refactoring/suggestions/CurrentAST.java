package il.org.spartan.refactoring.suggestions;

import static org.eclipse.jdt.core.dom.ASTParser.*;
import il.org.spartan.*;
import il.org.spartan.idiomatic.Producer;

import static il.org.spartan.idiomatic.run;
import static il.org.spartan.idiomatic.take;
import static il.org.spartan.idiomatic.katching;

import il.org.spartan.lazy.*;
import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.utils.Funcs.*;
import il.org.spartan.refactoring.wring.*;
import il.org.spartan.utils.*;
import il.org.spartan.lazy.Cookbook.Ingredient;
import il.org.spartan.lazy.Cookbook.Cell;

import static il.org.spartan.lazy.Cookbook.*;

import static il.org.spartan.idiomatic.*;

import static org.eclipse.jdt.core.JavaCore.createCompilationUnitFrom;

import java.util.*;
import java.util.function.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.annotation.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.ui.*;

import static il.org.spartan.refactoring.suggestions.DialogBoxes.*;
import static org.eclipse.core.runtime.IProgressMonitor.*;
import static org.eclipse.jdt.core.JavaCore.*;

/** @author Yossi Gil
 * @since 2016` */
@SuppressWarnings("javadoc")//
public class CurrentAST extends CurrentCompilationUnit.Context implements Selfie<CurrentAST> {
  /** factory method for this class,
   * @return a new empty instance */
  public static CurrentAST inContext() {
    return new CurrentAST();
  }
  public  List<ASTNode>     allNodes()     {  return  allNodes.get();         }
  public  char[]            array()        {  return  array.get();            }
  /** Returns an exact copy of this instance
   * @return Created clone object */
  @SuppressWarnings("unchecked") @Override public CurrentAST clone() {
    try {
      return (CurrentAST) super.clone();
    } catch (final CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }
  public  Document          document()     {  return  document.get();         }
  /** Compute a value within this context
   * @param ¢ JD
   * @return the computed value */
  @SuppressWarnings("static-method") public <T> T eval(final Provider<T> ¢) {
    return ¢.get();
  }
  public  int               kind()         {  return  kind.get().intValue();  }
  public  IMarker           marker()       {  return  marker.get();           }
  public  int               nodeCount()    {  return  allNodes().size();      }
  public  Range             range()        {  return  range.get();            }
  // Getters of all cells, which provide access to their cached or recomputed 
  // content; @formatter:off
  //
  // Sort alphabetically and placed columns; VIM: +,/^\s*\/\//-!sort -u | column -t | sed "s/^/  /"
  public  ASTNode           root()         {  return  root.get();             }
  public  ITextSelection    selection()    {  return  selection.get();        }
  public  List<Suggestion>  suggestions()  {  return  suggestions.get();      }
  public  String            text()         {  return  text.get();             }
  // Auxiliary function 
  private Range computeRange() {
    return idiomatic.<Range>katching(() -> new Range(intValue(IMarker.CHAR_START), intValue(IMarker.CHAR_END)));
  }
  protected <N extends ASTNode> ProgressVisitor computeSuggestions(final Wring<N> w) {
    return new TransformAndPrune<Suggestion>() {
      @SuppressWarnings("unchecked") @Override protected Suggestion transform(final ASTNode n) {
        if (w.scopeIncludes((N) n) || w.nonEligible((N) n))
          return null;
        return w.make((N) n);
      }
    };
  }
  /** @param n the node which needs to be within the range of
   *          <code><b>m</b></code>
   * @return True if the node is within range */
  final boolean applicable(final ASTNode n) {
    return marker() != null ? !isMarked(n) : !hasSelection() || !notSelected(n);
  }
  final boolean containedIn(final ASTNode n) {
    return range().includedIn(Funcs.range(n));
  }
  @SuppressWarnings("static-method") void exec(final Runnable r) {
    r.run();
  }
  final boolean hasSelection() {
    return selection() != null && !selection().isEmpty() && selection().getLength() != 0;
  }
  int intValue(final String propertyName) throws CoreException {
    return ((Integer) marker().getAttribute(propertyName)).intValue();
  }
  /** determine whether a given node is included in the marker
   * @param n JD
   * @return boolean whether a parameter is included in the marker */
  boolean isMarked(final ASTNode n) {
    try {
      return n.getStartPosition() < intValue(IMarker.CHAR_START) || n.getLength() + n.getStartPosition() > intValue(IMarker.CHAR_END);
    } catch (final CoreException e) {
      e.printStackTrace();
      return true;
    }
  }
boolean isSelected(final int offset) {
  return hasSelection() && offset >= selection().getOffset() && offset < selection().getLength() + selection().getOffset();
}
  /** Determines if the node is outside of the selected text.
   * @return true if the node is not inside selection. If there is no selection
   *         at all will return false. */
  boolean notSelected(final ASTNode n) {
    return !isSelected(n.getStartPosition());
  }
  final boolean outOfRange(final ASTNode n) {
    return marker() != null ? !containedIn(n) : !hasSelection() || !notSelected(n);
  }
  final Cell<List<@NonNull ASTNode>> allNodes = cook(() -> {
    final List<@NonNull ASTNode> $ = new ArrayList<>();
    root().accept(new ProgressVisitor() {
      @Override public void go(final ASTNode n) {
        $.add(n);
      }
    });
    return $;
  });
  final  Cell<char[]>   array  =  cook(()->  text().toCharArray());
  final  Cell<Document>  document  =  cook(()->  new  Document(text()));
  //
  // The cells themselves;  @formatter:off
  // Sort alphabetically and placed columns; VIM: +,/^\s*\/\//-!sort -u | column -t | sed "s/^/  /"
  final Cell<Integer> kind = value(ASTParser.K_COMPILATION_UNIT);
  // Inputs:
  // Sort alphabetically and placed columns; VIM: +,/^\s*\/\//-!sort -u | column -t | sed "s/^/  /"
  final  Cell<IMarker>         marker     =  input();
  // More complex recipes:
  final Cell<ASTParser> parser = from(array, kind).make(() -> {
    final ASTParser $ = ASTParser.newParser(AST.JLS8);
    $.setKind(kind());
    $.setResolveBindings(PluginPreferencesResources.getResolveBindingEnabled());
    $.setSource(array());
    return $;
  });
  final Cell<Range> range = cook(() -> computeRange());
  // Simple recipes:
  // Sort alphabetically and placed columns; VIM: +,/^\s*\/\//-!sort -u | column -t | sed "s/^/  /"
  final  Cell<ASTNode>  root   =  cook(()->  Make.COMPILIATION_UNIT.parser(context.compilationUnit()).createAST(progressMonitor()));
  final  Cell<ITextSelection>  selection  =  input();
  final Cell<List<Suggestion>> suggestions = from(root, allNodes).make(() -> {
    begin("Searching for suggestions...", nodeCount());
    final List<Suggestion> $ = new ArrayList<>();
    root().accept(new TransformAndPrune<Suggestion>($) {
      /** Simply return null by default */
      @Override protected Suggestion transform(@SuppressWarnings("unused") final ASTNode __) {
        return null;
      }
    });
    end();
    return $;
  });
  final  Cell<String>   text   =  cook(()->  document().get());
  // Lazy values
  // Sort alphabetically and placed columns; VIM: +,/^\s*\/\//-!sort -u | column -t | sed "s/^/  /"
  final  Cell<?>         toolbox   =  from().make(()->  new  Toolbox());
  /** Inner class, inheriting all of its container's {@link Cell}s, and possibly
  *adding some of its own. Access to container's c {@link Cells} is through the
     * {@link #context} variable.
     * <p>Clients extend this class to create more specialized contexts, adding more 
     * {@link Cell}s and {@link Cookbook#recipe(Supplier)}'s.
     * @author Yossi Gil
     * @since 2016` */
    public abstract class Context {
      /** the containing instance */
      @SuppressWarnings("hiding") protected final CurrentAST context = CurrentAST.this;
    }

  public abstract class ProgressVisitor extends ASTVisitor {
    public boolean filter(final ASTNode n) {
      return n != null;
    }
    @Override public final void preVisit(final ASTNode n) {
      work();
      go(n);
    }
    @Override public final boolean preVisit2(final ASTNode n) {
      return filter(n);
    }
    protected abstract void go(final ASTNode n);
  }


  abstract class TransformAndPrune<T> extends ProgressVisitor {
    TransformAndPrune() {
      this(new ArrayList<>());
    }

    TransformAndPrune(final List<T> pruned) {
      this.pruned = pruned;
    }
    private void go(final T ¢) {
      run(() -> {
        pruned.add(¢);
      }).unless(not(worthy(¢)));
    }
    private boolean not(final boolean b) {
      return !b;
    }
    @Override protected final void go(final ASTNode ¢) {
      go(transform(¢));
    }
    /** to be implemented by client: a function to convert nodes to a given type.
     * @param n
     * @return T TODO Javadoc(2016) automatically generated for returned value
     *         of method <code>transform</code> */
    protected abstract T transform(ASTNode n);
    /** determine whether a product of {@link #transform(ASTNode)} is worthy of
     * collecting
     * @param ¢ JD
     * @return true iff the parameter is worthy; by default all products which
     *         are not null are worthy; clients may override. */
    protected boolean worthy(final T ¢) {
      return ¢ != null;
    }
    /** this is where we collect what's {@link #worthy(Object)} */
    protected final List<T> pruned;
  }
}
