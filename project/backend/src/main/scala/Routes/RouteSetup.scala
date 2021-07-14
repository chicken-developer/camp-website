package Routes

import akka.actor.ActorSystem
import akka.http.scaladsl.{ConnectionContext, Http}
import akka.stream.Materializer

import java.io.InputStream
import java.security.{KeyStore, SecureRandom}
import javax.net.ssl.{KeyManagerFactory, SSLContext, TrustManagerFactory}
import scala.io.StdIn

object RouteSetup {
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


    val route = new RouteHandler()

    val host = "127.0.0.1"
    val finalRouteHandler = Http().newServerAt(host, 54000).bindFlow(route.finalRoute)


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
