package org.apache.syncope.ide.eclipse.plugin.editors.htmlhelpers;

import org.eclipse.jface.text.rules.IWordDetector;

public class JavaWordDetector implements IWordDetector {

	public boolean isWordStart(char c) {
		return Character.isJavaIdentifierStart(c);
	}

	public boolean isWordPart(char c) {
		return Character.isJavaIdentifierPart(c);
	}

}