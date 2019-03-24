// Copyright by Barry G. Becker, 2014 - 2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.aikido.generation

import com.barrybecker4.common.xml.DomUtil
import org.w3c.dom.Document
import org.w3c.dom.Node


/**
  * Converts from the xml file containing all the techniques to javascript data structures.
  * @author Barry Becker
  */
class XmlToJsConverter {

  /** Set up the javascript array structures based on the xml technique hierarchy.
    * @param document the DOM
    * @return javascript string
    */
    def generateJavaScript(document: Document): String = {
      val buf = new StringBuilder
      buf.append('\n')
      buf.append("  // setup structures for grammar\n")
      buf.append("  var attacks = new Array();\n")
      buf.append("  var next = new Array();\n")
      buf.append("  var img = new Array();\n")
      buf.append("  var label = new Array();\n\n")
      buf.append("  var desc = new Array();\n\n")
      val root = document.getDocumentElement
      val imgPath = DomUtil.getAttribute(root, "imgpath")
      val children = root.getChildNodes
      var i = 0
      while (i < children.getLength) {
        val child = children.item(i)
        val nodeInfo = new NodeInfo(imgPath, child)
        buf.append("  attacks[").append(i).append("]='").append(nodeInfo.getId).append("';\n")
        i += 1
      }
      buf.append(genJSForNode(root, imgPath))
      buf.append('\n')
      buf.toString
    }

  /**
    * Recursively generate the javascript structures.
    * @param node node representing a step in the technique.
    */
  private def genJSForNode(node: Node, imgPath: String): String = {
    // first print the img and label for the node, then next pointers for all children,
    // then do the same for all its children
    val buf = new StringBuilder
    val nodeInfo = new NodeInfo(imgPath, node)
    if (nodeInfo.getId == null) System.out.println("null id for " + node.getNodeName + ' ' + node.getNodeValue)
    val children = node.getChildNodes
    val id = nodeInfo.getId
    if (id != null) {
      buf.append("  img[\"").append(id).append("\"]=\"").append(nodeInfo.getImage).append("\";\n")
      buf.append("  label[\"").append(id).append("\"]=\"").append(nodeInfo.getLabel).append("\";\n")
      buf.append("  desc[\"").append(id).append("\"]=\"").append(nodeInfo.getDescription).append("\";\n\n")
      val len = children.getLength
      if (len > 0) buf.append("  next[\"").append(nodeInfo.getId).append("\"]= new Array();\n")
      var i = 0
      while (i < len) {
        val child = children.item(i)
        buf.append("  next[\"").append(id).append("\"][").append(i).append("]=\"")
          .append(DomUtil.getAttribute(child, "id")).append("\";\n")
        i += 1
      }
      if (len > 0) buf.append('\n')
    }
    var i = 0
    while (i < children.getLength) {
      val child = children.item(i)
      buf.append(genJSForNode(child, imgPath))
      i += 1
    }
    buf.toString
  }
}
