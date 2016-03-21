package tutorialspoint.example.com.iamhereoriginal;

/**
 * Created by Neeraj on 19/03/16.
 */

 public enum MotionSensorEnum {
    TYPE_ACCELEROMETER(1),
    TYPE_MAGNETIC_FIELD(2),
    TYPE_ORIENTATION (3),
    TYPE_GYROSCOPE(4),
    TYPE_LIGHT(5),
    TYPE_PRESSURE(6),
    TYPE_TEMPERATURE(7),
    TYPE_PROXIMITY(8),
    TYPE_GRAVITY(9),
    TYPE_LINEAR_ACCELERATION(10),
    TYPE_ROTATION_VECTOR(11),
    TYPE_RELATIVE_HUMIDITY(12),
    TYPE_AMBIENT_TEMPERATURE(13),
    TYPE_MAGNETIC_FIELD_UNCALIBRATED(14),
    TYPE_GAME_ROTATION_VECTOR(15),
    TYPE_GYROSCOPE_UNCALIBRATED(16),
    TYPE_SIGNIFICANT_MOTION(17),
    TYPE_STEP_DETECTOR(18),
    TYPE_STEP_COUNTER(19),
    TYPE_GEOMAGNETIC_ROTATION_VECTOR(20),
    TYPE_HEART_RATE(21),
    TYPE_TILT_DETECTOR(22),
    TYPE_WAKE_GESTURE(23),
    TYPE_GLANCE_GESTURE(24),
    TYPE_PICK_UP_GESTURE(25),
    TYPE_WRIST_TILT_GESTURE(26);


    public int value;

    MotionSensorEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
   public static String[] sensorList = {
        "TYPE_ACCELEROMETER",
        "TYPE_MAGNETIC_FIELD",
        "TYPE_ORIENTATION ",
        "TYPE_GYROSCOPE",
        "TYPE_LIGHT",
        "TYPE_PRESSURE",
        "TYPE_TEMPERATURE",
        "TYPE_PROXIMITY",
        "TYPE_GRAVITY",
        "TYPE_LINEAR_ACCELERATION",
        "TYPE_ROTATION_VECTOR",
        "TYPE_RELATIVE_HUMIDITY",
        "TYPE_AMBIENT_TEMPERATURE",
        "TYPE_MAGNETIC_FIELD_UNCALIBRATED",
        "TYPE_GAME_ROTATION_VECTOR",
        "TYPE_GYROSCOPE_UNCALIBRATED",
        "TYPE_SIGNIFICANT_MOTION",
        "TYPE_STEP_DETECTOR",
        "TYPE_STEP_COUNTER",
        "TYPE_GEOMAGNETIC_ROTATION_VECTOR",
        "TYPE_HEART_RATE",
        "TYPE_TILT_DETECTOR",
        "TYPE_WAKE_GESTURE",
        "TYPE_GLANCE_GESTURE",
        "TYPE_PICK_UP_GESTURE",
        "TYPE_WRIST_TILT_GESTURE"
   };

    public static final int[] sSensorReportingModes = {
            0, // padding because sensor types start at 1
            3, // SENSOR_TYPE_ACCELEROMETER
            3, // SENSOR_TYPE_GEOMAGNETIC_FIELD
            3, // SENSOR_TYPE_ORIENTATION
            3, // SENSOR_TYPE_GYROSCOPE
            3, // SENSOR_TYPE_LIGHT
            3, // SENSOR_TYPE_PRESSURE
            3, // SENSOR_TYPE_TEMPERATURE
            3, // SENSOR_TYPE_PROXIMITY
            3, // SENSOR_TYPE_GRAVITY
            3, // SENSOR_TYPE_LINEAR_ACCELERATION
            5, // SENSOR_TYPE_ROTATION_VECTOR
            3, // SENSOR_TYPE_RELATIVE_HUMIDITY
            3, // SENSOR_TYPE_AMBIENT_TEMPERATURE
            6, // SENSOR_TYPE_MAGNETIC_FIELD_UNCALIBRATED
            4, // SENSOR_TYPE_GAME_ROTATION_VECTOR
            6, // SENSOR_TYPE_GYROSCOPE_UNCALIBRATED
            1, // SENSOR_TYPE_SIGNIFICANT_MOTION
            1, // SENSOR_TYPE_STEP_DETECTOR
            1, // SENSOR_TYPE_STEP_COUNTER
            5, // SENSOR_TYPE_GEOMAGNETIC_ROTATION_VECTOR
            1, // SENSOR_TYPE_HEART_RATE_MONITOR
            1, // SENSOR_TYPE_WAKE_UP_TILT_DETECTOR
            1, // SENSOR_TYPE_WAKE_GESTURE
            1, // SENSOR_TYPE_GLANCE_GESTURE
            1, // SENSOR_TYPE_PICK_UP_GESTURE
            1, // SENSOR_TYPE_WRIST_TILT_GESTURE
    };
    public static boolean[] sensorRequired = {
            true, // SENSOR_TYPE_ACCELEROMETER
            false, // SENSOR_TYPE_GEOMAGNETIC_FIELD
            false, // SENSOR_TYPE_ORIENTATION
            false, // SENSOR_TYPE_GYROSCOPE
            false, // SENSOR_TYPE_LIGHT
            false, // SENSOR_TYPE_PRESSURE
            false, // SENSOR_TYPE_TEMPERATURE
            false, // SENSOR_TYPE_PROXIMITY
            false, // SENSOR_TYPE_GRAVITY
            false, // SENSOR_TYPE_LINEAR_ACCELERATION
            false, // SENSOR_TYPE_ROTATION_VECTOR
            false, // SENSOR_TYPE_RELATIVE_HUMIDITY
            false, // SENSOR_TYPE_AMBIENT_TEMPERATURE
            false, // SENSOR_TYPE_MAGNETIC_FIELD_UNCALIBRATED
            false, // SENSOR_TYPE_GAME_ROTATION_VECTOR
            false, // SENSOR_TYPE_GYROSCOPE_UNCALIBRATED
            false, // SENSOR_TYPE_SIGNIFICANT_MOTION
            false, // SENSOR_TYPE_STEP_DETECTOR
            false, // SENSOR_TYPE_STEP_COUNTER
            false, // SENSOR_TYPE_GEOMAGNETIC_ROTATION_VECTOR
            false, // SENSOR_TYPE_HEART_RATE_MONITOR
            false, // SENSOR_TYPE_WAKE_UP_TILT_DETECTOR
            false, // SENSOR_TYPE_WAKE_GESTURE
            false, // SENSOR_TYPE_GLANCE_GESTURE
            false, // SENSOR_TYPE_PICK_UP_GESTURE
            false // SENSOR_TYPE_WRIST_TILT_GESTURE
    };
}