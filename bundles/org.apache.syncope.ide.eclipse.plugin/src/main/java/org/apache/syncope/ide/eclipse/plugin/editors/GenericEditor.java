package org.apache.syncope.ide.eclipse.plugin.editors;

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
}
