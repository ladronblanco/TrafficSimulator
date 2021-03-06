package simulator.model;

abstract class NewRoadEvent extends Event {
    private final String id;
    private final String srcJunc;
    private final String destJunc;
    private final int length;
    private final int co2Limit;
    private final int maxSpeed;
    private final Weather weather;

    public NewRoadEvent(int time, String id, String srcJun, String
            destJunc, int length, int co2Limit, int maxSpeed, Weather weather) {
        super(time);
        this.id = id;
        this.srcJunc = srcJun;
        this.destJunc = destJunc;
        this.length = length;
        this.co2Limit = co2Limit;
        this.maxSpeed = maxSpeed;
        this.weather = weather;

    }


    @Override
    void execute(RoadMap map) {
        Road r = createRoadObject(map, getId(), srcJunc, destJunc, length, co2Limit, maxSpeed, weather);
        map.addRoad(r);
    }

    abstract Road createRoadObject(RoadMap map, String id, String srcJun, String
            destJunc, int length, int co2Limit, int maxSpeed, Weather weather);


	public String getId() {
		return id;
	}
	
	@Override
	public String toString() {
	return "New Road '"+id+"'";
	}
}
