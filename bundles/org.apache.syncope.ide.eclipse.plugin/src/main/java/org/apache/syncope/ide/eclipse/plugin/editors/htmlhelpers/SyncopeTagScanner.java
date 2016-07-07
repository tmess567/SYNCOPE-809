package org.apache.syncope.ide.eclipse.plugin.editors.htmlhelpers;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class SyncopeTagScanner extends RuleBasedScanner {

	public SyncopeTagScanner(boolean bold) {
		IToken string = null;
		if(bold){
			RGB rgb = IHTMLColorConstants.TAGLIB_ATTR;
			Color color = new Color(Display.getCurrent(), rgb);
            string = new Token(new TextAttribute(color));
		} else {
			RGB rgb = IHTMLColorConstants.STRING;
			Color color = new Color(Display.getCurrent(), rgb);
            string = new Token(new TextAttribute(color));
		}
		IRule[] rules = new IRule[3];
		
		rules[0] = new MultiLineRule("\"" , "\"" , string, '\\');
		rules[1] = new MultiLineRule("'"  , "'"  , string, '\\');
		rules[2] = new WhitespaceRule(new HTMLWhitespaceDetector());
		
		setRules(rules);
	}
}
