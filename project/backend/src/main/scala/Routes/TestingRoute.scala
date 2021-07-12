package Routes

import spray.json.DefaultJsonProtocol

import scala.language.postfixOps

case class Account(pin: Int, name: String)

trait AccountJsonProtocol extends DefaultJsonProtocol {
  implicit val accountJson = jsonFormat2(Account)
}
