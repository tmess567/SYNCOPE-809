package org.apache.syncope.ide.eclipse.plugin.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class AddTemplateDialog extends TitleAreaDialog {
	private Text key;

	private String keyString;

	public AddTemplateDialog(Shell parentShell) {
		super(parentShell);
		keyString = "";
	}

	@Override
	public void create() {
		super.create();
		setTitle("Add Template");
	}

	// Add Reset button
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Add", true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);

		createKeyField(container);

		return area;
	}

	private void createKeyField(Composite container) {
		Label keyLabel = new Label(container, SWT.NONE);
		keyLabel.setText("Key");

		GridData keyGD = new GridData();
		keyGD.grabExcessHorizontalSpace = true;
		keyGD.horizontalAlignment = GridData.FILL;

		key = new Text(container, SWT.BORDER);
		key.setLayoutData(keyGD);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	// save content of the Text fields because they get disposed
	// as soon as the Dialog closes
	private void saveInput() {
		keyString = key.getText();
	}

	@Override
	protected void okPressed() {
		saveInput();
		setReturnCode(200);
		super.okPressed();
	}

	public String getKey() {
		return keyString;
	}

}
