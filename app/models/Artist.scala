package models

import java.util.UUID

case class Artist(
    id: UUID,
    name: String,
    phone: String,
    email: String,
    admin_id: UUID,
) {}

object Artist {
  import play.api.libs.json._

  implicit val artistWrites: OWrites[Artist] = Json.writes[Artist]
}