// Copyright by Barry G. Becker, 2014 - 2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.aikido.generation

/** Safe embedding of XML-derived text into generated JavaScript string literals and HTML. */
private[generation] object GenerationEscape:

  def escapeJsString(s: String | Null): String =
    if s == null then ""
    else
      s.flatMap {
        case '\\' => "\\\\"
        case '"'  => "\\\""
        case '\n' => "\\n"
        case '\r' => "\\r"
        case '\t' => "\\t"
        case c if c < 0x20 =>
          "\\u%04x".format(c.toInt)
        case c => c.toString
      }

  def escapeHtmlText(s: String | Null): String =
    if s == null then ""
    else
      s.flatMap {
        case '&' => "&amp;"
        case '<' => "&lt;"
        case '>' => "&gt;"
        case c   => c.toString
      }

  /** For double-quoted attributes (`title="..."`, `src="..."`). */
  def escapeHtmlAttribute(s: String | Null): String =
    if s == null then ""
    else
      s.flatMap {
        case '&' => "&amp;"
        case '"' => "&quot;"
        case '<' => "&lt;"
        case c   => c.toString
      }
