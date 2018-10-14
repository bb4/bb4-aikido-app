package com.barrybecker4.aikido.generation

import org.scalatest.FunSuite


class HtmlUtilSuite extends FunSuite {

  test("get html file head") {
    assertResult(
      "<!DOCTYPE>\n<html>\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><title>foo</title>\n\n"
    ) {
      HtmlUtil.getHTMLHead("foo")
    }
  }
}