
public class Data {
	String weather,humidity, temp, windy, res;

	public Data(String s[]) {
		super();
		this.weather = s[0];
		this.humidity = s[1];
		this.temp = s[2];
		this.windy = s[3];
		this.res = s[4];
	}
	public String toString() {
		return this.weather+this.humidity+this.temp+this.windy;
	}
	
}
