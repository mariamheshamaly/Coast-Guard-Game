package code;
public class Station {
	public int Passengers_dropped;

	public Station() {

	}

	public Station(Station other) {
		this.Passengers_dropped = other.Passengers_dropped;

	}

	public int getPassengers_dropped() {
		return Passengers_dropped;
	}

	public void setPassengers_dropped(int passengers_dropped) {
		Passengers_dropped = passengers_dropped;
	}

}