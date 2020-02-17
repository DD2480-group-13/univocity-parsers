/*******************************************************************************
 * Copyright 2014 Univocity Software Pty Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.univocity.parsers.csv;

import com.univocity.parsers.common.*;
import com.univocity.parsers.common.fields.*;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

/**
 * A powerful and flexible CSV writer implementation.
 *
 * @author Univocity Software Pty Ltd - <a href="mailto:parsers@univocity.com">parsers@univocity.com</a>
 * @see CsvFormat
 * @see CsvWriterSettings
 * @see CsvParser
 * @see AbstractWriter
 */
public class CsvWriter extends AbstractWriter<CsvWriterSettings> {

	private char delimiter;
	private char[] multiDelimiter;
	private char quoteChar;
	private char escapeChar;
	private char escapeEscape;
	private boolean quoteAllFields;
	private boolean escapeUnquoted;
	private boolean inputNotEscaped;
	private char newLine;
	private boolean dontProcessNormalizedNewLines;
	private boolean[] quotationTriggers;
	private char maxTrigger;
	private Set<Integer> quotedColumns;
	private FieldSelector quotedFieldSelector;

	public static boolean[] appendBranchCoverage = new boolean[41];

	/**
	 * The CsvWriter supports all settings provided by {@link CsvWriterSettings}, and requires this configuration to be properly initialized.
	 * <p><strong>Important: </strong> by not providing an instance of {@link java.io.Writer} to this constructor, only the operations that write to Strings are available.</p>
	 *
	 * @param settings the CSV writer configuration
	 */
	public CsvWriter(CsvWriterSettings settings) {
		this((Writer) null, settings);
	}

	/**
	 * The CsvWriter supports all settings provided by {@link CsvWriterSettings}, and requires this configuration to be properly initialized.
	 *
	 * @param writer   the output resource that will receive CSV records produced by this class.
	 * @param settings the CSV writer configuration
	 */
	public CsvWriter(Writer writer, CsvWriterSettings settings) {
		super(writer, settings);
	}

	/**
	 * The CsvWriter supports all settings provided by {@link CsvWriterSettings}, and requires this configuration to be properly initialized.
	 *
	 * @param file     the output file that will receive CSV records produced by this class.
	 * @param settings the CSV writer configuration
	 */
	public CsvWriter(File file, CsvWriterSettings settings) {
		super(file, settings);
	}

	/**
	 * The CsvWriter supports all settings provided by {@link CsvWriterSettings}, and requires this configuration to be properly initialized.
	 *
	 * @param file     the output file that will receive CSV records produced by this class.
	 * @param encoding the encoding of the file
	 * @param settings the CSV writer configuration
	 */
	public CsvWriter(File file, String encoding, CsvWriterSettings settings) {
		super(file, encoding, settings);
	}

	/**
	 * The CsvWriter supports all settings provided by {@link CsvWriterSettings}, and requires this configuration to be properly initialized.
	 *
	 * @param file     the output file that will receive CSV records produced by this class.
	 * @param encoding the encoding of the file
	 * @param settings the CSV writer configuration
	 */
	public CsvWriter(File file, Charset encoding, CsvWriterSettings settings) {
		super(file, encoding, settings);
	}

	/**
	 * The CsvWriter supports all settings provided by {@link CsvWriterSettings}, and requires this configuration to be properly initialized.
	 *
	 * @param output   the output stream that will be written with the CSV records produced by this class.
	 * @param settings the CSV writer configuration
	 */
	public CsvWriter(OutputStream output, CsvWriterSettings settings) {
		super(output, settings);
	}

	/**
	 * The CsvWriter supports all settings provided by {@link CsvWriterSettings}, and requires this configuration to be properly initialized.
	 *
	 * @param output   the output stream that will be written with the CSV records produced by this class.
	 * @param encoding the encoding of the stream
	 * @param settings the CSV writer configuration
	 */
	public CsvWriter(OutputStream output, String encoding, CsvWriterSettings settings) {
		super(output, encoding, settings);
	}

