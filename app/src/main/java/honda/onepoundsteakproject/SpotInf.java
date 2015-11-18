package honda.onepoundsteakproject;

// tes
public class SpotInf {
    public int dataId;
	public String name;
    public float lat;
    public float lon;
    public float rate;
    public float dist;
    public String imageURL;
    public int duration;
    public int fare;

	public SpotInf(int dataId, String name,  float lon, float lat, float rate, float dist){
        this.dataId = dataId;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.rate = rate;
        this.dist = dist;
        this.imageURL = "";
        this.duration = -1;
        this.fare = -1;
    }
}