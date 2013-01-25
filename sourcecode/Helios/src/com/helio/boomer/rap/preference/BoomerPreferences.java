package com.helio.boomer.rap.preference;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.helio.boomer.rap.Activator;

public class BoomerPreferences extends FieldEditorPreferencePage implements
IWorkbenchPreferencePage {

	public static final String ID = "com.helio.boomer.rap.preference";

	public BoomerPreferences() {
		super(GRID);
//		setPreferenceStore(PlatformUI.getPreferenceStore());
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Boomer Preferences");
	}

	public void init(IWorkbench workbench) {
		System.out.println("Initializing preferences.");
	}

	protected void createFieldEditors() {
		IntegerFieldEditor costThresholdFieldEditor = new IntegerFieldEditor(
				PreferenceConstants.P_COSTTHRESHOLD,
				"&Cost Threshold",
				getFieldEditorParent());
		costThresholdFieldEditor.setValidRange(0, 10);
		costThresholdFieldEditor.setEnabled(false, getFieldEditorParent());
		StringFieldEditor costAllocationFieldEditor = new StringFieldEditor(
				PreferenceConstants.P_COSTALLOCATION,
				"Cost &Allocation",
				getFieldEditorParent());
		addField(costAllocationFieldEditor);
		costAllocationFieldEditor.setStringValue("$0.15");
		costAllocationFieldEditor.setEnabled(false, getFieldEditorParent());
		ColorFieldEditor stockTickerColorFieldEditor = new ColorFieldEditor(
				PreferenceConstants.P_STOCKTICKERCOLOR,
				"&Stock Ticker Color",
				getFieldEditorParent());
		addField(stockTickerColorFieldEditor);
		stockTickerColorFieldEditor.setEnabled(false, getFieldEditorParent());
		BooleanFieldEditor showLegendBooleanFieldEditor = new BooleanFieldEditor(
				PreferenceConstants.P_SHOWLEGEND,
				"Show &Legends",
				getFieldEditorParent());
		addField(showLegendBooleanFieldEditor);
		showLegendBooleanFieldEditor.setEnabled(false, getFieldEditorParent());
		BooleanFieldEditor showXAxisLabelBooleanFieldEditor = new BooleanFieldEditor(
				PreferenceConstants.P_SHOWXAXISLABEL,
				"Show &X Axis Labels",
				getFieldEditorParent());
		addField(showXAxisLabelBooleanFieldEditor);
		showXAxisLabelBooleanFieldEditor.setEnabled(false, getFieldEditorParent());
		BooleanFieldEditor showYAxisLabelBooleanFieldEditor = new BooleanFieldEditor(
				PreferenceConstants.P_SHOWYAXISLABEL,
				"Show &Y Axis Labels",
				getFieldEditorParent());
		addField(showYAxisLabelBooleanFieldEditor);
		showYAxisLabelBooleanFieldEditor.setEnabled(false, getFieldEditorParent());
		BooleanFieldEditor showChartNotesBooleanFieldEditor = new BooleanFieldEditor(
				PreferenceConstants.P_SHOWCHARTNOTES,
				"Show Chart &Notes",
				getFieldEditorParent());
		addField(showChartNotesBooleanFieldEditor);
		showChartNotesBooleanFieldEditor.setEnabled(false, getFieldEditorParent());
	}

}
