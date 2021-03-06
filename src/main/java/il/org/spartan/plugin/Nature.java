package il.org.spartan.plugin;

import static il.org.spartan.Utils.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;

/** @author Artium Nihamkin
 * @since 2013/07/01 */
public final class Nature implements IProjectNature {
  /** ID of this project nature */
  public static final String NATURE_ID = "il.org.spartan.nature";
  /** The project to which we relate */
  private IProject project;

  @Override public void configure() throws CoreException {
    final IProjectDescription d = project.getDescription();
    final ICommand[] cs = d.getBuildSpec();
    for (final ICommand ¢ : cs)
      if (¢.getBuilderName().equals(Builder.BUILDER_ID))
        return;
    set(d, cs);
  }

  @Override public void deconfigure() throws CoreException {
    final IProjectDescription description = getProject().getDescription();
    final ICommand[] cs = description.getBuildSpec();
    for (int ¢ = 0; ¢ < cs.length; ++¢)
      if (cs[¢].getBuilderName().equals(Builder.BUILDER_ID)) {
        description.setBuildSpec(delete(cs, ¢));
        project.setDescription(description, null);
        return;
      }
  }

  @Override public IProject getProject() {
    return project;
  }

  @Override public void setProject(final IProject ¢) {
    project = ¢;
  }

  private void set(final IProjectDescription d, final ICommand[] cs) throws CoreException {
    final ICommand c = d.newCommand();
    c.setBuilderName(Builder.BUILDER_ID);
    d.setBuildSpec(append(cs, c));
    project.setDescription(d, null);
  }
}
