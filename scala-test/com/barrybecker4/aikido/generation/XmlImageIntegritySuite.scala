package com.barrybecker4.aikido.generation

import com.barrybecker4.common.xml.DomUtil
import org.scalatest.funsuite.AnyFunSuite

import java.nio.file.{Files, Path, Paths}
import javax.xml.parsers.DocumentBuilderFactory
import scala.collection.mutable

class XmlImageIntegritySuite extends AnyFunSuite:

  private val techniquesXml = Paths.get("deployment/techniques/techniques.xml")
  private val imagesDir = Paths.get("deployment/techniques/images")

  test("techniques.xml and deployment/techniques/images exist") {
    assert(Files.exists(techniquesXml), s"techniques.xml not found at $techniquesXml")
    assert(Files.isDirectory(imagesDir), s"images directory not found at $imagesDir")
  }

  test("illegal image filenames: spaces and double jpg extensions") {
    assume(Files.isDirectory(imagesDir))
    
    val illegalFiles = mutable.ArrayBuffer.empty[String]
    val stream = Files.list(imagesDir)
    try
      stream.forEach { path =>
        val name = path.getFileName.toString
        if name.contains(" ") then
          illegalFiles += s"$name (contains space)"
        if name.endsWith(".jpg.jpg") then
          illegalFiles += s"$name (double .jpg extension)"
      }
    finally
      stream.close()
    
    if illegalFiles.nonEmpty then
      fail(s"Found ${illegalFiles.size} illegal image filename(s):\n  " + illegalFiles.mkString("\n  "))
  }

  test("orphan images: files not referenced by any node id") {
    assume(Files.exists(techniquesXml) && Files.isDirectory(imagesDir))
    
    val factory = DocumentBuilderFactory.newInstance()
    val builder = factory.newDocumentBuilder()
    val document = builder.parse(techniquesXml.toFile)
    val root = document.getDocumentElement
    
    val referencedIds = collectAllNodeIds(root)
    
    val imageFiles = mutable.ArrayBuffer.empty[String]
    val stream = Files.list(imagesDir)
    try
      stream.forEach { path =>
        val name = path.getFileName.toString
        if name.endsWith(".jpg") then
          val idFromFile = name.stripSuffix(".jpg")
          imageFiles += idFromFile
      }
    finally
      stream.close()
    
    val orphans = imageFiles.filterNot(referencedIds.contains)
    
    if orphans.nonEmpty then
      info(s"Found ${orphans.size} orphan image(s) (not referenced by any node id):\n  " +
        orphans.sorted.take(10).mkString("\n  ") +
        (if orphans.size > 10 then s"\n  ... and ${orphans.size - 10} more" else ""))
  }

  private def collectAllNodeIds(node: org.w3c.dom.Node): Set[String] =
    val ids = mutable.HashSet.empty[String]
    if node.getNodeType == org.w3c.dom.Node.ELEMENT_NODE && node.getNodeName == "node" then
      val attrs = node.getAttributes
      if attrs != null then
        val idAttr = attrs.getNamedItem("id")
        if idAttr != null then
          ids += idAttr.getNodeValue
    
    val children = node.getChildNodes
    var i = 0
    while i < children.getLength do
      ids ++= collectAllNodeIds(children.item(i))
      i += 1
    
    ids.toSet
