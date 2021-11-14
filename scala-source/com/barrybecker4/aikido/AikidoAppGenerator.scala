/** Copyright by Barry G. Becker, 2004-2014. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.aikido

import com.barrybecker4.aikido.generation.AllTechniqueConfig
import com.barrybecker4.aikido.generation.AllTechniqueHtmlGenerator
import com.barrybecker4.aikido.generation.AppHtmlGenerator
import com.barrybecker4.common.util.FileUtil
import com.barrybecker4.common.xml.DomUtil
import org.w3c.dom.Document
import java.io.File
import java.io.IOException


/**
  * Instructions for creating the Aikido Technique Builder app:
  *   1. fill in the <aikido_technique>.xml file. Its dtd is hierarchy.dtd.  It assumes one root.
  *   2. Take pictures corresponding to nodes in the hierarchy using a digital camera.
  * Store the images in deployment/images/katate_dori (or whichever attack)
  *   3. Run this program on the xml file to generate technique_builder.html
  * and all_techniques.html in deployment/.
  *   4. upload technique_builder.html, all_techniques.html and corresponding images to website.
  *
  * Some interesting links for kubi-shime (also known as ude gurame)
  *  - https://www.youtube.com/watch?v=05hBVD0tHgg
  *  - https://www.youtube.com/watch?v=PgLdErLByRs
  *  - https://www.youtube.com/watch?v=gLijUiaSm2E
  *
  * Features to add:
  *   - show video for as many leaf nodes as possible. Hard because of ref nodes that represent subtrees.
  *   - add more descriptions
  * @author Barry Becker
  */
object AikidoAppGenerator {

  private val DEFAULT_INPUT_FILE = AppHtmlGenerator.DATA_DIR + "techniques.xml"

  /** A self contained and transferable location. */
  private val RESULT_PATH = FileUtil.getHomeDir + "deployment/"

  //private val IMAGE_PATH = FileUtil.getHomeDir + "images"

  /** the builder DHTML application */
  private val RESULT_BUILDER_FILE = RESULT_PATH + "technique_builder.html"

  /** all the techniques in one file (for debugging mostly) */
  private val RESULT_ALL_FILE = RESULT_PATH + "all_techniques.html"
  private val RESULT_UNIQUE_FILE = RESULT_PATH + "all_unique.html"

  /** Auto generate the html app based on the XML file.
    * @param document xml document model of techniques
    * @param fileName the tile that will be written to
    */
  @throws[IOException]
  def generateHTMLAppFromDom(document: Document, fileName: String): Unit = {
    new AppHtmlGenerator().generateHTMLApp(document, fileName)
  }

  def main(args: Array[String]): Unit = {
    var document: Document = null
    var filename = DEFAULT_INPUT_FILE
    if (args.length == 1) filename = args(0)
    else {
      println("Usage: <xml file containing data>")
      println("Since no argument was supplied, " + filename + " will be used.")
    }
    val file = new File(filename)
    println("parsing xml from " + file)
    document = DomUtil.parseXMLFile(file, replaceUseWithDeepCopy = true)
    try {
      generateHTMLAppFromDom(document, RESULT_BUILDER_FILE)
      var config = new AllTechniqueConfig(false, 160, 10, 0, false)
      new AllTechniqueHtmlGenerator(config).generateAllElementsFromDom(document, RESULT_ALL_FILE)
      config = new AllTechniqueConfig(true, 100, 9, 0, true)
      new AllTechniqueHtmlGenerator(config).generateAllElementsFromDom(document, RESULT_UNIQUE_FILE)
    } catch {
      case e: IOException => e.printStackTrace()
    }
  }

}
