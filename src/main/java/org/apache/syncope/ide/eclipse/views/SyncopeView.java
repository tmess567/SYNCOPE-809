package org.apache.syncope.ide.eclipse.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;

import dialogs.LoginDialog;
import org.eclipse.jface.viewers.*;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.apache.syncope.client.lib.SyncopeClient;
import org.apache.syncope.client.lib.SyncopeClientFactoryBean;
import org.apache.syncope.common.lib.to.MailTemplateTO;
import org.apache.syncope.common.lib.to.ReportTemplateTO;
import org.apache.syncope.common.rest.api.service.MailTemplateService;
import org.apache.syncope.common.rest.api.service.ReportTemplateService;
import org.eclipse.core.runtime.IAdaptable;

public class SyncopeView extends ViewPart {
	
	public static final String ID = "org.apache.syncope.ide.eclipse.views.SyncopeView";

	private TreeViewer viewer;
	private DrillDownAdapter drillDownAdapter;
	public ViewContentProvider vcp;
	private Action loginAction;
	private Action doubleClickAction;

	class TreeObject implements IAdaptable {
		private String name;
		private TreeParent parent;

		public TreeObject(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setParent(TreeParent parent) {
			this.parent = parent;
		}

		public TreeParent getParent() {
			return parent;
		}

		public String toString() {
			return getName();
		}

		public Object getAdapter(Class key) {
			return null;
		}
	}

	class TreeParent extends TreeObject {
		private ArrayList children;

		public TreeParent(String name) {
			super(name);
			children = new ArrayList();
		}

		public void addChild(TreeObject child) {
			children.add(child);
			child.setParent(this);
		}

		public void removeChild(TreeObject child) {
			children.remove(child);
			child.setParent(null);
		}

		public TreeObject[] getChildren() {
			return (TreeObject[]) children.toArray(new TreeObject[children.size()]);
		}

		public boolean hasChildren() {
			return children.size() > 0;
		}
	}

	class ViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {
		private TreeParent invisibleRoot;
		private String deploymentUrl;
		private String username;
		private String password;

		public ViewContentProvider() {
			deploymentUrl = "";
			username = "";
			password = "";
		}

		public ViewContentProvider(String deploymentUrl, String username, String password) {
			this.deploymentUrl = deploymentUrl;
			this.username = username;
			this.password = password;
		}

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object parent) {
			if (parent.equals(getViewSite())) {
				if (invisibleRoot == null)
					initialize();
				return getChildren(invisibleRoot);
			}
			return getChildren(parent);
		}

		public Object getParent(Object child) {
			if (child instanceof TreeObject) {
				return ((TreeObject) child).getParent();
			}
			return null;
		}

		public Object[] getChildren(Object parent) {
			if (parent instanceof TreeParent) {
				return ((TreeParent) parent).getChildren();
			}
			return new Object[0];
		}

		public boolean hasChildren(Object parent) {
			if (parent instanceof TreeParent)
				return ((TreeParent) parent).hasChildren();
			return false;
		}

		/*
		 * We will set up a dummy model to initialize tree heararchy. In a real
		 * code, you will connect to a real model and expose its hierarchy.
		 */
		public void initialize() {
			invisibleRoot = new TreeParent("");

			if (this.deploymentUrl != null && !(this.deploymentUrl.equals("")) && this.username != null
					&& !(this.username.equals("")) && this.password != null && !(this.password.equals(""))) {
				TreeParent p1 = new TreeParent("Mail Templates");
				TreeParent p2 = new TreeParent("Report XSLTs");

				SyncopeClient syncopeClient = new SyncopeClientFactoryBean().setAddress(this.deploymentUrl)
						.create(this.username, this.password);
				try {
					// Adding mailTemplates to View
					MailTemplateService mailTemplateService = syncopeClient.getService(MailTemplateService.class);
					List<MailTemplateTO> mailTemplateTOs = mailTemplateService.list();

					for (int i = 0; i < mailTemplateTOs.size(); i++) {
						TreeObject obj = new TreeObject(mailTemplateTOs.get(i).getKey());
						p1.addChild(obj);
					}
					invisibleRoot.addChild(p1);
					// Adding reportTemplates to View
					ReportTemplateService reportTemplateService = syncopeClient.getService(ReportTemplateService.class);
					List<ReportTemplateTO> reportTemplateTOs = reportTemplateService.list();

					for (int i = 0; i < reportTemplateTOs.size(); i++) {
						TreeObject obj = new TreeObject(reportTemplateTOs.get(i).getKey());
						p2.addChild(obj);
					}
					invisibleRoot.addChild(p2);
				} catch (Exception e) {
					Shell shell = viewer.getControl().getShell();
					if(e instanceof java.security.AccessControlException){
						MessageDialog.openError(shell, 
								"Incorrect Credentials", 
								"Unable to authenticate "+this.username);
					}
					else if(e instanceof javax.ws.rs.ProcessingException){
						MessageDialog.openError(shell, 
								"Incorrect Url", 
								"Unable to find syncope at "+this.deploymentUrl);
					}
					else if(e instanceof javax.xml.ws.WebServiceException){
						MessageDialog.openError(shell, 
								"Invalid Url", 
								"Not a valid url "+this.username);
					}
					else
						e.printStackTrace();
				}
			}
		}
	}

	class ViewLabelProvider extends LabelProvider {

		public String getText(Object obj) {
			return obj.toString();
		}

		public Image getImage(Object obj) {
			String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
			if (obj instanceof TreeParent)
				imageKey = ISharedImages.IMG_OBJ_FOLDER;
			return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
		}
	}

	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public SyncopeView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		drillDownAdapter = new DrillDownAdapter(viewer);
		vcp = new ViewContentProvider();
		viewer.setContentProvider(vcp);
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "EclipsePlugin.viewer");
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				SyncopeView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(loginAction);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(loginAction);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(loginAction);
		manager.add(new Separator());
	}

	private void makeActions() {
		loginAction = new Action() {
			public void run() {
				Shell shell = viewer.getControl().getShell();
				LoginDialog dialog = new LoginDialog(shell);
				dialog.create();
				if (dialog.open() == Window.OK) {
					String deploymentUrl = dialog.getDeploymentUrl();
					String username = dialog.getUsername();
					String password = dialog.getPassword();
					
					vcp.deploymentUrl = deploymentUrl;
					vcp.username = username;
					vcp.password = password;
					
					vcp.initialize();
					SyncopeView.this.viewer.refresh();
				}
			}
		};
		loginAction.setText("Login");
		loginAction.setToolTipText("Set Syncope deployment url and login");
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				showMessage("Double-click detected on " + obj.toString());
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(), "Syncope View", message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}