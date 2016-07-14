package org.apache.syncope.ide.eclipse.plugin.views;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.*;
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
import org.apache.syncope.common.lib.SyncopeClientException;
import org.apache.syncope.common.lib.to.MailTemplateTO;
import org.apache.syncope.common.lib.to.ReportTemplateTO;
import org.apache.syncope.common.lib.types.ClientExceptionType;
import org.apache.syncope.common.lib.types.MailTemplateFormat;
import org.apache.syncope.common.lib.types.ReportTemplateFormat;
import org.apache.syncope.common.rest.api.service.MailTemplateService;
import org.apache.syncope.common.rest.api.service.ReportTemplateService;
import org.apache.syncope.ide.eclipse.plugin.dialogs.AddTemplateDialog;
import org.apache.syncope.ide.eclipse.plugin.dialogs.LoginDialog;
import org.apache.syncope.ide.eclipse.plugin.editors.TemplateEditor;
import org.apache.syncope.ide.eclipse.plugin.editors.TemplateEditorInput;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public class SyncopeView extends ViewPart {

	public static final String ID = "org.apache.syncope.ide.eclipse.plugin.views.SyncopeView";

	private TreeViewer viewer;
	public ViewContentProvider vcp;
	private static SyncopeClient syncopeClient;
	private Action loginAction;
	private Action refreshAction;
	private Action doubleClickAction;
	private Action addAction;
	private Action readAction;
	private Action removeAction;
	
	private static final String MAIL_TEMPLATE_LABEL = "Mail Templates";
	private static final String REPORT_TEMPLATE_LABEL = "Report Templates";
	private static final String LOGIN_ACTION_TEXT = "Login";
	private static final String LOGIN_ACTION_TOOLTIP_TEXT = "Set Apache Syncope deployment url and login";
	private static final String REFRESH_ACTION_TEXT = "Refresh";
	private static final String REFRESH_ACTION_TOOLTIP_TEXT = "Refresh the template listings";
	private static final String READ_ACTION_TEXT = "View Template";
	private static final String ADD_ACTION_TEXT = "Add Template";
	private static final String REMOVE_ACTION_TEXT = "Remove template";
	public static final String TEMPLATE_FORMAT_HTML = "HTML";
	public static final String TEMPLATE_FORMAT_XSL_HTML = "XSL-HTML";
	public static final String TEMPLATE_FORMAT_CSV = "CSV";
	public static final String TEMPLATE_FORMAT_XSL_FO = "XSL-FO";
	public static final String TEMPLATE_FORMAT_TEXT = "TEXT";
	private static final String LOADING_TEMPLATE_FORMAT_LABEL = "Loading template data";
	private static final String LOADING_TEMPLATE_LABEL = "Loading Templates";
	private static final String HELP_TEXT = "org.apache.syncope.ide.eclipse.plugin.viewer";
	
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

		public void initialize() throws java.security.AccessControlException, javax.ws.rs.ProcessingException {
			invisibleRoot = new TreeParent("");

			if (this.deploymentUrl != null && !(this.deploymentUrl.equals("")) && this.username != null
					&& !(this.username.equals("")) && this.password != null && !(this.password.equals(""))) {
				TreeParent p1 = new TreeParent(MAIL_TEMPLATE_LABEL);
				TreeParent p2 = new TreeParent(REPORT_TEMPLATE_LABEL);

				syncopeClient = new SyncopeClientFactoryBean().setAddress(this.deploymentUrl).create(this.username,
						this.password);
				MailTemplateService mailTemplateService = syncopeClient.getService(MailTemplateService.class);
				List<MailTemplateTO> mailTemplateTOs = mailTemplateService.list();

				for (int i = 0; i < mailTemplateTOs.size(); i++) {
					TreeObject obj = new TreeObject(mailTemplateTOs.get(i).getKey());
					p1.addChild(obj);
				}
				invisibleRoot.addChild(p1);
				ReportTemplateService reportTemplateService = syncopeClient.getService(ReportTemplateService.class);
				List<ReportTemplateTO> reportTemplateTOs = reportTemplateService.list();

				for (int i = 0; i < reportTemplateTOs.size(); i++) {
					TreeObject obj = new TreeObject(reportTemplateTOs.get(i).getKey());
					p2.addChild(obj);
				}
				invisibleRoot.addChild(p2);
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

	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		vcp = new ViewContentProvider();
		viewer.setContentProvider(vcp);
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), HELP_TEXT);
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
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(loginAction);
	}

	private void fillContextMenu(IMenuManager manager) {

		ISelection selection = viewer.getSelection();
		Object obj = ((IStructuredSelection) selection).getFirstElement();
		if (obj instanceof TreeParent) {
			manager.add(addAction);
		} else {
			manager.add(readAction);
			manager.add(removeAction);
		}
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(loginAction);
		refreshAction.setEnabled(false);
		manager.add(refreshAction);
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

					updateTreeViewer();
				}
			}
		};
		loginAction.setText(LOGIN_ACTION_TEXT);
		loginAction.setToolTipText(LOGIN_ACTION_TOOLTIP_TEXT);
		
		refreshAction = new Action() {
			public void run() {
				updateTreeViewer();
			}
		};
		refreshAction.setText(REFRESH_ACTION_TEXT);
		refreshAction.setToolTipText(REFRESH_ACTION_TOOLTIP_TEXT);

		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				if (!(obj instanceof TreeParent)) {
					openTemplateInEditor((TreeObject) obj);
				} else {
					viewer.expandToLevel(obj, 1);
				}
			}
		};

		readAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				TreeObject obj = (TreeObject) ((IStructuredSelection) selection).getFirstElement();
				openTemplateInEditor(obj);
			}
		};
		readAction.setText(READ_ACTION_TEXT);

		addAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				TreeParent tp = (TreeParent) ((IStructuredSelection) selection).getFirstElement();
				Shell shell = viewer.getControl().getShell();
				AddTemplateDialog addTemplateDialog = new AddTemplateDialog(shell);
				addTemplateDialog.create();
				if (addTemplateDialog.open() == Window.OK) {
					String key = addTemplateDialog.getKey();
					try {
						if (tp.getName().equals(MAIL_TEMPLATE_LABEL)) {
							MailTemplateService mailTemplateService = syncopeClient
									.getService(MailTemplateService.class);
							MailTemplateTO mtto = new MailTemplateTO();
							mtto.setKey(key);
							mailTemplateService.create(mtto);
						} else if (tp.getName().equals(REPORT_TEMPLATE_LABEL)) {
							ReportTemplateService reportTemplateService = syncopeClient
									.getService(ReportTemplateService.class);
							ReportTemplateTO rtto = new ReportTemplateTO();
							rtto.setKey(key);
							reportTemplateService.create(rtto);
						}
						updateTreeViewer();
					} catch (SyncopeClientException e) {
						if (e.toString().contains("EntityExists")) {
							MessageDialog.openError(shell, "Template already exists",
									"A template named " + key + " already exists.");
						}
					}
				}
			}
		};
		addAction.setText(ADD_ACTION_TEXT);

		removeAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				TreeObject obj = (TreeObject) ((IStructuredSelection) selection).getFirstElement();
				TreeParent tp = (TreeParent) vcp.getParent(obj);
				if (tp.getName().equals(MAIL_TEMPLATE_LABEL)) {
					MailTemplateService mailTemplateService = syncopeClient.getService(MailTemplateService.class);
					mailTemplateService.delete(obj.getName());
				} else if (tp.getName().equals(REPORT_TEMPLATE_LABEL)) {
					ReportTemplateService reportTemplateService = syncopeClient.getService(ReportTemplateService.class);
					reportTemplateService.delete(obj.getName());
				}
				updateTreeViewer();
			}
		};
		removeAction.setText(REMOVE_ACTION_TEXT);
	}

	protected void openTemplateInEditor(TreeObject obj) {
		TreeParent tp = (TreeParent) vcp.getParent(obj);
		if (tp.getName().equals(MAIL_TEMPLATE_LABEL)) {
			MailTemplateService mailTemplateService = syncopeClient.getService(MailTemplateService.class);
			final String[] templateData = new String[2];
			String[] editorTitles = { TEMPLATE_FORMAT_HTML, TEMPLATE_FORMAT_TEXT };
			String[] editorToolTips = { obj.getName(), obj.getName() };
			Job job = new Job(LOADING_TEMPLATE_FORMAT_LABEL) {
				@Override
				protected IStatus run(IProgressMonitor arg0) {
					templateData[0] = getStringFromTemplate(mailTemplateService, obj.getName(), MailTemplateFormat.HTML);
					templateData[1] = getStringFromTemplate(mailTemplateService, obj.getName(), MailTemplateFormat.TEXT);
					Display.getDefault().syncExec(new Runnable() {
						@Override
						public void run() {
							try {
								getViewSite().getPage().openEditor(new TemplateEditorInput(templateData, editorTitles, editorToolTips),
										TemplateEditor.ID);
							} catch (PartInitException e) {
								e.printStackTrace();
							}
						}
					});
					return Status.OK_STATUS;
				}
				private String getStringFromTemplate(MailTemplateService mailTemplateService, String name, MailTemplateFormat format) {
					try {
						InputStream inpstream = (InputStream) (mailTemplateService.getFormat(name, format)) .getEntity();
						Scanner sc = new Scanner(inpstream);
						String templateContent = sc.nextLine();
						while(sc.hasNext())
							templateContent += "\n" + sc.nextLine();
						sc.close();
						return (templateContent);
					} catch (SyncopeClientException e) {
			            if(ClientExceptionType.NotFound.equals(e.getType())){
			            	return "";
			            }
			        }
					return null;
				}
			};
			job.setUser(true);
			job.schedule();
			
		} else if (tp.getName().equals(REPORT_TEMPLATE_LABEL)) {
			ReportTemplateService reportTemplateService = syncopeClient.getService(ReportTemplateService.class);
			final String[] templateData = new String[3];
			String[] editorTitles = { TEMPLATE_FORMAT_CSV, TEMPLATE_FORMAT_XSL_FO, TEMPLATE_FORMAT_XSL_HTML };
			String[] editorToolTips = { obj.getName(), obj.getName(), obj.getName() };
			Job job = new Job(LOADING_TEMPLATE_FORMAT_LABEL) {
				@Override
				protected IStatus run(IProgressMonitor arg0) {
					templateData[0] = getStringFromTemplate(reportTemplateService, obj.getName(), ReportTemplateFormat.CSV);
					templateData[1] = getStringFromTemplate(reportTemplateService, obj.getName(), ReportTemplateFormat.FO);
					templateData[2] = getStringFromTemplate(reportTemplateService, obj.getName(), ReportTemplateFormat.HTML);
					Display.getDefault().syncExec(new Runnable() {
						@Override
						public void run() {
							try {
								getViewSite().getPage().openEditor(new TemplateEditorInput(templateData, editorTitles, editorToolTips),
										TemplateEditor.ID);
							} catch (PartInitException e) {
								e.printStackTrace();
							}
						}
					});
					return Status.OK_STATUS;
				}
				private String getStringFromTemplate(ReportTemplateService reportTemplateService, String name, ReportTemplateFormat format) {
					try {
						InputStream inpstream = (InputStream) (reportTemplateService.getFormat(name, format)) .getEntity();
						Scanner sc = new Scanner(inpstream);
						String templateContent = sc.nextLine();
						while(sc.hasNext())
							templateContent += "\n" + sc.nextLine();
						sc.close();
						return (templateContent);
					} catch (SyncopeClientException e) {
			            if(ClientExceptionType.NotFound.equals(e.getType())){
			            	return "";
			            }
			        }
					return null;
				}
			};
			job.setUser(true);
			job.schedule();
		}
	}
	
	private void updateTreeViewer() {
		Display display = Display.getDefault();
		Job job = new Job(LOADING_TEMPLATE_LABEL) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					vcp.initialize();
				} catch (Exception e) {
					display.syncExec(new Runnable() {
						public void run() {
							Shell shell = viewer.getControl().getShell();
							if (e instanceof java.security.AccessControlException) {
								MessageDialog.openError(shell, "Incorrect Credentials",
										"Unable to authenticate " + vcp.username);
							} else if (e instanceof javax.ws.rs.ProcessingException) {
								MessageDialog.openError(shell, "Incorrect Url",
										"Unable to find apache syncope at " + vcp.deploymentUrl);
							} else if (e instanceof javax.xml.ws.WebServiceException) {
								MessageDialog.openError(shell, "Invalid Url", "Not a valid url " + vcp.username);
							} else
								e.printStackTrace();
						}
					});
				} finally {
					display.syncExec(new Runnable() {
						public void run() {
							refreshAction.setEnabled(true);
							SyncopeView.this.viewer.refresh();
						}
					});
				}
				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.schedule();
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	public static void setMailTemplateContent(String key, MailTemplateFormat format, String content){
		MailTemplateService mailTemplateService = syncopeClient.getService(MailTemplateService.class);
		mailTemplateService.setFormat(key, format, new ByteArrayInputStream(content.getBytes()));
	}
	public static void setReportTemplateContent(String key, ReportTemplateFormat format, String content){
		ReportTemplateService reportTemplateService = syncopeClient.getService(ReportTemplateService.class);
		reportTemplateService.setFormat(key, format, new ByteArrayInputStream(content.getBytes()));
	}

}