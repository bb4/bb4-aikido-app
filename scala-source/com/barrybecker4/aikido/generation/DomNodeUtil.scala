// Copyright by Barry G. Becker, 2014 - 2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.aikido.generation

import com.barrybecker4.common.xml.DomUtil
import org.w3c.dom.Node

/** DOM helpers: element-only child iteration (ignores whitespace #text) and step id resolution for `node` / `use`. */
private[generation] object DomNodeUtil:

  def elementChildren(node: Node): IndexedSeq[Node] =
    val children = node.getChildNodes
    (0 until children.getLength)
      .map(children.item)
      .filter(_.getNodeType == Node.ELEMENT_NODE)

  /** Target step id for `next[]` / `attacks[]`: `node` uses `id`, `use` uses `ref`. */
  def childReferenceId(child: Node): String =
    child.getNodeName match
      case "use"   => DomUtil.getAttribute(child, "ref")
      case "node"  => DomUtil.getAttribute(child, "id")
      case _       => null
