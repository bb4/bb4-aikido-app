// Copyright by Barry G. Becker, 2014 - 2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.aikido.generation

import org.w3c.dom.NamedNodeMap
import org.w3c.dom.Node


object NodeInfo {
  private val IMG_SUFFIX = ".jpg"
}

/**
  * Represents a XML element (i.e. a step in the technique) in the document object model (DOM).
  * @author Barry Becker
  */
class NodeInfo private[generation](val imagePath: String, val node: Node) {
  val attributeMap: NamedNodeMap = node.getAttributes

  private var id: String = _
  private var image: String = _
  private var label: String = _
  private var description: String = _

  if (attributeMap == null || !("node" == node.getNodeName)) {
    id = null
    image = null
    label = null
  }
  else {
    var i = 0
    while (i < attributeMap.getLength) {
      val attr = attributeMap.item(i)
      val name = attr.getNodeName
      name match {
        case "id" =>
          id = attr.getNodeValue
          // the id gets reused for the image name
          image = imagePath + attr.getNodeValue + NodeInfo.IMG_SUFFIX
        case "label" =>
          label = attr.getNodeValue
        case "description" =>
          description = attr.getNodeValue
        case _ => throw new IllegalArgumentException("Unexpected attribute '" + name + "' on element.")
      }
      i += 1
    }
  }

  private[generation] def getId = id
  private[generation] def getImage = image
  private[generation] def getLabel = label

  /** @return the description, or label if no description was provided. */
  private[generation] def getDescription =
    if (description != null) description else label
}