package dialogs;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class LoginDialog extends TitleAreaDialog {
	private Text deploymentUrlInp;
	private Text usernameInp;
	private Text passwordInp;

	private String deploymentUrl;
	private String username;
	private String password;

	public LoginDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	public void create() {
		super.create();
		setTitle("Login to Syncope");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);

		createDeploymentUrl(container);
		createUsername(container);
		createPassword(container);
		
		return area;
	}

	private void createDeploymentUrl(Composite container) {
		Label lbtDeploymentUrl = new Label(container, SWT.NONE);
		lbtDeploymentUrl.setText("Deployment UrL");

		GridData dataDeploymentUrl = new GridData();
		dataDeploymentUrl.grabExcessHorizontalSpace = true;
		dataDeploymentUrl.horizontalAlignment = GridData.FILL;

		deploymentUrlInp = new Text(container, SWT.BORDER);
		deploymentUrlInp.setLayoutData(dataDeploymentUrl);
	}

	private void createUsername(Composite container) {
		Label lbtUsername = new Label(container, SWT.NONE);
		lbtUsername.setText("Username");

		GridData dataUsername = new GridData();
		dataUsername.grabExcessHorizontalSpace = true;
		dataUsername.horizontalAlignment = GridData.FILL;
		usernameInp = new Text(container, SWT.BORDER);
		usernameInp.setLayoutData(dataUsername);
	}
	
	private void createPassword(Composite container) {
		Label lbtPassword = new Label(container, SWT.NONE);
		lbtPassword.setText("Password");

		GridData dataPassword = new GridData();
		dataPassword.grabExcessHorizontalSpace = true;
		dataPassword.horizontalAlignment = GridData.FILL;
		passwordInp = new Text(container, SWT.BORDER| SWT.PASSWORD);
		passwordInp.setLayoutData(dataPassword);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	// save content of the Text fields because they get disposed
	// as soon as the Dialog closes
	private void saveInput() {
		deploymentUrl = deploymentUrlInp.getText();
		username = usernameInp.getText();
		password = passwordInp.getText();

	}

	@Override
	protected void okPressed() {
		saveInput();
		setReturnCode(200);
		super.okPressed();
	}

	public String getDeploymentUrl() {
		return deploymentUrl;
	}

	public String getPassword() {
		return password;
	}
	public String getUsername() {
		return username;
	}

}
