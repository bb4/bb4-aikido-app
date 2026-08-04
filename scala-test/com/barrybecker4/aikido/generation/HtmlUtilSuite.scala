package com.barrybecker4.aikido.generation

import org.scalatest.funsuite.AnyFunSuite


class HtmlUtilSuite extends AnyFunSuite:

  test("get html file head") {
    assertResult(
      "<!DOCTYPE html>\n<html>\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><title>foo</title>\n\n"
    ) {
      HtmlUtil.getHTMLHead("foo")
    }
  }

  test("escapes markup characters in title") {
    val head = HtmlUtil.getHTMLHead("a&b<c>")
    assert(head.contains("<title>a&amp;b&lt;c&gt;</title>"))
  }

  test("script open is plain script tag") {
    assert(HtmlUtil.getScriptOpen == "<script>")
  }

  test("script close is plain script end tag") {
    assert(HtmlUtil.getScriptClose == "</script>")
  }
