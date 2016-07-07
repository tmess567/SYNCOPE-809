package org.apache.syncope.ide.eclipse.plugin.editors;

import org.apache.syncope.ide.eclipse.plugin.editors.htmlhelpers.HTMLFileDocumentProvider;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.editors.text.TextEditor;

public class GenericEditor extends TextEditor{
	public GenericEditor(String title){
		super();
		switch(title){
			case "HTML": 
				setSourceViewerConfiguration(new HTMLSourceConfiguration());
				break;
			case "XSL-FO": 
				setSourceViewerConfiguration(new XSLSourceConfiguration());
				break;
			default: break;
		}
		
	}
	
	protected final void doSetInput(IEditorInput input) throws CoreException {
		setDocumentProvider(new HTMLFileDocumentProvider());
		super.doSetInput(input);
	}
}
