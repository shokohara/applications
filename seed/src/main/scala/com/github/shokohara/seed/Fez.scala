package com.github.shokohara.seed

import java.awt.{AWTException, Rectangle, Robot, Toolkit}
import java.io.{File, IOException, InputStream}

import better.files._
import javax.imageio.ImageIO

object Fez {

  val r = new Robot
  val capture = new Rectangle(Toolkit.getDefaultToolkit.getScreenSize)

  def main(args: Array[String]): Unit = {
    println(Resource.url("0331.wav"))
    val in: InputStream = Resource.getAsStream("button57.wav")
    import javax.sound.sampled.AudioSystem
    val clip = AudioSystem.getClip
    val inputStream = AudioSystem.getAudioInputStream(in)

//    while (true) {
    try {
      Thread.sleep(120)
      val ext = "png"
      val path = s"Screenshot.$ext"
      val Image = r.createScreenCapture(capture)
      ImageIO.write(Image, ext, new File(path))
      clip.open(inputStream)
      clip.start
    } catch {
      case ex @ (_: AWTException | _: IOException | _: InterruptedException) => ex.printStackTrace()
    }
    Thread.sleep(10000)
//    }
  }
}
