package services

import com.google.inject.Inject
import models.Artist.ArtistSessionsMonth
import models.{Artist, SessionTattoo}
import models.Artist

import scala.concurrent.{ExecutionContext, Future}

class ReportService @Inject() (dbService: DBService, artistAppointmentService: ArtistAppointmentService, artistService: ArtistService)(implicit
    ec: ExecutionContext
) {

  import dbService._
  import dbService.api._

  def topArtistByNumberOfSessions(): Any = {
  def topArtistByNumberOfSessions(): Future[ArtistSessionsMonth] = {
    for {
      artists <- artistService.listArtists
      artistsWithSessions <-
        Future.sequence(artists.map(artist => artistAppointmentService.listSessionsByArtistId(artist.id).map(sessions => (artist, sessions))))
      countSessionByArtist = artistsWithSessions.map(artistWithSession => (artistWithSession._1, artistWithSession._2.size)).toMap
      artistNameSession = countSessionByArtist.map(artistSession => (artistSession._1.name, artistSession._2))
    } yield ArtistSessionsMonth(artistNameSession)
  }


    } yield artistsWithSessions
  }

  def topArtistByWorkedHours(): Future[Seq[(Artist, Double)]] =
    artistService.listArtists.flatMap { artists =>
      Future.sequence(artists.map { artist =>
        artistAppointmentService.listSessionsByArtistId(artist.id).map { sessions =>
          (artist, sessions.map(_.estimated_time).sum)
        }
      })
    }
}
