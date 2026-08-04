// Copyright by Barry G. Becker, 2014 - 2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.aikido.generation

import org.w3c.dom.NamedNodeMap
import org.w3c.dom.Node


object NodeInfo:
  private val IMG_SUFFIX = ".jpg"

  private case class Parsed(
      id: String | Null,
      image: String | Null,
      label: String | Null,
      description: String | Null
  )

  private def parseAttributes(imagePath: String, node: Node): Parsed =
    val attributeMap: NamedNodeMap | Null = node.getAttributes
    if attributeMap == null || node.getNodeName != "node" then
      Parsed(null, null, null, null)
    else
      (0 until attributeMap.getLength).foldLeft(Parsed(null, null, null, null)) { (acc, i) =>
        val attr = attributeMap.item(i)
        val name = attr.getNodeName
        val value = attr.getNodeValue
        name match
          case "id" =>
            acc.copy(id = value, image = imagePath + value + IMG_SUFFIX)
          case "label" =>
            acc.copy(label = value)
          case "description" =>
            acc.copy(description = value)
          case _ =>
            throw IllegalArgumentException(s"Unexpected attribute '$name' on element.")
      }

/**
  * Represents a XML element (i.e. a step in the technique) in the document object model (DOM).
  * @author Barry Becker
  */
class NodeInfo private[generation](val imagePath: String, val node: Node):

  private val p: NodeInfo.Parsed = NodeInfo.parseAttributes(imagePath, node)

  private[generation] def getId: String | Null = p.id
  private[generation] def getImage: String | Null = p.image
  private[generation] def getLabel: String | Null = p.label

  /** @return the description, or label if no description was provided. */
  private[generation] def getDescription: String | Null =
    if p.description != null then p.description else p.label
