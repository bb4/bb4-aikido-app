package com.barrybecker4.aikido.generation

import org.scalatest.funsuite.AnyFunSuite

import java.nio.charset.StandardCharsets
import java.nio.file.Files

class AllTechniqueHtmlGeneratorSuite extends AnyFunSuite:

  private val sampleXml =
    """<?xml version="1.0"?>
      |<hierarchy title="test" imgpath="images/" date="01/01/2020" author="test">
      |  <node id="katate_dori" label="katate dori" description="left wrist grab">
      |    <node id="tenkan" label="tenkan" description="tenkan entrance"/>
      |  </node>
      |</hierarchy>
      |""".stripMargin

  test("generated all-techniques html uses modern head and CSS units with img alt") {
    val document = TestXmlDocuments.parse(sampleXml)
    val out = Files.createTempFile("all-techniques-", ".html")
    try
      new AllTechniqueHtmlGenerator(
        AllTechniqueConfig(debug = false, imageSize = 80, fontSize = 10, borderWidth = 0, showOnlyUniqueImages = false)
      ).generateAllElementsFromDom(document, out.toString)

      val html = Files.readString(out, StandardCharsets.UTF_8)
      assert(html.contains("<!DOCTYPE html>"))
      assert(html.contains("<html lang=\"en\">"))
      assert(html.contains("<meta charset=\"utf-8\">"))
      assert(html.contains("name=\"viewport\""))
      assert(html.contains("href=\"aikido.css\""))
      assert(!html.contains("<big>"))
      assert(!html.contains("<font"))
      assert(html.contains("font-size:10px"))
      assert(html.contains("height:20px"))
      assert(html.contains("alt=\"katate dori\"") || html.contains("alt=\"tenkan\""))
    finally
      Files.deleteIfExists(out)
  }

  test("technique paths ending with use ref are included in generated html") {
    val xmlWithUseRef =
      """<?xml version="1.0"?>
        |<hierarchy title="test" imgpath="images/" date="01/01/2020" author="test">
        |  <node id="start" label="start" description="starting position">
        |    <node id="entrance" label="entrance" description="entrance move">
        |      <use ref="finish"/>
        |    </node>
        |  </node>
        |  <node id="finish" label="finish" description="finishing move"/>
        |</hierarchy>
        |""".stripMargin
    
    val document = TestXmlDocuments.parse(xmlWithUseRef)
    val out = Files.createTempFile("all-techniques-use-", ".html")
    try
      new AllTechniqueHtmlGenerator(
        AllTechniqueConfig(debug = false, imageSize = 80, fontSize = 10, borderWidth = 0, showOnlyUniqueImages = false)
      ).generateAllElementsFromDom(document, out.toString)

      val html = Files.readString(out, StandardCharsets.UTF_8)
      assert(html.contains("start"))
      assert(html.contains("entrance"))
      assert(html.contains("finish"))
      
      val startIndex = html.indexOf("start")
      val entranceIndex = html.indexOf("entrance")
      val finishIndex = html.indexOf("finish")
      
      assert(startIndex > 0 && entranceIndex > startIndex && finishIndex > entranceIndex,
        "Expected technique row to include start, entrance, and finish in order")
    finally
      Files.deleteIfExists(out)
  }
