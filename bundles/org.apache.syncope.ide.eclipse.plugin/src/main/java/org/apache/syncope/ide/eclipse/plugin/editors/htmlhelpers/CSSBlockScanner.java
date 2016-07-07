package org.apache.syncope.ide.eclipse.plugin.editors.htmlhelpers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class CSSBlockScanner extends RuleBasedScanner {
	
	public CSSBlockScanner(){
		List<IRule> rules = createRules();
		setRules(rules.toArray(new IRule[rules.size()]));
	}
	
	protected List<IRule> createRules(){
		List<IRule> rules = new ArrayList<IRule>();
		rules.add(new CSSRule(
				new Token(new TextAttribute(new Color(Display.getCurrent(), 
						IHTMLColorConstants.CSS_PROP))),
				new Token(new TextAttribute(new Color(Display.getCurrent(), 
						IHTMLColorConstants.CSS_VALUE)))));
		return rules;
	}
	
}