import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Window extends JFrame {

    private final String[] nombres = {"Pinza", "Codo", "Hombro", "Base"};
    private final JSlider[] sliders = new JSlider[4];
    private final int[] valores = new int[4];
    private FileOutputStream puerto;

    public Window() {
        setTitle("Brazo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 350);
        setLocationRelativeTo(null);

        abrirPuerto();

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int i = 0; i < 4; i++) {
            JSlider slider = new JSlider(0, 100, 0);
            slider.setMajorTickSpacing(25);
            slider.setMinorTickSpacing(5);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);
            slider.setBorder(BorderFactory.createTitledBorder(nombres[i]));

            slider.addChangeListener(e -> {
                for (var j = 0; j < 4; j++) {
                    valores[j] = sliders[j].getValue();
                }

                int pinzaDeg = Servo.percentToGradePinza(valores[0]);
                int codoDeg = Servo.percentToGradeCodo(valores[1]);
                int hombroDeg = Servo.percentToGradeHombro(valores[2]);
                int baseDeg = Servo.percentToGradeBase(valores[3]);

                String json = "{\"servos\":[" + pinzaDeg + "," + codoDeg + "," + hombroDeg + "," + baseDeg + "]}";
                System.out.println(json);
                enviarJson(json);
            });

            sliders[i] = slider;
            panel.add(slider);
        }

        add(panel);
        setVisible(true);
    }

    private void abrirPuerto() {
        for (int i = 1; i <= 20; i++) {
            String name = "COM" + i;
            try {
                new ProcessBuilder("cmd", "/c", "mode", name + ":", "BAUD=115200", "PARITY=N", "DATA=8", "STOP=1")
                    .redirectErrorStream(true).start().waitFor();
                var stream = new FileOutputStream("\\\\.\\" + name);
                System.out.println("Conectado a " + name);
                puerto = stream;
                return;
            } catch (Exception e) {
            }
        }
        System.out.println("No se encontro puerto serial disponible");
    }

    private void enviarJson(String json) {
        if (puerto == null) return;
        try {
            puerto.write((json + "\n").getBytes());
            puerto.flush();
        } catch (Exception e) {
        }
    }
}
