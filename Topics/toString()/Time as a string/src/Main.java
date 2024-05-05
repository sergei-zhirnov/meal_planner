class Time {

    private int hours;
    private int minutes;
    private int seconds;

    public Time(int hours, int minutes, int seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    @Override
    public String toString() {
        String hoursRepresentation = hours % 10 == hours ? "0" + hours : String.valueOf(hours);
        String minutesRepresentation = minutes % 10 == minutes ? "0" + minutes : String.valueOf(minutes);
        String secondsRepresentation = seconds % 10 == seconds ? "0" + seconds : String.valueOf(seconds);
        return hoursRepresentation + ":" + minutesRepresentation + ":" + secondsRepresentation;
    }
}