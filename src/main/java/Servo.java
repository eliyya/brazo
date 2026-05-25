public class Servo {
    public static int percentToGradePinza(int percent) {
        var limit = 20;
        if (percent > 100) return 20;
        if (percent < 0) return 0;
        return (int) percent * limit / 100;
    }

    public static int percentToGradeCodo(int percent) {
        var min = 20;
        var max = 80;
        if (percent > 100) return max;
        if (percent < 0) return min;
        var limit = max - min;
        return (int) percent * limit / 100 + min;
    }

    public static int percentToGradeBase(int percent) {
        var min = 0;
        var max = 180;
        if (percent > 100) return max;
        if (percent < 0) return min;
        var limit = max - min;
        return (int) percent * limit / 100 + min;
    }

    public static int percentToGradeHombro(int percent) {
        var min = 45;
        var max = 90;
        if (percent > 100) return max;
        if (percent < 0) return min;
        var limit = max - min;
        return (int) -1 * (percent * limit / 100 + min) + max + min;
    }
}