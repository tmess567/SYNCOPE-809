package org.apache.syncope.ide.eclipse.plugin.editors.htmlhelpers;

import org.apache.syncope.ide.eclipse.plugin.Activator;
import org.eclipse.jface.text.templates.GlobalTemplateVariables;
import org.eclipse.jface.text.templates.TemplateContextType;

public class HTMLContextType extends TemplateContextType {
	
	public static final String CONTEXT_TYPE 
		= Activator.PLUGIN_ID + ".templateContextType.html";
	
	public HTMLContextType(){
		addResolver(new GlobalTemplateVariables.Cursor());
		addResolver(new GlobalTemplateVariables.WordSelection());
		addResolver(new GlobalTemplateVariables.LineSelection());
	}
	
}