package code;
public class Ship {

	public int NumberOfPassengers;
	public boolean Blackbox_taken = false;
	public boolean isWrecked = false;
	public boolean Blackbox_Available = false;
	public int timeforbox = 0;

	public Ship(Ship other) {
		this.NumberOfPassengers = other.NumberOfPassengers;
		this.Blackbox_taken = other.Blackbox_taken;
		this.isWrecked = other.isWrecked;
		this.Blackbox_Available = other.Blackbox_Available;
		this.timeforbox = other.timeforbox;
	}

	public boolean isBlackbox_Available() {

		return Blackbox_Available;
	}

	public void setBlackbox_Available(boolean blackbox_Available) {

		Blackbox_Available = blackbox_Available;

	}

	public boolean kill() {
		// boolean dead=false;
		if (NumberOfPassengers > 0) {
			NumberOfPassengers--;
			if (NumberOfPassengers == 0) {
				isWrecked = true;
				boxhelper();
			}
			return true;
		} else {
			isWrecked = true;
			boxhelper();
			return false;
		}

	}

	public void boxhelper() {
		if (Blackbox_taken == true)
			return;

		if (isWrecked == true) {

			if (timeforbox >= 20) {
				Blackbox_Available = false;
			} else {

				Blackbox_Available = true;
				timeforbox++;
			}
		}

	}

	public int getNumberOfPassengers() {
		return NumberOfPassengers;
	}

	public void setNumberOfPassengers(int numberOfPassengers) {
		NumberOfPassengers = numberOfPassengers;
	}

	public Ship(int NumberOfPassengers) {

		this.NumberOfPassengers = NumberOfPassengers;

	}

	public int pickup(int n) {
		int x;
		if (n >= NumberOfPassengers) {
			x = NumberOfPassengers;
			NumberOfPassengers = 0;
			isWrecked = true;
			boxhelper();

		} else {
			this.NumberOfPassengers = NumberOfPassengers - n;
			x = n;

		}

		return x;
	}

	public void Takebox() {
		Blackbox_taken = true;
		Blackbox_Available = false;
	}

	public boolean isWrecked() {
		return isWrecked;
	}

	public void setWrecked(boolean isWrecked) {
		this.isWrecked = isWrecked;
	}

}