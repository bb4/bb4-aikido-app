package com.barrybecker4.aikido.generation

import org.scalatest.funsuite.AnyFunSuite

class DomNodeUtilSuite extends AnyFunSuite:

  test("elementChildren skips whitespace text nodes") {
    val doc = TestXmlDocuments.parse(
      """<hierarchy imgpath="p/">
        |  <node id="a" label="A"/>
        |  <use ref="a"/>
        |</hierarchy>""".stripMargin
    )
    val kids = DomNodeUtil.elementChildren(doc.getDocumentElement)
    assert(kids.size == 2)
    assert(kids(0).getNodeName == "node")
    assert(kids(1).getNodeName == "use")
  }

  test("childReferenceId uses id for node and ref for use") {
    val doc = TestXmlDocuments.parse(
      """<hierarchy imgpath="p/">
        |  <node id="n1" label="N"/>
        |  <use ref="n1"/>
        |</hierarchy>""".stripMargin
    )
    val kids = DomNodeUtil.elementChildren(doc.getDocumentElement)
    assert(DomNodeUtil.childReferenceId(kids(0)) == "n1")
    assert(DomNodeUtil.childReferenceId(kids(1)) == "n1")
  }

  test("childReferenceId returns null for unknown element") {
    val doc = TestXmlDocuments.parse("""<hierarchy imgpath="p/"><other/></hierarchy>""")
    val other = DomNodeUtil.elementChildren(doc.getDocumentElement).head
    assert(DomNodeUtil.childReferenceId(other) == null)
  }
