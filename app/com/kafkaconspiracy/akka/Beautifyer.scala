package com.kafkaconspiracy.akka

import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO

import akka.actor.Actor
import akka.actor.Actor.Receive
import com.kafkaconspiracy.kafka.models.Picture
import com.kafkaconspiracy.kafka.tools.Base64._
import org.bytedeco.javacpp.opencv_core._
import org.bytedeco.javacpp.opencv_imgcodecs._
import org.bytedeco.javacpp.opencv_imgproc._
import org.bytedeco.javacpp.opencv_objdetect._
import org.bytedeco.javacv.OpenCVFrameConverter.ToMat
import org.bytedeco.javacv.{Java2DFrameConverter,OpenCVFrameConverter}
import play.api.Logger


class Beautifyer extends Actor {

  import Beautifyer._

  /**
   * Adds overlay to Received Image
   *
   *  - STEP 1. Decode Image Base64
   *  - STEP 2. Add Overlay
   *  - STEP 3. Convert to Image Base64
   *
   * @return
   */
  def receive: Receive = {
    case p@Picture(id, _, takenTime, _, _,_) => beautify(p)
    case _ => Logger.logger.debug(s"Beautifyer : Wot?")
  }
}

object Beautifyer {

  def beautify(pic: Picture):Unit = {

    val stringImage = "/9j/4AAQSkZJRgABAQIAHAAcAAD/2wBDAP//////////////////////////////////////////////////////////////////////////////////////2wBDAf//////////////////////////////////////////////////////////////////////////////////////wAARCACAAIADASIAAhEBAxEB/8QAFwABAQEBAAAAAAAAAAAAAAAAAAECA//EACYQAQABAgUDBQEBAAAAAAAAAAABETEhQVFh8HGB0QISobHBkeH/xAAVAQEBAAAAAAAAAAAAAAAAAAAAAf/EABQRAQAAAAAAAAAAAAAAAAAAAAD/2gAMAwEAAhEDEQA/AKAqAAAAAAAAAAKAAIAAAKi6AgAAAAAAAAAAAKM16qijSL4EZFFEFAQUBBQEAABQYhtmMmkUXwlFBABABQAAAAAAABmLw0xF4dEVms7fBXePgmPv9SccgXGdFEi8goAAAiViCsarNdZhKTrIFY1+J8Ldn27tRFMAAAYti17o3ZmzIqzPO4lJ0Wkg36ZrHdMK46npwwWlagolYprYrsCjKgozjqoKMxXkrzmIKJVKgk2/yuupr06ZRkuVJmnQrGUbVnlAIv2j7iczn12Ss5z2AK6FwAFQBYRQX6y8HOaH7ct+T5AReU8H86AyLSOkp7eUAAAAAAAAAFAIa2llpBm1+0rtP9ylbwzbCbKLa50LbwdAf//Z"

//    val bytes = pic.picture.toByteArray
    val bytes = stringImage.toByteArray
    val img: BufferedImage =  ImageIO.read(new ByteArrayInputStream(bytes))

    val imageRawMat = (new OpenCVFrameConverter.ToMat).convert((new Java2DFrameConverter).convert(img))

    //      new MatOfByte

//    Mat image = new Mat(height, width, CvType.CV_8UC3);
//    image.put(0, 0, pixelValues);
//    imageRawMat.put(0,0, bytes)

    val imageFilename = "data/skyfall.jpg"
    val overlayFilename = "data/doggy.png"

    val imageRaw = imread(imageFilename, CV_LOAD_IMAGE_COLOR)
    val mask = imread(overlayFilename, CV_LOAD_IMAGE_GRAYSCALE)
    val overlay = imread(overlayFilename, CV_LOAD_IMAGE_COLOR)

    val overlaySize = new Size(200, 200)

    val resizedOverlay = new Mat()
    resize(overlay, resizedOverlay, overlaySize)

    val resizedMask = new Mat()
    resize(mask, resizedMask, overlaySize)

    val imageROI = imageRaw(new Rect(imageRaw.cols - resizedOverlay.cols, imageRaw.rows - resizedOverlay.rows, resizedOverlay.cols, resizedOverlay.rows))
    resizedOverlay.copyTo(imageROI, resizedMask)


    // FACE DETECTION

    // Make it easier to detect
    // MAke it gray
    val greyMat = new Mat()
    cvtColor(imageRaw, greyMat, CV_BGR2GRAY, 1)

    // Histrogram equalization
    val equalizedMat = new Mat()
    equalizeHist(greyMat, equalizedMat)


    // load classifier
    val faceDetector = new CascadeClassifier("data/haarcascade_frontalface_alt.xml")
    val faceDetections = new RectVector()
    faceDetector.detectMultiScale(equalizedMat, faceDetections)

    for {
      i <- 0 until faceDetections.size().toInt
      rect = faceDetections.get(i)
    } {
      rectangle(imageRaw, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255))
    }
  }
}