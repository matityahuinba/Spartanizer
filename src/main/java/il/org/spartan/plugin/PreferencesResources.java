package il.org.spartan.plugin;

import org.eclipse.jface.preference.*;

import il.org.spartan.spartanizer.dispatch.*;

public final class PreferencesResources {
  /** An enum holding together all the "enabled spartanizations" options, also
   * allowing to get the set preference value for each of them */
  public enum WringGroup {
    Abbreviation(Category.Abbreviation.class), //
    Annonimaization(Category.Annonimization.class), //
    Canonicalization(Category.Collapse.class), //
    CommonFactoring(Category.CommnoFactoring.class), //
    Centification(Category.Centification.class), //
    Dollarization(Category.Dollarization.class), //
    EarlyReturn(Category.EarlyReturn.class), //
    Idiomatic(Category.Idiomatic.class), //
    Inlining(Category.Inlining.class), //
    InVain(Category.InVain.class), //
    ScopeReduction(Category.ScopeReduction.class), //
    Sorting(Category.Sorting.class), //
    SyntacticBaggage(Category.SyntacticBaggage.class), //
    Ternarization(Category.Ternarization.class), //
    Nanopatterns(Category.Nanos.class), //
    ;
    private static WringGroup find(final Class<? extends Category> ¢) {
      for (final WringGroup $ : WringGroup.values())
        if ($.clazz.isAssignableFrom(¢))
          return $;
      return null;
    }

    public static WringGroup find(final Category ¢) {
      return find(¢.getClass());
    }

    static IPreferenceStore store() {
      return Plugin.plugin().getPreferenceStore();
    }

    private final Class<? extends Category> clazz;
    final String id;
    final String label;

    private WringGroup(final Class<? extends Category> clazz) {
      this.clazz = clazz;
      id = clazz.getCanonicalName();
      label = getLabel(clazz) + "";
    }

    private Object getLabel(final Class<? extends Category> k) {
      try {
        return k.getField("label").get(null);
      } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
        LoggingManner.logEvaluationError(this, e);
        return null;
      }
    }

    public boolean isEnabled() {
      return Plugin.plugin() == null || "on".equals(store().getString(id));
    }
  }

  /** Page description **/
  public static final String PAGE_DESCRIPTION = "Preferences for the laconization plug-in";
  /** General preferences **/
  public static final String PLUGIN_STARTUP_BEHAVIOR_ID = "pref_startup_behavior";
  public static final String PLUGIN_STARTUP_BEHAVIOR_TEXT = "Plugin startup behavior:";
  public static final String[][] PLUGIN_STARTUP_BEHAVIOR_OPTIONS = {
      { "Remember individual project settings", //
          "remember" },
      { "Enable for all projects", //
          "always_on" }, //
      { "Disable for all projects", //
          "always_off" } };
  public static final String NEW_PROJECTS_ENABLE_BY_DEFAULT_ID = "Preference_enable_by_default_for_new_projects";
  public static final String NEW_PROJECTS_ENABLE_BY_DEFAULT_TEXT = "Enable by default for newly created projects";
}