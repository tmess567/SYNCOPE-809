package org.apache.syncope.ide.eclipse.plugin.editors.htmlhelpers;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.WhitespaceRule;

public class HTMLScanner extends RuleBasedScanner {

	public HTMLScanner() {
		
		IRule[] rules = new IRule[1];
		rules[0] = new WhitespaceRule(new HTMLWhitespaceDetector());
		
		setRules(rules);
	}
}
