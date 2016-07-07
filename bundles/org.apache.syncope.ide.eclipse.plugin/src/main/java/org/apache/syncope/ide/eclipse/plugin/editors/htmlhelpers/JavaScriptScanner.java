package org.apache.syncope.ide.eclipse.plugin.editors.htmlhelpers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class JavaScriptScanner extends RuleBasedScanner {

	public static final String KEYWORDS[] = {
			"abstract",
			"boolean", "break", "byte",
			"case", "catch", "char", "class", "const", "continue",
			"debugger", "default", "delete", "do", "double",
			"else", "enum", "export", "extends",
			"false", "final", "finally", "float", "for", "function",
			"goto", "if", "implements", "import", "in", "instanceof", "int", "interface",
			"let", "long",
			"native", "new", "null",
			"package", "private", "protected", "prototype", "public",
			"return", "short", "static", "super", "switch", "synchronized",
			"this", "throw", "throws", "transient", "true", "try", "typeof",
			"var", "void", "while", "with",
			"typeof", "yield", "undefined", "Infinity", "NaN"
	};

	public JavaScriptScanner(){
		List<IRule> rules = createRules();
		setRules(rules.toArray(new IRule[rules.size()]));
	}

	/**
	 * Creates the list of <code>IRule</code>.
	 * If you have to customize rules, override this method.
	 *
	 * @param colorProvider ColorProvider
	 * @return the list of <code>IRule</code>
	 */
	protected List<IRule> createRules(){
		IToken normal  = new Token(new TextAttribute(new Color(Display.getCurrent(), 
				IHTMLColorConstants.FOREGROUND)));
		IToken string  = new Token(new TextAttribute(new Color(Display.getCurrent(), 
				IHTMLColorConstants.JAVA_STRING)));
		IToken comment = new Token(new TextAttribute(new Color(Display.getCurrent(), 
				IHTMLColorConstants.JAVA_COMMENT)));
		IToken keyword = new Token(new TextAttribute(new Color(Display.getCurrent(), 
				IHTMLColorConstants.JAVA_KEYWORD)));

		List<IRule> rules = new ArrayList<IRule>();
		rules.add(new SingleLineRule("\"", "\"", string, '\\'));
		rules.add(new SingleLineRule("'", "'", string, '\\'));
		rules.add(new SingleLineRule("\\//", null, normal));
		rules.add(new EndOfLineRule("//", comment));

		WordRule wordRule = new WordRule(new JavaWordDetector(), normal);
		for(int i=0;i<KEYWORDS.length;i++){
			wordRule.addWord(KEYWORDS[i], keyword);
		}
		rules.add(wordRule);
		return rules;
	}

}
