package Routes

import akka.actor.ActorSystem
import akka.http.scaladsl.{ConnectionContext, Http}
import akka.stream.Materializer

import java.io.InputStream
import java.security.{KeyStore, SecureRandom}
import javax.net.ssl.{KeyManagerFactory, SSLContext, TrustManagerFactory}
import scala.io.StdIn

object FinalRoute {
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


    val userService = new UserRoute()
    val campService = new CampRoute()
    val adminService = new AdminRoute()

    val host = "127.0.0.1"
    val accountServerBind = Http().newServerAt(host, 8080).bindFlow(userService.finalRoute)
    val lobbyServerBind = Http().newServerAt(host, 8081).bindFlow(campService.finalRoute)
    val gameServerBind = Http().newServerAt(host, 8082).bindFlow(adminService.finalRoute)


    val listBindingFutureWithSecurity = List(accountServerBind, lobbyServerBind, gameServerBind)
    println(s"Server is progressing...\nPress RETURN to stop...")
    StdIn.readLine()
    listBindingFutureWithSecurity
      .foreach { server =>
        server.flatMap(_.unbind())
          .onComplete(_ => system.terminate())
      }

  }
}
