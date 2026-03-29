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

  test("script open is plain script tag") {
    assert(HtmlUtil.getScriptOpen == "<script>")
  }