	/**
	 * The CsvWriter supports all settings provided by {@link CsvWriterSettings}, and requires this configuration to be properly initialized.
	 *
	 * @param output   the output stream that will be written with the CSV records produced by this class.
	 * @param encoding the encoding of the stream
	 * @param settings the CSV writer configuration
	 */
	public CsvWriter(OutputStream output, Charset encoding, CsvWriterSettings settings) {
		super(output, encoding, settings);
	}

	/**
	 * Initializes the CSV writer with CSV-specific configuration
	 *
	 * @param settings the CSV writer configuration
	 */
	protected final void initialize(CsvWriterSettings settings) {
		CsvFormat format = settings.getFormat();
		this.multiDelimiter = format.getDelimiterString().toCharArray();
		if (multiDelimiter.length == 1) {
			delimiter = multiDelimiter[0];
			multiDelimiter = null;
		}
		this.quoteChar = format.getQuote();
		this.escapeChar = format.getQuoteEscape();
		this.escapeEscape = settings.getFormat().getCharToEscapeQuoteEscaping();
		this.newLine = format.getNormalizedNewline();

		this.quoteAllFields = settings.getQuoteAllFields();
		this.escapeUnquoted = settings.isEscapeUnquotedValues();
		this.inputNotEscaped = !settings.isInputEscaped();
		this.dontProcessNormalizedNewLines = !settings.isNormalizeLineEndingsWithinQuotes();

		this.quotationTriggers = null;
		this.quotedColumns = null;
		this.maxTrigger = 0;

		quotedColumns = Collections.emptySet();
		quotedFieldSelector = settings.getQuotedFieldSelector();

		char[] sep = format.getLineSeparator();

		int triggerCount = 3 + settings.getQuotationTriggers().length + sep.length;
		int offset = settings.isQuoteEscapingEnabled() ? 1 : 0;
		char[] tmp = Arrays.copyOf(settings.getQuotationTriggers(), triggerCount + offset);
		if (offset == 1) {
			tmp[triggerCount] = quoteChar;
		}

		tmp[triggerCount-1] = '\n';
		tmp[triggerCount-2] = '\r';
		tmp[triggerCount-3] = newLine;
		tmp[triggerCount-4] = sep[0];
		if(sep.length > 1) {
			tmp[triggerCount - 5] = sep[1];
		}

		for (int i = 0; i < tmp.length; i++) {
			if (maxTrigger < tmp[i]) {
				maxTrigger = tmp[i];
			}
		}
		if (maxTrigger != 0) {
			maxTrigger++;
			this.quotationTriggers = new boolean[maxTrigger];
			Arrays.fill(quotationTriggers, false);
			for (int i = 0; i < tmp.length; i++) {
				quotationTriggers[tmp[i]] = true;
			}
		}
	}

	@Override
	protected void processRow(Object[] row) {
		if (recordCount == 0L && quotedFieldSelector != null) {
			int[] quotedIndexes = quotedFieldSelector.getFieldIndexes(headers);
			if (quotedIndexes.length > 0) {
				quotedColumns = new HashSet<Integer>();
				for (int idx : quotedIndexes) {
					quotedColumns.add(idx);
				}
			}
		}
		for (int i = 0; i < row.length; i++) {
			if (i != 0) {
				if (multiDelimiter == null) {
					appendToRow(delimiter);
				} else {
					appendToRow(multiDelimiter);
				}
			}

			if (dontProcessNormalizedNewLines) {
				appender.enableDenormalizedLineEndings(false);
			}

			boolean allowTrim = allowTrim(i);

			String nextElement = getStringValue(row[i]);
			int originalLength = appender.length();
			boolean isElementQuoted = append(i,quoteAllFields || quotedColumns.contains(i), allowTrim, nextElement);

			//skipped all whitespaces and wrote nothing
			if (appender.length() == originalLength && !usingNullOrEmptyValue) {
				if (isElementQuoted) {
					if (nextElement == null) {
						append(i,false, allowTrim, nullValue);
					} else {
						append(i,true, allowTrim, emptyValue);
					}
				} else if (nextElement == null) {
					append(i,false, allowTrim, nullValue);
				} else {
					append(i,false, allowTrim, emptyValue);
				}
			}

			if (isElementQuoted) {
				appendToRow(quoteChar);
				appendValueToRow();
				appendToRow(quoteChar);
				if (dontProcessNormalizedNewLines) {
					appender.enableDenormalizedLineEndings(true);
				}
			} else {
				appendValueToRow();
			}
		}
	}


