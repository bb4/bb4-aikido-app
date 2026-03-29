package com.barrybecker4.aikido.generation

import org.scalatest.funsuite.AnyFunSuite

class NodeInfoSuite extends AnyFunSuite:

  test("parses node id label and description") {
    val doc = TestXmlDocuments.parse(
      """<hierarchy imgpath="p/"><node id="a" label="L" description="D"/></hierarchy>"""
    )
    val n = DomNodeUtil.elementChildren(doc.getDocumentElement).head
    val info = new NodeInfo("p/", n)
    assert(info.getId == "a")
    assert(info.getImage == "p/a.jpg")
    assert(info.getLabel == "L")
    assert(info.getDescription == "D")
  }

  test("description falls back to label") {
    val doc = TestXmlDocuments.parse(
      """<hierarchy imgpath="x/"><node id="n" label="only"/></hierarchy>"""
    )
    val n = DomNodeUtil.elementChildren(doc.getDocumentElement).head
    val info = new NodeInfo("x/", n)
    assert(info.getDescription == "only")
  }

  test("non-node element has null id") {
    val doc = TestXmlDocuments.parse("""<hierarchy imgpath="p/"><use ref="z"/></hierarchy>""")
    val useEl = DomNodeUtil.elementChildren(doc.getDocumentElement).head
    val info = new NodeInfo("p/", useEl)
    assert(info.getId == null)
  }

  test("unexpected attribute throws") {
    val doc = TestXmlDocuments.parse(
      """<hierarchy imgpath="p/"><node id="a" label="L" badattr="x"/></hierarchy>"""
    )
    val n = DomNodeUtil.elementChildren(doc.getDocumentElement).head
    intercept[IllegalArgumentException](new NodeInfo("p/", n))
  }
