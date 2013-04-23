package binders

import play.api.mvc.PathBindable
import org.bson.types.ObjectId

object Binders {

  implicit def objectIdPathBindable = new PathBindable[ObjectId] {

    def bind(key: String, value: String): Either[String, ObjectId] = {
      try {
        Right(new ObjectId(value))
      } catch {
        case e: Exception => Left("Cannot parse parameter %s as ObjectId: %s".format(key, e.getMessage))
      }
    }

    def unbind(key: String, objectId: ObjectId): String = objectId.toString
  }
}
