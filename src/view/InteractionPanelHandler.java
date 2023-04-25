package view;

import control.MainController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Created by Jean-Pierre on 27.04.2017.
 */
public class InteractionPanelHandler {
    private JTextArea output;
    private JTextField personInSN;
    private JButton insertButton;
    private JTextField friendInSN;
    private JButton newFriendshipButton;
    private JButton killFriendshipButton;
    private JTextField personFrom;
    private JTextField personTo;
    private JButton searchButton;
    private JButton deleteButton;
    private JPanel panel;
    private JTextArea systemOutput;
    private JButton someoneIsLonleyButton;
    private JButton transitiveFriendshipsButton;
    private JButton searchShortestPathButton;

    private MainController mainController;

    public InteractionPanelHandler(MainController mainController) {
        this.mainController = mainController;
        createButtons();
        update();
    }

    private void createButtons(){
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!personInSN.getText().isEmpty()){
                    String name = personInSN.getText();
                    if(mainController.insertUser(name)){
                        update();
                        addToSysoutput("Es wurde der Nutzer " + name + " hinzugefügt.");
                    }else{
                        addToSysoutput("Der Nutzer " + name + " existiert bereits. Der Graph wurde nicht verändert.");
                    }
                }else{
                    addToSysoutput("Bitte geben Sie einen Namen ein.");
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!personInSN.getText().isEmpty()){
                    String name = personInSN.getText();
                    if(mainController.deleteUser(name)){
                        update();
                        addToSysoutput("Es wurde der Nutzer " + name + " gelöscht. Ebenfalls wurden alle Verbindungen zu diesem Nutzer entfernt.");
                    }else{
                        addToSysoutput("Der Nutzer  " + name + " existiert nicht.");
                    }
                }else{
                    addToSysoutput("Bitte geben Sie einen Namen ein.");
                }
            }
        });
        newFriendshipButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!personInSN.getText().isEmpty() && !friendInSN.getText().isEmpty()){
                    String person = personInSN.getText();
                    String friend = friendInSN.getText();
                    if(mainController.befriend(person, friend)){
                        update();
                        addToSysoutput("Die Nutzer " + person + " und " + friend + " sind nun Freunde.");
                    }else{
                        addToSysoutput("Der Nutzer " + person + " oder der Nutzer " + friend + " existiert nicht oder sie waren bereits befreundet. Der Graph wurde nicht verändert.");
                    }
                }else{
                    addToSysoutput("Sie müssen eine Person im Netzwerk und einen Freund angeben.");
                }
            }
        });
        killFriendshipButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!personInSN.getText().isEmpty() && !friendInSN.getText().isEmpty()){
                    String person = personInSN.getText();
                    String friend = friendInSN.getText();
                    if(mainController.unfriend(person, friend)){
                        update();
                        addToSysoutput("Die Freundschaft der Nutzer " + person + " und " + friend + " wurde beendet. Böse Welt!");
                    }else{
                        addToSysoutput("Der Nutzer " + person + " oder der Nutzer " + friend + " existiert nicht oder sie waren vorher schon nicht befreundet. Der Graph wurde nicht verändert.");
                    }
                }else{
                    addToSysoutput("Sie müssen eine Person im Netzwerk und einen Freund angeben.");
                }
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!personFrom.getText().isEmpty() && !personTo.getText().isEmpty()){
                    String from = personFrom.getText();
                    String to   = personTo.getText();
                    String[] links = mainController.getLinksBetween(from, to);
                    if(links != null){
                        String str = "";
                        for(int i = 0; i < links.length; i++){
                            str = str + links[i];
                            if(i != links.length - 1){
                                str = str + " -> ";
                            }
                        }                        
                        addToSysoutput(str);
                    }else{
                        addToSysoutput("Es wurde keine Verbindung zwischen " + from + " und " + to + " gefunden.");
                    }
                }else{
                    addToSysoutput("Für die Suche müssen Sie zwei Personen angeben.");
                }
            }
        });
        someoneIsLonleyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addToSysoutput("Einsame Knoten vorhanden: "+mainController.someoneIsLonely());
            }
        });
        transitiveFriendshipsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!personInSN.getText().isEmpty()){
                    String[] reachable = mainController.transitiveFriendships(personInSN.getText());
                    String output = "Von der Person ";
                    output += personInSN.getText();
                    output += " ausgehend sind folgende Personen erreichbar: ";
                    if(reachable != null){
                        String str = "";
                        for(int i = 0; i < reachable.length; i++){
                            str = str + reachable[i];
                            if(i != reachable.length - 1){
                                str = str + ", ";
                            }
                        }
                        output += str;
                    }else{
                        output += "niemand.";
                    }
                    addToSysoutput(output);
                }else{
                    addToSysoutput("Für die Suche muss eine Person angegeben werden.");
                }
            }
        });
        searchShortestPathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public JPanel getPanel(){
        return panel;
    }

    private void update(){
        resetOutput();

        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.GERMAN);
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#.##",otherSymbols);
        df.setRoundingMode(RoundingMode.HALF_UP);

        String[] allUsers = mainController.getAllUsers();
        if(allUsers != null){
            for(int i = 0; i < allUsers.length; i++){
                String[] friendsOfUser = mainController.getAllFriendsFromUser(allUsers[i]);
                int amount = 0;
                String allFriends = "";
                if(friendsOfUser != null){
                    amount = friendsOfUser.length;

                    for(int j = 0; j < amount; j++){
                        if(j != amount - 1){
                            allFriends = allFriends + friendsOfUser[j] + ", ";
                        }else{
                            allFriends = allFriends + friendsOfUser[j];
                        }

                    }
                }
                double cd = Double.parseDouble(df.format(mainController.centralityDegreeOfUser(allUsers[i])));

                addToOutput(amount, cd, allUsers[i], allFriends);
            }
        }

        double dense = Double.parseDouble(df.format(mainController.dense()));
        addToOutput(dense);
    }

    private void resetOutput(){
        String str = " #Freunde | Zentralitätsgrad |       Person       |       Freunde"+"\n";
        str = str +  "---------------------------------------------------------------------------------------------"+"\n";
        output.setText(str);
    }

    private void addToOutput(int amount, double loveliness, String name, String friends){
        String str = " ";
        for(int i = 0; i < 8-String.valueOf(amount).length(); i++){
            str = str + " ";
        }
        str = str + amount;
        str = str + " | " + loveliness;
        for(int i = 0; i < 16-String.valueOf(loveliness).length();i++){
            str = str + " ";
        }
        str = str + " | " + name;
        for(int i = 0; i < 18-name.length(); i++){
            str = str + " ";
        }
        str = str + " | " + friends + "\n";
        output.setText(output.getText()+str);
    }

    private void addToOutput(double dense){
        String str = "\n\n\nDichte des Netzwerks: " + dense;
        output.setText(output.getText()+str);
    }

    private void addToSysoutput(String text){
        systemOutput.setText(systemOutput.getText()+"\n"+text);
    }
}
