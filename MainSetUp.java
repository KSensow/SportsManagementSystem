import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;

public class MainSetUp {
    private JFrame frame;
    private int width;
    private int height;
    private ArrayList savedPlayer = new ArrayList<>();
    private static final String FILE_NAME = "sportsData.txt";
    
    public MainSetUp(int w, int h) {
        frame = new JFrame();
        width = w;
        height = h;
    }

    public void updateTextFile(String player, String sport,JButton ConfirmPlayer) 
    {
        try {
            boolean isNull = (player == null || sport == null);
            boolean isEmpty = (player.trim().isEmpty() || sport.trim().isEmpty());

            if (isNull || isEmpty) {
                ConfirmPlayer.setText("Cannot submit! Missing some of the data.");
                return;
            }
            ConfirmPlayer.setText("Successfully added!");
            
            FileWriter myWriter = new FileWriter(FILE_NAME, true);

            myWriter.write(" Player " + player + " Sport " + sport +"\n");
            myWriter.close();

        } catch (Exception e) {
            ConfirmPlayer.setText("Error: " + e);
        }
    }

    public List<String> loadSportsData(String searchWord){
        String lowerSearchWord = searchWord.toLowerCase();
        List<String> matchingLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))){
            String line;

            while ((line = reader.readLine()) != null){
                if (line.toLowerCase().contains(lowerSearchWord)){
                    matchingLines.add(line);
                }
            }

            if (matchingLines.isEmpty()) {
                System.out.println("   No records found matching the search term.");
            } else {
                System.out.println("   Found " + matchingLines.size() + " records.");
            }

        } catch (IOException e) {
            System.err.println("❌ Could not read the file (" + FILE_NAME + "): " + e.getMessage());
        }

        return matchingLines;
    }

    public JFrame setUpGUI() {
        frame = new JFrame("Sports Management System");
        frame.setLayout(new FlowLayout());

        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        return frame;
    }

}
