// Copyright by Barry G. Becker, 2014 - 2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.aikido.generation

/**
  * Options for how the "All Techniques" page is shown.
  *
  * When `debug` is true:
  *   - on the "all techniques" page, show ids instead of cut-point labels and larger images;
  *   - when replacing refs, substitute only the subtree root (if applicable).
  *
  * @author Barry Becker
  */
case class AllTechniqueConfig(
    debug: Boolean = false,
    imageSize: Int = 80,
    fontSize: Int = 9,
    borderWidth: Int = 1,
    showOnlyUniqueImages: Boolean = false
):
  def showImages: Boolean = imageSize > 0
