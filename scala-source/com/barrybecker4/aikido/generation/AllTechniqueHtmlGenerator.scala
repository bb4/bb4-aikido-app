// Copyright by Barry G. Becker, 2014 - 2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.aikido.generation

import com.barrybecker4.common.xml.DomUtil
import org.w3c.dom.Document
import org.w3c.dom.Node

import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets
import scala.collection.mutable
import scala.util.Using

/**
  * Generate expressions for all possible statements that the grammar can produce
  * @param config provides optional configuration options to use when creating the page
  * @author Barry Becker
  */
class AllTechniqueHtmlGenerator(val config: AllTechniqueConfig = AllTechniqueConfig()):

  private val imageSet = mutable.HashSet.empty[String]
  private var imagesShown = 0
  private var potentialImages = 0
  private var rowNumber = 1

  /** Auto generate all elements based on the XML file. Throws IOException if error writing to the specified file.
    * @param document contains all the techniques in XML.
    * @param fileName file to write to.
    */
  @throws[IOException]
  def generateAllElementsFromDom(document: Document, fileName: String): Unit =
    Using.resource(new FileOutputStream(fileName)) { fos =>
      fos.write(HtmlUtil.getHTMLHead("All Aikido Techniques (from katate dori)").getBytes(StandardCharsets.UTF_8))
      fos.write("</head>\n".getBytes(StandardCharsets.UTF_8))
      fos.write(getAllBody(document).getBytes(StandardCharsets.UTF_8))
      fos.write("</html>\n".getBytes(StandardCharsets.UTF_8))
    }

  /** @param document document object model of all techniques
    * @return html body containing a table of all the techniques.
    */
  private def getAllBody(document: Document): String =
    "<body>\n" + "<big><big style=\"font-weight: bold; text-decoration: underline;\">Aikido\n" +
      "Techniques</big></big><br>\n" +
      "<br>\n" +
      "This page contains all traditional unarmed standing aikido techniques.<br> " +
      "<font size='-1'>This application was built using XML, java and DHTML " +
      "(<a href='technique_builder_desc.html'>more details</a>).</font> " +
      "<br><br>\n\n" +
      "<table id='outerTable' border=\"0\">\n" +
      "  <tr>\n" +
      "    <td>\n" +
      "      <div style=\"width:100%; overflow: auto; font-family:arial; font-size:" + config.fontSize + ";\">\n\n" +
      getTechniqueTable(document) +
      "      </div>\n" +
      "    </td>\n" +
      "  </tr>\n" +
      "</table> \n\n" +
      "<br>\n" +
      "<div>Total number of images shown = " + imagesShown + " (out of " + potentialImages + " possible)</div>\n" +
      "</body> \n"

  /** @return table containing all techniques. Each row may have a different number of cells. */
  private def getTechniqueTable(document: Document): String =
    val buf = new StringBuilder
    val parentList = mutable.ArrayBuffer.empty[NodeInfo]
    buf.append("<table id='techniqueTable' width=\"100%\" cellspacing=\"1\" cellpadding=\"2\" border=\"")
      .append(config.borderWidth).append("\">\n")
    val root = document.getDocumentElement
    val imgPath = DomUtil.getAttribute(root, "imgpath")
    buf.append(genRowForNode(root, parentList, imgPath))
    buf.append("</table>\n\n")
    buf.toString

  /** @return html for the row representing the technique. Each cell (td) in the row is a different step. */
  private def genRowForNode(node: Node, parentList: mutable.ArrayBuffer[NodeInfo], imgPath: String): String =
    val buf = new StringBuilder
    val nodeInfo = new NodeInfo(imgPath, node)
    if nodeInfo.getId == null && config.debug then
      System.out.println("null id for " + node.getNodeName + ' ' + node.getNodeValue)
    parentList += nodeInfo
    val elemChildren = DomNodeUtil.elementChildren(node)
    if elemChildren.isEmpty && nodeInfo.getId != null then
      techniqueStepsRow(parentList, buf)
      if config.showImages then thumbnailImageRow(parentList, buf)
    for child <- elemChildren do
      buf.append(genRowForNode(child, parentList, imgPath))
    parentList.remove(parentList.length - 1)
    buf.toString

  /** Write the row of titles */
  private def techniqueStepsRow(parentList: mutable.ArrayBuffer[NodeInfo], buf: StringBuilder): Unit =
    buf.append("  <tr style=\"white-space:nowrap; font-size:").append(config.fontSize).append("\">\n")
    var i = 1
    buf.append("    <td>" + rowNumber + "</td>")
    rowNumber += 1

    while i < parentList.length do
      val info = parentList(i)
      val label = if config.debug then info.getId else info.getLabel
      buf.append("    <td title=\"")
        .append(GenerationEscape.escapeHtmlAttribute(info.getDescription))
        .append("\" style=\"width:50px; max-width:150px; height:20; overflow:hidden;\">\n")
        .append(GenerationEscape.escapeHtmlText(label))
      buf.append("    </td>\n")
      i += 1
    buf.append("  </tr>\n")

  /** Write the row of thumbnail images under the titles. */
  private def thumbnailImageRow(parentList: mutable.ArrayBuffer[NodeInfo], buf: StringBuilder): Unit =
    buf.append("  <tr>\n")
    buf.append("    <td></td>")
    var i = 1
    while i < parentList.length do
      val info = parentList(i)
      buf.append("    <td>\n")
      if !config.showOnlyUniqueImages || !imageSet.contains(info.getId) then
        buf.append("      <img src=\"")
          .append(GenerationEscape.escapeHtmlAttribute(info.getImage))
          .append("\" height=\"" + config.imageSize + "\" title=\"")
          .append(GenerationEscape.escapeHtmlAttribute(info.getDescription)).append("\">\n")
        imageSet += info.getId
        imagesShown += 1
      potentialImages += 1
      buf.append("    </td>\n")
      i += 1
    buf.append("  </tr>\n")
