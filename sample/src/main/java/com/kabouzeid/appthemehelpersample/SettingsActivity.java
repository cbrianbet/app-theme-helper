package com.kabouzeid.appthemehelpersample;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.kabouzeid.appthemehelper.ATH;
import com.kabouzeid.appthemehelper.ThemeStore;
import com.kabouzeid.appthemehelper.common.ATHActionBarActivity;
import com.kabouzeid.appthemehelper.common.prefs.ATEColorPreference;
import com.kabouzeid.appthemehelper.common.prefs.ATESwitchPreference;
import com.kabouzeid.appthemehelpersample.dialogs.AboutDialog;

/**
 * @author Aidan Follestad (afollestad)
 */
@SuppressLint("NewApi")
public class SettingsActivity extends ATHActionBarActivity
        implements ColorChooserDialog.ColorCallback {

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        final ThemeStore themeStore = ThemeStore.editTheme(this);
        switch (dialog.getTitle()) {
            case R.string.primary_color:
                themeStore.primaryColor(selectedColor);
                break;
            case R.string.accent_color:
                themeStore.accentColor(selectedColor);
                break;
            case R.string.primary_text_color:
                themeStore.textColorPrimary(selectedColor);
                break;
            case R.string.secondary_text_color:
                themeStore.textColorSecondary(selectedColor);
                break;
        }
        themeStore.commit();
        recreate();
    }

    @Override
    public void onColorChooserDismissed(@NonNull ColorChooserDialog dialog) {

    }

    public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            invalidateSettings();
        }

        public void invalidateSettings() {
            ATEColorPreference primaryColorPref = (ATEColorPreference) findPreference("primary_color");
            primaryColorPref.setColor(ThemeStore.primaryColor(getActivity()), Color.BLACK);
            primaryColorPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new ColorChooserDialog.Builder((SettingsActivity) getActivity(), R.string.primary_color)
                            .preselect(ThemeStore.primaryColor(getActivity()))
                            .show((SettingsActivity) getActivity());
                    return true;
                }
            });

            ATEColorPreference accentColorPref = (ATEColorPreference) findPreference("accent_color");
            accentColorPref.setColor(ThemeStore.accentColor(getActivity()), Color.BLACK);
            accentColorPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new ColorChooserDialog.Builder(getActivity(), R.string.accent_color)
                            .preselect(ThemeStore.accentColor(getActivity()))
                            .show((SettingsActivity) getActivity());
                    return true;
                }
            });

            ATEColorPreference textColorPrimaryPref = (ATEColorPreference) findPreference("text_primary");
            textColorPrimaryPref.setColor(ThemeStore.textColorPrimary(getActivity()), Color.BLACK);
            textColorPrimaryPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new ColorChooserDialog.Builder(getActivity(), R.string.primary_text_color)
                            .preselect(ThemeStore.textColorPrimary(getActivity()))
                            .show((SettingsActivity) getActivity());
                    return true;
                }
            });

            ATEColorPreference textColorSecondaryPref = (ATEColorPreference) findPreference("text_secondary");
            textColorSecondaryPref.setColor(ThemeStore.textColorSecondary(getActivity()), Color.BLACK);
            textColorSecondaryPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new ColorChooserDialog.Builder(getActivity(), R.string.secondary_text_color)
                            .preselect(ThemeStore.textColorSecondary(getActivity()))
                            .show((SettingsActivity) getActivity());
                    return true;
                }
            });

            findPreference("dark_theme").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    ThemeStore.editTheme(getActivity())
                            .activityTheme(((Boolean) newValue) ? R.style.AppThemeDark : R.style.AppTheme)
                            .commit();
                    getActivity().recreate();
                    return true;
                }
            });

            final ATESwitchPreference statusBarPref = (ATESwitchPreference) findPreference("colored_status_bar");
            final ATESwitchPreference navBarPref = (ATESwitchPreference) findPreference("colored_nav_bar");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                statusBarPref.setChecked(ThemeStore.coloredStatusBar(getActivity()));
                statusBarPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        ThemeStore.editTheme(getActivity())
                                .coloredStatusBar((Boolean) newValue)
                                .commit();
                        getActivity().recreate();
                        return true;
                    }
                });


                navBarPref.setChecked(ThemeStore.coloredNavigationBar(getActivity()));
                navBarPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        ThemeStore.editTheme(getActivity())
                                .coloredNavigationBar((Boolean) newValue)
                                .commit();
                        getActivity().recreate();
                        return true;
                    }
                });
            } else {
                statusBarPref.setEnabled(false);
                statusBarPref.setSummary(R.string.not_available_below_lollipop);
                navBarPref.setEnabled(false);
                navBarPref.setSummary(R.string.not_available_below_lollipop);
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_activity_custom);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ATH.setActivityToolbarColorAuto(this, getATHToolbar());
        ATH.setStatusbarColorAuto(this);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame, new SettingsFragment()).commit();
        } else {
            SettingsFragment frag = (SettingsFragment) getFragmentManager().findFragmentById(R.id.content_frame);
            if (frag != null) frag.invalidateSettings();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.about) {
            AboutDialog.show(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getThemeRes() {
        int defaultThemeRes = super.getThemeRes();
        switch (defaultThemeRes) {
            case R.style.AppTheme:
                return R.style.AppTheme_ActionBar;
            case R.style.AppThemeDark:
                return R.style.AppThemeDark_ActionBar;
        }
        return defaultThemeRes;
    }
}