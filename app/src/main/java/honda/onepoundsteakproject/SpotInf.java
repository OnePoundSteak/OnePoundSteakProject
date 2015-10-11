package honda.onepoundsteakproject;


public class SpotInf {
    public int dataId;
	public String name;
    public float lat;
    public float lon;
    public float rate;
    public float dist;

    public  SpotInf(){
        this.dataId = -1;
        this.name = "";
        this.lat = 0;
        this.lon = 0;
        this.rate = 0;
        this.dist = 0;
    }

	public  SpotInf(int dataId, String name,  float lon, float lat, float rate, float dist){
        this.dataId = dataId;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.rate = rate;
        this.dist = dist;
    }
}
