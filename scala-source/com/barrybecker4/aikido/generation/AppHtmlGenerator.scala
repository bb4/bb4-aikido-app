// Copyright by Barry G. Becker, 2014. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.aikido.generation

import com.barrybecker4.common.util.FileUtil._
import org.w3c.dom.Document
import java.io.FileOutputStream
import java.io.IOException


/**
  * @author Barry Becker
  */
object AppHtmlGenerator {
  /** location where data files are read from */
  val PROJECT_DIR: String = getHomeDir + "scala-source/com/barrybecker4/aikido/generation/"
  private val JAVASCRIPT_FILE = PROJECT_DIR + "methods.js"
  private val BODY_HTML_FILE = PROJECT_DIR + "body.html"
}

class AppHtmlGenerator() {

  /** Auto generate the html app based on the XML file.
    * @param document xml document model containing all techniques
    * @param fileName name of the XML config file to read.
    */
  @throws[IOException]
  def generateHTMLApp(document: Document, fileName: String): Unit = {
    println("file = " + fileName + " doc = " + document)
    val fos = new FileOutputStream(fileName)
    fos.write(HtmlUtil.getHTMLHead("Aikido Technique Builder").getBytes)
    fos.write(HtmlUtil.getScriptOpen.getBytes)
    fos.write(new XmlToJsConverter().generateJavaScript(document).getBytes)
    fos.write(readTextFile(AppHtmlGenerator.JAVASCRIPT_FILE).getBytes)
    fos.write(HtmlUtil.getScriptClose.getBytes)
    fos.write("</head>\n".getBytes)
    fos.write(readTextFile(AppHtmlGenerator.BODY_HTML_FILE).getBytes)
    fos.write("</html>\n".getBytes)
    fos.close()
  }
}