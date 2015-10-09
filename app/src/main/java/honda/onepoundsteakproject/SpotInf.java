package honda.onepoundsteakproject;


public class SpotInf {
    public int dataId;
	public String name;
	public float lat;
    public float lng;
    public float rate;
    public float dist;

    public  SpotInf(){
        this.dataId = -1;
        this.name = "dummy";
        this.lat = 0;
        this.lng = 0;
        this.rate = 0;
        this.dist = 0;
    }

	public  SpotInf(int dataId, String name, float lat, float lng, float rate, float dist){
        this.dataId = dataId;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.rate = rate;
        this.dist = dist;
    }
}
