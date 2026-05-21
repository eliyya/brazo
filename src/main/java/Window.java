import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    public Window() {
        setTitle("Brazo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Hello World!", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.PLAIN, 24));
        add(label);

        setVisible(true);
    }
}
