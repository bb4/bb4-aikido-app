package com.barrybecker4.aikido.generation

import org.scalatest.funsuite.AnyFunSuite

import java.nio.charset.StandardCharsets
import java.nio.file.Files

class UseRefExpansionSuite extends AnyFunSuite:

  // Issue #4: AllTechniqueHtmlGenerator does not expand <use ref> elements
  // This test documents the intended behavior. Currently fails on master because
  // paths ending in <use ref> are dropped instead of being expanded.
  ignore("hierarchy ending in use ref produces technique rows") {
    val sampleXml =
      """<?xml version="1.0"?>
        |<hierarchy title="test" imgpath="images/" date="01/01/2020" author="test">
        |  <node id="root" label="root" description="root technique">
        |    <node id="middle" label="middle" description="middle step">
        |      <use ref="leaf"/>
        |    </node>
        |  </node>
        |  <node id="leaf" label="leaf" description="leaf step"/>
        |</hierarchy>
        |""".stripMargin

    val document = TestXmlDocuments.parse(sampleXml)
    val out = Files.createTempFile("use-ref-test-", ".html")
    try
      new AllTechniqueHtmlGenerator(
        AllTechniqueConfig(debug = false, imageSize = 80, fontSize = 10, borderWidth = 0, showOnlyUniqueImages = false)
      ).generateAllElementsFromDom(document, out.toString)

      val html = Files.readString(out, StandardCharsets.UTF_8)
      
      // Expected: a complete technique row showing all three steps
      // (root, middle, and the referenced leaf step)
      assert(html.contains("root"), "Should show root node")
      assert(html.contains("middle"), "Should show middle node")
      assert(html.contains("leaf"), "Should expand use ref to show referenced leaf step")
    finally
      Files.deleteIfExists(out)
  }

  // Issue #4: AllTechniqueHtmlGenerator does not expand <use ref> elements
  ignore("multiple use refs in same hierarchy path") {
    val sampleXml =
      """<?xml version="1.0"?>
        |<hierarchy title="test" imgpath="images/" date="01/01/2020" author="test">
        |  <node id="start" label="start" description="start">
        |    <node id="step1" label="step1" description="first step">
        |      <use ref="finish"/>
        |    </node>
        |    <node id="step2" label="step2" description="second step">
        |      <use ref="finish"/>
        |    </node>
        |  </node>
        |  <node id="finish" label="finish" description="finish step"/>
        |</hierarchy>
        |""".stripMargin

    val document = TestXmlDocuments.parse(sampleXml)
    val out = Files.createTempFile("use-ref-multiple-", ".html")
    try
      new AllTechniqueHtmlGenerator(
        AllTechniqueConfig(debug = false, imageSize = 80, fontSize = 10, borderWidth = 0, showOnlyUniqueImages = false)
      ).generateAllElementsFromDom(document, out.toString)

      val html = Files.readString(out, StandardCharsets.UTF_8)
      
      // Both paths (start->step1->finish and start->step2->finish) should appear
      assert(html.contains("start"), "Should show start node")
      assert(html.contains("step1") && html.contains("step2"),
        "Should show both step1 and step2 nodes")
      assert(html.contains("finish"), "Should expand use refs to show finish step in both paths")
    finally
      Files.deleteIfExists(out)
  }

  test("use ref to non-existent node does not crash") {
    val sampleXml =
      """<?xml version="1.0"?>
        |<hierarchy title="test" imgpath="images/" date="01/01/2020" author="test">
        |  <node id="start" label="start" description="start">
        |    <use ref="nonexistent"/>
        |  </node>
        |</hierarchy>
        |""".stripMargin

    val document = TestXmlDocuments.parse(sampleXml)
    val out = Files.createTempFile("use-ref-bad-", ".html")
    try
      // This should not crash, even with a broken reference
      new AllTechniqueHtmlGenerator(
        AllTechniqueConfig(debug = false, imageSize = 80, fontSize = 10, borderWidth = 0, showOnlyUniqueImages = false)
      ).generateAllElementsFromDom(document, out.toString)

      val html = Files.readString(out, StandardCharsets.UTF_8)
      // Generator completes successfully even with broken reference
      assert(html.contains("<!DOCTYPE html>"), "Should generate valid HTML structure")
    finally
      Files.deleteIfExists(out)
  }
