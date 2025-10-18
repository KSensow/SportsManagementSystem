import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import javax.swing.*;

public class NewMain {
    private static NewMain instance = new NewMain(); // To grab the global framework
    private static setPlayer plr = new setPlayer(); // To grab the Getters and Setters
    private static Thread cooldownThread; // countdown cooldown
    private static MainSetUp gd = new MainSetUp(400, 300); // main setup frame

    public static void main(String[] args) {
        JFrame frame = gd.setUpGUI(); // grab frames
        
        //We start!
        CreateTextFile();
        loadComponents(frame);
    }

    public static void CreateTextFile(){
        try {
            File myObj = new File("sportsData.txt");
            
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void loadUpdatedFile(JTextArea updateText){
        Path filePath = Paths.get("sportsData.txt");
        try {
            List<String> lines = Files.readAllLines(filePath);
            String fileContent = String.join(System.lineSeparator(), lines);
            updateText.setText(fileContent);
            System.out.println("Adding data");
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    public static void startCooldown(int Miliseconds,JTextArea cooldown) { // Seconds until cooldown here
            long totalMilliseconds = Miliseconds * 2;
            
            if (cooldownThread != null && cooldownThread.isAlive()) {
                return;
            }

            cooldownThread = new Thread(() -> {
                try {
                    for (long i = totalMilliseconds; i > 0; i--){
                        Thread.sleep(1000);
                        cooldown.setText(i - 1 +" Left");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Cooldown interrupted.");
                }
            });

            cooldownThread.start();
        }

    public static void loadComponents(JFrame frame){
       JButton setPlayerName = new JButton("Enter Player Data!!");
       JButton confirmPlayer = new JButton("Confirm Data?");
        JTextArea playerName = new JTextArea("Enter Player Name");
        JTextArea whatItShows = new JTextArea("Shows what you got before submitting");
        JTextArea coolDownCount = new JTextArea("Counting down: 0 ");

        JPanel plrActions = new JPanel(); // Stuff the input gets added in
        String[] Choices = {"Hockey","Football","Tennis"}; // Sports choices
        final JComboBox <String> cb = new JComboBox<String>(Choices);
        JTextArea textArea = new JTextArea(10, 30);
        JScrollPane scrollPane = new JScrollPane(textArea,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel mainPanel = new JPanel(); // Main panel of things
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.setBackground(Color.ORANGE);

        //Java addons here

        playerName.setLineWrap(true);
        playerName.setWrapStyleWord(true);

        plrActions.add(playerName);
        plrActions.add(cb);
        plrActions.setAlignmentX(100);
        plrActions.setAlignmentY(100);

        plrActions.add(setPlayerName);
        plrActions.add(confirmPlayer);
        plrActions.add(whatItShows);
        plrActions.add(coolDownCount);

        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);

        whatItShows.setEditable(false);
        whatItShows.setLineWrap(true);
        whatItShows.setWrapStyleWord(true);

        coolDownCount.setEditable(false);
        coolDownCount.setLineWrap(true);
        coolDownCount.setWrapStyleWord(true);

        //Pre-Select data Text format
        loadUpdatedFile(textArea);

        //Player input actions here
        setPlayerName.addActionListener(new java.awt.event.ActionListener() {
            //Set the data here first to finish any changes
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                String player = playerName.getText().trim();
                String sport = (String) cb.getSelectedItem();

                if (player.isEmpty() || sport == null){
                    whatItShows.setText("Please enter a player name.");
                    return;
                }

                whatItShows.setText("Player: " + player + "Sport: " + sport);
                plr.setPlayerData(player, sport);
            }
        });

        confirmPlayer.addActionListener(new java.awt.event.ActionListener() {
            //Upload here
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e){
                String player = plr.getPlayer();
                String sport = plr.getSport();

                //Check if cooldown is live
                if (cooldownThread != null && cooldownThread.isAlive()){
                    confirmPlayer.setText("it's running cannot start");
                    return;
                }

                //Check if components is empty
                if (player == null || sport == null){
                    confirmPlayer.setText("Cannot Confirm! - It's Empty");
                    return;
                }

                //Load data
                gd.updateTextFile(player, sport, confirmPlayer);

                //Load
                loadUpdatedFile(textArea);
                startCooldown(2,coolDownCount);

                //Reset previous data
                
                playerName.setText("");
                whatItShows.setText("");

                plr.setPlayerData("","");
            }    
        }); 

        //Frame Sets Here
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(mainPanel);
        frame.pack();
        frame.add(plrActions,BorderLayout.SOUTH);
    }

}