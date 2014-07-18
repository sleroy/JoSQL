package org.josql.csv;

public class CsvOptions {

	public static final char DEFAULT_CSV_SEPARATOR = ';';
	public static final char DEFAULT_CSV_QUOTE = '\'';
	public static final int DEFAULT_CSV_FIRST_LINE = 1;
	
	private char separator;
	private char quote;
	private int firstLine;
	
	public CsvOptions() {
		setSeparator(DEFAULT_CSV_SEPARATOR);
		setQuote(DEFAULT_CSV_QUOTE);
		setFirstLine(DEFAULT_CSV_FIRST_LINE);	
	}
	
	public CsvOptions(final char _separator, final char _quote, final int _firstLine) {
		setSeparator(_separator);
		setQuote(_quote);
		setFirstLine(_firstLine);
	}
	
	public char getSeparator() {
		return separator;
	}
	
	public void setSeparator(final char _separator) {
		separator = _separator;
	}
	
	public char getQuote() {
		return quote;
	}
	
	public void setQuote(final char _quote) {
		quote = _quote;
	}

	public int getFirstLine() {
		return firstLine;
	}

	public void setFirstLine(final int _firstLine) {
		firstLine = _firstLine;
	}
	
}
