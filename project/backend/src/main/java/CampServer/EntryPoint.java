package CampServer;
import CampServer.MongoConfig.MongoController;
import Routes.RouteEntryPoint;

public class EntryPoint {
    public static void main(String[] args) {
        MongoController.Connect();
        RouteEntryPoint.Start();
    }
}
