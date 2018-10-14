// Copyright by Barry G. Becker, 2014 - 2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.aikido.generation

/**
  * Some options for how the "All Techniques" page is shown.
  * @author Barry Becker
  */
class AllTechniqueConfig() {

  /** If in debug mode, then we do the following things differently
    * 1) in the all techniques page, show the ids instead of the cut-points, and make the images bigger.
    * 2) when replacing refs, don't substitute the whole subtree, just the subtree root node.
    */
  private[generation] var debug = false
  private[generation] var showImages = true
  private[generation] var showOnlyUniqueImages = false
  private[generation] var fontSize = 9
  private[generation] var borderWidth = 1
  private[generation] var imageSize = 80

  def this(debug: Boolean, imageSize: Int, fontSize: Int, borderWidth: Int, showOnlyUniqueImages: Boolean) {
    this()
    this.debug = debug
    this.showImages = imageSize > 0
    this.imageSize = imageSize
    this.fontSize = fontSize
    this.borderWidth = borderWidth
    this.showOnlyUniqueImages = showOnlyUniqueImages
  }
}
