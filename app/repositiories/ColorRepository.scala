package repositiories

import javax.inject.Inject
import models.Color
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ Future, ExecutionContext }

class ColorRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class ColorTable(tag: Tag) extends Table[Color](tag, "Color") {
    def name = column[String]("name")

    def * = (name) <> (Color.apply, Color.unapply)
  }

  private val colors = TableQuery[ColorTable]

  def listAll: Future[Seq[Color]] = db.run {
    colors.result
  }

  def findByName(name: String): Future[Option[Color]] = db.run {
    colors.filter(_.name.toUpperCase === name.toUpperCase).result.headOption
  }
}
