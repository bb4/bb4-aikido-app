// Copyright by Barry G. Becker, 2014 - 2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.aikido.generation

import com.barrybecker4.xml.DomUtil
import org.w3c.dom.Node

import scala.collection.mutable

/** DOM helpers: element-only child iteration (ignores whitespace #text) and step id resolution for `node` / `use`. */
private[generation] object DomNodeUtil:

  def elementChildren(node: Node): IndexedSeq[Node] =
    val children = node.getChildNodes
    val buf = mutable.ArrayBuffer.empty[Node]
    var i = 0
    while i < children.getLength do
      val c = children.item(i)
      if c.getNodeType == Node.ELEMENT_NODE then buf += c
      i += 1
    buf.toIndexedSeq

  /** Target step id for `next[]` / `attacks[]`: `node` uses `id`, `use` uses `ref`. */
  def childReferenceId(child: Node): String =
    child.getNodeName match
      case "use"   => DomUtil.getAttribute(child, "ref")
      case "node"  => DomUtil.getAttribute(child, "id")
      case _       => null
