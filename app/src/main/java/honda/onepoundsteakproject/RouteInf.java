package honda.onepoundsteakproject;

/**
 * Created by ain on 15/10/11.
 */
public class RouteInf {
    public double distance;
    public int duration;
    public int fare;
    public double startLat;
    public double startLon;
    public double endLat;
    public double endLon;


    public RouteInf(double distance, int duration, int fare, double startLat, double startLon, double endLat, double endLon){
        this.distance = distance;
        this.duration = duration;
        this.fare = fare;
        this.startLat = startLat;
        this.startLon = startLon;
        this.endLat = endLat;
        this.endLon = endLon;
    }

    public RouteInf(){
        this.distance = -1;
        this.duration = -1;
        this.fare = -1;
        this.startLat = 0;
        this.startLon = 0;
        this.endLat = 0;
        this.endLon = 0;
    }
}
