package CampServer;
import CampServer.MongoConfig.MongoController;
import Routes.RouteSetup;

public class EntryPoint {
    public static void main(String[] args) {
        System.out.println("Server is running in host");

        MongoController.Connect();
        RouteSetup.Start();
    }
}
