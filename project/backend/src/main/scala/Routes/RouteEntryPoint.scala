package Routes
import CampRestful.Camp.CampRoute
import CampRestful.User.UserRoute

import scala.language.postfixOps
import akka.actor.ActorSystem
import akka.http.scaladsl.{ConnectionContext, Http}
import akka.stream.Materializer

import java.io.InputStream
import java.security.{KeyStore, SecureRandom}
import javax.net.ssl.{KeyManagerFactory, SSLContext, TrustManagerFactory}
import scala.io.StdIn
import akka.http.scaladsl.server.Directives._

object RouteEntryPoint {
  def Start(): Unit = {
    implicit val system = ActorSystem()
    implicit val materializer = Materializer
    implicit val executionContext = system.dispatcher

    //Setup ssl
    val key: KeyStore = KeyStore.getInstance("PKCS12")
    val keyStoreFile: InputStream = getClass.getClassLoader.getResourceAsStream("myKeystore.p12")
    val password = "MY_PASSWORD".toCharArray
    key.load(keyStoreFile, password)

    val keyManagerFactory = KeyManagerFactory.getInstance("SunX509")
    keyManagerFactory.init(key, password)

    val trustManagerFactory = TrustManagerFactory.getInstance("SunX509")
    trustManagerFactory.init(key)

    val sslContext: SSLContext = SSLContext.getInstance("TLS")
    sslContext.init(keyManagerFactory.getKeyManagers, trustManagerFactory.getTrustManagers, new SecureRandom)

    val httpsConnectionContext = ConnectionContext.httpsServer(sslContext)
    //Setup ssl finish----------

    val userRoute =  new UserRoute()
    val campRoute =  new CampRoute()
    val cors = new CORSHandler {}

    val finalRoute = cors.corsHandler{ userRoute.userFinalRoute ~ campRoute.campFinalRoute }

    val localhost = "127.0.0.1"
    val vpshost = "103.153.65.194"

    val finalRouteHandler = Http().newServerAt(localhost, 54000).bindFlow(finalRoute)

    val listBindingFutureWithSecurity = List(finalRouteHandler)

    println(s"Server is progressing...\nPress RETURN to stop...")
    StdIn.readLine()
    listBindingFutureWithSecurity
      .foreach { server =>
        server.flatMap(_.unbind())
          .onComplete(_ => system.terminate())
      }

  }
}
