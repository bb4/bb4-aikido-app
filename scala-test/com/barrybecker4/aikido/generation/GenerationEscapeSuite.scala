package com.barrybecker4.aikido.generation

import org.scalatest.funsuite.AnyFunSuite

class GenerationEscapeSuite extends AnyFunSuite:

  test("escapeJsString returns empty for null") {
    assert(GenerationEscape.escapeJsString(null) == "")
  }

  test("escapeJsString escapes quotes backslash and control chars") {
    assert(GenerationEscape.escapeJsString("a\"b\\c") == "a\\\"b\\\\c")
    assert(GenerationEscape.escapeJsString("a\nb\tc\rd") == "a\\nb\\tc\\rd")
    assert(GenerationEscape.escapeJsString("\u0001") == "\\u0001")
  }

  test("escapeHtmlText returns empty for null and escapes markup") {
    assert(GenerationEscape.escapeHtmlText(null) == "")
    assert(GenerationEscape.escapeHtmlText("a&b<c>d") == "a&amp;b&lt;c&gt;d")
  }

  test("escapeHtmlAttribute returns empty for null and escapes quotes") {
    assert(GenerationEscape.escapeHtmlAttribute(null) == "")
    assert(GenerationEscape.escapeHtmlAttribute("a&\"b<c") == "a&amp;&quot;b&lt;c")
  }
