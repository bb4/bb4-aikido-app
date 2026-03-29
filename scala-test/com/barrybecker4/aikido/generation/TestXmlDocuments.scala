package com.barrybecker4.aikido.generation

import org.w3c.dom.Document

import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory
import org.xml.sax.InputSource

object TestXmlDocuments:

  def parse(xml: String): Document =
    val factory = DocumentBuilderFactory.newInstance()
    factory.setNamespaceAware(false)
    val builder = factory.newDocumentBuilder()
    builder.parse(InputSource(StringReader(xml.trim)))
