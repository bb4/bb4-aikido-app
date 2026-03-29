package com.barrybecker4.aikido.generation

import org.scalatest.funsuite.AnyFunSuite

class XmlToJsConverterSuite extends AnyFunSuite:

  private def jsFor(xml: String): String =
    new XmlToJsConverter().generateJavaScript(TestXmlDocuments.parse(xml))

  test("ignores whitespace text nodes for attacks index") {
    val out = jsFor(
      """<hierarchy imgpath="img/">
        |  <node id="only" label="O"/>
        |</hierarchy>""".stripMargin
    )
    assert(out.contains("attacks[0]='only'"))
    assert(!out.contains("attacks[1]='"))
  }

  test("use ref appears in next array") {
    val out = jsFor(
      """<hierarchy imgpath="i/">
        |  <node id="root" label="R">
        |    <node id="c" label="C"/>
        |    <use ref="c"/>
        |  </node>
        |</hierarchy>""".stripMargin
    )
    assert(out.contains("""next["root"][0]="c""""))
    assert(out.contains("""next["root"][1]="c""""))
  }

  test("escapes double quote in label for JavaScript") {
    val out = jsFor(
      """<hierarchy imgpath="p/"><node id="x" label='say "hi"' description="d"/></hierarchy>"""
    )
    val labelLine = out.linesIterator.find(_.contains("label[\"x\"]")).getOrElse("")
    assert(
      labelLine.contains("say \\" + "\"" + "hi" + "\\" + "\""),
      s"expected escaped quotes in: $labelLine"
    )
  }
