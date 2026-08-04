package com.barrybecker4.aikido.generation

import org.scalatest.funsuite.AnyFunSuite

import java.nio.file.Files

class AllTechniqueHtmlGeneratorSuite extends AnyFunSuite:

  test("writes technique step labels and omits images when imageSize is 0") {
    val doc = TestXmlDocuments.parse(
      """<hierarchy imgpath="img/">
        |  <node id="attack" label="Katate">
        |    <node id="tech" label="Ikkyo" description="first teaching"/>
        |  </node>
        |</hierarchy>""".stripMargin
    )
    val tmp = Files.createTempFile("all-techniques", ".html")
    try
      new AllTechniqueHtmlGenerator(AllTechniqueConfig(imageSize = 0))
        .generateAllElementsFromDom(doc, tmp.toString)
      val html = Files.readString(tmp)
      assert(html.contains("Katate"))
      assert(html.contains("Ikkyo"))
      assert(html.contains("title=\"first teaching\""))
      assert(!html.contains("<img "))
      assert(html.contains("Total number of images shown = 0"))
    finally
      Files.deleteIfExists(tmp)
  }

  test("debug mode uses ids instead of labels") {
    val doc = TestXmlDocuments.parse(
      """<hierarchy imgpath="img/">
        |  <node id="a1" label="Attack">
        |    <node id="t1" label="Technique"/>
        |  </node>
        |</hierarchy>""".stripMargin
    )
    val tmp = Files.createTempFile("all-techniques-debug", ".html")
    try
      new AllTechniqueHtmlGenerator(AllTechniqueConfig(debug = true, imageSize = 0))
        .generateAllElementsFromDom(doc, tmp.toString)
      val html = Files.readString(tmp)
      // Cell text is the id; description/title still falls back to the label.
      assert(html.contains("overflow:hidden;\">\na1"))
      assert(html.contains("overflow:hidden;\">\nt1"))
      assert(html.contains("title=\"Attack\""))
      assert(html.contains("title=\"Technique\""))
      assert(!html.contains("overflow:hidden;\">\nAttack"))
      assert(!html.contains("overflow:hidden;\">\nTechnique"))
    finally
      Files.deleteIfExists(tmp)
  }
