public class Person {
    public final int WIDOWED = 0;
    public final int DIVORCED = 1;
    public final int MARRIED = 2;
    public final int SINGLE = 3;

    private int maritalStatus;
    private String placeOfOrigin;
    private String name;

    public Person(String name, String placeOfOrigin, int maritalStatus) {
        this.name = name;
        this.placeOfOrigin = placeOfOrigin;
        this.maritalStatus = maritalStatus;
    }

    public Person(String formattedString) {
        String[] parts = formattedString.split(", ");
        this.name = parts[0].split(": ")[1];
        this.placeOfOrigin = parts[1].split(": ")[1];
        String maritalStatusString = parts[2].split(": ")[1];
        switch (maritalStatusString.toLowerCase()) {
            case "widowed":
                this.maritalStatus = WIDOWED;
                break;
            case "divorced":
                this.maritalStatus = DIVORCED;
                break;
            case "married":
                this.maritalStatus = MARRIED;
                break;
            case "single":
                this.maritalStatus = SINGLE;
                break;
            default:
                throw new IllegalArgumentException("Invalid marital status: " + maritalStatusString);
        }
    }

    public String getName() {
        return name;
    }

    public String getPlaceOfOrigin() {
        return placeOfOrigin;
    }

    public int getMaritalStatus() {
        return maritalStatus;
    }

    private String getMaritalStatusString() {
        switch (maritalStatus) {
            case WIDOWED:
                return "Widowed";
            case DIVORCED:
                return "Divorced";
            case MARRIED:
                return "Married";
            case SINGLE:
                return "Single";
            default:
                throw new IllegalStateException("Invalid marital status: " + maritalStatus);
        }
    }

    public String toString() {
        return "Name: " + name + ", Place of Origin: " + placeOfOrigin + ", Marital Status: " + getMaritalStatusString();
    }
}
