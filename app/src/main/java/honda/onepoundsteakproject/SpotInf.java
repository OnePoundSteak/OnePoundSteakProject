package honda.onepoundsteakproject;
public class SpotInf {
	
	public String name;
    //地名
	public double lat;
	//latitude
    public double lng;
	//longitude
    public double rate;
	//評価値

	public  SpotInf(String name, double lat, double lng, double rate){
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.rate = rate;
    }
	
	
}
