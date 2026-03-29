// Copyright by Barry G. Becker, 2014 - 2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.aikido.generation

import com.barrybecker4.common.xml.DomUtil
import org.w3c.dom.Document
import org.w3c.dom.Node

/**
  * Converts from the xml file containing all the techniques to javascript data structures.
  * @author Barry Becker
  */
class XmlToJsConverter:

  /** Set up the javascript array structures based on the xml technique hierarchy.
    * @param document the DOM
    * @return javascript string
    */
  def generateJavaScript(document: Document): String =
    val buf = new StringBuilder
    buf.append('\n')
    buf.append("  // setup structures for grammar\n")
    buf.append("  var attacks = [];\n")
    buf.append("  var next = [];\n")
    buf.append("  var img = [];\n")
    buf.append("  var label = [];\n\n")
    buf.append("  var desc = [];\n\n")
    val root = document.getDocumentElement
    val imgPath = DomUtil.getAttribute(root, "imgpath")
    val rootElems = DomNodeUtil.elementChildren(root)
    for i <- rootElems.indices do
      val refId = DomNodeUtil.childReferenceId(rootElems(i))
      buf.append("  attacks[").append(i).append("]='")
        .append(GenerationEscape.escapeJsString(refId)).append("';\n")
    buf.append(genJSForNode(root, imgPath))
    buf.append('\n')
    buf.toString

  private def genJSForNode(node: Node, imgPath: String): String =
    val buf = new StringBuilder
    val kids = DomNodeUtil.elementChildren(node)
    val nodeInfo = new NodeInfo(imgPath, node)
    val id = nodeInfo.getId
    if id != null then
      buf.append(emitNodeBindings(nodeInfo, id))
      buf.append(emitNextArray(id, kids))
    for child <- kids do
      buf.append(genJSForNode(child, imgPath))
    buf.toString

  private def emitNodeBindings(nodeInfo: NodeInfo, id: String): String =
    val b = new StringBuilder
    b.append("  img[\"").append(GenerationEscape.escapeJsString(id)).append("\"]=\"")
      .append(GenerationEscape.escapeJsString(nodeInfo.getImage)).append("\";\n")
    b.append("  label[\"").append(GenerationEscape.escapeJsString(id)).append("\"]=\"")
      .append(GenerationEscape.escapeJsString(nodeInfo.getLabel)).append("\";\n")
    b.append("  desc[\"").append(GenerationEscape.escapeJsString(id)).append("\"]=\"")
      .append(GenerationEscape.escapeJsString(nodeInfo.getDescription)).append("\";\n\n")
    b.toString

  private def emitNextArray(parentId: String, elementChildren: Seq[Node]): String =
    if elementChildren.isEmpty then ""
    else
      val b = new StringBuilder
      b.append("  next[\"").append(GenerationEscape.escapeJsString(parentId)).append("\"]= new Array();\n")
      for (child, i) <- elementChildren.zipWithIndex do
        val childId = DomNodeUtil.childReferenceId(child)
        b.append("  next[\"").append(GenerationEscape.escapeJsString(parentId)).append("\"][")
          .append(i).append("]=\"")
          .append(GenerationEscape.escapeJsString(childId)).append("\";\n")
      b.append('\n')
      b.toString
