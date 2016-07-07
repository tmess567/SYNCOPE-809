package org.apache.syncope.ide.eclipse.plugin.editors.htmlhelpers;

import java.util.List;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class InnerJavaScriptScanner extends JavaScriptScanner {

	public InnerJavaScriptScanner() {
		super();
	}

	@Override protected List<IRule> createRules() {
		IToken tag = new Token(new TextAttribute(new Color(Display.getCurrent(), 
				IHTMLColorConstants.TAG)));
		IToken comment = new Token(new TextAttribute(new Color(Display.getCurrent(), 
				IHTMLColorConstants.JAVA_COMMENT)));
		IToken jsdoc = new Token(new TextAttribute(new Color(Display.getCurrent(), 
				IHTMLColorConstants.JSDOC)));

		List<IRule> rules = super.createRules();
		rules.add(new SingleLineRule("<script", ">", tag));
		rules.add(new SingleLineRule("</script", ">", tag));
		rules.add(new MultiLineRule("/**", "*/", jsdoc));
		rules.add(new MultiLineRule("/*", "*/", comment));

		return rules;
	}


}
