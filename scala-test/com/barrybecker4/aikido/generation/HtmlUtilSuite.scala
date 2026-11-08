package com.barrybecker4.aikido.generation

import org.scalatest.funsuite.AnyFunSuite


class HtmlUtilSuite extends AnyFunSuite:

  test("get html file head") {
    assertResult(
      "<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n<meta charset=\"utf-8\">\n<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n<link rel=\"stylesheet\" href=\"aikido.css\">\n<title>foo</title>\n\n"
    ) {
      HtmlUtil.getHTMLHead("foo")
    }
  }

  test("script open is plain script tag") {
    assert(HtmlUtil.getScriptOpen == "<script>")
  }
