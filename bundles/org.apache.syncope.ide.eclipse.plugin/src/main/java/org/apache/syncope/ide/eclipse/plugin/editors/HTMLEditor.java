package org.apache.syncope.ide.eclipse.plugin.editors;

import org.apache.syncope.ide.eclipse.plugin.editors.htmlhelpers.HTMLFileDocumentProvider;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.editors.text.TextEditor;

public class HTMLEditor extends TextEditor{
	public HTMLEditor(){
		super();
		setSourceViewerConfiguration(new HTMLSourceConfiguration());
	}
	
	protected final void doSetInput(IEditorInput input) throws CoreException {
		setDocumentProvider(new HTMLFileDocumentProvider());
		super.doSetInput(input);
	}
}
