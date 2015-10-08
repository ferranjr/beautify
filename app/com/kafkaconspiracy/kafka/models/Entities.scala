package com.kafkaconspiracy.kafka.models

import play.api.libs.json.{Json, Format}


case class Picture(
                    id: String,
                    picture: String,
                    takenTime: Long,
                    barcode: Option[String],
                    ride: Option[String],
                    effects: Option[List[String]]
                  )
object Picture {
  implicit val formatPicture:Format[Picture] = Json.format[Picture]
}

case class CompPicture(
                    id: String,
                    picture: String,
                    takenTime: Long,
                    barcode: Option[String],
                    ride: Option[String],
                    effects: Option[List[String]],
                    origUUID: String
                  )

object CompPicture {
  implicit val formatCompPicture:Format[CompPicture] = Json.format[CompPicture]
}