	private boolean matchMultiDelimiter(String element, int from) {
		if(from + multiDelimiter.length -2 >= element.length()){
			return false;
		}
		for (int j = 1; j < multiDelimiter.length; j++, from++) {
			if(element.charAt(from) != multiDelimiter[j]){
				return false;
			}
		}
		return true;
	}

	private boolean quoteElement(int start, String element) {
		final int length = element.length();
		if (multiDelimiter == null) {
			if (maxTrigger == 0) {
				for (int i = start; i < length; i++) {
					char nextChar = element.charAt(i);
					if (nextChar == delimiter || nextChar == newLine) {
						return true;
					}
				}
			} else {
				for (int i = start; i < length; i++) {
					char nextChar = element.charAt(i);
					if (nextChar == delimiter || nextChar < maxTrigger && quotationTriggers[nextChar]) {
						return true;
					}
				}
			}
		} else {
			if (maxTrigger == 0) {
				for (int i = start; i < length; i++) {
					char nextChar = element.charAt(i);
					if ((nextChar == multiDelimiter[0] && matchMultiDelimiter(element, i + 1)) || nextChar == newLine) {
						return true;
					}
				}
			} else {
				for (int i = start; i < length; i++) {
					char nextChar = element.charAt(i);
					if ((nextChar == multiDelimiter[0] && matchMultiDelimiter(element, i + 1)) || nextChar < maxTrigger && quotationTriggers[nextChar]) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean append(int columnIndex, boolean isElementQuoted, boolean allowTrim, String element) {
		if (element == null) {
			appendBranchCoverage[0] = true;
			if (nullValue == null) {
				appendBranchCoverage[1] = true;
				return isElementQuoted;
			} else {
				appendBranchCoverage[2] = true;
			}
			element = nullValue;
		} else {
			appendBranchCoverage[3] = true;
		}

		int start = 0;
		if (allowTrim && this.ignoreLeading) {
			appendBranchCoverage[4] = true;
			start = skipLeadingWhitespace(whitespaceRangeStart, element);
		} else {
			appendBranchCoverage[5] = true;
		}

		final int length = element.length();
		if (start < length && (element.charAt(start) == quoteChar || columnIndex == 0 && element.charAt(0) == comment)) {
			appendBranchCoverage[6] = true;
			isElementQuoted = true;
		} else {
			appendBranchCoverage[7] = true;
		}

		if (isElementQuoted) {
			appendBranchCoverage[8] = true;
			if (usingNullOrEmptyValue && length >= 2) {
				appendBranchCoverage[9] = true;
				if (element.charAt(0) == quoteChar && element.charAt(length - 1) == quoteChar) {
					appendBranchCoverage[10] = true;
					appender.append(element);
					return false;
				} else {
					appendBranchCoverage[11] = true;
					appendQuoted(start, allowTrim, element);
					return true;
				}
			} else {
				appendBranchCoverage[12] = true;
				appendQuoted(start, allowTrim, element);
				return true;
			}
		} else {
			appendBranchCoverage[13] = true;
		}

		int i = start;
		char ch = '\0';

		if (multiDelimiter == null) {
			appendBranchCoverage[14] = true;
			for (; i < length; i++) {
				appendBranchCoverage[15] = true;
				ch = element.charAt(i);
				if (ch == quoteChar || ch == delimiter || ch == escapeChar || (ch < maxTrigger && quotationTriggers[ch])) {
					appendBranchCoverage[16] = true;
					appender.append(element, start, i);
					start = i + 1;

					if (ch == quoteChar || ch == escapeChar) {
						appendBranchCoverage[17] = true;
						if (quoteElement(i, element)) {
							appendBranchCoverage[18] = true;
							appendQuoted(i, allowTrim, element);
							return true;
						} else if (escapeUnquoted) {
							appendBranchCoverage[19] = true;
							appendQuoted(i, allowTrim, element);
						} else {
							appendBranchCoverage[20] = true;
							appender.append(element, i, length);
							if (allowTrim && ignoreTrailing && element.charAt(length - 1) <= ' ' && whitespaceRangeStart < element.charAt(length - 1)) {
								appendBranchCoverage[21] = true;
								appender.updateWhitespace();
							}
						}
						return isElementQuoted;
					} else if (ch == escapeChar && inputNotEscaped && escapeEscape != '\0' && escapeUnquoted) {
						appendBranchCoverage[22] = true;
						appender.append(escapeEscape);
					} else if (ch == delimiter || ch < maxTrigger && quotationTriggers[ch]) {
						appendBranchCoverage[23] = true;
						appendQuoted(i, allowTrim, element);
						return true;
					} else {
						appendBranchCoverage[24] = true;
					}
					appender.append(ch);
				} else {
					appendBranchCoverage[25] = true;
				}
			}
		} else {
			appendBranchCoverage[26] = true;
			for (; i < length; i++) {
				appendBranchCoverage[27] = true;
				ch = element.charAt(i);
				if (ch == quoteChar || (ch == multiDelimiter[0] && matchMultiDelimiter(element, i + 1)) || ch == escapeChar || (ch < maxTrigger && quotationTriggers[ch])) {
					appendBranchCoverage[28] = true;
					appender.append(element, start, i);
					start = i + 1;

					if (ch == quoteChar || ch == escapeChar) {
						appendBranchCoverage[29] = true;
						if (quoteElement(i, element)) {
							appendBranchCoverage[30] = true;
							appendQuoted(i, allowTrim, element);
							return true;
						} else if (escapeUnquoted) {
							appendBranchCoverage[31] = true;
							appendQuoted(i, allowTrim, element);
						} else {
							appendBranchCoverage[32] = true;
							appender.append(element, i, length);
							if (allowTrim && ignoreTrailing && element.charAt(length - 1) <= ' ' && whitespaceRangeStart < element.charAt(length - 1)) {
								appendBranchCoverage[33] = true;
								appender.updateWhitespace();
							} else {
								appendBranchCoverage[34] = true;
							}
						}
						return isElementQuoted;
					} else if (ch == escapeChar && inputNotEscaped && escapeEscape != '\0' && escapeUnquoted) {
						appendBranchCoverage[35] = true;
						appender.append(escapeEscape);
					} else if ((ch == multiDelimiter[0] && matchMultiDelimiter(element, i + 1))|| ch < maxTrigger && quotationTriggers[ch]) {
						appendBranchCoverage[36] = true;
						appendQuoted(i, allowTrim, element);
						return true;
					} else {
						appendBranchCoverage[37] = true;
					}
					appender.append(ch);
				} else {
					appendBranchCoverage[38] = true;
				}
			}
		}

		appender.append(element, start, i);
		if (allowTrim && ch <= ' ' && ignoreTrailing && whitespaceRangeStart < ch) {
			appendBranchCoverage[39] = true;
			appender.updateWhitespace();
		} else {
			appendBranchCoverage[40] = true;
		}
		return isElementQuoted;
	}

	private void appendQuoted(int start, boolean allowTrim, String element) {
		final int length = element.length();
		int i = start;
		char ch = '\0';
		for (; i < length; i++) {
			ch = element.charAt(i);
			if (ch == quoteChar || ch == newLine || ch == escapeChar) {
				appender.append(element, start, i);
				start = i + 1;
				if (ch == quoteChar && inputNotEscaped) {
					appender.append(escapeChar);
				} else if (ch == escapeChar && inputNotEscaped && escapeEscape != '\0') {
					appender.append(escapeEscape);
				}
				appender.append(ch);
			}
		}
		appender.append(element, start, i);
		if (allowTrim && ch <= ' ' && ignoreTrailing && whitespaceRangeStart < ch) {
			appender.updateWhitespace();
		}
	}
}
