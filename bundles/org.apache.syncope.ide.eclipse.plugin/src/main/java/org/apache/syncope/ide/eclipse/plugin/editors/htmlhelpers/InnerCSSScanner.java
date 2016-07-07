package org.apache.syncope.ide.eclipse.plugin.editors.htmlhelpers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class InnerCSSScanner extends CSSBlockScanner {

	public InnerCSSScanner() {
		super();
	}
	
	@Override protected List<IRule> createRules() {
		RGB rgb = IHTMLColorConstants.TAGLIB;
		Color color = new Color(Display.getCurrent(), rgb);
		IToken tag = new Token(new TextAttribute(color));
		
		rgb = IHTMLColorConstants.CSS_COMMENT;
		color = new Color(Display.getCurrent(), rgb);
		IToken comment = new Token(new TextAttribute(color));
		
		List<IRule> rules = new ArrayList<IRule>();
		rules.add(new SingleLineRule("<style", ">", tag));
		rules.add(new SingleLineRule("</style", ">", tag));
		rules.add(new MultiLineRule("/*", "*/", comment));
		rules.addAll(super.createRules());
		
		return rules;
	}
}
