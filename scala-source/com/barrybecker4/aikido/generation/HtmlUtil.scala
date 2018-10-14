// Copyright by Barry G. Becker, 2014 - 2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.aikido.generation

/**
  * @author Barry Becker
  */
object HtmlUtil {
  def getHTMLHead(title: String): String =
      "<!DOCTYPE>\n" +
      "<html>\n" +
      "<head>\n" +
      "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
      "<title>" + title + "</title>" + "\n\n"

  def getScriptOpen = "<script language=\"JavaScript\">"
  def getScriptClose = "</script>"
}