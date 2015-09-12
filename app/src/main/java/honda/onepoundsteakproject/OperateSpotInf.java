package honda.onepoundsteakproject;

import java.util.ArrayList;

public class OperateSpotInf {

	public ArrayList<SpotInf> makeList(){
		
		ArrayList<SpotInf> list= new ArrayList<SpotInf>();
		list.add(new SpotInf("東大寺",34.686926,135.83925,4.4));
		list.add(new SpotInf("奈良公園",34.685047,135.843012,4.2));
		list.add(new SpotInf("谷瀬の吊り橋",34.102003,135.76278,4.2));
		list.add(new SpotInf("興福寺国宝館",34.683535,135.832548,4.6));
		list.add(new SpotInf("海遊館",34.654518,135.428965,4.1));
		
		return list;
	}
}
