// Copyright by Barry G. Becker, 2014 - 2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.aikido.generation

/** Safe embedding of XML-derived text into generated JavaScript string literals and HTML. */
private[generation] object GenerationEscape:

  def escapeJsString(s: String | Null): String =
    if s == null then ""
    else
      val b = new StringBuilder
      var i = 0
      while i < s.length do
        s.charAt(i) match
          case '\\' => b.append("\\\\")
          case '"'  => b.append("\\\"")
          case '\n' => b.append("\\n")
          case '\r' => b.append("\\r")
          case '\t' => b.append("\\t")
          case c if c < 0x20 =>
            b.append("\\u%04x".format(c.toInt))
          case c    => b.append(c)
        i += 1
      b.toString

  def escapeHtmlText(s: String | Null): String =
    if s == null then ""
    else
      val b = new StringBuilder
      var i = 0
      while i < s.length do
        s.charAt(i) match
          case '&'  => b.append("&amp;")
          case '<'  => b.append("&lt;")
          case '>'  => b.append("&gt;")
          case c    => b.append(c)
        i += 1
      b.toString

  /** For double-quoted attributes (`title="..."`, `src="..."`). */
  def escapeHtmlAttribute(s: String | Null): String =
    if s == null then ""
    else
      val b = new StringBuilder
      var i = 0
      while i < s.length do
        s.charAt(i) match
          case '&'  => b.append("&amp;")
          case '"'  => b.append("&quot;")
          case '<'  => b.append("&lt;")
          case c    => b.append(c)
        i += 1
      b.toString
