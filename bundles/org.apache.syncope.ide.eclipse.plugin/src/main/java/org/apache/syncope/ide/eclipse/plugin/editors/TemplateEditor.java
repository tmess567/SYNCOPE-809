package org.apache.syncope.ide.eclipse.plugin.editors;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.*;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.ide.IDE;

public class TemplateEditor extends MultiPageEditorPart implements IResourceChangeListener{

	public static final String ID = "org.apache.syncope.ide.eclipse.plugin.editors.TemplateEditor";
	
	private TextEditor editor;
	private TemplateEditorInput input;
	
	private String[] inputStringList;
	private String[] titleList;
	private String[] tooltipList;
	
	void createPage(String inputString, String title, String tooltip) {
		try {
			editor = new TextEditor();
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
		getEditor(0).doSave(monitor);
	}
	
	public void doSaveAs() {
		IEditorPart editor = getEditor(0);
		editor.doSaveAs();
		setPageText(0, editor.getTitle());
		setInput(editor.getEditorInput());
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
		return false;
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