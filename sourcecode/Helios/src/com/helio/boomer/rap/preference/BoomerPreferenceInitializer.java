package com.helio.boomer.rap.preference;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.PlatformUI;

public class BoomerPreferenceInitializer extends AbstractPreferenceInitializer {

	public BoomerPreferenceInitializer() {
		System.out.println("Initializing Preferences.");
	}

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = PlatformUI.getPreferenceStore();
		store.setDefault(PreferenceConstants.P_COSTALLOCATION, "$0.15");
		store.setDefault(PreferenceConstants.P_SHOWLEGEND, false);
		store.setDefault(PreferenceConstants.P_SHOWXAXISLABEL, true);
		store.setDefault(PreferenceConstants.P_SHOWYAXISLABEL, true);
		store.setDefault(PreferenceConstants.P_SHOWCHARTNOTES, true);
	}
	
}
