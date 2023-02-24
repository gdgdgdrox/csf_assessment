package vttp2022.csf.assessment.server.models;

// Do not modify this class
public class LatLng {
	private float latitude;
	private float longitude;

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public float getLatitude() {
		return this.latitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	public float getLongitude() {
		return this.longitude;
	}

	// NOTE TO SELF : REMEMBERB TO DELETE THIS BEFORE PUSHING TO GIT
	//forgive me if i forget
	@Override
	public String toString() {
		return "{" +
			" latitude='" + getLatitude() + "'" +
			", longitude='" + getLongitude() + "'" +
			"}";
	}

}
