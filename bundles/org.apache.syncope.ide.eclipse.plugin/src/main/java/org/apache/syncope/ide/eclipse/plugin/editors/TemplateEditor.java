package org.apache.syncope.ide.eclipse.plugin.editors;

import org.apache.syncope.common.lib.types.MailTemplateFormat;
import org.apache.syncope.ide.eclipse.plugin.views.SyncopeView;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.*;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.texteditor.ITextEditor;

import org.eclipse.ui.ide.IDE;

public class TemplateEditor extends MultiPageEditorPart implements IResourceChangeListener{

	public static final String ID = "org.apache.syncope.ide.eclipse.plugin.editors.TemplateEditor";
	private static final String SAVE_TEMPLATE_LABEL = "Saving Template";
	
	private GenericEditor editor;
	private TemplateEditorInput input;
	
	private String[] inputStringList;
	private String[] titleList;
	private String[] tooltipList;
	
	void createPage(String inputString, String title, String tooltip) {
		try {
			editor = new GenericEditor(title);
			int index = addPage(editor, (IEditorInput)new TemplateEditorInput(inputString, title, tooltip));
			setPageText(index, editor.getTitle());
		} catch (PartInitException e) {
			ErrorDialog.openError(
				getSite().getShell(),
				"Error creating nested text editor",
				null,
				e.getStatus());
		}
	}
	
	protected void createPages() {
		for(int i=0; i<inputStringList.length; i++){
			createPage(inputStringList[i], titleList[i], tooltipList[i]);
		}
	}
	
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}
	
	public void doSave(IProgressMonitor monitor) {
		ITextEditor ite = (ITextEditor) getActiveEditor();
		String content = ite.getDocumentProvider().getDocument(ite.getEditorInput()).get();
		Job saveJob = new Job(SAVE_TEMPLATE_LABEL){
			@Override
			protected IStatus run(IProgressMonitor arg0) {
				try{
					SyncopeView.setMailTemplateContent(ite.getTitleToolTip(), 
							MailTemplateFormat.HTML, content);
				} catch (Exception e){
					e.printStackTrace();
				} finally {
					Display.getDefault().syncExec(new Runnable() {
						@Override
						public void run() {
							ite.doSave(monitor);
							System.out.println("Monitor done called");
						}
					});
				}
				if (monitor.isCanceled()) {
		            return org.eclipse.core.runtime.Status.CANCEL_STATUS;
		        }
		        return org.eclipse.core.runtime.Status.OK_STATUS;
			}
		};
		saveJob.setUser(true);
		saveJob.schedule();
	}
	
	public void doSaveAs() {
		getActiveEditor().doSaveAs();
	}
	public void gotoMarker(IMarker marker) {
		setActivePage(0);
		IDE.gotoMarker(getEditor(0), marker);
	}
	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
		if (!(editorInput instanceof TemplateEditorInput)) {
			throw new RuntimeException("Wrong input");
		}
		this.input = (TemplateEditorInput) editorInput;
		
		this.inputStringList = this.input.getInputStringList();
		this.titleList = this.input.getTitleList();
		this.tooltipList = this.input.getTooltipList();
		
	    setSite(site);
	    setInput(input);
	    setPartName(this.tooltipList[0]);
	}
	
	public boolean isSaveAsAllowed() {
		return true;
	}
	
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
	}
	
	public void resourceChanged(final IResourceChangeEvent event){
		if(event.getType() == IResourceChangeEvent.PRE_CLOSE){
			Display.getDefault().asyncExec(new Runnable(){
				public void run(){
					IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
					for (int i = 0; i<pages.length; i++){
						if(((FileEditorInput)editor.getEditorInput()).getFile().getProject().equals(event.getResource())){
							IEditorPart editorPart = pages[i].findEditor(editor.getEditorInput());
							pages[i].closeEditor(editorPart,true);
						}
					}
				}            
			});
		}
	}
}
