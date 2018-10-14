// Copyright by Barry G. Becker, 2014 - 2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.aikido.generation

import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException


/**
  * Move this to bb4-common and use that instead
  * @author Barry Becker
  */
object LocalFileUtil {

  /** @param filename name of file to read from
    * @return text within the file
    * @throws IllegalStateException if could not read the file
    */
  def readTextFile(filename: String): String = {
    var br: BufferedReader = null
    val bldr = new StringBuilder(1000)
    try {
      br = new BufferedReader(new FileReader(filename))
      var currentLine: String = br.readLine
      while (currentLine != null) {
        bldr.append(currentLine).append('\n')
        currentLine = br.readLine
      }
    } catch {
      case e: IOException => throw new IllegalStateException("Could not read " + filename, e)
    } finally {
      if (br != null) br.close()
    }
    bldr.toString
  }
}