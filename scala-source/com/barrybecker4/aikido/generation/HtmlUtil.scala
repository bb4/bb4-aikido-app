// Copyright by Barry G. Becker, 2014 - 2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.aikido.generation

/**
  * Helpers for emitting modern HTML document scaffolding.
  * @author Barry Becker
  */
object HtmlUtil:

  def getHTMLHead(title: String): String =
    val t = GenerationEscape.escapeHtmlText(title)
    "<!DOCTYPE html>\n" +
      "<html lang=\"en\">\n" +
      "<head>\n" +
      "<meta charset=\"utf-8\">\n" +
      "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
      "<link rel=\"stylesheet\" href=\"aikido.css\">\n" +
      "<title>" + t + "</title>\n\n"

  def getScriptOpen: String = "<script>"

  def getScriptClose: String = "</script>"
