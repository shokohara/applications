package controllers

import buildinfo.BuildInfo
import io.circe.syntax._
import play.api.libs.circe.Circe
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

class BuildInfoController(cc: ControllerComponents)(implicit val ec: ExecutionContext)
  extends AbstractController(cc) with Circe {

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.ToString"))
  def index =
    Action {
      Ok(BuildInfo.toMap.view.mapValues(_.toString).toMap.asJson)
    }
}
